package com.idyria.osi.tea.errors

import scala.collection.mutable.Stack

trait ErrorSupport {

  var errors = Stack[Throwable]()

  // Error Add/Remove
  //-----------
  def addError(e: Throwable) = {
    this.errors.push(e)
  }
  
  def resetErrors = this.errors.clear()

  // Errors get
  //---------------
  def hasErrors = this.errors.size match {
    case 0 => false
    case _ => true
  }

  def getLastError = this.errors.headOption

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
  def keepErrorsOn[RT](target: ErrorSupport)(cl: => RT): Option[RT] = {
    try {
      target.resetErrors
      Some(cl)
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        target.addError(e)
        None
    }
  }

}