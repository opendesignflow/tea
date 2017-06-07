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
package com.idyria.osi.tea.errors

import scala.collection.mutable.Stack
import scala.reflect.ClassTag
import com.idyria.osi.tea.TeaPredef

trait ErrorSupport extends TeaPredef {

  var errors = Stack[Throwable]()

  // Error Add/Remove
  //-----------
  def addError(e: Throwable) = this.errors.contains(e) match {
    case true => 
    case false => 
      this.errors.push(e)
  }
  
  def resetErrors = this.errors.clear()

  def resetErrorsOfType[TT <: Throwable](implicit tag : ClassTag[TT]) = {
    this.errors = this.errors.filter { err => !tag.runtimeClass.isInstance(err) }
  }
  
  // Errors get
  //---------------
  def hasErrors = this.errors.size match {
    case 0 => false
    case _ => true
  }

  def getLastError = this.errors.headOption
  
  def getErrorsOfType[ET <: Throwable](implicit tag : ClassTag[ET]) = {
    
    this.errors.collect {
      case err if (tag.runtimeClass.isInstance(err))=>err.asInstanceOf[ET]
    }.toList
    
  }

  // Comsume etc..
  //------------------

  def consumeErrors(cl: Throwable => Unit) = {
    while(!errors.isEmpty) {
      cl(errors.pop)
    }
  }
  
  def consumePrintErrorsToStdErr = {
    this.consumeErrors {
      e => 
        e.printStackTrace(System.err)
    }
  }
  
  /**
   * Run somethign and catch error on target object
   * Error is transmitted
   */
  def catchErrorsOn[RT](target: ErrorSupport)(cl: => RT): RT = {

    try {
      //target.resetErrors
      cl
    } catch {
      case e: Throwable =>
        target.addError(e)
        throw e
    }
  }

  /**
   * Catch errors but don't transmit them
   * Closure returns None in that case
   */
  def keepErrorsOn[RT](target: ErrorSupport,verbose:Boolean=false)(cl: => RT): Option[RT] = {
    try {
     // target.resetErrors
      Some(cl)
    } catch {
      case e: Throwable =>
        verbose match {
          case true => 
             e.printStackTrace()
          case false => 
        }
         
        target.addError(e)
        None
    }
  }
  
  def ignoreErrors[RT](cl: => RT) : Option[RT]  = {
    try {
     // target.resetErrors
      Some(cl)
    } catch {
      case e: Throwable =>
        None
    }
  }

}
