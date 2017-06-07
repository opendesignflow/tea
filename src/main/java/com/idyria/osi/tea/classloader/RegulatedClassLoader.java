/**
 * 
 */
package com.idyria.osi.tea.classloader;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Vector;

/**
 * @author Richnou
 * 
 */
public class RegulatedClassLoader extends ClassLoader {

	private URLClassLoader urlClassLoader = null;

	/**
	 * @param urls
	 */
	public RegulatedClassLoader(URL[] urls) {
		super();
		urlClassLoader = new URLClassLoader(consolidate(urls));
	}

	/**
	 * @param urls
	 * @param parent
	 */
	public RegulatedClassLoader(URL[] urls, ClassLoader parent) {
		super();
		urlClassLoader = new URLClassLoader(consolidate(urls),parent);
	}

	/**
	 * @param urls
	 * @param parent
	 * @param factory
	 */
	public RegulatedClassLoader(URL[] urls, ClassLoader parent,
			URLStreamHandlerFactory factory) {
		super();
		urlClassLoader = new URLClassLoader(consolidate(urls),parent,factory);
	}

	private URL[] consolidate(URL[] urls) {
		
		Vector<URL> set = new Vector<URL>();
		HashSet<String> comparison = new HashSet<String>();
		for (URL url : urls) {
			// Check not already existing
			if (comparison.contains(url.toExternalForm()))
				continue;
			// Record further
			comparison.add(url.toExternalForm());
			set.add(url);
		}
		
		return set.toArray(new URL[set.size()]);
		
	}

	public void clearAssertionStatus() {
		urlClassLoader.clearAssertionStatus();
	}

	public boolean equals(Object obj) {
		return urlClassLoader.equals(obj);
	}

	public URL findResource(String name) {
		return urlClassLoader.findResource(name);
	}

	public Enumeration<URL> findResources(String name) throws IOException {
		return urlClassLoader.findResources(name);
	}


	public URL getResource(String name) {
		return urlClassLoader.getResource(name);
	}

	public InputStream getResourceAsStream(String name) {
		return urlClassLoader.getResourceAsStream(name);
	}

	public Enumeration<URL> getResources(String name) throws IOException {
		return urlClassLoader.getResources(name);
	}

	public URL[] getURLs() {
		return urlClassLoader.getURLs();
	}

	public int hashCode() {
		return urlClassLoader.hashCode();
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return urlClassLoader.loadClass(name);
	}

	public void setClassAssertionStatus(String className, boolean enabled) {
		urlClassLoader.setClassAssertionStatus(className, enabled);
	}

	public void setDefaultAssertionStatus(boolean enabled) {
		urlClassLoader.setDefaultAssertionStatus(enabled);
	}

	public void setPackageAssertionStatus(String packageName, boolean enabled) {
		urlClassLoader.setPackageAssertionStatus(packageName, enabled);
	}

	public String toString() {
		return urlClassLoader.toString();
	}

	
	
}
