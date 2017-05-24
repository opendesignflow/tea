/**
 * 
 */
package com.idyria.osi.tea.datatransfer;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * @author rleys
 * 
 */
public class JPEGImageTransferable implements Transferable,ClipboardOwner {

	private byte[] imagebytes = null;

	private DataFlavor pngFlavor = null;

	/**
	 * 
	 */
	public JPEGImageTransferable(byte[] imagebytes) {
		this.imagebytes = imagebytes;
		try {
			this.pngFlavor = new DataFlavor("image/png");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		System.out.println("Requested for flavors ");
		
		return new DataFlavor[] { pngFlavor };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.
	 * datatransfer.DataFlavor)
	 */
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		System.out.println("Requested for flavor: "+flavor.getMimeType()+"->"+pngFlavor.equals(flavor));
		return pngFlavor.equals(flavor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer
	 * .DataFlavor)
	 */
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		System.out.println("Requested data: "+flavor.getMimeType());
		
		if (this.isDataFlavorSupported(flavor))
			return this.imagebytes;
		else
			throw new UnsupportedFlavorException(flavor);
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub
		
	}

}
