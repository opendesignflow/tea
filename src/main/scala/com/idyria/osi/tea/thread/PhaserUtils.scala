package com.idyria.osi.tea.thread

import java.util.concurrent.Phaser


trait PhaserUtils extends ThreadLanguage {
    
    /**
     * This task registers a new Thread to be activated upon arrival on a phaser
     * The thread is set as daemon per default and is started
     */
    def phaserTaskOnArrive(phaser:Phaser)(cl: => Any) : Thread = {
        
        var th = createThread {
            phaser.register()
            
            while (!phaser.isTerminated()) {
                
                // Arrive and Wait
                // -> Then run closure
                // -> Loop
                // -> Catch termination to gracefully exit
                try {
                   
                    phaser.awaitAdvanceInterruptibly(phaser.arrive())
                    cl
                } catch {
                    case e : InterruptedException => 
                    case e : Throwable => e.printStackTrace()
                }
            }
        }
        th.setDaemon(true)
        th.start
        
        th
        
    }
    
}