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
package compile

import org.odfi.tea.compile.IDCompiler

import java.io.File
import org.odfi.tea.file.DirectoryUtilities
import org.scalatest.BeforeAndAfter
import org.scalatest.GivenWhenThen
import org.scalatest.funsuite.AnyFunSuite

import java.lang.reflect.InvocationTargetException
import java.net.URLClassLoader

class CompileTest extends AnyFunSuite with BeforeAndAfter with GivenWhenThen {

  val sourceFolder = new File("src/test/resources/compile")
  val outputFolder = new File("build/tco")
  val startupCL = getClass.getClassLoader

  before {
    outputFolder.mkdirs()
    DirectoryUtilities.deleteDirectoryContent(outputFolder)
    Thread.currentThread().setContextClassLoader(startupCL)
  }

  def createCompilerWithOutput = {
    var idCompiler = new IDCompiler
    //-- Set Source and Output
    idCompiler.setCompilerOutput(outputFolder)
    idCompiler
  }

  test("Startup and Classloader") {

    println("DefaultCL: " + Thread.currentThread().getContextClassLoader)
    var idCompiler = createCompilerWithOutput

    Thread.sleep(2000)

    println("END CL: " + Thread.currentThread().getContextClassLoader)
  }

  test("Compilation Source and Output check") {

    //-- Create Compiler
    var idCompiler = createCompilerWithOutput


    //-- Compile Files, check output
    var sourceFile = new File(sourceFolder, "TestCompile.scala").getAbsoluteFile

    var res = idCompiler.compileFile(sourceFile)
    assert(res.isLeft, "Compilation was not successfull")
    /*
    res match {
      case Some(error) =>
        println(s"Error ${error.message}")
        println(s"CP: " + idCompiler.settings2.classpath)
      case None =>
    }*/

    //-- No Errors
    // assertResult(None, "No Compilation Errors")(res)
    /* println(s"$res")
    println(s"Settings: "+idCompiler.settings2.outputDirs.outputs)
    println(s"Settings: "+idCompiler.settings2.outputDirs.getSingleOutput)*/
    //Thread.sleep(5000)

    //-- Check output content
    var classFile = new File(outputFolder, "org.odfi.tea.compile.TestCompile".replace(".", File.separator) + ".class")
    assert(classFile.exists(), "Class File must be present in output")

  }

  test("Class Loading Works with Classloader for output folder and parent current Thread CL (to get URL classpath set at startup)") {

    //-- Compile File
    var idCompiler = createCompilerWithOutput
    var sourceFile = new File(sourceFolder, "TestCompile.scala").getAbsoluteFile

    //-- Try to Load class
    idCompiler.compileFile(sourceFile)

    //-- Class Name 
    val className = "org.odfi.tea.compile.TestCompile"

    //-- Add Output to current Thread
    Thread.currentThread().setContextClassLoader(URLClassLoader.newInstance(Array(outputFolder.toURI().toURL()), Thread.currentThread().getContextClassLoader))
    Thread.currentThread().getContextClassLoader.loadClass(className)

  }

  test("Class Cast Works if Classloading works") {

    //-- Compile File
    var idCompiler = createCompilerWithOutput
    var sourceFile = new File(sourceFolder, "TestCompile.scala").getAbsoluteFile

    //-- Try to Load class
    idCompiler.compileFile(sourceFile)

    //-- Class Name 
    val className = "org.odfi.tea.compile.TestCompile"

    //-- Add Output to current Thread
    Thread.currentThread().setContextClassLoader(URLClassLoader.newInstance(Array(outputFolder.toURI().toURL()), Thread.currentThread().getContextClassLoader))
    var cl = Thread.currentThread().getContextClassLoader.loadClass(className)

    //-- Cast
    cl.asInstanceOf[Class[TestCompileParent]]

  }

  test("Compile a subclass of a class in extra source Folder") {

    //-- Compile File
    var idCompiler = createCompilerWithOutput
    var sourceFiles = List(new File(sourceFolder, "TestCompileSubclass.scala").getAbsoluteFile, new File(sourceFolder, "TestCompile.scala").getAbsoluteFile)

    //-- Try to Load class
    var res = idCompiler.compileFiles(sourceFiles)
    res match {
      case Right(error) =>
        println(s"Error ${error.getLocalizedMessage}")
      case other =>
    }

    //-- Check
    assert(res.isLeft, "No Compilation Errors")

    //-- Class Name 
    val parentClassName = "org.odfi.tea.compile.TestCompile"
    val className = "org.odfi.tea.compile.TestCompileSublass"

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
    var idCompiler = createCompilerWithOutput
    var sourceFiles = new File(sourceFolder, "TestCompileWithDependency.scala").getAbsoluteFile

    //-- Try to Load class 
    var res = idCompiler.compileFile(sourceFiles)
    res match {
      case Right(error) =>
        println(s"Error ${error.getLocalizedMessage}")
        throw error
      case Left(result) =>
        //-- Check
        assert(result.hasErrors, "Expected Compilation Errors")
    }


  }

  test("Class Compile with dependency, success if Dep Jar is set") {

    //-- Compile File
    var idCompiler = createCompilerWithOutput
    var sourceFiles = new File(sourceFolder, "TestCompileWithDependency.scala").getAbsoluteFile

    //-- Add jar to ClassPath
    idCompiler.addClasspathURL(new File(sourceFolder, "bridj-0.7.0.jar").toURI().toURL())

    //-- Try to Load class 
    var res = idCompiler.compileFile(sourceFiles)
    res match {
      case Right(error) =>
        println(s"Error ${error.getLocalizedMessage}")
      case other =>
    }

    //-- Check
    assert(res.isLeft, "No Compilation Errors")

  }

  test("Class Compile with dependency, instance requires Context Class Loader update") {

    //-- Compile File
    var idCompiler = createCompilerWithOutput
    var sourceFile = new File(sourceFolder, "TestCompileWithDependency.scala").getAbsoluteFile

    //-- Add jar to ClassPath
    var dependencyJar = new File(sourceFolder, "bridj-0.7.0.jar").toURI().toURL()
    idCompiler.addClasspathURL(dependencyJar)

    //-- Try to Load class 
    var res = idCompiler.compileFile(sourceFile)
    res match {
      case Right(error) =>
        println(s"Error ${error.getLocalizedMessage}")
        throw error
      case Left(res) =>

        //-- Check no compilation errors
        assert(!res.hasErrors, "Compilation Errors")

        //-- Add Output to current Thread
        var initCL = Thread.currentThread().getContextClassLoader
        Thread.currentThread().setContextClassLoader(URLClassLoader.newInstance(Array(outputFolder.toURI().toURL()), Thread.currentThread().getContextClassLoader))

        //var superCl = Thread.currentThread().getContextClassLoader.loadClass(parentClassName)

        //-- Load Class
        val className = "org.odfi.tea.compile.TestCompileWithDependency"
        var cl = Thread.currentThread().getContextClassLoader.loadClass(className)

        //-- Instantiate without the dependency, should fail
        Given("The Dependency Jar is missing from the Classloader")
        Then("Instance should fail with No Class Def Found")
        intercept[InvocationTargetException] {
          var instance = cl.getDeclaredConstructor().newInstance()
        }

        //-- Add Dependency
        When("The JAR is added to the classloader, it MUST work")
        Thread.currentThread().setContextClassLoader(URLClassLoader.newInstance(Array(outputFolder.toURI().toURL(), dependencyJar), initCL))
        cl = Thread.currentThread().getContextClassLoader.loadClass(className)
        var instance = cl.getDeclaredConstructor().newInstance()
    }



  }

}
