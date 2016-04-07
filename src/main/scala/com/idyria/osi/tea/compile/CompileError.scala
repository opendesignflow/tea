package com.idyria.osi.tea.compile

import java.io.File

class CompileError(msg:String) extends RuntimeException(msg) {

}

class FileCompileError(var file: File, var message: String) extends CompileError(message) {

}

object CompileError {

}