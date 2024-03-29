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
package org.odfi.tea.errors

import java.io.File

class TError(message: String, cause: Throwable) extends Throwable(message, cause) {

  def this(message: String) = this(message, null)

  def this(e: Throwable) = this(e.getLocalizedMessage, e)

  // Location
  var file: Option[String] = None
  var line: Option[Int] = None
  var column: Option[Int] = None

}


class TImmediateError(message: String, cause: Throwable) extends TError(message, cause) {
  def this(message: String) = this(message, null)

  def this(e: Throwable) = this(e.getLocalizedMessage, e)
}