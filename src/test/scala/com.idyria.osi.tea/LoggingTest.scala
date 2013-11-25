package com.idyria.osi.tea

import com.idyria.osi.tea.logging.TLogSource
import org.scalatest.FunSuite


trait TestLogging extends TLogSource  {
 
  def test = {
    logInfo(s"Test")
  }
  
}

class LoggingTest extends FunSuite with TLogSource {
 
  
  test("Classtag resolving") {
    
    logInfo(s"Test")
    logInfo[TestLogging](s"Test")
    
  }
  
}