/**
 * 
 */
package com.idyria.osi.tea.i2n;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.idyria.osi.tea.TeaStringUtils;
import com.idyria.osi.tea.reflect.ReflectUtilities;

/**
 * This class is a proxy for Internationalization.<br/> It is designed to help
 * working easily with Bundles Text fetching
 * 
 * FIXME Multi classloader reload
 * 
 * @author Richnou
 * 
 */
public class TeaI2n {

	private static TeaI2n ref = null;

	/**
	 * Maps bundles to universe keys
	 */
	private HashMap<String, ResourceBundle> bundles = new HashMap<String, ResourceBundle>();

	private HashSet<WeakReference<TeaI2nTranslatable>> translatables = new HashSet<WeakReference<TeaI2nTranslatable>>();

	/**
	 * Bundle for this class messages
	 */
	private String localBundleUniverse = "twist";

	private static final String basePath = "META-INF/i2n/";

	/**
	 * 
	 */
	private TeaI2n() {
		super();
	}

	/**
	 * Retrieves the
	 * 
	 * @param universe
	 *            the universe name
	 * @param key
	 *            the bundle.key where to find the value
	 */
	public synchronized String getText(String universe, String key) {

		// // Prepare Path to bundle
		// String path = basePath + universe.replace('.', '/');
		// String finalkey = "";
		//
		// // Extract key from bundle
		// String[] splitted = key.split("\\.");
		// if (splitted.length == 1) {
		// finalkey = splitted[0];
		// } else if (splitted.length > 1) {
		// // Complete Path
		// for (int i = 0; i < splitted.length - 1; i++) {
		// path += "/" + splitted[i];
		// }
		// // key
		// finalkey = splitted[splitted.length - 1];
		// }
		String path = basePath + universe;
		String finalkey = key;

		// Try to get the bundle from map
		System.out.println("Bundle: " + path);
		// URL urlpath = getClass().getClassLoader().getResource(path);
		ResourceBundle.clearCache();
		ResourceBundle bundle = this.bundles.get(path);
		if (bundle == null) {
			try {
				bundle = ResourceBundle.getBundle(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (bundle == null)
				return null;
			// Record bundle
			this.bundles.put(path, bundle);
		}

		try {
			return bundle.getString(finalkey);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns value for a key in the calling class universe
	 * 
	 * @param key
	 * @return
	 */
	public synchronized String getValue(String key)
			throws TeaI2nUniverseNotFound {

		// Prepare
		// TeaI2nTranslate tr = new TeaI2nTranslate(key,null);
		return this.getValue(key, null);

	}
	
	/**
	 * Returns value for a key in the calling class universe. Doesn't throw any exceptions
	 * 
	 * @param key
	 * @return
	 */
	public synchronized String getValueNoException(String key) {

		// Prepare
		// TeaI2nTranslate tr = new TeaI2nTranslate(key,null);
		try {
			return this.getValue(key, null);
		} catch (TeaI2nUniverseNotFound e) {
			return null;
		} catch (Exception e) {
			return null;
		}

	}
	

	/**
	 * Returns value for a key in the calling class universe
	 * 
	 * @param key
	 * @return
	 */
	public synchronized String getValue(String key, Object callerInstance)
			throws TeaI2nUniverseNotFound {

		// Prepare
		// -------------

		// Try to get caller class
		Class<?> caller = ReflectUtilities.getCallerClass(2);
		if (caller == this.getClass())
			caller = ReflectUtilities.getCallerClass(3);

		TeaI2nTranslate tr = new TeaI2nTranslate(key, caller);
		return this.getValue(tr, callerInstance);

	}

	/**
	 * Returns value for a key in the calling class universe
	 * 
	 * @param key
	 * @return
	 */
	public synchronized String getValue(TeaI2nTranslate tr)
			throws TeaI2nUniverseNotFound {
		return this.getValue(tr, null);
	}

	/**
	 * Returns value for a key in the calling class universe
	 * 
	 * @param key
	 * @return
	 */
	public synchronized String getValue(TeaI2nTranslate tr,
			Object callerInstance) throws TeaI2nUniverseNotFound {

		// Prepare
		ResourceBundle universe = null;

		// Get the Universe
		Class<?> caller = tr.getUniverse();
		if (caller != null) {
			// Try to get Universe
			universe = this.getBundle(TeaStringUtils.javaPathToFilePath(caller
					.getPackage().getName()
					+ "." + caller.getSimpleName()));
		}
		if (universe == null) {
			throw new TeaI2nUniverseNotFound(TeaStringUtils.javaPathToFilePath(caller
					.getPackage().getName()
					+ "." + caller.getSimpleName()));
		}
		// TeaLogging.teaLogInfo(caller.getSimpleName());

		// Register caller?
		if (callerInstance != null
				&& TeaI2nTranslatable.class.isAssignableFrom(callerInstance
						.getClass())) {
			synchronized (this.translatables) {
				this.registerTranslatable((TeaI2nTranslatable) callerInstance);
			}
		}

		// Get key
		try {
			return universe.getString(tr.getKey());
		} catch (MissingResourceException ex) {
			return "";
		}

	}
	
	/**
	 * Create TeaI2nTranslate with key and detects Universe alone (the caller)
	 * @param key
	 * @return
	 */
	public TeaI2nTranslate getTeaI2nTranslate(String key) {
		
		// Get Universe
		Class<?> universe = ReflectUtilities.getCallerClass(2);
		
		// Create
		return new TeaI2nTranslate(key,universe);
	}

	/**
	 * 
	 * @param lc
	 */
	public void changeDefaultLocale(Locale lc) {
		// Change locale
		Locale.setDefault(lc);
		// clear Bundles
		ResourceBundle.clearCache();

		// Signal to EveryBody
		synchronized (this.translatables) {
			Iterator<WeakReference<TeaI2nTranslatable>> it = this.translatables
					.iterator();
			while (it.hasNext()) {

				// Get and check
				WeakReference<TeaI2nTranslatable> ref = it.next();
				TeaI2nTranslatable translatable = ref.get();
				if (translatable == null) {
					it.remove();
					continue;
				}

				// Signal
				translatable.defaultLocalChanged();
			}
		}
	}

	private synchronized ResourceBundle getBundle(String path) {
		// ResourceBundle res = this.bundles.get(path);
		// if (res == null) {
		// try {
		// res = ResourceBundle.getBundle(path);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// if (res == null)
		// return null;
		// // Record bundle
		// this.bundles.put(path, res);
		// }
		//		
		//		
		// return res;
		try {
			return ResourceBundle.getBundle(path,Locale.getDefault(), Thread.currentThread().getContextClassLoader());
		} catch (MissingResourceException e) {
			//e.printStackTrace();
			return null;
		}
	}

	private synchronized void registerTranslatable(
			TeaI2nTranslatable translatable) {

		synchronized (this.translatables) {
			Iterator<WeakReference<TeaI2nTranslatable>> it = this.translatables
					.iterator();
			boolean found = false;
			while (it.hasNext()) {

				// Get and check
				WeakReference<TeaI2nTranslatable> ref = it.next();
				TeaI2nTranslatable trans = ref.get();
				if (translatable == null) {
					it.remove();
					continue;
				}

				// Check
				if (trans == translatable) {
					found = true;
					break;
				}
			}
			// Not found, record
			if (!found) {
				System.out.println("Registering a Translatable");
				this.translatables.add(new WeakReference<TeaI2nTranslatable>(
						(TeaI2nTranslatable) translatable));
			}

		}

	}

	public static synchronized TeaI2n getInstance() {
		if (ref == null)
			ref = new TeaI2n();
		return ref;
	}

}
