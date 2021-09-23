package compile

import java.io.File
import org.odfi.tea.compile.IDCompiler
object TryIDCompilerS3 extends App {

  println("Hello")

  val src = new File("src/test/resources/compile/TestSimpleCompile.scala")
  val out = new File("build/out").getCanonicalFile
  out.mkdirs()



  /*val drv = new Driver
  val comp = new dotc.Compiler*/

  val compiler =  new IDCompiler
  compiler.setCompilerOutput(out)

  compiler.compileFile(src)

  println("------- Done ------------")



}
