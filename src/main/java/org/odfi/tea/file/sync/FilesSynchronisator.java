/**
 * 
 */
package org.odfi.tea.file.sync;

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
import java.util.LinkedList;

import org.odfi.tea.collections.Pair;
import org.odfi.tea.hash.HashUtils;

/**
 * This class compares File pairs and says if they are different using HashFunctions.
 * It also provides utility methods to synchronize them
 * 
 * @author rleys
 *
 */
public class FilesSynchronisator {

	private LinkedList<FilesSynchronisatorUnit> sources = new LinkedList<FilesSynchronisatorUnit>();
	
	
	/**
	 * label for "Source" side to allow application to customize 
	 */
	protected String sourceLabel = "source";
	
	/**
	 * label for "Reference" side to allow application to customize 
	 */
	protected String referenceLabel = "reference";
	
	/**
	 * 
	 */
	public FilesSynchronisator() {
		// TODO Auto-generated constructor stub
	}
	
	
	public class FilesSynchronisatorUnit {
		
		Pair<File,File> files;

		/**
		 * @param files
		 */
		public FilesSynchronisatorUnit(Pair<File, File> files) {
			super();
			this.files = files;
		}

		/**
		 * Check equivalency using a simple Hash method
		 * @return
		 */
		public boolean identical() {
			
			if ( HashUtils.hashFileAsBase64(this.getLeft(), "MD5").equals(HashUtils.hashFileAsBase64(this.getRight(), "MD5"))) {
				return true;
			}
			return false;
			
		}
		
		
		/**
		 * @return
		 * @see com.idyria.utils.java.collections.Pair#getLeft()
		 */
		public File getLeft() {
			return files.getLeft();
		}

		/**
		 * @return
		 * @see com.idyria.utils.java.collections.Pair#getRight()
		 */
		public File getRight() {
			return files.getRight();
		}
		
		/**
		 * @return the sourceLabel
		 */
		public String getSourceLabel() {
			return sourceLabel;
		}

		/**
		 * @return the referenceLabel
		 */
		public String getReferenceLabel() {
			return referenceLabel;
		}

		
		
		
	}
	
	/**
	 * Add files to compare
	 * @param source
	 * @param reference
	 */
	public synchronized void addCheck(File source,File reference) {
		this.sources.add(new FilesSynchronisatorUnit(new Pair<File,File>(source,reference)));
	}

	/**
	 * @return the sources
	 */
	public LinkedList<FilesSynchronisatorUnit> getSources() {
		return sources;
	}

	/**
	 * @return the sourceLabel
	 */
	public String getSourceLabel() {
		return sourceLabel;
	}

	/**
	 * @param sourceLabel the sourceLabel to set
	 */
	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
	}

	/**
	 * @return the referenceLabel
	 */
	public String getReferenceLabel() {
		return referenceLabel;
	}

	/**
	 * @param referenceLabel the referenceLabel to set
	 */
	public void setReferenceLabel(String referenceLabel) {
		this.referenceLabel = referenceLabel;
	}
	
	
	
	

}
