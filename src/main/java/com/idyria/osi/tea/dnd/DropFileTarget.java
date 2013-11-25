/**
 * 
 */
package com.idyria.osi.tea.dnd;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorMap;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * A drop Target to easily receive files dropped (for example dropped from the
 * system)
 * 
 * @author rtek
 * 
 */
public class DropFileTarget extends
		TeaSpecificDropTarget<DropFileTargetListener> implements
		DropTargetListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5943525545440908943L;

	/**
	 * A File filter to limit accepted files
	 */
	private FileFilter allowedFileFilter = null;

	/**
	 * @throws HeadlessException
	 */
	public DropFileTarget() throws HeadlessException {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws HeadlessException
	 */
	public DropFileTarget(Component arg0, DropTargetListener arg1)
			throws HeadlessException {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws HeadlessException
	 */
	public DropFileTarget(Component arg0, int arg1, DropTargetListener arg2)
			throws HeadlessException {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @throws HeadlessException
	 */
	public DropFileTarget(Component arg0, int arg1, DropTargetListener arg2,
			boolean arg3) throws HeadlessException {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @throws HeadlessException
	 */
	public DropFileTarget(Component arg0, int arg1, DropTargetListener arg2,
			boolean arg3, FlavorMap arg4) throws HeadlessException {
		super(arg0, arg1, arg2, arg3, arg4);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DropTarget#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public synchronized void dragEnter(DropTargetDragEvent dtde) {
		System.out.println("Drag entered");
		boolean accept = false;
		for (DataFlavor dtf : dtde.getCurrentDataFlavorsAsList()) {

			System.out.println("Entered with Flavor: " + dtf.getMimeType());

			// Accept if it is a file list
			// And file filter is matched
			// ------------------------------------
			if (this.allowedFileFilter == null) {
				accept = (dtf.isFlavorJavaFileListType() || (dtf
						.isMimeTypeEqual("text/uri-list") && dtf
						.getRepresentationClass() == String.class)) ? true : false;
				
				System.out.println("-> accept: " + accept);
				
			} else if (dtf.isFlavorJavaFileListType()) {

				// -- Try to find at least one file that match the filter
				try {
					for (File file : (List<File>) dtde.getTransferable()
							.getTransferData(dtf)) {
						if (this.allowedFileFilter.accept(file)) {
							accept = true;
							break;
						}
					}
				} catch (UnsupportedFlavorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (dtf.isMimeTypeEqual("text/uri-list")) {

				try {

					if (dtf.getRepresentationClass().equals(byte[].class)) {

						System.out.println("Byte Array URI List: "
								+ new String((byte[]) dtde.getTransferable()
										.getTransferData(dtf)));

					} else if (dtf.getRepresentationClass()
							.equals(String.class)) {

						// -- Try to find at least one file that match the
						// filter

						for (String file : dtde.getTransferable()
								.getTransferData(dtf).toString()
								.split("\\n\\r")) {
							try {
								if (this.allowedFileFilter.accept(new File(
										new URI(file).getPath()))) {
									accept = true;
									break;
								}
							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}

				} catch (UnsupportedFlavorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			// If accepted, stop looking
			if (accept)
				break;
			
		} // EOF For
		

		if (accept)
			dtde.acceptDrag(dtde.getDropAction());
		else
			dtde.rejectDrag();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DropTarget#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public synchronized void drop(DropTargetDropEvent dtde) {
		// TODO Auto-generated method stub
		System.out.println("Drop");

		try {
			// Accept
			dtde.acceptDrop(dtde.getDropAction());

			// Check
			if (dtde.getTransferable() == null
					|| dtde.getTransferable().getTransferData(
							dtde.getCurrentDataFlavors()[0]) == null) {
				return;
			}

			// The list of files to give
			List<File> flist = new LinkedList<File>();

			// Find the first suitable dataflavor and add files in it to list
			for (DataFlavor dtf : dtde.getCurrentDataFlavorsAsList()) {

				// File list
				// ------------
				if (dtf.isFlavorJavaFileListType()) {

					// -- Add to list of files, filter perhaps
					if (this.allowedFileFilter == null)
						flist.addAll((List<File>) dtde.getTransferable()
								.getTransferData(dtf));
					else {
						for (File file : (List<File>) dtde.getTransferable()
								.getTransferData(dtf)) {
							if (this.allowedFileFilter.accept(file)) {
								flist.add(file);
							}
						}
					}
					break;

				}
				// URI List
				// --------------
				else if (dtf.isMimeTypeEqual("text/uri-list")
						&& dtf.getRepresentationClass().equals(String.class)) {

					System.out.println("Dropping file: "
							+ dtde.getTransferable().getTransferData(dtf));

					for (String file : dtde.getTransferable()
							.getTransferData(dtf).toString().split("\\n\\r")) {
						try {
							// -- Add to list of files, filter perhaps
							File jFile = new File(new URI(file).getPath());
							if (this.allowedFileFilter == null)
								flist.add(jFile);
							else {
								if (this.allowedFileFilter.accept(jFile)) {
									flist.add(jFile);
								}
							}
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					break;
				}

			}

			// Distribute
			// -------------
			for (DropFileTargetListener list : this.getListeners()) {
				System.out.println("Giving to listener");
				// Drop All list
				list.dropped(flist);
				// Drop one by one
				for (File f : flist) {
					if (f.exists() && f.isDirectory())
						list.droppedDirectory(f);
					else if (f.exists()) {
						list.droppedFile(f);
					}
				}

			}

		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @return the allowedFileFilter
	 */
	public FileFilter getAllowedFileFilter() {
		return allowedFileFilter;
	}

	/**
	 * @param allowedFileFilter
	 *            the allowedFileFilter to set
	 */
	public void setAllowedFileFilter(FileFilter allowedFileFilter) {
		this.allowedFileFilter = allowedFileFilter;
	}

}
