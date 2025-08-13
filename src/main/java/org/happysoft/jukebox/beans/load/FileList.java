/*
 Java Jukebox
 Copyright (C) 2004, Chris Francis.

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package org.happysoft.jukebox.beans.load;

import java.io.*;
import java.util.*;
import org.happysoft.jukebox.model.RemoteDirectory;

public class FileList {

  private final JBFilenameFilter fileFilter = new JBFilenameFilter();
  private DirectoryFilter directoryFilter = null;

  private File[] looseFiles;
  private File[] directories;

  public FileList(RemoteDirectory remote, String subDirectory, List<String> exclude) throws FileNotFoundException {  
    
    directoryFilter = new DirectoryFilter(exclude);    
    File f;
    
    if(subDirectory != null)  {
      f = new File(remote.toString(), subDirectory);
    } else {
      f = new File(remote.toString());
    }
    
    if(!f.exists()) {
      System.out.println("Can't find: " + f.getAbsolutePath());
      throw new FileNotFoundException(remote.toString());
    }
    looseFiles = f.listFiles(fileFilter);
    directories = f.listFiles(directoryFilter);
  }

  public File[] getLooseFiles() {
    return looseFiles;
  }

  public File[] getDirectories() {
    return directories;
  }

}




