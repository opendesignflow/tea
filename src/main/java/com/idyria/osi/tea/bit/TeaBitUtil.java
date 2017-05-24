/**
 * 
 */
package com.idyria.osi.tea.bit;

import java.util.BitSet;

/**
 * 
 * Bit / Byte manipulation utility
 * 
 * @author rleys
 *
 */
public class TeaBitUtil {

	/**
	 * 
	 */
	public TeaBitUtil() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * FIXME This method takes a long value, and returns an array of the single bytes
	 * @param value
	 * @return
	 */
	public static byte[] splitBytes(long value) {
		
		
		return null;
	}
	
	/**
	 * Sets the bits between lsb and msb to the value 
	 * @param value
	 * @param lsb
	 * @param msb
	 * @param value
	 * @return
	 */
	public static long setBits(long baseValue,int lsb,int msb,long newValue) {
		
		int width = msb-lsb+1;
		
		// Variables
	    //----------------
		long fullMask = 0xFFFFFFFFFFFFFFFFL;
		long fullMaskLeft = 0;
		long newValShifted = 0;
	    long resultVal = 0;
	    long baseValueRight = 0;

	    //-- Shift newVal left to its offset position
	    newValShifted = newValue << lsb;

	    //-- Suppress right bits of baseValue by & masking with F on the left
	    fullMaskLeft = fullMask << (lsb+width);
	    resultVal =  baseValue & fullMaskLeft;

	    //-- Set Result value in result by ORing with the placed shifted bits new value
	    resultVal =  resultVal | newValShifted;

	    // Reconstruct  Right part
	    //----------------------------------

	    //-- Isolate base value right part
	    baseValueRight = baseValue & (fullMask >> (64-lsb));

	    //-- Restore right part
	    resultVal =  resultVal | baseValueRight;

	    // Return
	    return resultVal;
		
	}
	
	/**
	 * masks against 64bits!
	 * @param value
	 * @param lsb
	 * @param msb
	 * @return
	 */
	public static long extractBits(long value,int lsb,int msb) {
		
		long mask = 0xFFFFFFFFFFFFFFFFL;
		
		return ( (value >>> lsb) & (mask >>> (63-(msb-lsb))));
		
	}
	
	
	/**
	 * Replace an array of bytes into an integer
	 * Big endian !!
	 * @param bytes Be careful, length is not checked, don't exceed 4 bytes!
	 * @return
	 */
	public static int bytesToInteger(byte[] bytes) {
		
		int result = 0;
		
		for (int i = 0 ; i<bytes.length;i++) {
			
			result = result | (bytes[i] << 8 );
			
		}
		
		
		return result;
		
	}

	/**
	 * Changes a bit in the provided byte and returns the new value
	 * @param value
	 * @param value
	 * @return
	 */
	public static byte setBitInByte(byte value,int bitPosition,boolean bitValue) {
		
		// Use a bit set to perform modification
		BitSet bistset = BitSet.valueOf(new byte[]{value});
		bistset.set(bitPosition, bitValue);
		return bistset.toByteArray()[0];
		
	}
	
	/**
	 * 
	 * @param value
	 * @param bitPosition
	 * @return true if 1 , false if 0
	 */
	public static boolean getBitInByte(byte value,int bitPosition) {
		
		// Use a bit set to perform modification
		BitSet bistset = BitSet.valueOf(new byte[]{value});
		return bistset.get(bitPosition);
		
	}
	
	
	
}
