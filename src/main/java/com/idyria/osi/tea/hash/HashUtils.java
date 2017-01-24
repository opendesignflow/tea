/**
 * 
 */
package com.idyria.osi.tea.hash;

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
