package com.idyria.osi.tea.thread

/**
 * A trait to provide a few utility methods to run code on Threads
 */
trait ThreadLanguage {
  
  def fork(cl: => Any) : Thread = {
    
    var th = new Thread(new Runnable {
      def run() = {
        cl
      }
    })
    th.start
    th
    
  }
  
}