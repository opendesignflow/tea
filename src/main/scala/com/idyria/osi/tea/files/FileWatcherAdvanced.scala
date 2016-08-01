package com.idyria.osi.tea.files

import com.idyria.osi.tea.thread.ThreadLanguage
import com.idyria.osi.tea.logging.TLogSource
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.StandardWatchEventKinds
import java.nio.file.StandardCopyOption
import java.nio.file.WatchKey
import java.nio.file.WatchEvent
import java.nio.file.Files
import java.nio.file.Path
import scala.collection.JavaConversions._
import java.lang.ref.WeakReference
import com.idyria.osi.tea.os.OSDetector

/**
 * @author zm4632
 */
class FileWatcherAdvanced extends ThreadLanguage with TLogSource {

  var baseDirectories = Map[WatchKey, File]()
  var changeListeners = Map[String, Map[WeakReference[Any], List[File => Any]]]()
  var directoryChangeListeners = Map[String, Map[WeakReference[Any], List[File => Any]]]()

  def start = {
    logFine[FileWatcherAdvanced](s"////////////// Startin watcher")
    watcherThread.start()
  }

  def stop = {

  }

  def onDirectoryChange(sourceListener: Any, f: File)(cl: File => Any) = {
    f match {
      case f if (!f.isDirectory()) =>
        new RuntimeException("Cannot Watch Directory Change on File")
      case d =>

        // to path
        var directoryPath = FileSystems.getDefault().getPath(d.getAbsolutePath)

        // Register if necessary
        this.baseDirectories.find { case (key, file) => file.getCanonicalFile.getAbsolutePath == d.getCanonicalFile.getAbsolutePath } match {
          case Some(entry) =>
          case None =>
            logFine[FileWatcherAdvanced](s"///////////// Registering Folder " + directoryPath)
            var watchKey = directoryPath.register(watcher, StandardWatchEventKinds.ENTRY_CREATE)
            this.baseDirectories = this.baseDirectories + (watchKey -> d.getCanonicalFile)
        }

        // Save listener
        //-----------------

        //-- Get Listerners map 
        var listenersMap = directoryChangeListeners.get(f.getAbsolutePath) match {
          case Some(listeners) => listeners
          case None => Map[WeakReference[Any], List[File => Any]]()
        }

        //-- Add Listeners list for a specific listener
        var sourceListenerPair = listenersMap.find { case (ref, list) => ref.get != null && ref.get == sourceListener } match {
          // A map exists for the source 
          case Some(m) => m
          case None => (new WeakReference(sourceListener), List[File => Any]())
        }

        //-- Add closure to  listeners list  in source pair
        sourceListenerPair = (sourceListenerPair._1, sourceListenerPair._2 :+ cl)

        //-- Update source listener Pair in main map 
        listenersMap = listenersMap + sourceListenerPair

        //-- Update Map in main listeners
        this.directoryChangeListeners = this.directoryChangeListeners.updated(f.getAbsolutePath, listenersMap)
    }
  }

  def onFileChange(sourceListener: Any, f: File)(cl: File => Any) = {

    f match {
      case f if (f.isDirectory()) => new RuntimeException("Cannot Watch File Change on Directory")
      case f =>

        // to path
        var sourcePath = FileSystems.getDefault().getPath(f.getCanonicalFile.getCanonicalPath)
        var directoryPath = FileSystems.getDefault().getPath(f.getParentFile.getCanonicalFile.getCanonicalPath)

        // Register if necessary
        this.baseDirectories.find { case (key, file) => file.getCanonicalPath == f.getParentFile.getCanonicalFile.getCanonicalPath } match {
          case Some(entry) =>
          case None =>
            logFine[FileWatcherAdvanced](s"///////////// Registering Folder " + directoryPath)
            var watchKey = directoryPath.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY)
            this.baseDirectories = this.baseDirectories + (watchKey -> f.getParentFile.getCanonicalFile)
        }

        // Save listener
        //-----------------
         //-- Make File path low case on windows
        /*var fileAbsolutePath = OSDetector.isWindows match {
          case true => f.getAbsolutePath.toLowerCase()
          case false => f.getAbsolutePath
        }*/
        
        //-- Get Listerners map 
        var listenersMap = changeListeners.get(f.getAbsolutePath) match {
          case Some(listeners) => listeners
          case None => Map[WeakReference[Any], List[File => Any]]()
        }

        //-- Add Listeners list for a specific listener
        var sourceListenerPair = listenersMap.find { case (ref, list) => ref.get != null && ref.get == sourceListener } match {
          // A map exists for the source 
          case Some(m) => m
          case None => (new WeakReference(sourceListener), List[File => Any]())
        }

        //-- Add closure to  listeners list  in source pair
        sourceListenerPair = (sourceListenerPair._1, sourceListenerPair._2 :+ cl)

        //-- Update source listener Pair in main map 
        listenersMap = listenersMap + sourceListenerPair

        //-- Update Map in main listeners
       
        this.changeListeners = this.changeListeners.updated(sourcePath.toFile().getCanonicalPath, listenersMap)

        /* // Save listener
        var listeners = changeListeners.get(f.getAbsolutePath) match {
          case Some(listeners) => listeners
          case None => List()
        }
        listeners = listeners :+ { () => cl }
        this.changeListeners = this.changeListeners.updated(f.getAbsolutePath, listeners)*/

        logFine[FileWatcherAdvanced](s"///////////// Recorded event for " +sourcePath.toFile().getCanonicalPath)
      // 
    }

  }

  // Watcher Thread
  //--------------------
  var watcher = FileSystems.getDefault().newWatchService();
  var watcherThread = createThread {

    var stop = false
    while (!stop) {

      // Get Key
  
      var key = watcher.take()

      try {
        /* key.pollEvents().foreach {
          e => e.
        }*/

        //println(s"///////////// Got Key ")
        var events = key.pollEvents()
        key.reset()
        //println(s"Key got events: "+events.size)
        events.filter { ev => ev.kind() != StandardWatchEventKinds.OVERFLOW } foreach {

          // Directory Add
          //----------------------
          case be: WatchEvent[_] if (be.kind() == StandardWatchEventKinds.ENTRY_CREATE && be.count()<=1) =>
            var e = be.asInstanceOf[WatchEvent[Path]]

            // Get Path of directory
            var directoryFile = this.baseDirectories.get(key).get

            // Get Path of file
            var fileAbsoluteFile = directoryFile.toPath().resolve(e.context()).toAbsolutePath().toFile()
            var filePath = fileAbsoluteFile.getAbsolutePath

            // Get and run listeners
            directoryChangeListeners.get(directoryFile.getAbsolutePath) match {
              case Some(listenersMap) =>

                // Go through all sourceListeners and their closures
                // Clear Listeners for a source listener if the reference is null
                listenersMap.foreach {
                  case (ref, listeners) if (ref.get == null) =>
                    var newListernersMap = listenersMap - ref
                   this.directoryChangeListeners = this.directoryChangeListeners.updated(directoryFile.getAbsolutePath, listenersMap)
                  
                  case (ref, listeners) =>
                    listeners.foreach {
                      l =>
                        // println(s"Running event")
                        l(fileAbsoluteFile)
                    }
                }

              case None =>
            }
          /*directoryChangeListeners.get(directoryFile) match {
              case Some(listeners) =>
                listeners.foreach {
                  l =>
                    // println(s"Running event")
                    l(filePath)
                }
              case None =>
            }*/

          // File Modify
          //----------------
          case be: WatchEvent[_] if (be.kind() == StandardWatchEventKinds.ENTRY_MODIFY && be.count()<=1) =>

            var e = be.asInstanceOf[WatchEvent[Path]]
          

            // Get Path of directory
            var directoryFile = this.baseDirectories.get(key).get

            // Get Path of file
            var filePath = directoryFile.toPath().resolve(e.context()).toAbsolutePath().toFile()
            var fileAbsoluteFile = filePath.getCanonicalFile
            //var filePath = directoryFile.toPath().resolve(e.context()).toAbsolutePath().toFile().getAbsolutePath

            //var filePath = 
            logFine[FileWatcherAdvanced](s"///////////// Got Modify event for "+filePath+ s" ${fileAbsoluteFile.lastModified()}// "+e.context().toAbsolutePath().toFile().getAbsolutePath+"//"+directoryFile.toPath())
            logFine[FileWatcherAdvanced](s"///////////// Count: "+be.count())

            // Get and run listeners
            
            changeListeners.get(fileAbsoluteFile.getAbsolutePath) match {
              case Some(listenersMap) =>
                
                logFine[FileWatcherAdvanced](s"///////////// Listeners Map Present")
                listenersMap.foreach {
                  case (ref, listeners) if (ref.get == null) =>
                    
                    logFine[FileWatcherAdvanced](s"///////////// Reference for set of listeners is void")
                    var newListernersMap = listenersMap - ref
                   this.directoryChangeListeners = this.directoryChangeListeners.updated(directoryFile.getAbsolutePath, listenersMap)
                  
                  case (ref, listeners) =>
                    
                    logFine[FileWatcherAdvanced](s"///////////// Delivering for reference listener: "+ref.get)
                    listeners.foreach {
                      l =>
                        // println(s"Running event")
                        l(fileAbsoluteFile)
                    }
                }
                /*listeners.foreach {
                  l =>
                    // println(s"Running event")
                    l()
                }*/
              case None =>
            }

          case e =>

           // logWarn(s"///////////// Got Unsupported Event ")
        }

      } finally {
        //-- invalid key
        //println(s"Reset Key -> "+key.isValid())
        key.reset()
      }

    }

  }
  watcherThread.setDaemon(true)

}