/**
 * 
 */
package org.odfi.tea;

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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.odfi.tea.io.TeaStdoutPrintWriter;

/**
 * @author rtek
 *
 */
public class TeaStringUtils {

	/**
	 * 
	 */
	public TeaStringUtils() {
		// TODO Auto-generated constructor stub
	}

	public static String javaPathToFilePath(String source) {
		return source.replace('.','/');
	}
	
	/**
	 * Creates a string by repeating the base string a count times
	 * @param base
	 * @param count
	 * @return
	 */
	public static String repeat(String base,int count) {
		String res = "";
		for (int i= 0 ; i<count; i++) {
			res += base;
		}
		
		return res;
		
	}
	
	
	/**
	 * For each line in the base string, determine all the colums by splitting to white space,
	 * and rewrite string so that all columns start at the same point, producing an "aligned" effect
	 * @param base
	 * @param columnInjectPosition The extra columns to be injected are placed at this position
	 * @return
	 */
	public static String alignLinesColumns(String base,int columnInjectPosition) {
		
		//-- Get All lines
		String[] lines = base.split("\\n\\r?");
		
		// Determine the highest number of columns
		//-------------------------
		int columnsCount = 0;
		for (String line : lines) {
			
			//-- Split to whitespaces and count
			String[] columns = line.split("\\s+");
			if (columns.length>columnsCount)
				columnsCount = columns.length;
		}
		
		TeaStdoutPrintWriter.println("Aligner has "+lines.length+" lines and a max of "+columnsCount+" columns");
		
		// Construct a table of linesxhighest number of columns
		//----------------------
		
		//-- Prepare
		String[][] linesAndColumns = new String[lines.length][columnsCount];
		int[] columnsLengths = new int[columnsCount];
		for (int i=0;i<columnsCount;i++)
			columnsLengths[i] = 0;
		
		//-- For each line, and each column, copy at the right place in the table
		//-- And detect for each column,the longest string
		for (int lineIndex = 0; lineIndex<lines.length;lineIndex++) {
			
			//-- Columns: Split to whitespaces
			String[] columns = lines[lineIndex].split("\\s+");
			boolean inject = columns.length<columnsCount?true:false;
			int injectCount = columnsCount-columns.length;
			
			//-- Copy
			for (int i=0;i<columnsCount;i++) {
				
				//-- If we are at injection position, do inject if necessary
				//-- injecting is equivalent to jumping
				if (inject && i==columnInjectPosition) {
					i+=injectCount;
				} 
				
				//-- Copy string
				int columnIndex = !inject || i<columnInjectPosition ? i : i-injectCount;
				linesAndColumns[lineIndex][i] = columns[columnIndex];
				
				//-- Check on length
				if (columnsLengths[i]<columns[columnIndex].length())
					columnsLengths[i] = columns[columnIndex].length();
					
				
				
			}
			
		}
		
		
		
		// Rebuild String
		StringWriter sout = new StringWriter();
		PrintWriter out = new PrintWriter(sout);
		for (String[] line : linesAndColumns ) {
			for (int i=0;i<line.length;i++) {
				
				// Write out string, and padd it if necessary
				String resultLine = line[i];
				if (resultLine==null)
					resultLine = "";
				resultLine+=TeaStringUtils.repeat(" ", columnsLengths[i]-(resultLine.length()));
				out.print(resultLine+" ");
			}
			out.println();
		
		}
		
		return sout.toString();
		
	}
	
	/**
	 * Joins the string array in a single String by putting the separator char in between every provided strings:
	 * 
	 * string[] = {A,B,C}
	 * separator = ";"
	 * 
	 * will output: "A;B;C"
	 * 
	 * @param strings
	 * @param separator
	 * @return The produced string, or "" if strings is empty or null
	 */
	public static String join(String[] strings,String separator) {
		
		if (strings==null || strings.length==0)
			return "";
		
		String result = "";
		
		//-- Loop on table, and produce the string
		for (int i =0 ; i<strings.length;i++) {
			result += strings[i];
			result += i==strings.length-1 ? "":separator;
		}
		
		return result;
		
		
	}
	
	
	
}
