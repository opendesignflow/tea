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
package com.idyria.osi.tea.compile

import java.net.URL
import java.net.URLClassLoader
import com.sun.org.apache.bcel.internal.generic.LoadClass
import java.io.File
import com.idyria.osi.tea.io.TeaIOUtils
import java.io.FileInputStream

/**
 *
 * This is a URL Classloader to which URLs can be added
 *
 * It is used to try and keep a good track of classes loaded by which class domain, the help write correct code when classes are live compiled and not get mixed up
 * between compiler classpath and current classes classloaders
 *
 * @author zm4632
 */
class ClassDomain(arr: Array[URL], p: ClassLoader) extends URLClassLoader(arr, p) {

  var name = "ClassDomain"
  var tainted = false
  def isTainted = tainted

  def this(arr: Array[URL]) = this(arr, Thread.currentThread().getContextClassLoader)
  def this() = this(Array[URL](), Thread.currentThread().getContextClassLoader)
  def this(p: ClassLoader) = this(Array[URL](), p)

  override def addURL(u: URL) = super.addURL(u)


  override def loadClass(name: String) : Class[_] = {
    try {
      super.loadClass(name)
    } catch {
      // Try in dependent CD
      case nodef: NoClassDefFoundError =>
        var search = nodef.getLocalizedMessage.replace('/','.')
        println(s"Missing Def: "+nodef.getLocalizedMessage+ "-> "+search)
        dependsOnClassDomain.find {
          cd =>
            try {
              cd.loadClass(search)
              true
            } catch {
              case e: NoClassDefFoundError =>
                false
            }
        } match {
          case Some(cd) => 
            this.resolveClass(cd.loadClass(search))
            cd.loadClass(search)
            //super.loadClass(name)
          case _ => throw nodef
        }

    }
  }
  
  def loadClassFromFile(name:String,f:File) = {
    
    var bytes = TeaIOUtils.swallow(new FileInputStream(f))
    
    defineClass(name, bytes, 0 , bytes.length)
    
    
    
  }


  override def toString = {
    s"ClassDomain $name (tainted=$tainted) :" + hashCode()
  }

  // Dep CD
  //-----------------
  var dependsOnClassDomain = List[ClassDomain]()

  def addClassDomain(cd: ClassDomain) = {
    this.dependsOnClassDomain = this.dependsOnClassDomain :+ cd
  }

}
