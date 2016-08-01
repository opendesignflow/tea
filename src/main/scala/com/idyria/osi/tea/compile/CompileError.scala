package com.idyria.osi.tea.compile

import java.io.File

class CompileError(msg:String) extends RuntimeException(msg) {

}

class FileCompileError(var message: String) extends CompileError(message) {

  var file = "<none>"
  var line = 0
  var column = 0
  var errorMessage = ""
  
  // Search for file and columns etc...
  val errorFindRegexp = """(.+):([0-9]+):\s+error:(.*)""".r
  val columnFindRegexp = """(\s+)\^""".r
  errorFindRegexp.findFirstMatchIn(message) match {
    case None => 
    case Some(res) => 
      
      // File Line
      file = res.group(1)
      line = res.group(2).toInt
      errorMessage = res.group(3)
      
      // Column
      columnFindRegexp.findFirstIn(message) match {
        
        // Column is number of spaces minus to remove the ^ character present in error message
        case Some(columnStrMatch) => 
          column = columnStrMatch.length() -1
        case None => 
      }
     
      
  }
  
}

object CompileError {

}