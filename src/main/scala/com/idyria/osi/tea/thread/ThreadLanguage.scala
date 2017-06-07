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
package com.idyria.osi.tea.thread

/**
 * A trait to provide a few utility methods to run code on Threads
 */
trait ThreadLanguage {
  
  def fork(cl: => Any) : Thread = {
    
    //-- Create Thread in local thread group
    var th = new Thread(new Runnable {
      def run() = {
        cl
      }
    })
    //th.getThreadGroup()
    th.start
    th
    
  }
  
  /**
   * Forks the provided closure in a Thread and waits for it to end
   */
  def forkJoin[T](cl: => T) :T = {
    
    //-- Create Thread in local thread group
    var res : Any = null
    var th = new Thread(new Runnable {
      def run() = {
        res = cl
      }
    })
    //th.getThreadGroup()
    th.start
    th.join
    
    res.asInstanceOf[T]
  }
  
  def createThread (cl: => Any) : Thread = {
    var th = new Thread(new Runnable {
      def run() = {
        cl
      }
    })
    th
  }
  
}
