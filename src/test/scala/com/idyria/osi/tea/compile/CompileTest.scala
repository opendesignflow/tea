package com.idyria.osi.tea.compile

import org.scalatest.FunSuite
import java.io.File
import com.idyria.osi.tea.file.DirectoryUtilities
import scala.reflect.io.AbstractFile
import org.scalatest.BeforeAndAfter
import java.net.URLClassLoader
import org.scalatest.GivenWhenThen

class CompileTest extends FunSuite with BeforeAndAfter with GivenWhenThen {

  val sourceFolder = new File("src/test/resources/compile")
  val outputFolder = new File("target/tco")
  val startupCL = getClass.getClassLoader
  
  before {
    outputFolder.mkdirs()
    DirectoryUtilities.deleteDirectoryContent(outputFolder)
    Thread.currentThread().setContextClassLoader(startupCL)
  }

  test("Startup and Classloader") {
    
    println("DefaultCL: "+Thread.currentThread().getContextClassLoader)
    var idCompiler = new IDCompiler
    //-- Set Source and Output
    idCompiler.addSourceOutputFolders(sourceFolder -> outputFolder)
    
    idCompiler.updateSettings
    Thread.sleep(2000)
    
    println("END CL: "+Thread.currentThread().getContextClassLoader)
  }
  
  test("Compilation Source and Output check") {

    //-- Create Compiler
    var idCompiler = new IDCompiler

    //-- Set Source and Output
    idCompiler.addSourceOutputFolders(sourceFolder -> outputFolder)

    //-- Compile Files, check output
    var sourceFile = new File(sourceFolder, "TestCompile.scala").getAbsoluteFile

    var res = idCompiler.compileFile(sourceFile)
    res match {
      case Some(error) =>
        println(s"Error ${error.message}")
        println(s"CP: " + idCompiler.settings2.classpath)
      case None =>
    }

    //-- No Errors
    assertResult(None, "No Compilation Errors")(res)
    /* println(s"$res")
    println(s"Settings: "+idCompiler.settings2.outputDirs.outputs)
    println(s"Settings: "+idCompiler.settings2.outputDirs.getSingleOutput)*/
    //Thread.sleep(5000)

    //-- Check output content
    var classFile = new File(outputFolder, "com.idyria.osi.tea.compile.TestCompile".replace(".", File.separator) + ".class")
    assertResult(true, "Class File must be present in output")(classFile.exists())

  }

  test("Class Loading Works with Classloader for output folder and parent current Thread CL (to get URL classpath set at startup)") {

    //-- Compile File
    var idCompiler = new IDCompiler
    idCompiler.addSourceOutputFolders(sourceFolder -> outputFolder)
    var sourceFile = new File(sourceFolder, "TestCompile.scala").getAbsoluteFile

    //-- Try to Load class
    idCompiler.compileFile(sourceFile)

    //-- Class Name 
    val className = "com.idyria.osi.tea.compile.TestCompile"

    //-- Add Output to current Thread
    Thread.currentThread().setContextClassLoader(URLClassLoader.newInstance(Array(outputFolder.toURI().toURL()), Thread.currentThread().getContextClassLoader))
    Thread.currentThread().getContextClassLoader.loadClass(className)

  }

  test("Class Cast Works if Classloading works") {

    //-- Compile File
    var idCompiler = new IDCompiler
    idCompiler.addSourceOutputFolders(sourceFolder -> outputFolder)
    var sourceFile = new File(sourceFolder, "TestCompile.scala").getAbsoluteFile

    //-- Try to Load class
    idCompiler.compileFile(sourceFile)

    //-- Class Name 
    val className = "com.idyria.osi.tea.compile.TestCompile"

    //-- Add Output to current Thread
    Thread.currentThread().setContextClassLoader(URLClassLoader.newInstance(Array(outputFolder.toURI().toURL()), Thread.currentThread().getContextClassLoader))
    var cl = Thread.currentThread().getContextClassLoader.loadClass(className)

    //-- Cast
    cl.asInstanceOf[Class[TestCompileParent]]

  }

  test("Compile a subclass of a class in extra source Folder") {

    //-- Compile File
    var idCompiler = new IDCompiler
    idCompiler.addSourceOutputFolders(sourceFolder -> outputFolder)
    var sourceFiles = List(new File(sourceFolder, "TestCompileSubclass.scala").getAbsoluteFile, new File(sourceFolder, "TestCompile.scala").getAbsoluteFile)

    //-- Try to Load class 
    var res = idCompiler.compileFiles(sourceFiles)
    res match {
      case Some(error) =>
        println(s"Error ${error.message}")
      case None =>
    }

    //-- Check
    assertResult(None, "No Compilation Errors")(res)

    //-- Class Name 
    val parentClassName = "com.idyria.osi.tea.compile.TestCompile"
    val className = "com.idyria.osi.tea.compile.TestCompileSublass"

    //-- Add Output to current Thread
    Thread.currentThread().setContextClassLoader(URLClassLoader.newInstance(Array(outputFolder.toURI().toURL()), Thread.currentThread().getContextClassLoader))
    var cl = Thread.currentThread().getContextClassLoader.loadClass(className)
    var superCl = Thread.currentThread().getContextClassLoader.loadClass(parentClassName)

    //-- Cast to its parent which has been compile, and to the common TestCompileParent
    cl.asInstanceOf[Class[TestCompileParent]]
    cl.asSubclass(superCl)

  }

  test("Class Compile with dependency, Compilation Errors if Dep Jar not set") {

    //-- Compile File
    var idCompiler = new IDCompiler
    idCompiler.addSourceOutputFolders(sourceFolder -> outputFolder)
    var sourceFiles = List(new File(sourceFolder, "TestCompileWithDependency.scala").getAbsoluteFile)

    //-- Try to Load class 
    var res = idCompiler.compileFiles(sourceFiles)
    res match {
      case Some(error) =>
        println(s"Error ${error.message}")
      case None =>
    }

    //-- Check
    assertResult(true, "Expected Compilation Errors")(res.isDefined)

  }

  test("Class Compile with dependency, success if Dep Jar is set") {

    //-- Compile File
    var idCompiler = new IDCompiler
    idCompiler.addSourceOutputFolders(sourceFolder -> outputFolder)
    var sourceFiles = List(new File(sourceFolder, "TestCompileWithDependency.scala").getAbsoluteFile)

    //-- Add jar to ClassPath
    idCompiler.addClasspathURL(new File(sourceFolder, "bridj-0.7.0.jar").toURI().toURL())

    //-- Try to Load class 
    var res = idCompiler.compileFiles(sourceFiles)
    res match {
      case Some(error) =>
        println(s"Error ${error.message}")
      case None =>
    }

    //-- Check
    assertResult(None, "No Compilation Errors")(res)

  }

  test("Class Compile with dependency, instance requires Context Class Loader update") {

    //-- Compile File
    var idCompiler = new IDCompiler
    idCompiler.addSourceOutputFolders(sourceFolder -> outputFolder)
    var sourceFiles = List(new File(sourceFolder, "TestCompileWithDependency.scala").getAbsoluteFile)

    //-- Add jar to ClassPath
    var dependencyJar = new File(sourceFolder, "bridj-0.7.0.jar").toURI().toURL()
    idCompiler.addClasspathURL(dependencyJar)

    //-- Try to Load class 
    var res = idCompiler.compileFiles(sourceFiles)
    res match {
      case Some(error) =>
        println(s"Error ${error.message}")
      case None =>
    }

    //-- Check
    assertResult(None, "No Compilation Errors")(res)

    //-- Add Output to current Thread
    var initCL = Thread.currentThread().getContextClassLoader
    Thread.currentThread().setContextClassLoader(URLClassLoader.newInstance(Array(outputFolder.toURI().toURL()), Thread.currentThread().getContextClassLoader))

    //var superCl = Thread.currentThread().getContextClassLoader.loadClass(parentClassName)

    //-- Load Class
    val className = "com.idyria.osi.tea.compile.TestCompileWithDependency"
    var cl = Thread.currentThread().getContextClassLoader.loadClass(className)

    //-- Instantiate without the dependency, should fail
    Given("The Dependency Jar is missing from the Classloader")
    Then("Instance should fail with No Class Def Found")
    intercept[NoClassDefFoundError] {
      var instance = cl.newInstance()
    }

    //-- Add Dependency
    When("The JAR is added to the classloader, it MUST work")
    Thread.currentThread().setContextClassLoader(URLClassLoader.newInstance(Array(outputFolder.toURI().toURL(), dependencyJar), initCL))
    cl = Thread.currentThread().getContextClassLoader.loadClass(className)
    var instance = cl.newInstance()
    
  }

}