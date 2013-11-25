/**
 * 
 */
package com.idyria.osi.tea.i2n;

/**
 * Object containing parameters necessary to find a translation
 * @author rtek
 *
 */
public class TeaI2nTranslate {

	private Class<?> universe = null;
	private String key = null;
	
	/**
	 * 
	 */
	public TeaI2nTranslate() {
		// TODO Auto-generated constructor stub
	}
	
	

	public TeaI2nTranslate(String key, Class<?> universe) {
		super();
		this.key = key;
		this.universe = universe;
	}



	/**
	 * @return the universe
	 */
	public Class<?> getUniverse() {
		return universe;
	}

	/**
	 * @param universe the universe to set
	 */
	public void setUniverse(Class<?> universe) {
		this.universe = universe;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	
	
}
