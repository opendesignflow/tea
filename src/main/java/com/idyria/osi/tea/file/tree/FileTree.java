/**
 * 
 */
package com.idyria.osi.tea.file.tree;

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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import com.idyria.osi.tea.file.DirectoryWalker;
import com.idyria.osi.tea.file.DirectoryWalkerListener;

/**
 * @author Richnou
 *
 */
public class FileTree extends JTree {

	private DefaultTreeModel model = null ;
	
	/**
	 * 
	 */
	public FileTree(File dir) {
		super();
		model = new DefaultTreeModel(new FileNode(dir));
		this.setModel(model);
	}
	
	public static class FileNode extends DefaultMutableTreeNode {
		
		private File file = null;
		
		public FileNode(File file) {
			super(file.getName());
			this.file = file;
		}

		/**
		 * @return the file
		 */
		public File getFile() {
			return file;
		}
		
		
	}

	/**
	 * Creates the Tree
	 * @throws IOException 
	 */
	public void createTree() throws IOException {
		
		
		// Create the Tree
		DirectoryWalker walker = new DirectoryWalker();
		walker.run(((FileNode)this.getModel().getRoot()).getFile(), new DirectoryWalkerListener<Boolean,Boolean,File>() {

			private HashMap<String,FileNode> pathToNodes = new HashMap<String,FileNode>();
			
			private String rootPath = ((FileNode)getModel().getRoot()).getFile().getCanonicalPath();
			
			@Override
			public Boolean fileFound(File file) {
			
				this.addToTree(file);
				
				return true;
			}

			@Override
			public Boolean directoryFound(File file) {

				// Add To tree
				FileNode node = this.addToTree(file);
				
				return true;
			}
			
			private FileNode addToTree(File file) {
				
				// Get File path
				try {
					String path = file.getCanonicalPath().replace(rootPath, "");
					System.out.println("Path: "+path);
					
					FileNode node = new FileNode(file);
					// Are we under Root ?
					if (path.lastIndexOf(File.separator)==0) {
						System.out.println("\tROOT");
						model.insertNodeInto(node, (MutableTreeNode) model.getRoot(), model.getChildCount( model.getRoot()));
						
					} else {
						
						// Find parent in map
						FileNode parent = pathToNodes.get(file.getParentFile().getCanonicalPath().replace(rootPath, ""));
						if (parent==null)
							System.err.println("Parent for "+path+" not found");
						model.insertNodeInto(node, parent, model.getChildCount(parent));
						
					}
					
					if (file.isDirectory()) {
						// Record into map
						this.pathToNodes.put(path, node);
					}
					
					return node;
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
		});
		
		// Reload display
		this.setRootVisible(false);
		this.setRootVisible(true);
		
	}
	

}
