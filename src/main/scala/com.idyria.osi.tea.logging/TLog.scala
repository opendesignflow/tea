package com.idyria.osi.tea.logging

import scala.collection.SortedMap
import scala.util.matching.Regex

trait TLogSource {

  def logError(msg: ⇒ String): Unit = {

    TLog.doLog(getClass, TLog.Level.ERROR, { () ⇒ msg })

  }

  def logWarn(msg: ⇒ String): Unit = {

    TLog.doLog(getClass, TLog.Level.WARNING, { () ⇒ msg })

  }

  /**
   * Log Infos based on a closure that will be executed only if the log Level is allowed
   */
  def logInfo(msg: ⇒ String): Unit = {

    TLog.doLog(getClass, TLog.Level.INFO, { () ⇒ msg })

  }

  def logFine(msg: ⇒ String): Unit = {

    TLog.doLog(getClass, TLog.Level.FINE, { () ⇒ msg })

  }

  def logFull(msg: ⇒ String): Unit = {

    TLog.doLog(getClass, TLog.Level.FULL, { () ⇒ msg })

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

  // Logger management
  //-------------------------
  def doLog(realm: Class[_], level: TLog.Level.Level, message: () ⇒ String): Unit = {

    var realmName = realm.getCanonicalName() match {
      case null if (realm.getEnclosingClass()!=null)=> realm.getEnclosingClass().getCanonicalName()
      case null => ""
      case nm => nm
    }
    
    doLog(realmName,level,message)
    
  }
  def doLog(realm: String, level: TLog.Level.Level, message: () ⇒ String): Unit = {

    var resolvedRealm = realm match {
      case null => "undefined"
      case r => r
    }
    
   //println("Logging realm: " + realm)

    levels.get(resolvedRealm) match {
      case Some(rl) if (rl >= level)           ⇒ println(s"""$resolvedRealm [$level] ${message()}""")
      case None if (level <= TLog.Level.FATAL) ⇒ println(s"""$resolvedRealm [$level] ${message()}""")
      case _                                   ⇒
    }

  }

}
