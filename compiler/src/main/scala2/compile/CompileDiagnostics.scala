package org.odfi.tea.compile

import dotty.tools.dotc.interfaces.Diagnostic

trait CompileDiagnostics {
  var errors: List[Diagnostic] = List()

  def hasErrors = errors.nonEmpty

  def resetDiagnostics = {
    errors = List()
  }
}
