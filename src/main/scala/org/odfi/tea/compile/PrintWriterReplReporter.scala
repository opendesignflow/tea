package org.odfi.tea.compile

import java.io.PrintWriter

import scala.reflect.internal.util.Position
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.{ReplReporter, ReplRequest}

class PrintWriterReplReporter(var settings: Settings , val outImpl: PrintWriter)  extends ReplReporter {
  override def out: PrintWriter = outImpl

  override def printMessage(msg: String): Unit = outImpl.println(msg)

  override def suppressOutput[T](body: => T): T =  {
    body
  }

  override def withoutTruncating[T](body: => T): T = {
    body
  }

  override def withoutUnwrapping(body: => Unit): Unit = {
    body
  }

  override def indenting(n: Int)(body: => Unit): Unit = {
    body
  }

  override def printResult(result: Either[String, String]): Unit = {
    if (result.isLeft) {
      printMessage(result.left.toString)
    } else {
      printMessage(result.right.toString)
    }
  }

  override def withoutPrintingResults[T](body: => T): T = {
    body
  }

  override def printResults: Boolean = true

  override def togglePrintResults(): Unit =  {

  }

  override def isDebug: Boolean = true

  override def isTrace: Boolean = true

  var currentRequest: ReplRequest = _

 // override def currentRequest_=(req: ReplRequest): Unit = ???



  override def doReport(pos: Position, msg: String, severity: Severity): Unit =  {

  }
}
