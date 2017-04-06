package edu.kit.ipe.adl.ptest

trait WhenTrait {
  
  def when[T,RT](test:Option[T],cl: T => RT,other:RT):RT = test match {
    case Some(v) => cl(v)
    case None => other
  }
  
}