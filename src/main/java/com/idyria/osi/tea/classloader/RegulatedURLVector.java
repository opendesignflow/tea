/**
 * 
 */
package com.idyria.osi.tea.classloader;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;

/**
 * @author Richnou
 *
 */
public class RegulatedURLVector extends Vector<URL> {

	
	
	public RegulatedURLVector() {
		super();
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4933854320969526531L;
	private HashSet<String> comparison = new HashSet<String>();
	
	@Override
	public synchronized boolean add(URL url) {
		// Check not already existing
		if (comparison.contains(url.toExternalForm())) {
			return false;
		}
		comparison.add(url.toExternalForm());	
		return super.add(url);
	}

	@Override
	public synchronized boolean addAll(Collection<? extends URL> c) {
		for (URL url : c)
			add(url);
		return true;
	}

	
	
	
	
}
