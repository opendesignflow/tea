/**
 * 
 */
package com.idyria.osi.tea.thread;

/**
 * @author Richnou
 *
 */
public abstract class TeaRunnable<T> implements Runnable {

	/**
	 * The object to carry
	 */
	protected T object = null;
	
	
	/**
	 * 
	 */
	public TeaRunnable() {
		// TODO Auto-generated constructor stub
	}

	
	

	/**
	 * @param object
	 */
	public TeaRunnable(T object) {
		super();
		this.object = object;
	}




	/**
	 * @return the object
	 */
	public T getObject() {
		return this.object;
	}


	/**
	 * @param object the object to set
	 */
	public void setObject(T object) {
		this.object = object;
	}

	
	
}
