/**
 * 
 */
package com.idyria.osi.tea.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author rleys
 * 
 */
public class TeaFileUtils {

	/**
	 * 
	 */
	public TeaFileUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates the provided path folders
	 * 
	 * @param path
	 * @return The provided path
	 */
	public static File createPath(File path) {

		// If path is a file, get the parent folder
		File targetPath = path.isFile() ? path.getParentFile() : path;

		// Just call mkdirs
		targetPath.mkdirs();

		return targetPath;

	}

	/**
	 * Creates a filename filter which checks if the file name (not path) ends
	 * with the provided filter.
	 * 
	 * @param filter
	 * @return
	 */
	public static FilenameFilter createEndsWithFileFilter(final String filter) {

		return new FilenameFilter() {

			@Override
			public boolean accept(File file, String name) {
				// Check
				if (name.endsWith(filter))
					return true;
				return false;
			}
		};

	}

	/**
	 * Creates a filename filter which checks if the filename matches the regexp
	 * filter
	 * 
	 * @param filter
	 *            A regexp compatible string
	 * @return
	 */
	public static FilenameFilter createRegexpFileNameFilter(final String filter) {

		return new FilenameFilter() {

			@Override
			public boolean accept(File file, String name) {
				// Check
				if (name.matches(filter))
					return true;
				return false;
			}
		};

	}

	/**
	 * uses all components+File.separator to render a string
	 * 
	 * @param components
	 * @return
	 */
	public static String buildPath(String... components) {
		String result = components[0];
		for (int i = 1; i < components.length; i++) {
			result += File.separator + components[i];
		}
		return result;

	}

	public static String buildPath(File firstFile, String... components) {
		String[] newcomponents = new String[components.length + 1];
		newcomponents[0] = firstFile.getAbsolutePath();
		System.arraycopy(components, 0, newcomponents, 1, components.length);
		return TeaFileUtils.buildPath(newcomponents);

	}

	/**
	 * @see TeaFileUtils#buildPath(String...) same but returns a file
	 * @param components
	 * @return
	 */
	public static File buildPathAsFile(String... components) {
		return new File(TeaFileUtils.buildPath(components));

	}

	public static File buildPathAsFile(File firstFile, String... components) {
		return new File(TeaFileUtils.buildPath(firstFile, components));

	}

	/**
	 * This method tries to create a File for the given path.
	 * If the path is absolute, just create. <br/>
	 * If it is relative, create it relative to provided parent file
	 * @param parent - Base path is path is relative
	 * @param path File path to resolve
	 * @return A File representing a valid absolute path to path argument
	 */
	public static File resolveAndCreatePath(File parent,String path) {
		
		File pathFile = new File(path);
		if (pathFile.isAbsolute())
			return pathFile;
		else 
			return TeaFileUtils.buildPathAsFile(parent,path);
		
	}
	
	/**
	 * 
	 * @param file
	 * @return true if the path ends with an extension (like a file), false if like a directory
	 */
	public static boolean isPathLikeFile(File file) {
		
		return file.getPath().matches(".*\\.[\\w]+");
		
	}
	
	/**
	 * Copy source into target. Replaces target if it is a file, or just place
	 * into folder if target is a folder
	 * 
	 * @param source
	 * @param target
	 * @return boolean true if succeeded, false if failed
	 * @throws IOException
	 */
	public static boolean copy(File source, File target) throws IOException {

		// -- Prepare target File
		File targetFile = null;
		if (!target.isDirectory())
			targetFile = target;
		else
			targetFile = TeaFileUtils.buildPathAsFile(target.getAbsolutePath(),
					source.getName());

		// -- Make a NIO transfert
		try {
			FileChannel in = (new FileInputStream(source)).getChannel();
			FileChannel out = (new FileOutputStream(targetFile)).getChannel();
			in.transferTo(0, source.length(), out);
			in.close();
			out.close();
		} finally {
		}
		return true;

	}

	/**
	 * Copies the provided target FILE to the same path, with a .teabk after the
	 * name Fairly stupid and easy backup
	 * 
	 * @param target
	 *            - The file to backup
	 * @return false if the target is not a file, or not existing, or if an
	 *         other error occurs
	 * @throws IOException 
	 */
	public static boolean backupLocal(File target) throws IOException {

		// -- Check it is a file and it exists
		if (!target.isFile() || !target.exists()) {
			//throw new IOException("target to backup is not a file or does not exists: "+target.getAbsolutePath());
			return false;
		}


		// -- Copy
		return TeaFileUtils.copy(
				target,
				TeaFileUtils.buildPathAsFile(target.getParentFile(),
						target.getName() + ".teabk"));


	}

	/**
	 * Writes content string in file, overriding file. Writing using character
	 * dependend functions (not pure binary)
	 * 
	 * @param content
	 * @param file
	 */
	public static void writeToFile(String content, File file) {

		// -- Open
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
