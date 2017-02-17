package com.idyria.osi.tea.errors

import java.io.File

class TError( message:String,cause : Throwable) extends Throwable(message,cause) {

  def this(message:String) = this(message,null)
  def this(e:Throwable) = this(e.getLocalizedMessage,e)
  
  // Location
  var file: Option[String] = None
  var line: Option[Int] = None
  var column: Option[Int] = None

}