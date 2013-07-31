package com.idyria.osi.tea.logging

object TLog {

    // Logger management
    //-------------------------


    // Log Interfaces
    //----------------------


    def logInfo( msg : String ) = {

    }

    /**
        Log Infos based on a closure that will be executed only if the log Level is allowed
    */
    def logInfo( cl : => String ) = { cl }


    def logFine( msg : String ) = {

    }


}
