package com.idyria.osi.tea.nio

import java.nio._


/**
    TeaBuffer is only a utility class for repetetive work using NIO Buffers

*/
object TeaBuffer {



    /**
        Read Remaining Bytes from source and write them to target

        FIXE: Check capacities
    */
   /* def readRemainingTo(source : ByteBuffer,target: ByteBuffer) = {

        // Checks
        //-------------
        if (source.remaining==0)
            return


        // Prepare Read
        //---------
        var readBytes = new Array[Byte](source.remaining)
        source.get(readBytes,0,source.remaining)

        // Write
        //------------
        target.put(readBytes)

    }
*/
}
