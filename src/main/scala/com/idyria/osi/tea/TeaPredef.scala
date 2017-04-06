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
  
  def withDefinedNoError[A,B](cond:ErrorOption[A])( cl: A => B) = {
    cond match {
      case ESome(a) => ESome(cl(a))
      case ENone => ENone
      case EError(e) => throw e
      
    }
  }
  
  
  
  /**
   * If condition is not defined, create it
   */
  def withNotDefined[T](cond:ErrorOption[_])( cl: => T) = {
    cond match {
      case ENone => ESome(cl)
      case other => ENone
      
    }
  }
  
  /**
   * Run closure if Error Option is ENone
   */
  def onNotDefined[A](cond:ErrorOption[_])( cl: => Unit) : Unit = {
    cond match {
      case ENone => cl
      case other => 
      
    }
  }
  
  def withOptionDefinedElse[A,B](cond:Option[A],elseCl: => B)(cl: A => B) : B = {
    cond match {
      case Some(a) => cl(a)
      case None => elseCl
    }
  }
  
  def withEmpty[A](lst:Iterable[A])(cl: PartialFunction[Option[Iterable[A]],Unit]) = lst.size match {
    case 0 => cl(None)
    case other => cl(Some(lst))
  }
  
}