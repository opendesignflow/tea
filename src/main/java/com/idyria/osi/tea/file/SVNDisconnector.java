/**
 * 
 */
package com.idyria.osi.tea.file;

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

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.io.File;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 * @author Richnou
 *
 */
public class SVNDisconnector extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private JButton selectButton = null;
	
	private File selectedFile = null;

	private JButton disconnectButton = null;

	/**
	 * @throws HeadlessException
	 */
	public SVNDisconnector() throws HeadlessException {
		// TODO Auto-generated constructor stub
		super();
		initialize();
	}

	/**
	 * @param gc
	 */
	public SVNDisconnector(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
		initialize();
	}

	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public SVNDisconnector(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
		initialize();
	}

	/**
	 * @param title
	 * @param gc
	 */
	public SVNDisconnector(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
		initialize();
	}

	/**
	 * This method initializes selectButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSelectButton() {
		if (selectButton == null) {
			selectButton = new JButton();
			selectButton.setText("Select Directory");
			selectButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					JFileChooser fchoose = new JFileChooser();
					fchoose.setDialogType(JFileChooser.OPEN_DIALOG);
					fchoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = fchoose.showOpenDialog(SVNDisconnector.this);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						selectedFile = fchoose.getSelectedFile();
					       disconnectButton.setEnabled(true);
					}
					
					
				}
			});
		}
		return selectButton;
	}

	/**
	 * This method initializes disconnectButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDisconnectButton() {
		if (disconnectButton == null) {
			disconnectButton = new JButton();
			disconnectButton.setText("Disconnect");
			disconnectButton.setEnabled(false);
			disconnectButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (selectedFile == null)
						return;
					
					// Execute algorithm
					//---------------------------
					
					// List of directories to proceed
					LinkedList<File> dirs = new LinkedList<File>();
					dirs.add(selectedFile);
					
					// Proceed
					while (!dirs.isEmpty()) {
						
						// get the file
						File dir = dirs.poll();
						
						// Foreach all subfiles
						for (File file : dir.listFiles()) {
							
							// If it is the .svn dir -> delete
							if (file.isDirectory() && file.getName().equals(".svn"))
								DirectoryUtilities.deleteDirectory(file);
							// If it is a directory -> add to list
							else if (file.isDirectory())
								dirs.add(file);
						}
					} // END MAIN LOOP
					
					
				}
			});
		}
		return disconnectButton;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SVNDisconnector thisClass = new SVNDisconnector();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("SVN Disconnector");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridwidth = 3;
			gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints21.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints1.gridy = 0;
			jLabel1 = new JLabel();
			jLabel1.setText("");
			jLabel1.setPreferredSize(new Dimension(250, 20));
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Directory:");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(jLabel, gridBagConstraints);
			jContentPane.add(jLabel1, gridBagConstraints1);
			jContentPane.add(getSelectButton(), gridBagConstraints2);
			jContentPane.add(getDisconnectButton(), gridBagConstraints21);
		}
		return jContentPane;
	}

}
