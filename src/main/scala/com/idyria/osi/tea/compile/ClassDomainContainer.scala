package com.idyria.osi.tea.compile

import com.idyria.osi.tea.listeners.ListeningSupport

trait ClassDomainContainer extends ListeningSupport {

  var parentClassDomainContainer: Option[ClassDomainContainer] = None
  var childrenClassDomains = List[ClassDomainContainer]()
  var classdomain: Option[ClassDomain] = None

  // Hierarchy Utils
  //--------------

  /**
   * Change parent container:
   * - Taint actual classdomain if parent has one to provide
   * - Create a new classdomain if needed
   */
  def changeParentClassDomainContainer(p: ClassDomainContainer) = {

    //-- Update hierarchy
    this.parentClassDomainContainer = Some(p)
    p.addChildClassDomainContainer(this)

    //-- Update classdomain
    p.classdomain match {
      case Some(parentClassDomain) =>
        this.changeParentClassDomain(parentClassDomain)
      case None =>
    }

  }

  def addChildClassDomainContainer(c: ClassDomainContainer) = {
    this.childrenClassDomains.contains(c) match {
      case true => false
      case false =>
        this.childrenClassDomains = this.childrenClassDomains :+ c
    }
  }
  
  /**
   * Returns the Class domain of parent if any
   */
  def getParentClassDomain = {
    this.parentClassDomainContainer match {
      case Some(parentCD) =>
        parentCD.classdomain
      case None => None
    }
    
    
  }
  /**
   * Get the parent classloader of the class domain
   * This can be used to compare the result of direct call to parent container's class domain, and the actual local one
   */
  def getClassDomainParent = {
    this.classdomain match {
      case Some(cd) =>
        Some(cd.getParent)
      case None => None
    }
    
    
  }

  def isChild(cd:ClassDomainContainer) : Boolean = {
    this.childrenClassDomains.contains(cd) || childrenClassDomains.find{cc => cc.isChild(cd)}.isDefined
  }
  
  // Local Utils
  //-------------------

  def taintClassDomain = {
    this.classdomain match {
      case Some(cd) => 
        this.classdomain = None
        cd.tainted = true
        cd.close
      case None =>
    }
  }

  /**
   * Create new ClassDomain
   * @param rebuild If false, won't be rebuild. This is convienient for classdomain initialisation in a constructor without triggering build actions like dependencies setup
   */
  def createNewClassDomain(p: ClassLoader,rebuild:Boolean=true) = {
    // Taint Actual
    taintClassDomain

    // Create new
    this.classdomain = Some(new ClassDomain(p))
    if(rebuild) {
      this.@->("rebuild")
    }
    
  }

  def recreateClassDomain = {

    // Create and keep old parent
    this.classdomain match {
      case None =>
        createNewClassDomain(Thread.currentThread().getContextClassLoader)
      case Some(actualCD) =>
        createNewClassDomain(actualCD.getParent)
    }

  }

  def changeParentClassDomain(pc: ClassDomain) = {

    // Tain Actual
    this.taintClassDomain

    // Create new
    this.createNewClassDomain(pc)

  }

  /**
   * When this class domain needs to be rebuild, after a taint and rebuild operation
   */
  def onRebuildClassDomain(cl: => Any) = {
    this.on("rebuild") {
      cl
    }
  }

}