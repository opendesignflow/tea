/**
 * 
 */
package org.odfi.tea.dnd;

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
