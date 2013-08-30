package com.idyria.osi.tea.logging

trait TLogSource {



    def logError( msg : => String ) : Unit = {

        TLog.doLog("",TLog.ERROR,{ () => msg})

    }

    def logWarn( msg : => String ) : Unit = {

        TLog.doLog("",TLog.WARNING,{ () => msg})

    }


    /**
        Log Infos based on a closure that will be executed only if the log Level is allowed
    */
    def logInfo( msg : => String ) : Unit = {

        TLog.doLog("",TLog.INFO,{ () => msg})

    }


    def logFine( msg : => String ) : Unit = {

        TLog.doLog("",TLog.FINE,{ () => msg})

    }

    def logFull( msg : => String ) : Unit = {

        TLog.doLog("",TLog.FULL,{ () => msg})

    }
    
     
}

object TLog extends Enumeration {

    type Level = Value
    val FATAL, ERROR, WARNING, INFO, FINE, FULL = Value

    // Logger management
    //-------------------------

    def doLog(realm: String,level: Level,message: () => String) : Unit = {

    }




}
