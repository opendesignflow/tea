/**
 * 
 */
package org.odfi.tea.logging;

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

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author rtek
 *
 */
public class TeaLogRecord extends LogRecord {

	/**
	 * identation level
	 */
	protected int indentation = 0;
	
	public TeaLogRecord(Level level, Throwable e) {
		super(level,e.getMessage());
		this.setThrown(e);
	}
	
	public TeaLogRecord(Level level, String msg) {
		super(level, msg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the indentation
	 */
	public int getIndentation() {
		return indentation;
	}

	/**
	 * @param indentation the indentation to set
	 */
	public void setIndentation(int indentation) {
		this.indentation = indentation;
	}

	

}
