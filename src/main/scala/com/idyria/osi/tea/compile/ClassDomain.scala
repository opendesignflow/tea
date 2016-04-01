package com.idyria.osi.tea.compile

import java.net.URL
import java.net.URLClassLoader

/**
 * 
 * This is a URL Classloader to which URLs can be added
 * 
 * It is used to try and keep a good track of classes loaded by which class domain, the help write correct code when classes are live compiled and not get mixed up
 * between compiler classpath and current classes classloaders
 * 
 * @author zm4632
 */
class ClassDomain(arr:Array[URL],p:ClassLoader) extends URLClassLoader(arr,p) {
  
  def this(arr:Array[URL]) = this(arr,Thread.currentThread().getContextClassLoader)
  def this() = this(Array[URL](),Thread.currentThread().getContextClassLoader)
  def this(p:ClassLoader) = this(Array[URL](),p)
  
  override def addURL(u:URL) = super.addURL(u)
}