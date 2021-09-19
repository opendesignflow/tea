/*-
 * #%L
 * Tea Scala Utils Library
 * %%
 * Copyright (C) 2006 - 2017 Open Design Flow
 * %%
 * This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.odfi.tea.compile

import dotty.tools.dotc
import dotty.tools.dotc.Driver

import java.io.StringWriter
import java.net.URL
import java.io.PrintWriter
import java.io.File
import org.odfi.tea.os.OSDetector

import java.net.URLClassLoader
import org.odfi.tea.thread.ThreadLanguage

class IDCompiler extends ClassDomainSupport with ThreadLanguage {

  // Compiler Base
  //---------------
  val compiler = new Driver
  val reporter = new CompilerReporter

  var compilerBaseArguments = List("-usejavacp")
  var compilerPlugins: List[(String, String)] = List()

  /*
  var interpreterOutput = new StringWriter
  val settings2 = new Settings({
    error => println("******* Error Happened ***********")
  })
  var imain: Option[IMain] = None*/

  //-- Create Classdomain for Compiler
  var compilerClassDomain = new ClassDomain(Thread.currentThread().getContextClassLoader)

  // Source/Output Pairs
  //---------------------------
  var compilerOutput: Option[File] = None

  def setCompilerOutput(f: File): Unit = {

    this.compilerOutput = Some(f.getCanonicalFile)

    // Recreate Class Domain for new output
    this.compilerClassDomain = new ClassDomain(this.compilerClassDomain.getParent)
    this.compilerClassDomain.addURL(this.compilerOutput.get.toURI.toURL)
    this.updateSettings
  }

  // Classpath
  //------------------
  var bootclasspath: List[URL] = List[URL]()

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

  def setParentClassLoader(cl: ClassLoader) = {
    this.compilerClassDomain = new ClassDomain(cl)
    updateSettings
  }

  //--- Scala Compiler and library go to boot classpath
  try {
    val compilerPath = java.lang.Class.forName("dotty.tools.dotc.Driver").getProtectionDomain.getCodeSource.getLocation
    val libPath = java.lang.Class.forName("scala.Some").getProtectionDomain.getCodeSource.getLocation
    addClasspathURL(compilerPath, libPath)
  } catch {
    case e: Throwable =>
      e.printStackTrace()
  }

  /**
   * Update Settings values
   */
  def updateSettings = {

    bootclasspath.foreach {
      u =>
        compilerClassDomain.addURL(u)
    }

  }

  // Compiler Getter
  //-----------------------

  def buildBaseCompilerArguments() = {

    assert(this.compilerOutput.isDefined, {
      "Cannot compile if no compiler output is defined"
    })

    this.compilerBaseArguments :::
      List("-bootclasspath",this.bootclasspath.map(_.getFile.stripPrefix("/")).mkString(File.pathSeparator))  :::
      this.compilerPlugins.map(p => s"-P:${p._1}:${p._2}") :::
      List("-d", this.compilerOutput.get.getCanonicalPath)

  }



  // Copmilation Interface
  //-------------------

  /**
   * Just compiles string
   *
   * @param str
   * @return
   */
  def compileString(str: String) = {

    throw new IllegalAccessError("Deprecated")

  }

  /**
   * Compile a file and return informations about the compilation result
   *
   * @param f
   * @return
   */
  def compileFile(f: File): Either[CompilationOutputResult, Throwable] = {

    // Prepare Arguments
    val baseArguments = this.buildBaseCompilerArguments()
    val allArguments = baseArguments ::: List(f.getCanonicalPath)

    // Compile
    println("Compile Arguments: "+allArguments)
    val result = new CompilationOutputResult(this.compilerClassDomain)
    try {
      this.compiler.process(allArguments.toArray, this.reporter, result)
      Left(result)
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        Right(e)
    }


  }

  /*
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
  }*/

}
