/**
 * 
 */
package com.idyria.osi.tea.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * TeaPrintWriter that defaults to System.out output
 * 
 * @author rleys
 *
 */
public class TeaStdoutPrintWriter {

	/**
	 * Internal singleton reference
	 */
	private static TeaPrintWriter writer = null;
	
	/**
	 * @param out
	 */
	private TeaStdoutPrintWriter() {
		super();
	}
	
	

	/**
	 * @param s
	 * @see com.idyria.utils.java.io.TeaPrintWriter#dprintln(java.lang.String)
	 */
	public static void dprintln(String s) {
		TeaStdoutPrintWriter.getInstance().dprintln(s);
		TeaStdoutPrintWriter.getInstance().flush();
	}



	/**
	 * 
	 * @see com.idyria.utils.java.io.TeaPrintWriter#println()
	 */
	public static void println() {
		TeaStdoutPrintWriter.getInstance().println();
		TeaStdoutPrintWriter.getInstance().flush();
	}

	/**
	 * @param x
	 * @see java.io.PrintWriter#println(java.lang.String)
	 */
	public static void println(String x) {
		TeaStdoutPrintWriter.getInstance().println(x);
		TeaStdoutPrintWriter.getInstance().flush();
	}

	/**
	 * 
	 * @see com.idyria.utils.java.io.TeaPrintWriter#indent()
	 */
	public static void indent() {
		TeaStdoutPrintWriter.getInstance().indent();
	}


	/**
	 * @param count
	 * @see com.idyria.utils.java.io.TeaPrintWriter#indent(int)
	 */
	public static void indent(int count) {
		TeaStdoutPrintWriter.getInstance().indent(count);
	}

	/**
	 * 
	 * @see com.idyria.utils.java.io.TeaPrintWriter#outdent()
	 */
	public static void outdent() {
		TeaStdoutPrintWriter.getInstance().outdent();
	}

	/**
	 * @param count
	 * @see com.idyria.utils.java.io.TeaPrintWriter#outdent(int)
	 */
	public static void outdent(int count) {
		TeaStdoutPrintWriter.getInstance().outdent(count);
	}



	/**
	 * Prive singleton reference
	 * @return
	 */
	private static synchronized TeaPrintWriter getInstance() {
		if (writer==null) {
			writer = new TeaPrintWriter(System.out);
		}
		return writer;
	}

}
