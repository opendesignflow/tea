package com.idyria.osi.tea

trait TeaPredef {
  
  // Options Support
  //------------------
  def withDefined[A,B](cond:ErrorOption[A])( cl: A => B) = {
    cond match {
      case ESome(a) => ESome(cl(a))
      case ENone => ENone
      case EError(e) => EError(e)
      
    }
  }
  
  def withOptionDefinedElse[A,B](cond:Option[A],elseCl: => B)(cl: A => B) : B = {
    cond match {
      case Some(a) => cl(a)
      case None => elseCl
    }
  }
  
}