package com.idyria.osi.tea.timing


trait TimingSupport {
  
  
  /**
   * Time in miliseconds using system time the provided closure:
   * 
   * var res = time {
   *   // here some code
   * }
   */
  def time(cl: => Unit) : Long = {
    
    var  startTime = System.currentTimeMillis()
    cl
    var  stopTime = System.currentTimeMillis()
    stopTime-startTime
  }
}