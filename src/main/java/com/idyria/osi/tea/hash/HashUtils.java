/**
 * 
 */
package com.idyria.osi.tea.hash;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author rtek
 *
 */
public class HashUtils {

	/**
	 * 
	 */
	public HashUtils() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Calculate Hash over a file
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public static byte[] hashFile(File file,String alg) throws FileNotFoundException {

		// Check file existence
		if (file == null || !file.exists())
			throw new FileNotFoundException("File Not Found: "+file.getAbsolutePath());

		byte[] result = null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			// Create the message digest algorithm
			MessageDigest digest = MessageDigest.getInstance(alg);

			// Create the FileInputStream
			FileInputStream filereader = new FileInputStream(file);

			// Update digest with bytes
			int read = 0;
			while ((read = filereader.read()) != -1) {
				// digester.write(read);
				digest.update(Integer.valueOf(read).byteValue());

			}
			// Close everybody
			filereader.close();

			// Digest and return
			result = digest.digest();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	public static byte[] hashBytes(byte[] bytes,String algo) {

		byte[] result = null;
		try {
			// Create the message digest algorithm
			MessageDigest digest = MessageDigest.getInstance(algo);

			digest.update(bytes);

			// Digest and return
			result = digest.digest();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	public static String hashBytesAsBase64(byte[] bytes,String alg) {
		return Base64.encodeBytes(HashUtils.hashBytes(bytes,alg));
	}
	
	

	public static String hashFileAsBase64(File file,String alg) {
		byte[] bytes;
		try {
			bytes = HashUtils.hashFile(file,alg);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bytes = null;
		}
		if (bytes == null || bytes.length == 0)
			return null;

		// Encode as Base64 and return
		String result = Base64.encodeBytes(bytes);
		return result;
	}

}
