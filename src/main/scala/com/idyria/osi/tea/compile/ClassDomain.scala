package com.idyria.osi.tea.compile

import java.net.URL
import java.net.URLClassLoader
import com.sun.org.apache.bcel.internal.generic.LoadClass

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
