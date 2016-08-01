package com.idyria.osi.tea.compile

import java.io.StringWriter
import java.net.URL
import scala.tools.nsc.GenericRunnerSettings
import scala.tools.nsc.interpreter.IMain
import java.io.PrintWriter
import scala.reflect.internal.util.SourceFile
import java.io.File
import scala.reflect.io.AbstractFile
import scala.reflect.internal.util.BatchSourceFile
import com.idyria.osi.tea.os.OSDetector
import java.net.URLClassLoader
import scala.tools.nsc.settings.MutableSettings
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.Results.Result
import com.idyria.osi.tea.thread.ThreadLanguage

class IDCompiler extends ClassDomainSupport with ThreadLanguage {

  // Compiler Base
  //---------------
  var interpreterOutput = new StringWriter
  var settings2 = new Settings({
    error => println("******* Error Happened ***********")
  })
  var imain: Option[IMain] = None

  //-- Create Classdomain for Compiler
  var compilerClassDomain = new ClassDomain(Thread.currentThread().getContextClassLoader)

  // Source/Output Pairs
  //---------------------------
  var sourceOutputPairs = List[(File, File)]()

  def addSourceOutputFolders(pair: (File, File)): Unit = {

    this.sourceOutputPairs = this.sourceOutputPairs :+ (pair._1.getAbsoluteFile, pair._2.getAbsoluteFile)
    //this.settings2.outputDirs.add(AbstractFile.getDirectory(pair._1), AbstractFile.getDirectory(pair._2))
    this.updateSettings
  }

  // Classpath
  //------------------
  var bootclasspath = List[URL]()

  def addClasspathURL(u: Array[URL]): Unit = {
    u.filter(candidateURL => this.bootclasspath.find { cu => cu.toExternalForm() == candidateURL.toExternalForm() } == None) match {
      case newURLS if (newURLS.size > 0) =>
        bootclasspath = newURLS.toList ::: bootclasspath
        this.updateSettings
      case _ =>
    }

  }
  def addClasspathURL(u: URL*): Unit = {
    addClasspathURL(u.toArray)
    /*var filtered = u.filter( candidateURL => this.bootclasspath.find { cu => cu.toExternalForm()==candidateURL.toExternalForm() }==None)
    bootclasspath = filtered.toList ::: bootclasspath*/

  }

  //--- Scala Compiler and library go to boot classpath
  try {
    val compilerPath = java.lang.Class.forName("scala.tools.nsc.Interpreter").getProtectionDomain.getCodeSource.getLocation
    val libPath = java.lang.Class.forName("scala.Some").getProtectionDomain.getCodeSource.getLocation
    addClasspathURL(compilerPath, libPath)
  } catch {
    case e: Throwable =>
  }

  //-- Add stadnard classloader classes
  List(getClass.getClassLoader).foreach {
    case cl: URLClassLoader =>
      //-- Gather URLS
      addClasspathURL(cl.getURLs())
      cl.getURLs().foreach {
        url =>
        //println(s"**ECompiler adding: ${url.toExternalForm()}")
        //bootclasspath = url :: bootclasspath

      }
    case _ =>
  }

  /**
   * Update Settings values
   */
  def updateSettings = {

    //println(s"Updating Settings")
    // settings2.nc.value = true
    settings2.usejavacp.value = true
    settings2.defines.value = List("-Xexperimental")
    if (OSDetector.getOS == OSDetector.OS.LINUX) {

      settings2.classpath.value = bootclasspath mkString java.io.File.pathSeparator
      settings2.bootclasspath.value = bootclasspath mkString java.io.File.pathSeparator

    } else {

      settings2.classpath.value = bootclasspath.map(u => u.getPath.replaceFirst("/", "")) mkString java.io.File.pathSeparator
      settings2.bootclasspath.value = bootclasspath.map(u => u.getPath.replaceFirst("/", "")) mkString java.io.File.pathSeparator

    }

    bootclasspath.foreach {
      u =>
        compilerClassDomain.addURL(u)
    }

    this.imain

  }

  // Compiler Getter
  //-----------------------

  def getIMain = imain match {
    case Some(main) => main
    case None =>

      updateSettings

      // Create
      fork {

        this.imain = Some(new IMain(settings2, new PrintWriter(interpreterOutput)) {

          override protected def parentClassLoader: ClassLoader = {

            /*parentLoader match {
        case null => super.parentClassLoader
        case _ => parentLoader
      }*/
            //println(s"Returning current thread CL as aprent CL")
            //Thread.currentThread().getContextClassLoader
            compilerClassDomain
          }

          def compileSourcesSeq(sources: Seq[SourceFile]): Boolean =
            compileSourcesKeepingRun(sources: _*)._1

          //-- Init Main here before output dirs may be overriden and it won't work
          //println("Recreating! Recreatin!")
          this.interpret("var init = true")

          //-- Update output dirs
          //-- Do this here, otherwise Main creation overrides this setting
          sourceOutputPairs.foreach {
            case (source, output) =>

              source.mkdirs()
              output.mkdirs()
              // println(s"Setting Outputs: " + AbstractFile.getDirectory(source))

              //settings2.outputDirs.add(AbstractFile.getDirectory(source), AbstractFile.getDirectory(output))
              settings2.outputDirs.add(source.getAbsolutePath, output.getAbsolutePath)
            //settings2.outputDirs.setSingleOutput(AbstractFile.getDirectory(output))
          }
          //println(s"CL: "+Thread.currentThread().getContextClassLoader)
          //
          //updateSettings

        })
      }.join()

      /*imain = Some(new IMain(settings2, new PrintWriter(interpreterOutput)) {

        override protected def parentClassLoader: ClassLoader = {

          /*parentLoader match {
        case null => super.parentClassLoader
        case _ => parentLoader
      }*/
          println(s"Returning current thread CL as aprent CL")
          Thread.currentThread().getContextClassLoader

        }

        def compileSourcesSeq(sources: Seq[SourceFile]): Boolean =
          compileSourcesKeepingRun(sources: _*)._1
      })*/
      imain.get

  }

  // Copmilation Interface
  //-------------------

  def compileSourcesSeq(sources: Seq[SourceFile]): Boolean =
    getIMain.compileSourcesKeepingRun(sources: _*)._1

  /**
   * Compile Some files
   */
  def compileFiles(f: Seq[File]): Option[FileCompileError] = {

    // Reset output
    interpreterOutput.getBuffer().setLength(0)

    // Make sure Context Classloader URLS are all available to compiler
    this.addClasspathURL(this.getClassLoaderCrossHierarchyURLS(Thread.currentThread().getContextClassLoader).toArray)

    try {
      //compileSourcesKeepingRun(sources: _*)._1
      compileSourcesSeq(f.map { f => new BatchSourceFile(AbstractFile.getFile(f.getAbsoluteFile)) }) match {
        case false =>

          // Prepare error
          Some(new FileCompileError( interpreterOutput.toString().trim))

        //throw new RuntimeException(s"Could not compile content: ${interpreterOutput.toString()}")
        case _ => None
      }

    } finally {

      // Reset output
      interpreterOutput.getBuffer().setLength(0)

    }

  }

  /**
   * Compile a file
   */
  def compileFile(f: File): Option[FileCompileError] = {

    try {

      getIMain.compileSources(new BatchSourceFile(AbstractFile.getFile(f.getAbsoluteFile))) match {
        case false =>

          // Prepare error
          Some(new FileCompileError( interpreterOutput.toString()))

        //throw new RuntimeException(s"Could not compile content: ${interpreterOutput.toString()}")
        case _ => None
      }

    } finally {

      // Reset output
      interpreterOutput.getBuffer().setLength(0)

    }

  }

  def interpret(l: String): Option[FileCompileError] = {
    try {

      getIMain.interpret(l) match {
        case r if (r == scala.tools.nsc.interpreter.Results.Incomplete) =>

          // Prepare error
          //Some(new FileCompileError(f, interpreterOutput.toString()))

          throw new RuntimeException(s"Could not Interpreset content: ${interpreterOutput.toString()}")
        case _ => None
      }

    } finally {

      // Reset output
      interpreterOutput.getBuffer().setLength(0)

    }
  }

}