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
package org.happysoft.jukebox.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.happysoft.jukebox.beans.entity.JBFileType;

public class JBFilenameFilter implements FilenameFilter {
  
  private final List<String> fileTypes = new ArrayList<>();
  
  public JBFilenameFilter(List<JBFileType> types) {
    types.forEach(t -> fileTypes.add(t.getFileType()));
  }

  @Override
  public boolean accept(File dir, String name) {
    name = name.toLowerCase();
    boolean matched = false;
    for (String s : fileTypes) {
      if (name.endsWith(s)) {
        matched = true;
        break;
      }
    }
    return matched;
  }

}
