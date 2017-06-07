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
package com.idyria.osi.tea.compile

import java.io.File
import com.idyria.osi.tea.errors.TError

class CompileError(msg:String) extends TError(msg) {

}

class FileCompileError(var message: String) extends CompileError(message) {

  //var file = "<none>"
  //var line = 0
 // var column = 0
  var errorMessage = ""
  
  // Search for file and columns etc...
  val errorFindRegexp = """(.+):([0-9]+):\s+error:(.*)""".r
  val columnFindRegexp = """(\s+)\^""".r
  errorFindRegexp.findFirstMatchIn(message) match {
    case None => 
    case Some(res) => 
      
      // File Line
      file = Some(res.group(1))
      line = Some(res.group(2).toInt)
      errorMessage = res.group(3)
      
      // Column
      columnFindRegexp.findFirstIn(message) match {
        
        // Column is number of spaces minus to remove the ^ character present in error message
        case Some(columnStrMatch) => 
          column = Some(columnStrMatch.length() -1)
        case None => 
      }
     
      
  }
  
}

object CompileError {

}
