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
package com.idyria.osi.tea.listeners

import scala.reflect.ClassTag

trait ListenerTransient {
  
}

/**
 *
 * Trait to be implemented by classes whishing to offer support for listening points
 *
 * FIXME: Documentation here:
 *
 * @odfi-doc
 *
 *
 *
 */
trait ListeningSupport {

  // Register Closures
  //-------------------

  // All the registered closures
  var listeningPoints = Map[String, scala.collection.mutable.Set[(() => Unit)]]()

  var listeningPointsWith = Map[String, scala.collection.mutable.Set[(Any => Unit)]]()

  // Deregister
  //---------------
  
  /**
   * Deregister a closure from where it could be
   */
  def deregister(listener: AnyRef) = {

    listeningPoints.foreach {
      case (id, closures) =>
        
        closures.find( c => c==listener) match {
          case Some(toRemove) => closures.remove(toRemove)
          case None => 
        }
       
    }
    
    listeningPointsWith.foreach {
      case (id, closures) =>
        
        closures.find( c => c==listener) match {
          case Some(toRemove) => closures.remove(toRemove)
          case None => 
        }
       
    }

  }

  /**
   * Used to provide a closure to be run on a specific listening point
   *
   * Example:
   *
   * instance.on "listening.point.name" {
   * println("Hello World from closure")
   * }
   *
   */
  def on(point: String)(closure: => Unit) : (() => Unit) = {

    // println("Registering Closure for point "+point)

    // Prepare Final closure
    var wrappedClosure = { () => closure }

    // - Create Set if non existend
    // - Update otherwise
    //-------------------
    this.listeningPoints.get(point) match {
      case Some(set) => set += wrappedClosure
      case None => {

        this.listeningPoints += (point -> scala.collection.mutable.Set[(() => Unit)](wrappedClosure))

        //println("Setting up set, size is now: "+this.listeningPoints.size)
      }
    }

    // Return reference to closure
    return wrappedClosure
  }

  def onWith[T <: Any](point: String)(closure: T => Unit)(implicit tag:ClassTag[T]): T => Unit = {

    // println("Registering Closure for point "+point)

    // Prepare closure
    var cl = { 
      (in: Any) => 
        //if (tag.runtimeClass.isAssignableFrom(in.getClass())) {
          closure(in.asInstanceOf[T]) 
        //}
        
    }
    
    // - Create Set if non existend
    // - Update otherwise
    //-------------------
    this.listeningPointsWith.get(point) match {
      case Some(set) => 
        set += cl
        //this.listeningPointsWith += (point -> scala.collection.mutable.Set[(Any => Unit)](cl))
      case None => {

        this.listeningPointsWith += (point -> scala.collection.mutable.Set[(Any => Unit)](cl))

        //println("Setting up set, size is now: "+this.listeningPoints.size)
      }
    }
    return cl
  }
  
  /**
   * Listener removed after one hit
   */
  def onWithTransient[T <: Any](point: String)(closure: T => Unit)(implicit tag:ClassTag[T]): T => Unit = {

    var realCl = new Function1[T,Unit] with ListenerTransient {
      def apply(v:T) = {
        closure(v)
      }
    }
    
    var id  = this.onWith[T](point) {
      v=> 
      realCl(v)
    }
    
    id
  }

  def onMatch(point: String)(closure: PartialFunction[Any, Unit]) : Any => Unit = {

    // println("Registering Closure for point "+point)

    var cl = { (in: Any) => closure(in) }
    
    // - Create Set if non existend
    // - Update otherwise
    //-------------------
    this.listeningPointsWith.get(point) match {
      case Some(set) => set += cl
      case None => {

        this.listeningPointsWith += (point -> scala.collection.mutable.Set[(Any => Unit)](cl))

        //println("Setting up set, size is now: "+this.listeningPoints.size)
      }
    }
    
    return cl
  }

  // Call Listenening point
  //-----------------

  /**
   * This method is used to call the closures registered for the listenerning point
   *
   * @param point The Listenening point name to be called
   */
  def @->[T <: Any](point: String) = {

    //println("Running point "+point+", size: "+this.listeningPoints.size)

    this.listeningPoints.get(point) match {
      case Some(set) => set.foreach(cl => cl())
      case None      =>
    }
  }

  /**
   * Type mismatch between listener and event triggers are just caught
   */
  def @->[T <: Any](point: String, input: T) = {

    //println("Running point "+point+", size: "+this.listeningPoints.size)

    this.listeningPointsWith.get(point) match {
      case Some(set) => set.foreach {
       cl =>
         // Class Cast exception might happend if listeners are listening to the wrong type so catch it
         try {
          cl(input)
         } catch {
           case e : java.lang.ClassCastException => 
             
         } finally {
           if (cl.isInstanceOf[ListenerTransient]) {
             deregister(cl)
           }
         }
      }
      case None      =>
    }
  }

}
