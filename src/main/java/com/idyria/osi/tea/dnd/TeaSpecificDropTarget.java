/**
 * 
 */
package com.idyria.osi.tea.dnd;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.datatransfer.FlavorMap;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.util.LinkedList;

/**
 * Pass Through implementation that allows more specificly designed drop target
 * to be created, and have listeners registered with them
 * 
 * @author rtek
 * 
 */
public abstract class TeaSpecificDropTarget<LT> extends DropTarget {

	// protected WeakReferencedList<LT> listeners = new
	// WeakReferencedList<LT>();

	private LinkedList<LT> listeners = new LinkedList<LT>();

	/**
	 * @throws HeadlessException
	 */
	public TeaSpecificDropTarget() throws HeadlessException {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param c
	 * @param dtl
	 * @throws HeadlessException
	 */
	public TeaSpecificDropTarget(Component c, DropTargetListener dtl)
			throws HeadlessException {
		super(c, dtl);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param c
	 * @param ops
	 * @param dtl
	 * @throws HeadlessException
	 */
	public TeaSpecificDropTarget(Component c, int ops, DropTargetListener dtl)
			throws HeadlessException {
		super(c, ops, dtl);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param c
	 * @param ops
	 * @param dtl
	 * @param act
	 * @throws HeadlessException
	 */
	public TeaSpecificDropTarget(Component c, int ops, DropTargetListener dtl,
			boolean act) throws HeadlessException {
		super(c, ops, dtl, act);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param c
	 * @param ops
	 * @param dtl
	 * @param act
	 * @param fm
	 * @throws HeadlessException
	 */
	public TeaSpecificDropTarget(Component c, int ops, DropTargetListener dtl,
			boolean act, FlavorMap fm) throws HeadlessException {
		super(c, ops, dtl, act, fm);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Register a listener
	 * 
	 * @param listener
	 * @return
	 */
	public boolean addListener(LT listener) {
		return this.listeners.add(listener);
	}

	/**
	 * @return the listeners
	 */
	public LinkedList<LT> getListeners() {
		return listeners;
	}

}
