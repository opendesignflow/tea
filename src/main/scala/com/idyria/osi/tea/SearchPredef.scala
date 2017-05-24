package com.idyria.osi.tea

trait SearchPredef {
  
  def findByException[T](cls:Iterable[ Unit =>T ]) :Option[T] = {
    
    var res : Option[T] = None
    cls.find {
      cl => 
        try {
          res = Some(cl())
          true
        } catch {
          case e : Throwable => 
            false
        }
    }
    res
    
  }
  def findByExceptionNotNull[T](cls:Iterable[ () =>T ]) :Option[T] = {
    
    var res : Option[T] = None
    cls.find {
      cl => 
        try {
          cl() match {
            case null => false
            case other => 
              res = Some(other)
              true
          }
        } catch {
          case e : Throwable => 
            false
        }
    }
    res
    
  }
  
  
}