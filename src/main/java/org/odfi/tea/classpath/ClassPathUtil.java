/**
 * 
 */
package org.odfi.tea.classpath;

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
import java.util.Vector;

import org.odfi.tea.file.AbstractDirectoryWalkerListener;
import org.odfi.tea.file.DirectoryWalker;

/**
 * Utils to play with classpath properties
 * 
 * @author Richnou
 * 
 */
public class ClassPathUtil {

	private ClassFoundListener classFoundListener = null;

	/**
	 * 
	 */
	public ClassPathUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * This utility finds classes
	 */
	public void findClasses() {

		// path Separator
		String separator = System.getProperty("path.separator");
		String classPath = System.getProperty("java.class.path");

		// Split classPath
		String[] classPaths = classPath.split(separator);

		// Explore each Path
		for (String path : classPaths) {
			// Process if it is a directory
			File dir = new File(path);

			if (!dir.isDirectory())
				continue;

			DirectoryWalker wk = new DirectoryWalker();
			wk.run(dir, new AbstractDirectoryWalkerListener<Boolean,Boolean,File>(dir) {

				@Override
				public Boolean directoryFound(File file) {
					// continue
					return true;
				}

				@Override
				public Boolean fileFound(File file) {
					// Class file?
					if (file.getName().endsWith(".class")) {
						try {
							String className = file.getAbsolutePath().replace(
									this.initialDirectory.getAbsolutePath()+File.separator,
									(CharSequence) "").replace(
									File.separatorChar, '.').replace(
									(CharSequence) ".class", "");
							if (ClassPathUtil.this.classFoundListener != null)
								ClassPathUtil.this.classFoundListener
										.classFileFound(className);
						} catch (Throwable e) {
							e.printStackTrace();
						}

					}

					// Continue
					return true;
				}

				

			});

		}

	}

	/**
	 * Returns an array containing the directories classpath entries
	 * @return empty if none found.
	 */
	public File[] findClassPathDirectories() {
		
		Vector<File> dirs = new Vector<File>();
		
		// path Separator
		String separator = System.getProperty("path.separator");
		String classPath = System.getProperty("java.class.path");

		// Split classPath
		String[] classPaths = classPath.split(separator);

		// Explore each Path
		for (String path : classPaths) {
			// Process if it is a directory
			File dir = new File(path);

			if (!dir.isDirectory())
				continue;
			dirs.add(dir);
		}
		
		return dirs.toArray(new File[dirs.size()]);
		
		
	}
	
	public File[] findClassPathEntries() {

		Vector<File> dirs = new Vector<File>();
		
		// path Separator
		String separator = System.getProperty("path.separator");
		String classPath = System.getProperty("java.class.path");

		// Split classPath
		String[] classPaths = classPath.split(separator);

		// Explore each Path
		for (String path : classPaths) {
			// Process if it is a directory
			File dir = new File(path);

			dirs.add(dir);
		}
		
		return dirs.toArray(new File[dirs.size()]);
	}
	
	/**
	 * @return the classFoundListener
	 */
	public ClassFoundListener getClassFoundListener() {
		return classFoundListener;
	}

	/**
	 * @param classFoundListener
	 *            the classFoundListener to set
	 */
	public void setClassFoundListener(ClassFoundListener classFoundListener) {
		this.classFoundListener = classFoundListener;
	}

}
