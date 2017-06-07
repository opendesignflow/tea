/*-
 * #%L
 * Tea Scala Utils Library
 * %%
 * Copyright (C) 2006 - 2017 Open Design Flow
 * %%
 * This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
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
