/**
 * 
 */
package com.idyria.osi.tea;

/**
 * @author Richnou
 * 
 */
public class InvocationUtilities {

	/**
	 * 
	 */
	public InvocationUtilities() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param <T>
	 * @param value
	 * @throws IllegalArgumentException
	 */
	public static <T> void validateArgumentThrowing(T value,String name)
			throws IllegalArgumentException {

		if (value == null) {
			// Throw
			throw new IllegalArgumentException("The Argument "+name+" is null");
		} else if (value instanceof String && ((String) value).length() == 0) {
			// Throw
			throw new IllegalArgumentException("The String argument "+name+" is null");
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param value
	 * @return
	 */
	public static <T> boolean validateArgumentBoolean(T value) {
		boolean res = true;
		try {
			InvocationUtilities.validateArgumentThrowing(value,"");
		} catch (IllegalArgumentException ex) {
			res = false;
		}
		return res;
	}

}
