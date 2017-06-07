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
package com.idyria.osi.tea.progress

import com.idyria.osi.tea.listeners.ListeningSupport

trait ProgressSupport extends ListeningSupport {
  
  // Progress
  //------------ 
  def progressInit(operation: String) = {
    this.@->("progress.init", operation)
  }
  def onProgressInit(cl: String => Unit) = {
    this.onWith("progress.init") {
      m: String => cl(m)
    }
  }
  def progressUpdate(p: Double) = {
    //println(s"MG Progress update..."+this.listeningPointsWith.size)
    this.@->("progress.update", p)
  }
  def onProgressUpdate(cl: Double => Unit) = {

    //println(s"MG Registering Progress update...")
    this.onWith("progress.update") {
      d: Double =>
        // println(s"MG received updated")
        cl(d)
    }
  }
  
}
