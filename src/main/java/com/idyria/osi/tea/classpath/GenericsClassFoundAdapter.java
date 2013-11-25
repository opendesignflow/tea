/**
 * 
 */
package com.idyria.osi.tea.classpath;

/**
 * @author Richnou
 *
 */
public abstract class GenericsClassFoundAdapter<T> implements ClassFoundListener {

	protected T utilObject;
	
	
	
	public GenericsClassFoundAdapter(T utilObject) {
		super();
		this.utilObject = utilObject;
	}



	/**
	 * 
	 */
	public GenericsClassFoundAdapter() {
		// TODO Auto-generated constructor stub
	}

}
