package com.idyria.osi.tea.progress

import com.idyria.osi.tea.listeners.ListeningSupport

trait ProgressSupport extends ListeningSupport {
  
  // Progress
  //------------ 
  def progressInit(operation: String) = {
    this.@->("progress.init", operation)
  }
  def onProgressInit(cl: String => Unit) = {
    this.onWith("progress.init") {
      m: String => cl(m)
    }
  }
  def progressUpdate(p: Double) = {
    //println(s"MG Progress update..."+this.listeningPointsWith.size)
    this.@->("progress.update", p)
  }
  def onProgressUpdate(cl: Double => Unit) = {

    //println(s"MG Registering Progress update...")
    this.onWith("progress.update") {
      d: Double =>
        // println(s"MG received updated")
        cl(d)
    }
  }
  
}