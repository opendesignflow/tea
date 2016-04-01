package com.idyria.osi.tea.compile

import java.io.File

class CompileError {

}

class FileCompileError(var file: File, var message: String) extends CompileError {

}

object CompileError {

}