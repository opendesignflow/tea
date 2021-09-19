package org.odfi.tea.compile

import dotty.tools.dotc
import dotty.tools.dotc.Driver
import dotty.tools.dotc.interfaces.{AbstractFile, CompilerCallback, SourceFile}

import java.io.File



object TryIDCompilerS3 extends App {

  println("Hello")

  val src = new File("src/test/resources/compile/TestSimpleCompile.scala")
  val out = new File("build/out").getCanonicalFile
  out.mkdirs()



  /*val drv = new Driver
  val comp = new dotc.Compiler*/

  val compiler = new IDCompiler
  compiler.setCompilerOutput(out)

  compiler.compileFile(src)

  println("------- Done ------------")



}
class TestCB extends CompilerCallback {

  override def onClassGenerated(src: SourceFile,cl: AbstractFile,name:String)  {

  }

}