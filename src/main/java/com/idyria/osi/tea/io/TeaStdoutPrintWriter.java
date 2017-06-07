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
