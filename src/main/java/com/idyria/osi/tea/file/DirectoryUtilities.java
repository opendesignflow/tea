/**
 * 
 */
package com.idyria.osi.tea.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author Richnou
 *
 */
public class DirectoryUtilities {

	/**
	 * 
	 */
	public DirectoryUtilities() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * A cell to store dirs when copying
	 * @author Richnou
	 *
	 */
	public static class CopyingCell {
		
		private File source = null;
		private File destination = null;
		/**
		 * @param source
		 * @param destination
		 */
		public CopyingCell(File source, File destination) {
			super();
			this.source = source;
			this.destination = destination;
		}
		/**
		 * @return the destination
		 */
		public File getDestination() {
			return destination;
		}
		/**
		 * @return the source
		 */
		public File getSource() {
			return source;
		}
		
		
		
	}
	
	/**
	 * deletes the provided directory
	 * @param f
	 */
	public static final void deleteDirectory(File dir) {
		
		// Foreach all files
		File[] files = dir.listFiles();
		if (files==null)
			return;
		for (File file: files) {
			
			// if file is a directory -> recursive
			if (file.isDirectory())
				deleteDirectory(file);
			// If it is a file -> delete
			else
				file.delete();
			
		}
		
		// Delete ourselves
		dir.delete();
		
	}
	
	/**
	 * deletes the provided directory, but not itseld
	 * @param f
	 */
	public static final void deleteDirectoryContent(File dir) {
		
		// Foreach all files
		File concernedfile = new File(dir.getAbsolutePath()+File.separator);
		
//		System.out.println("Is a directory ("+concernedfile.getAbsolutePath()+"): "+concernedfile.isDirectory());
		
		
		File[] files = concernedfile.listFiles();
	
//		System.out.println("FOund files: "+files);
		if (files==null)
			return;
		for (File file: files) {
			
			// if file is a directory -> recursive
//			System.out.println("Deleting: "+file.getName());
			if (file.isDirectory())
				deleteDirectory(file);
			// If it is a file -> delete
			else
				file.delete();
			
		}
		
	}
	
	/**
	 * Says Whether this directory contains a certain file or not
	 * @param filename
	 * @return true|false Will return true if the filename is found, whether it is a directory or a file
	 */
	public static final boolean directoryContains(File directory,String filename) {
		
		File searchedFile = new File(directory.getAbsolutePath()+File.separator+filename);
		if (searchedFile.exists()) {
			return true;
		}
		
		return false;
	}
	
	
	
	public static final void CopyContent(File source,File destination) {
		
		// Check arguments
		if (source==null || destination==null || !source.isDirectory() || !destination.isDirectory())
			throw new IllegalArgumentException();
		
		// Create initial cell
		CopyingCell ccell  =  new CopyingCell(source,destination);
		
		// Foreach all cells
		LinkedList<CopyingCell> cells = new LinkedList<CopyingCell>();
		cells.add(ccell);
		while (!cells.isEmpty()) {
			
			// Get the cell
			CopyingCell cell = cells.poll();
			
			// Foreach All the files
			for (File child : cell.getSource().listFiles()) {
				
				// If it is a DIrectory, create it in destination, and launch a cell
				if (child.isDirectory()) {
					
					// Create in destination
					File newdestination = new File(cell.getDestination().getAbsolutePath()+File.separator+child.getName());
					newdestination.mkdir();
					
					// Launch cell
					cells.add(new CopyingCell(child,newdestination));
					
				} else if (child.isFile()) {
					
					// Copy Content straight
					
					// Create File
					File newdestination = new File(cell.getDestination().getAbsolutePath()+File.separator+child.getName());
					
					
					try {
//						 Create InputStream to read original file
						FileInputStream input = new FileInputStream(child);
						
//						 Create OutputStream to write destination file
						FileOutputStream output = new FileOutputStream(newdestination);
						
						// Bulk Copy
						//---------------
						byte[] buff = new byte[500000];
						int state = 0;
						// Copy while ok
						while ((state = input.read(buff))!=-1)
							output.write(buff);
						
						// Finally Flush and close everybody
						output.flush();
						output.close();
						input.close();
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				
			}
			
		}
		
		
	}
	
}
