package com.idyria.osi.tea.progress

object ConsoleSimpleProgress {
  
  
  def apply(p:ProgressSupport) = {
    var it = ""
    p.onProgressInit {
      text => 
        it = text
        println(s"[0%] Starting $text....")
    }
    p.onProgressUpdate {
      i => 
        println(s"[$i%] $it....")
    }
  }
  
}