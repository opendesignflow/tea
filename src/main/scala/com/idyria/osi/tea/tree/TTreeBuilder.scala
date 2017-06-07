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
package com.idyria.osi.tea.tree

/**
 * Builder fr single root tree
 */
trait TTreeBuilder[T <: TTreeNode[T]] {
  
  
  var tNodeStack = new scala.collection.mutable.ArrayStack[T]()
  
  
  def onNode(n:T) = {
    
  }
  
  /**
   * Add to current top if necessary
   * Push on Stack
   */
  def pushNode(n:T) = {
    
    println("Push node: "+n)
    tNodeStack.headOption match {
      case Some(top) =>
        top.addChild(n)
      case other => 
    }
    
    this.tNodeStack.push(n)
    
    n
  }
  
  /**
   * Pop Node if currently on top
   */
  def popNode(n:T) = {
    tNodeStack.headOption match {
      case Some(top) if(tNodeStack.size>1) =>
        
        tNodeStack.pop()
        println("Removing, head now: "+tNodeStack.head)
      case other =>
        println("Statck size is: "+tNodeStack.size)
        
    }
    n
  }
  
}
