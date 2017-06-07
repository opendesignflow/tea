/**
 * 
 */
package com.idyria.osi.tea.io;

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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Scanner;

/**
 * @author rtek
 *
 */
public class TeaIOUtils {

	/**
	 * Buffer size used in I/O Operations
	 */
	public static Integer BUFF_SIZE = 2048;
	
	/**
	 * 
	 */
	public TeaIOUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static String waitForNewLineConsoleInput() {
		// get scanner
		Scanner scan = new Scanner(System.in);
		return scan.nextLine();
		
	}
	
	public static String waitForNewCharacterConsoleInput() {
		// get scanner
		Scanner scan = new Scanner(System.in);
		return scan.next();
		
	}
	
	
	/**
	 * Returns a byte array containing the totality of the InputStream.
	 * @param is The available size should return the complete size of the stream
	 * @return null if stream is not readable
	 */
	public static byte[] swallow(InputStream is) {
		
		
		try {
			// Create
			byte[] res = new byte[is.available()];
			int buffsize = BUFF_SIZE;
			byte[] buff = new byte[buffsize];
			int sizeRead = 0;
			int position = 0;
			// Swallow
			while ( (sizeRead = is.read(buff))!=-1  ) {
				
				// Copy
				System.arraycopy(buff, 0, res, position, sizeRead);
				position+=sizeRead;
				
			}
			return res;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	/**
	 * Returns a byte array containing the totality of the InputStream.
	 * @param is The available size should return the complete size of the stream
	 * @return null if stream is not readable
	 */
	public static byte[] swallowBytes(InputStream is,int count) {
		
		
		try {
			// Create
			byte[] res = new byte[count];
			int buffsize = BUFF_SIZE;
			byte[] buff = new byte[buffsize];
			int sizeRead = 0;
			int position = 0;
			// Swallow
			while ( (sizeRead = is.read(buff))!=-1  ) {
				
				// Copy
				System.arraycopy(buff, 0, res, position, sizeRead);
				position+=sizeRead;
				
			}
			return res;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	/**
	 * Returns a byte array containing the totality of the InputStream.
	 * Does not rely on available
	 * @param is The Output byte array will be extended
	 * @return if stream is not readable
	 */
	public static byte[] swallowStream(InputStream is) {
		
		try {
			// Create
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			int buffsize = BUFF_SIZE;
			byte[] buff = new byte[buffsize];
			int sizeRead = 0;

			// Swallow
			while ( (sizeRead = is.read(buff))!=-1  ) {
				
				// Copy
				os.write(buff,0,sizeRead);
				
				
			}
			return os.toByteArray();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	
	/**
	 * Generates a temporary directory that will be deleted by the end of the JVM.
	 * The directory has a random name
	 * @return
	 */
	public static File getRandomTempDir() {
		
		// Get random
		//--------------
		long randlong = Calendar.getInstance().getTimeInMillis();
		String randstring = null;
		byte[] result = null;
		try {
			// Create the message digest algorithm
			MessageDigest digest = MessageDigest.getInstance("MD5");

			digest.update(Long.valueOf(randlong).byteValue());

			// Digest and return
			result = digest.digest();
			
			randstring = Base64.encodeBytes(result);

			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Create file
		String tempdir = System.getProperty("java.io.tmpdir");
		File tempdirFile = new File(tempdir+File.separatorChar+randstring);
		tempdirFile.mkdir();
		tempdirFile.deleteOnExit();
		
		
		return tempdirFile;
		
		
	}
	
	/**
	 * Extract the specified resource to temp directory.
	 * Uses Thread.currentThread().getContextClassLoader() to extract resourcePath
	 * WARNING: Goes through RAM first. Don't use on big datas
	 * @param applicationPath
	 * @return null if no resource path or not succeeded
	 */
	public static File extractToTemp(String resourcePath) {
		
		// In/out
		File res = null;
		File temp = null;
		File resourcePathFile = new File(resourcePath);
		
		// Find IO Dir
		String tempdir = System.getProperty("java.io.tmpdir");
		if (tempdir!=null) {
			temp = new File(tempdir);
			if (!temp.isDirectory())
				temp=null;
		}
		if (tempdir==null)
			return null;
		// Load resource
		URL resource = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
		if (resource!=null) {
			// Found, open stream
			try {
				InputStream is = resource.openStream();
				
				// Swallow
				byte[] inBytes = TeaIOUtils.swallow(is);
				
				// Create output file
				res = new File(temp.getAbsolutePath()+File.separator+resourcePathFile.getName());
				res.deleteOnExit();
				
				// Write on it
				FileOutputStream os = new FileOutputStream(res);
				os.write(inBytes);
				os.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return res;
	}

	/**
	 * Writes the provided content string complete to target file using file Writer
	 * @param file
	 * @param content
	 * @throws IOException 
	 */
	public static File writeToFile(File file , String content) throws IOException {
		return TeaIOUtils.writeToFile(file, content, true);
	}
	
	public static File  writeToFile(File file , String content,boolean overwrite) throws IOException {
		
		//-- If exists and nor overwrite, don'T do
		if (file.exists() && !overwrite) {
			
		} else {
		
			//-- Write
			FileWriter out = new FileWriter(file);
			out.write(content);
			out.close();
		
		}
		return file;
	}
	
	/**
	 * Writes the provided input stream bufferto target file using nio mapping
	 * @param file
	 * @param content
	 * @throws IOException 
	 */
	public static File writeToFile(File file , InputStream content) throws IOException {

		
		//-- Use Java 1.7 Nio
		Files.copy(content, Paths.get(file.toURI()), StandardCopyOption.REPLACE_EXISTING);
		
		/*
		//-- Source Channel
		ReadableByteChannel contentChannel = Channels.newChannel(content);
		
		//-- Write output
		FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().write(contentChannel.)
		*/
		
		return file;
		
	}
	
	
}
