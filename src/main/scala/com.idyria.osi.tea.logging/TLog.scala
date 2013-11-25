package com.idyria.osi.tea.logging

import scala.collection.SortedMap
import scala.util.matching.Regex
import scala.reflect.ClassTag

trait TLogSource {

  def logError[T : ClassTag](msg: ⇒ String): Unit = {

    doLog[T](TLog.Level.ERROR, { () ⇒ msg })

  }

  def logWarn[T : ClassTag](msg: ⇒ String): Unit = {

    doLog[T](TLog.Level.WARNING, { () ⇒ msg })

  }

  /*def logInfo(msg: ⇒ String) : Unit = {
    logInfo[this.type]({msg})
  }*/

  /**
   * Log Infos based on a closure that will be executed only if the log Level is allowed
   */
  def logInfo[T : ClassTag ](msg: ⇒ String): Unit = {

   
      doLog[T](TLog.Level.INFO, { () ⇒ msg })


  }

  def logFine[T : ClassTag](msg: ⇒ String): Unit = {

    doLog[T](TLog.Level.FINE, { () ⇒ msg })

  }

  def logFull[T : ClassTag](msg: ⇒ String): Unit = {

    doLog(TLog.Level.FULL, { () ⇒ msg })

  }
  
  // Logger management
  //-------------------------
  def doLog[T](level: TLog.Level.Level, message: () ⇒ String)(implicit tag: ClassTag[T]): Unit = {

    // Determine Realm using class
    //------------------
    
    //-- Resolve target class to local class if no classtag has been provided by the user
    var targetClass = tag.runtimeClass match {
      case nothing if (nothing == classOf[Nothing]) =>
        //println(s"Logging info for: " + getClass)
        getClass
      case cl  =>
        //println(s"Logging info for: " + cl)
        cl
        
    }
   
    
    var realmName = targetClass.getCanonicalName() match {
      case null if (targetClass.getEnclosingClass() != null) => targetClass.getEnclosingClass().getCanonicalName()
      case null => ""
      case nm => nm
    }

    doLog(realmName, level, message)

  }
  def doLog(realm: String, level: TLog.Level.Level, message: () ⇒ String): Unit = {

    var resolvedRealm = realm match {
      case null => "undefined"
      case r    => r
    }

    //println("Logging realm: " + realm)

    TLog.levels.get(resolvedRealm) match {
      case Some(rl) if (rl >= level)           ⇒ println(s"""$resolvedRealm [$level] ${message()}""")
      case None if (level <= TLog.Level.FATAL) ⇒ println(s"""$resolvedRealm [$level] ${message()}""")
      case _                                   ⇒
    }

  }

}

object TLog {

  object Level extends Enumeration {
    type Level = Value
    val FATAL, ERROR, WARNING, INFO, FINE, FULL = Value
  }

  // Level Management
  //------------------------
  var levels = SortedMap[String, TLog.Level.Level]()

  def setLevel(cl: Class[_], level: TLog.Level.Level) = {
    levels = levels + (cl.getCanonicalName() -> level)
  }

  

}
