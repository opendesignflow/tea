/**
 * 
 */
package com.idyria.osi.tea.classpath;

import java.io.File;
import java.util.Vector;

import com.idyria.osi.tea.file.AbstractDirectoryWalkerListener;
import com.idyria.osi.tea.file.DirectoryWalker;

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
