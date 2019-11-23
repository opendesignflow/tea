/**
 * 
 */
package org.odfi.tea.file;

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

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.odfi.tea.recursivity.RecursiveParentCell;

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
