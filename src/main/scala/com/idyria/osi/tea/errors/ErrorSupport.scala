package com.idyria.osi.tea.errors

import scala.collection.mutable.Stack
import scala.reflect.ClassTag

trait ErrorSupport {

  var errors = Stack[Throwable]()

  // Error Add/Remove
  //-----------
  def addError(e: Throwable) = {
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
  
  /**
   * Run somethign and catch error on target object
   * Error is transmitted
   */
  def catchErrorsOn[RT](target: ErrorSupport)(cl: => RT): RT = {

    try {
      target.resetErrors
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
      target.resetErrors
      Some(cl)
    } catch {
      case e: Throwable =>
        if(verbose)
          e.printStackTrace()
        target.addError(e)
        None
    }
  }

}