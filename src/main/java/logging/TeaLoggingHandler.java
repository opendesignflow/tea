/**
 * 
 */
package com.idyria.osi.tea.logging;

import java.io.PrintStream;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Richnou
 *
 */
public class TeaLoggingHandler extends Handler {

	private ReentrantLock consoleLock = new ReentrantLock();
	
	/**
	 * 
	 */
	public TeaLoggingHandler() {
		// TODO Auto-generated constructor stub
		//this.g
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Handler#close()
	 */
	@Override
	public void close() throws SecurityException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.util.logging.Handler#flush()
	 */
	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public void publish(LogRecord record) {

		// Select output
		PrintStream out = record.getLevel() == Level.SEVERE?System.err:System.out;

		if (record.getLevel().intValue() < this.getLevel().intValue())
			return;
		
		consoleLock.lock();
		// Print preamble
		//---------------
		
		// Indentation
		if (record instanceof TeaLogRecord) {
			for (int i=0;i<((TeaLogRecord) record).getIndentation();i++) {
				out.println("\t");
			}
		}
		
		//out.print("[");
		// Level info
		out.print("("+record.getLevel().toString()+") ");
		
		// Time
		out.print("("+record.getMillis()+") ");
		
		// Localisation
		if (record.getSourceClassName()!=null)
			out.print(record.getSourceClassName());
		if (record.getSourceMethodName()!=null)
			out.print("."+record.getSourceMethodName()+"()");
		//out.print("]");
		
		// Print message
		//----------------------
		out.print(record.getMessage());
		
		// Close line
		out.println();
		
		consoleLock.unlock();

	}

}
