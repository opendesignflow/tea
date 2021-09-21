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
/*
import org.scalatest.GivenWhenThen
import org.scalatest.BeforeAndAfter
import org.odfi.tea.file.DirectoryUtilities
import org.scalatest.funsuite.AnyFunSuite

import java.io.File
import java.net.URLClassLoader

class CompileClassDomainTest  extends AnyFunSuite with GivenWhenThen with BeforeAndAfter  {
  
  
  val sourceFolder = new File("src/test/resources/compile")
  val outputFolder = new File("target/tco")
  val startupCL = getClass.getClassLoader
  
  before {
    println(s"Doing before")
    outputFolder.mkdirs()
    DirectoryUtilities.deleteDirectoryContent(outputFolder)
    Thread.currentThread().setContextClassLoader(startupCL)
  }
  
  /**
   * 
   */
  test("Compile a new simple class, enforce current thread updating") {
    
    //-- Create Compiler
    var idCompiler = new IDCompiler
    idCompiler.addSourceOutputFolders(sourceFolder -> outputFolder)

    //-- Compile Files
    var sourceFile = new File(sourceFolder, "TestCompile.scala").getAbsoluteFile
    idCompiler.compileFile(sourceFile)
    
    //-- Load class
    var initClassLoader = Thread.currentThread().getContextClassLoader
    Given("A correct classloader, load the class")
    val className = "org.odfi.tea.compile.TestCompile"
    val loadClassDomain = new ClassDomain(Array(outputFolder.toURI().toURL()), startupCL)
    Thread.currentThread().setContextClassLoader(loadClassDomain)
    var cl = Thread.currentThread().getContextClassLoader.loadClass(className)
    println(s"${cl.getClassLoader.isInstanceOf[ClassDomain]}")
    
    
    //-- Then Go Back 
    When("Reset Classloader to initial state")
    Thread.currentThread().setContextClassLoader(startupCL)
    var classdomainSupport = new ClassDomainSupport {
      
    }
    Then("get classloader should determine usage of previous Class Domain")
    var resClassloader = classdomainSupport.getClassLoaderFor(cl)
    assertResult(loadClassDomain)(resClassloader)
    
    //-- Now instantiate class with enforced classloader
    And("Instantiating should work with no typing")
    var obj = classdomainSupport.instanceOfClass[Object](cl)
    
    And("also with parent typing")
    var obj2 = classdomainSupport.instanceOfClass[TestCompileParent](cl)
    /*classdomainSupport.withClassLoaderFor(cl) {
      //var obj2 = classdomainSupport.instanceOfClass[TestCompileParent](cl)
      var parentClass = classOf[TestCompileParent]
      var objCl = classdomainSupport.getClassLoaderFor(cl)
      
      println(s"Child: "+classdomainSupport.isClassLoaderChildOf(loadClassDomain, startupCL))
      var obj2 = obj.asInstanceOf[TestCompileParent]
    }*/
    
  }
  
  /**
   * 
   */
  test("Enforce Compiler Output in Classloader") {
    
    //-- Create Compiler
    var idCompiler = new IDCompiler
    idCompiler.addSourceOutputFolders(sourceFolder -> outputFolder)

    //-- Compile Files
    var sourceFile = new File(sourceFolder, "TestCompile.scala").getAbsoluteFile
    idCompiler.compileFile(sourceFile)
    
    //-- Enforce Classloader output, and load class based on this
    Given("The Class has been compiled")
    Then("Use the withURLInClassLoader function to ensure Compiler Output is in classloader")
    var classdomainSupport = new ClassDomainSupport {
      
    }
    classdomainSupport.withURLInClassloader(outputFolder.toURI().toURL()) {
      val className = "org.odfi.tea.compile.TestCompile"
      Thread.currentThread().getContextClassLoader.loadClass(className).getDeclaredConstructor().newInstance()
    }
    
    
  }
  
  
}
*/