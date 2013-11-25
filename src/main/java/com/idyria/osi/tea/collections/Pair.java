/**
 * 
 */
package com.idyria.osi.tea.collections;

/**
 * Like the pair interface of C++
 * @author rleys
 *
 */
public class Pair<L, R> {

	
	private L left = null;
	private R right = null;
	
	/**
	 * 
	 */
	public Pair() {
		// TODO Auto-generated constructor stub
	}

	
	
	/**
	 * @param left
	 * @param right
	 */
	public Pair(L left, R right) {
		super();
		this.left = left;
		this.right = right;
	}



	/**
	 * @return the left
	 */
	public L getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(L left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public R getRight() {
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(R right) {
		this.right = right;
	}

	
	
	
}
