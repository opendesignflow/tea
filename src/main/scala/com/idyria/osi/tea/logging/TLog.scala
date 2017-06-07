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

package com.idyria.osi.tea.logging

import scala.collection.SortedMap
import scala.util.matching.Regex
import scala.reflect.ClassTag

trait TLogSource {

  def tlogEnableFull[T](implicit tag: ClassTag[T]) = {
    var t = new Throwable
    t.fillInStackTrace()
    var cause = t.getStackTrace()(2)
    
    println(s"****////***** Enabling full log for ${tag.runtimeClass} from: ${cause.getClassName}:${cause.getLineNumber} ****////*****")
    t.printStackTrace(System.out)
    TLog.setLevel(tag.runtimeClass, TLog.Level.FULL)
  }
  
  def logError[T: ClassTag](msg: => String): Unit = {

    doLog[T](TLog.Level.ERROR, { () => msg })

  }

  def logWarn[T: ClassTag](msg: => String): Unit = {

    doLog[T](TLog.Level.WARNING, { () => msg })

  }

  /*def logInfo(msg: => String) : Unit = {
    logInfo[this.type]({msg})
  }*/

  /**
   * Log Infos based on a closure that will be executed only if the log Level is allowed
   */
  def logInfo[T: ClassTag](msg: => String): Unit = {
    
    doLog[T](TLog.Level.INFO, { () => msg })

  }

  def logFine[T: ClassTag](msg: => String): Unit = {

    doLog[T](TLog.Level.FINE, { () => msg })

  }
  def isLogFine[T: ClassTag]: Boolean = {
    isLogLevel(TLog.Level.FINE, TLog.getRealm[T])
  }
  def onLogFine[T: ClassTag](cl: => Unit): Unit = {
    onLogLevel[T](TLog.Level.FINE, { () => cl })
  }

  def logFull[T: ClassTag](msg: => String): Unit = {

    doLog(TLog.Level.FULL, { () => msg })

  }

  def isLogLevel(level: TLog.Level.Level, realm: String) = {
    TLog.levels.get(realm) match {
      case Some(rl) if (rl >= level) => true
      case None if (level <= TLog.Level.WARNING) => true
      case _ => false
    }
  }

  // Logger management
  //-------------------------

  def onLogLevel[T: ClassTag](level: TLog.Level.Level, cl: () => Unit) = {
    this.isLogLevel(level, TLog.getRealm[T]) match {
      case true => cl()
      case false =>
    }
  }

  def doLog[T](level: TLog.Level.Level, message: () => String)(implicit tag: ClassTag[T]): Unit = {
    doLog(TLog.getRealm[T], level, message)

  }
  def doLog(realm: String, level: TLog.Level.Level, message: () => String): Unit = {

    var resolvedRealm = realm match {
      case null => "undefined"
      case r => r
    }

    //println("Logging realm: " + realm)

    isLogLevel(level, realm) match {
      case true => println(s"""$resolvedRealm (T:${Thread.currentThread().getId}) [$level] ${message()}""")
      case false =>
    }
    /*TLog.levels.get(resolvedRealm) match {
      case Some(rl) if (rl >= level)           => println(s"""$resolvedRealm [$level] ${message()}""")
      case None if (level <= TLog.Level.ERROR) => println(s"""$resolvedRealm [$level] ${message()}""")
      case _                                   =>
    }*/

  }

}

object TLog {

  object Level extends Enumeration {
    type Level = Value
    val FATAL, ERROR, WARNING, INFO, FINE, FULL = Value
  }

  // Realms
  //----------------
  // Realm Resolution
  //----------
  def getRealm[T](implicit tag: ClassTag[T]): String = {
    // Determine Realm using class
    //------------------

    //-- Resolve target class to local class if no classtag has been provided by the user
    var targetClass = tag.runtimeClass match {
      case nothing if (nothing == classOf[Nothing]) =>
        //println(s"Logging info for: " + getClass)
        getClass
      case cl =>
        //println(s"Logging info for: " + cl)
        cl

    }

    targetClass.getCanonicalName() match {
      case null if (targetClass.getEnclosingClass() != null) => targetClass.getEnclosingClass().getCanonicalName()
      case null => ""
      case nm => nm
    }
  }

  // Level Management
  //------------------------
  var levels = SortedMap[String, TLog.Level.Level]()

  def resetLevels = {
    levels = SortedMap[String, TLog.Level.Level]()
  }

  def setLevel(obj: Any, level: Level.Level) : Unit  = {
    levels = levels + (obj.getClass().getCanonicalName() -> level)
  }
  
  def setLevel(cl: Class[_], level: Level.Level) : Unit  = {
    levels = levels + (cl.getCanonicalName() -> level)
  }
  
  def enableFull[T](implicit tag : ClassTag[T]) = {
    this.setLevel(tag.runtimeClass, Level.FULL)
  }
  
  def getRealms = {
    this.levels.keys
  }

}
