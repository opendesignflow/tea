/**
 * 
 */
package com.idyria.osi.tea.file;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.idyria.osi.tea.recursivity.RecursiveParentCell;

/**
 * This class walks through a directory from end to end and calls a listener
 * each time is find a new file
 * 
 * @author Richnou
 * 
 */
public class DirectoryWalker {

	/**
	 * List-based recursivity
	 */
	private List<File> files = Collections.synchronizedList(new LinkedList<File>());
	
	/**
	 * 
	 */
	public DirectoryWalker() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void run(File directory,DirectoryWalkerListener<Boolean,Boolean,File> listener) {
		
		// Inital import
		File[] foundFiles = directory.listFiles();
		files.addAll(Arrays.asList(foundFiles==null ? new File[]{}:foundFiles));
		
		System.out.println("Initial found files in "+directory.getAbsolutePath()+": "+files.size());
		
		// Scan files
		//ListIterator<File> iterator = (ListIterator<File>) files.iterator();
		File current = null;
		while (files.size()>0) {
			
			// Get file
			current = files.get(0);
			files.remove(0);
			
			// File or directory ?
			if (current.isDirectory()) {
				// Add subfiles
				if (listener.directoryFound(current)) {
					File[] currentFoundFiles = current.listFiles();
					files.addAll(Arrays.asList(currentFoundFiles==null?new File[0]:currentFoundFiles));
					continue;
				} else {
					break;
				}
			} else {
				if (listener.fileFound(current)) {
					continue;
				} else {
					break;
				}
			}
			
		}
		
	}
	
	public <PT> void  runWithCell(RecursiveParentCell<PT,File> initialCell,DirectoryWalkerListener<Boolean,PT,RecursiveParentCell<PT,File>> listener) {
		
		// Initial import file -> cells
		//----------------------------
		
		// list of cells
		LinkedList<RecursiveParentCell<PT,File>> cells = new LinkedList<RecursiveParentCell<PT,File>>();
		
		// List content of initial
		for (File f: initialCell.getChild().listFiles()) {
			cells.add(new RecursiveParentCell<PT,File>(initialCell.getParent(),f));
		}
		
		
		
		while (cells.size()>0) {
			
			// get current cell
			RecursiveParentCell<PT,File> currentCell = cells.poll();
			
			
			// Get file
			File current = currentCell.getChild();
			
			// File or directory ?
			if (current.isDirectory()) {
				// Signal a directory found
				Object res = listener.directoryFound(currentCell);
				if (res!=null) {
					// Res!=null -> continue
					// List all files, create cells and add to processing list
					for (File next:Arrays.asList(current.listFiles())) {
						cells.add(new RecursiveParentCell(res,next));
					}
					continue;
				} else {
					break;
				}
			} else {
				if (listener.fileFound(currentCell)) {
					continue;
				} else {
					break;
				}
			}
		}
		
		
		
	}

}
