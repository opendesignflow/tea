package org.odfi.tea.security

import java.security.MessageDigest

object TSecUtils {

    def hashBytes(bytes: Array[Byte], alg: String) = {
        // Create the message digest algorithm
        var digest = MessageDigest.getInstance(alg);

        digest.update(bytes);

        // Digest and return
        digest.digest();
    }

    def hashBytesToHexString(bytes: Array[Byte], alg: String,join:String=":") = {

        
        hashBytes(bytes,alg).map {
            b => 
                Math.abs(b.toInt).toHexString.padTo(2, 0).mkString
        }.mkString(join)
        
    }

}