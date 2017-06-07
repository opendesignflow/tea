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
