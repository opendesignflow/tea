package com.idyria.osi.tea.compile

import java.net.URL
import java.net.URLClassLoader

trait ClassDomainSupport {



  /**
   * Returns a list with 0 -> n, 0 beein the parent top most clasloader
   * The Result includes the testCl itself
   */
  def getClassLoaderHierary(testCl: ClassLoader) = {
    require(testCl != null, "Cannot create classloader hierarchy of null classloader")

    //-- Init 
    var currentCL = testCl
    var res = List(currentCL)

    //-- Go through parents 
    while (currentCL.getParent != null) {
      res = res :+ currentCL
      currentCL = currentCL.getParent
    }

    res.reverse

  }

  def getClassLoaderCrossHierarchyURLS(testCl: ClassLoader) = {
    //-- Get Hierarchy
    var hierarchy = this.getClassLoaderHierary(testCl)
    var urlClassloaderHierarchy = hierarchy.collect { case cl: URLClassLoader => cl }

    //-- Gather all URLS
    var allHierarchyURLS = urlClassloaderHierarchy.map { cl => cl.getURLs }.flatten
    
    allHierarchyURLS

  }

  def isClassLoaderChildOf(child: ClassLoader, parent: ClassLoader): Boolean = {

    //--Look in testCL hierarchy, if the parent is present 
    getClassLoaderHierary(child).contains(parent)

  }

  def isClassLoaderParentOf(parent: ClassLoader, child: ClassLoader): Boolean = {

    //--Look in testCL hierarchy, if the parent is present 
    getClassLoaderHierary(child).contains(parent)

  }

  /**
   * This Method makes sure the Current Thread Classloader has the Output Path into its output URLS
   * If not, it will force updating the classloader
   *
   * If a target class is provided, and this class is already into a ClassDomain, the current Thread ClassLoader is resolved to make sure it is correctly set
   */
  def getClassLoaderFor(targetClass: Class[_]): ClassLoader = {

    //-- Check Class Classloader and compare to current Thread
    val currentThreadClassloader = Thread.currentThread().getContextClassLoader
    targetClass.getClassLoader match {

      //-- Class has same classloader, then nothing to do
      case classClassLoader if (classClassLoader == currentThreadClassloader) => currentThreadClassloader

      //-- Class has a classloader which is a child of the current classloader, return the class's classloader
      case classClassLoader if (isClassLoaderChildOf(classClassLoader, currentThreadClassloader)) => classClassLoader

      //-- Class has a classloader which is a parent of context, then return context classloader
      case classClassLoader if (isClassLoaderParentOf(classClassLoader, currentThreadClassloader)) => currentThreadClassloader

      case classClassLoader =>
        classClassLoader
      //throw new RuntimeException("getting ClassLoader for class is in an unknown case ")
    }
    /*
    
    //-- Add Output to CurrentClassLoader, or Update ClassLoader
    Thread.currentThread().getContextClassLoader match {
      case cl: ClassDomain =>

        /*var eout = new File("eout")
      eout.mkdirs()
      compiler.settings2.outputDirs.setSingleOutput(eout.getAbsolutePath)
      
      println(s"Adding output to cl: "+this.compiler.settings2.outputDirs.getSingleOutput.get.file)*/
        // cl.addURL(new File(this.compiler.settings2.outdir.value).getAbsoluteFile.toURI().toURL())

        // cl.addURL(this.compiler.settings2.outputDirs.getSingleOutput.get.file.getAbsoluteFile.toURI().toURL())
        //cl.addURL(this.outputClassesFolder.toURI().toURL())
      case other =>

        //var cl = new ExtensibleURLClassLoader(Thread.currentThread().getContextClassLoader)
        //cl.addURL(this.outputClassesFolder.toURI().toURL())
        //Thread.currentThread().setContextClassLoader(cl)

      //println(s"Single Output: "+this.compiler.settings2.outputDirs.getSingleOutput.get.path)
      //cl.addURL(this.compiler.settings2.outputDirs.getSingleOutput.get.file.getAbsoluteFile.toURI().toURL())

    }
    
    null*/
  }

  /**
   * Get A classloader satisfying the provided URLs
   *
   * If all the urls can be found in the ClassDomain Chain, then return the current classloader
   * If not, create a new ClassDomain
   *
   */
  def getClassLoaderFor(urls: Array[URL]) = {

    //println(s"Ensuring ${urls.toList} are in the classloader")

    //-- Get Hierarchy
    var hierarchy = this.getClassLoaderHierary(Thread.currentThread().getContextClassLoader)
    var urlClassloaderHierarchy = hierarchy.collect { case cl: URLClassLoader => cl }

    //-- Gather all URLS
    var allHierarchyURLS = urlClassloaderHierarchy.map { cl => cl.getURLs }.flatten

    //-- Check all input urls are available
    //-- Collect all which are not already defined
    urls.collect { case inputURL if (allHierarchyURLS.find { u => u.toExternalForm() == inputURL.toExternalForm() } == None) => inputURL } match {

      //-- Everything is already provided, then return context classlaoder
      case nonDefined if (nonDefined.size == 0) =>

        Thread.currentThread().getContextClassLoader

      //-- if something is missing, then create a new ClassDomain
      case nonDefined =>
        // println(s"URLS $nonDefined are missing")
        var domain = new ClassDomain(nonDefined, Thread.currentThread().getContextClassLoader)
        domain

    }

  }

  /**
   * Runs a closure and enforces the Thread classloader to be compatible with the target class
   */
  def withClassLoader[RT](cl: ClassLoader)(run: => RT): RT = {

    //-- Get CL
    var classLoader = cl

    //-- Run Closure with correct Thread Classloader
    val oldCL = Thread.currentThread().getContextClassLoader
    try {
      Thread.currentThread().setContextClassLoader(classLoader)
      run

    } finally {
      Thread.currentThread().setContextClassLoader(oldCL)
    }

  }

  /**
   * Runs a closure and enforces the Thread classloader to be compatible with the target class
   */
  def withClassLoaderFor[RT](cl: Class[_])(run: => RT): RT = {

    //-- Get CL
    var classLoader = this.getClassLoaderFor(cl)
    //println(s"Running withClassLoaderFor with cl: $classLoader")
    withClassLoader(classLoader)(run)

  }

  /**
   * Creates and instance of provided class, while enforcign the class loader
   */
  def instanceOfClass[U <: Any](cl: Class[_]): U = {

    this.withClassLoaderFor(cl) {
      cl.newInstance().asInstanceOf[U]
    }

  }

  /**
   * Make Sure the provided URLS are available in Classloader context and run Closure
   */
  def withURLInClassloader[RT](arr: Array[URL])(run: => RT): RT = {

    var classLoader = this.getClassLoaderFor(arr)
    this.withClassLoader(classLoader)(run)

  }
  def withURLInClassloader[RT](arr: URL*)(run: => RT): RT = withURLInClassloader(arr.toArray)(run)

}
