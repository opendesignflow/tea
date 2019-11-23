/**
 * 
 */
package org.odfi.tea.io;

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
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.odfi.tea.TeaStringUtils;

/**
 * 
 * A Print Writer with a few additions
 * 
 * @author rleys
 *
 */
public class TeaPrintWriter extends PrintWriter {

	
	/**
	 * A number of tabs*indent will be added to output to help simplify beautifying outputs.
	 * 
	 */
	protected int lineIndent = 0;
	
	/**
	 * Cares that we only indent once per line
	 * Will be reseted to false by println() call.
	 * Used to avoid two succesful call to print(xxx) have indentation.
	 */
	protected boolean currentLineIndented = false;
	
	/**
	 * The used character to indent
	 */
	protected String indentCharacter = "    ";
	
	/**
	 * @param out
	 */
	public TeaPrintWriter(Writer out) {
		super(out);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param out
	 */
	public TeaPrintWriter(OutputStream out) {
		super(out);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public TeaPrintWriter(String fileName) throws FileNotFoundException {
		super(fileName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param file
	 * @throws FileNotFoundException
	 */
	public TeaPrintWriter(File file) throws FileNotFoundException {
		super(file);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param out
	 * @param autoFlush
	 */
	public TeaPrintWriter(Writer out, boolean autoFlush) {
		super(out, autoFlush);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param out
	 * @param autoFlush
	 */
	public TeaPrintWriter(OutputStream out, boolean autoFlush) {
		super(out, autoFlush);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param fileName
	 * @param csn
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public TeaPrintWriter(String fileName, String csn)
			throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param file
	 * @param csn
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public TeaPrintWriter(File file, String csn) throws FileNotFoundException,
			UnsupportedEncodingException {
		super(file, csn);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Double Println: println the provided string, and add another println() afterwards
	 * @param s
	 */
	public void dprintln(String s ) {
		super.println(s);
		super.println();
	}
	
	/* (non-Javadoc)
	 * @see java.io.PrintWriter#write(char[], int, int)
	 */
	@Override
	public void write(char[] buf, int off, int len) {
		//-- Add the number of tabs
		if (!this.currentLineIndented && this.lineIndent > 0 ) {
			
			StringWriter sout = new StringWriter();
			
			//-- Prepare addition
			String indentString = TeaStringUtils.repeat(this.indentCharacter, this.lineIndent);
			String indentedString = indentString+new String(buf);
			
			//-- Inject indentation at every new line char
			for (char c : indentedString.toCharArray()) {
				
				// Stream char
				sout.write(c);
				
				// If its a newline, inject tabbing
				if (c == '\n') {
					sout.write(indentString);
				}
				
			}
			
			//char[] indentarray = indentString.toCharArray();
			//char[] newbuf = new char[len+indentString.length()];
			
			// Copy Indentation and original buf
			//System.arraycopy(indentarray, 0, newbuf, 0, indentarray.length);
			//System.arraycopy(buf, off, newbuf, indentarray.length, len);
			
			//-- Write
			super.write(sout.toString(),0, sout.toString().length());
			
			//-- Set indented
			this.currentLineIndented = true;
			
		} else
			super.write(buf, off, len);
	}
	
	

	/* (non-Javadoc)
	 * @see java.io.PrintWriter#write(java.lang.String, int, int)
	 */
	@Override
	public void write(String s, int off, int len) {
		
		//-- Add the number of tabs
		if (!this.currentLineIndented) {
			
			//-- Prepare addition
			String indentString = TeaStringUtils.repeat(this.indentCharacter, this.lineIndent);
			String indented =  indentString + s.substring(off, off+len);
			
			//-- Inject indentation at every new line char
			StringWriter sout = new StringWriter();
			for (char c : indented.toCharArray()) {
				
				// Stream char
				sout.write(c);
				
				// If its a newline, inject tabbing
				if (c == '\n') {
					sout.write(indentString);
				}
				
			}
			
			//-- Write
			super.write(sout.toString(),0, sout.toString().length());
			
			//-- Set indented
			this.currentLineIndented = true;
			
		} else
			super.write(s, off, len);
	}

	/* (non-Javadoc)
	 * @see java.io.PrintWriter#println()
	 */
	@Override
	public void println() {
		//-- Let super
		super.println();
		
		//-- Reset indentation
		this.currentLineIndented = false;
	}
	
	

	@Override
	public void println(String x) {
		
		//-- Write
		this.write(x);
		
		//-- Add Line Return
		this.println();
	}

	/**
	 * Will add one tab more to line outputs
	 */
	public void indent() {
		this.indent(1);
	}
	
	/**
	 * Will add count tab more to line outputs
	 * @param count
	 */
	public void indent(int count) {
		lineIndent+=count;
	}
	
	/**
	 * Will remove one tab to line output
	 */
	public void outdent() {
		this.outdent(1);
	}
	
	/**
	 * Will remove count tab to line output
	 * @param count
	 */
	public void outdent(int count) {
		lineIndent-=count;
	}

}
