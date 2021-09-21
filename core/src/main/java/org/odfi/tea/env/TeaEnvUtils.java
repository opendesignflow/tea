/**
 * 
 */
package org.odfi.tea.env;

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

import java.io.PrintStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rleys
 *
 */
public class TeaEnvUtils {

	/**
	 * 
	 */
	public TeaEnvUtils() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	/**
	 * List environement state to output
	 * @param output
	 */
	public static void listEnvironement(PrintStream output) {
		
		for (Entry<String,String> entry:System.getenv().entrySet()) {
			output.println(entry.getKey()+"="+entry.getValue());
		}
		
	}
	
	/**
	 * Replaces parts of the source String with environement variables
	 * @param source
	 * @return
	 */
	public static String replaceWithEnv(String source) {
		
		
		
		return TeaEnvUtils.replaceWithEnv(source, System.getenv());		
	}
	
	/**
	 * Replaces parts of the source String with provided environment variables
	 * @param source
	 * @param sourceEnv map<varname,value> of the allowed variable for replacement
	 * @return
	 */
	public static String replaceWithEnv(String source,Map<String,String> sourceEnv) {
		
		//-- Foreach All Env variable
		String result = source;
		int replacedChars = 0; // Number of chars replaced by last match
		
		for (String key : sourceEnv.keySet()) {
			
			//-- Can we find this env variable value in our source
			//-- Make replacement if we can replace more chars than last replacement
			String envValue = System.getenv(key);
			if(source.contains(envValue) && envValue.length()>replacedChars) {
				
				
				
				result = source.replace(envValue, "${"+key+"}");
				
				System.err.println("Replacing in "+source+" : "+envValue+" by ${"+key+"} => "+result);
				
				replacedChars = envValue.length();	
			}
			
		} // EOF Env foreach
		
		return result;		
	}
	
	/**
	 * Replace ${} defined env variables in string with their values
	 * @param source
	 * @return
	 */
	public static String resolveEnv(String source) {
	
		String result = source;
		// Regexp
		String regexp="(?:.*(?:\\$\\{(.*)\\}))+.*";
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(source);
		if (m.matches()) {
		//System.err.println("MATCH!: "+m.groupCount());
			// Foreach groups to locate each variable to replace
			for (int i=1;i<=m.groupCount();i++) {
				
				// Get Variable
				String envvar = m.group(i);
				//System.err.println("Replacing!: "+envvar);
				// Get Value
				String val = System.getenv(envvar);
				
				// Add to map and replace if there is one
				if (val!=null) {
					//this.stringToEnvironement.put(val, envvar);
					result = result.replace("${"+envvar+"}",val);
				}
				
			}
			
		} else {
			//System.err.println("Doesn't match ("+source+")");
		}
		
		return result;
		
	}
	

}
