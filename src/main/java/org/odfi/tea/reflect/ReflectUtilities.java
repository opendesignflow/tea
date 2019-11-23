/**
 * 
 */
package org.odfi.tea.reflect;

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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Vector;

/**
 * @author Richnou
 * 
 */
public class ReflectUtilities {

	/**
	 * 
	 */
	public ReflectUtilities() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * This methods foreaches all the parent classes and provided class itself
	 * to find all the fields and return them<BR/>
	 * 
	 * TODO Explore implemented interfaces
	 * 
	 * @param baseClass
	 * @return
	 */
	public static Field[] getAllFieldsFromTop(Class<?> baseClass) {

		
		// Result vector
		Vector<Field> vect = new Vector<Field>();
		// Pointer class
		Class<?> currentClass = baseClass;

		// Foreach for this class and parent classes:-)
		while (currentClass != null) {

			// Get all fields
			for (Field f : currentClass.getDeclaredFields()) {
				
				// Don't consider Synthetic Fields !
				if (f.isSynthetic())
					continue;
				
				vect.addElement(f);
			}
			// Go one level top
			currentClass = currentClass.getSuperclass();
		}

		// Foreach for interfaces
		// currentClass = baseClass.
		// while ( currentClass!=null) {
		//			
		// // Get all fields
		// for (Field f:currentClass.getDeclaredFields()) {
		// vect.addElement(f);
		// }
		// // Go one level top
		// currentClass = currentClass.getSuperclass();
		// }

		// Generate Field[] reversed
		Field[] fields = new Field[vect.size()];
		for (int i = (vect.size() - 1); i >= 0; i--) {
			fields[(vect.size() - 1) - i] = vect.get(i);
		}

		return fields;

	}
	
	/**
	 * This methods foreaches all the parent classes and provided class itself
	 * to find all the fields and return them<BR/>
	 * 
	 * TODO Explore implemented interfaces
	 * 
	 * @param baseClass
	 * @return
	 */
	public static LinkedList<Field> getAllFieldsListFromTop(Class<?> baseClass) {

		
		// Result vector
		LinkedList<Field> vect = new LinkedList<Field>();
		// Pointer class
		Class<?> currentClass = baseClass;

		// Foreach for this class and parent classes:-)
		while (currentClass != null) {

			// Get all fields
			for (Field f : currentClass.getDeclaredFields()) {
				// Don't consider Synthetic Fields !
				if (f.isSynthetic())
					continue;
				vect.add(f);
			}
			// Go one level top
			currentClass = currentClass.getSuperclass();
		}

		return vect;

	}

	/**
	 * 
	 * @param sourceClass
	 * @param fieldName
	 * @return
	 */
	public static Field getField(Class<?> sourceClass,String fieldName) {
		// Declare results
		Field res = null;
		
		while ( sourceClass!=null){
			try {
				res=sourceClass.getDeclaredField(fieldName);
				break;
			} catch (SecurityException e) {
				break;
			} catch (NoSuchFieldException e) {
				sourceClass = sourceClass.getSuperclass();
			}
		}
		
		return res;
	}
	
	/**
	 * Call getFieldName() on the source object
	 * This is a silent method
	 * 
	 * @param sourceClass
	 * @param fieldName
	 * @return the value, null otherwise
	 */
	public static Object getFieldValue(Object source,String fieldName) {
		
		
		
		// Declare results
		Field res = ReflectUtilities.getField(source.getClass(), fieldName);
		res.setAccessible(true);
		
		// First try using getter method
		try {
			Object value = ReflectUtilities.callGetter(source, res);
			return value;
		} catch (Exception e1) {
			// Problem using getter => fail
			e1.printStackTrace();
			try {
				return res.get(source);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		return null;
		
	}
	
	/**
	 * Get Straight one level caller class
	 * @return
	 */
	public static Class<?> getCallerClass() {
		return ReflectUtilities.getCallerClass(2);
	}
	
	public static Class<?> getCallerClass(int level) {
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(new Throwable().fillInStackTrace().getStackTrace()[level].getClassName());
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static StackTraceElement getCallerStackTrace() {
		return ReflectUtilities.getCallerStackTrace(2);
	}
	
	public static StackTraceElement getCallerStackTrace(int level) {
		try {
			return new Throwable().fillInStackTrace().getStackTrace()[level];
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Invokes the standart constructor on the class
	 * 
	 * @param target
	 * @return
	 */
	public  static <C> C instantiate(Class<C> target) {
		try {
			return target.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * This method calls the standart javabean getter method <BR />
	 * If the field is called : field, then this method calls: getField();
	 * 
	 * @param object
	 * @param field
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public static Object callGetter(Object target, Field field) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		String methodName = "get"
				+ Character.toUpperCase(field.getName().charAt(0))
				+ field.getName().substring(1);

		
			return target.getClass().getMethod(methodName, null).invoke(target,
					null);
		
	}
	
	public static Object callGetter(Object target, Field field,Object value) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		String methodName = "get"
				+ Character.toUpperCase(field.getName().charAt(0))
				+ field.getName().substring(1);

		
			return target.getClass().getMethod(methodName, new Class[]{value.getClass()}).invoke(target,
					new Object[]{value});
		
	}
	
	/**
	 * 
	 * @param target
	 * @param field
	 * @param value
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public static void callSetter(Object target, Field field,Object value) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String methodName = "set"
			+ Character.toUpperCase(field.getName().charAt(0))
			+ field.getName().substring(1);

	
		target.getClass().getMethod(methodName, new Class[]{field.getType()}).invoke(target,
				new Object[]{value});
	
	}
	
	public static void setField(Object target,String fieldName,Object value) {
		Field targetField = ReflectUtilities.getField(target.getClass(), fieldName);
		targetField.setAccessible(true);
		try {
			targetField.set(target, value);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get annotation
	 * @param <T>
	 * @param source
	 * @param annotationClass
	 * @return
	 */
	public static <T extends Annotation> T getAnnotation(Class<?> source,Class<T> annotationClass) {
		
		// result annotation
		Annotation res = null;
		
		// Get The annotation by foreaching a source
		for (Annotation ann : source.getAnnotations()) {
//			System.out.println("\t\t[wsp]OUT: Found " + ann.annotationType().getCanonicalName());
			
//			if (ann.annotationType() == Ooxnode.class) {
//				System.out.println("\t\t[wsp]INSTANCE Found OOXnode : ");
//			}
			
			if (ann.annotationType().getCanonicalName().equals(
					annotationClass.getCanonicalName())) {
				// Found
				res = ann;
			}
		}
		
		return (T) res;
		
	}
	
}
