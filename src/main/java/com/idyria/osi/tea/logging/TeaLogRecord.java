/**
 * 
 */
package com.idyria.osi.tea.logging;

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
