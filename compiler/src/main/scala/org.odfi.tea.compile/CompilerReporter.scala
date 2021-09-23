package org.odfi.tea.compile

import dotty.tools.dotc.interfaces.{Diagnostic, SimpleReporter}

class CompilerReporter extends SimpleReporter with CompileDiagnostics {


  override def report(diag: Diagnostic): Unit = {

    // Print
    println(s"[IDC/${diag.level()}] ${diag.message()}")
    diag.position().ifPresent {
      p =>
        println(s"    ${p.source()}, line=${p.line()},column=${p.column()}")
    }

    // Save Error
    if (diag.level() == 2) {
      errors = errors :+ diag
    }

  }
}
