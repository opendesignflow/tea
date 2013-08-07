/**
 * 
 */
package com.idyria.osi.tea.logging;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Richnou
 * 
 */
public class TeaLogging {

	private static ConcurrentHashMap<String, Logger> loggers = new ConcurrentHashMap<String, Logger>();

	private static Class<? extends Handler> defaultHandler = TeaLoggingHandler.class;
	
	/**
	 * 
	 */
	public TeaLogging() {
		Logger logger = Logger.getLogger("tea");
		logger.setUseParentHandlers(false);
		try {
			logger.addHandler(defaultHandler.newInstance());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized (loggers) {
			loggers.put("tea", logger);
		}
	}


	/**
	 * Sets the default handler to be used by tea logging
	 * @param defaultHandlerClass
	 */
	public static synchronized void setDefaultHandler (Class<? extends Handler> defaultHandlerClass) {
		TeaLogging.defaultHandler = defaultHandlerClass;
	}
	
	
	/**
	 * Get a logger using standard Logger.getLogger, put retains a reference in the MAP
	 * @param name
	 * @return
	 */
	public static synchronized Logger getLogger(String name) {
		Logger logger = Logger.getLogger("tea."+name);
		synchronized (loggers) {
			loggers.put("tea."+name, logger);
		}
		return logger;
	}
	
	/**
	 * 
	 * @return The default handler defined in TeaLogging or null if could not created
	 */
	public static synchronized Handler getNewDefaultHandler() {
		try {
			return TeaLogging.defaultHandler.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void teaLog(String message, Level level) {

		teaLog(message, level, 0);

	}

	public static void teaLogInfo(String message, int deep) {

		teaLog(message, Level.INFO, deep);

	}

	public static void teaLogInfo(String message) {

		teaLog(message, Level.INFO, 0);

	}

	public static void teaLogFine(String message, int deep) {

		teaLog(message, Level.FINE, deep);

	}

	public static void teaLogFine(String message) {

		teaLog(message, Level.FINE, 0);

	}
	
	public static void teaLogFiner(String message, int deep) {

		teaLog(message, Level.FINER, deep);

	}

	public static void teaLogFiner(String message) {

		teaLog(message, Level.FINER, 0);

	}
	
	public static void teaLogFinest(String message, int deep) {

		teaLog(message, Level.FINEST, deep);

	}

	public static void teaLogFinest(String message) {

		teaLog(message, Level.FINEST, 0);

	}
	
	public static void teaLogWarning(String message, int deep) {

		teaLog(message, Level.WARNING, deep);

	}

	public static void teaLogWarning(String message) {

		teaLog(message, Level.WARNING, 0);

	}
	
	public static void teaLogSevere(String message, int deep) {

		teaLog(message, Level.SEVERE, deep);

	}

	public static void teaLogSevere(String message) {

		teaLog(message, Level.SEVERE, 0);

	}
	
	public static void teaLogSevere(Throwable e) {

		teaLog(e, Level.SEVERE);

	}

	private static void teaLog(String message, Level level, int deep) {

		// Retrieve Caller informations
		StackTraceElement elt = getCallerStackTrace(3);

		Class cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader().loadClass(elt.getClassName());
			while (cl.getEnclosingClass() != null)
				cl = cl.getEnclosingClass();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String callerClass = cl.getSimpleName();
		String methodName = elt.getMethodName();
		

		// Prepare message deepnest
		for (int i = 0; i < deep; i++) {
			message = "    " + message;
		}

		//--- Log
		
		// Get logger
		//----------------------------
		Logger logger = null;
		synchronized (loggers) {
			logger = loggers.get(cl);
			/*if (logger == null) {
				
				logger = Logger.getLogger(cl.getPackage().getName());
				//logger.addHandler(new TeaLoggingHandler());
				//logger.setUseParentHandlers(true);
				
				
				//loggers.put(cl, logger);
			}*/
			
			logger = TeaLogging.getLogger(cl.getPackage().getName());
			
			
			
			//if (logger.getParent()!=null) logger.setLevel(logger.getParent().getLevel());
			
			//System.err.println("DDDDD-> "+cl.getPackage().getName()+" with level "+(logger.getLevel()!=null?logger.getLevel().getName():"no level"));
			
			if (logger.getHandlers().length==0) {
				//logger.addHandler(TeaLogging.getNewDefaultHandler());
				//logger.setUseParentHandlers(false);
			} else {
				
			}
			//if (logger.getParent()==null || logger.getParent()==Logger.getGlobal()) {
				
			//} else {
				
			//}
			
		}
		
		// Prepare LogRecord && log
		//--------------------------
		TeaLogRecord record = new TeaLogRecord(level, message);
		record.setSourceClassName(callerClass);
		record.setSourceMethodName(methodName);
		logger.log(record); // Log
		
	}
	
	private static void teaLog(Throwable exception, Level level) {

		// Retrieve Caller informations
		StackTraceElement elt = getCallerStackTrace(3);

		Class cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader().loadClass(elt.getClassName());
			while (cl.getEnclosingClass() != null)
				cl = cl.getEnclosingClass();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String callerClass = cl.getSimpleName();
		String methodName = elt.getMethodName();
	

		//--- Log
		
		// Get logger
		//----------------------------
		Logger logger = null;
		synchronized (loggers) {
			/*logger = loggers.get(cl);
			if (logger == null) {
				logger = Logger.getLogger(cl.getCanonicalName());
				logger.addHandler(new TeaLoggingHandler());
				logger.setUseParentHandlers(false);
				loggers.put(cl, logger);
			}*/
			logger = Logger.getLogger(cl.getCanonicalName());
			if (logger.getHandlers().length==0) {
				logger.addHandler(TeaLogging.getNewDefaultHandler());
				logger.setUseParentHandlers(false);
			}
			//if (logger.getParent()==null || logger.getParent()==Logger.getGlobal()) {
				
			//} else {
				
			//}
			
		}
		
		// Prepare LogRecord && log
		//--------------------------
		TeaLogRecord record = new TeaLogRecord(level, exception);
		record.setSourceClassName(callerClass);
		record.setSourceMethodName(methodName);
		logger.log(record); // Log

	}

	public static boolean teaIsLoggable(Level level) {

		// Get Caller
		StackTraceElement caller = getCallerStackTrace(2);
		Logger log = Logger.getLogger(caller.getClassName());
		if (log.isLoggable(level))
			return true;

		return false;
	}


	/**
	 * Gets the caller stacktrace depending on the level of invocation asked
	 * 
	 * @param level
	 * @return
	 */
	private static StackTraceElement getCallerStackTrace(int level) {
		return new Throwable().fillInStackTrace().getStackTrace()[level];
	}
	
	

}
