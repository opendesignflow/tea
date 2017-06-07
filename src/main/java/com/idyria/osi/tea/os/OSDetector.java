/**
 * 
 */
package com.idyria.osi.tea.os;

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
 * @author rtek
 *
 */
public class OSDetector {
 
	/**
	 * 
	 */
	public OSDetector() {
		// TODO Auto-generated constructor stub
	}
	
	public static enum OS {
		UNKNOWN,LINUX,WINXP,WINXP_64,VISTA,VISTA_64,WIN7,WIN8,WIN10
	}
	
	
	public static OS getOS() {
		String osname = System.getProperty("os.name");
		if (osname.contains("Windows Vista"))
			return OS.VISTA;
		else if (osname.contains("Windows XP"))
			return OS.WINXP;
		else if (osname.contains("Windows 7"))
			return OS.WIN7;
		else if (osname.contains("Windows 8"))
			return OS.WIN8;
		else if (osname.contains("Windows 10"))
			return OS.WIN10;
		else if (osname.contains("Linux"))
			return OS.LINUX;
		else
			return OS.UNKNOWN;
	}
	
	public static boolean  isWindows() {
		String osname = System.getProperty("os.name");
		if (osname.contains("Windows")) {
			return true;
		} else {
			return false;
		}
	}

}
