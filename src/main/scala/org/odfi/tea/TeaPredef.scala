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
package org.odfi.tea

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
  
  def withEmpty[A,RT](lst:Iterable[A])(cl: PartialFunction[Option[Iterable[A]],RT]) = lst.size match {
    case 0 => cl(None)
    case other => cl(Some(lst))
  }
  
}
