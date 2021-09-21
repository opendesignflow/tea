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

import org.odfi.tea.listeners.ListeningSupport

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
