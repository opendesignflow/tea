/**
 * 
 */
package com.idyria.osi.tea.eclipse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import com.idyria.osi.tea.io.TeaIOUtils;
import com.idyria.osi.tea.logging.TeaLogging;

/**
 * @author rtek
 *
 */
public class EclipseWorkspaceTools {

	/**
	 * 
	 */
	public EclipseWorkspaceTools() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @author rtek
	 *
	 */
	public static abstract class ProjectFoundListener {
		
		public abstract void projectFound(File location);
		
	}

	/**
	 * Detects projects in an eclipse workspace and returns their locations.
	 * The workspace base location is defined through the ECLIPSE_WORKSPACE environment variable
	 * @param listener a listener invoqued on each found project.
	 * @return
	 */
	public static File[] detectWorkspaceProjects(ProjectFoundListener listener) {
		
		// Result vector
		Vector<File> projectFolders = new Vector<File>();
		
		if (System.getenv("ECLIPSE_WORKSPACE")!=null) {
			TeaLogging.teaLogInfo("Eclipse workspace given");
			// Get
			File projectsLocations = new File(System.getenv("ECLIPSE_WORKSPACE")+File.separator+".metadata"+File.separator+".plugins"+File.separator+"org.eclipse.core.resources"+File.separator+".projects");
			if (!projectsLocations.isDirectory()) {
				TeaLogging.teaLogWarning("ECLIPSE_WORKSPACE variable defined but don't refer to a 3.4+ workspace");
			} else {
				// Make list of the projects
				for (File projectRessource: projectsLocations.listFiles()) {
					
					// Determine location
					File location = null;
					File locationFile = new File(projectRessource.getAbsolutePath()+File.separator+".location");
					if (!locationFile.isFile()) {
						// Project is in workspace root
						location = new File(System.getenv("ECLIPSE_WORKSPACE")+File.separator+projectRessource.getName());
						// Check it
						if (!location.isDirectory()) {
							location=null;
						}
					} else {
						// Project is deported
						// Read an URI from file
						try {
							// Suck file
							byte[] filecontent = TeaIOUtils.swallow(new FileInputStream(locationFile));
							// Copy between 24th byte and length (remove 23 bytes)
							byte[] locationBytes = new byte[filecontent.length-23];
							System.arraycopy(filecontent, 23, locationBytes, 0, locationBytes.length);
							
							// Read content to first NULL byte
							ByteArrayOutputStream bout = new ByteArrayOutputStream();
							for (byte b:locationBytes) {
								if (b == '\0') {
									break;
								}
								bout.write(b);
							}
							
							TeaLogging.teaLogInfo("Project location for "+projectRessource.getName()+" is: "+new String(bout.toByteArray()));
						
							// Create file
							URL locationURL = new URL(new String(bout.toByteArray()));
							//location = new File(new String(bout.toByteArray()));
							location = new File(locationURL.getFile());
							// Check it
							if (!location.isDirectory()) {
								location=null;
							}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
//							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
//							e.printStackTrace();
						} 
						
						
					} // End determine location
					
					// Did we find a location
					if (location==null) {
						TeaLogging.teaLogWarning("Project "+projectRessource.getName()+" location was not found");
					} else {
						// Record project location
						projectFolders.add(location);
						// Call up action
						if (listener!=null)
							listener.projectFound(location);
					}
					
				} // End scan ws projects
			} // End If workspace location valid
		} // End if workspace
		
		
		return projectFolders.toArray(new File[projectFolders.size()]);
		
	}
 	
	
}
