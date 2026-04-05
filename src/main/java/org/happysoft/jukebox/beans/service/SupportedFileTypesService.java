package org.happysoft.jukebox.beans.service;

import java.util.List;
import org.happysoft.jukebox.beans.entity.JBFileType;

/**
 *
 * @author chrisf
 */
public interface SupportedFileTypesService {

  public List<JBFileType> getSupportedTypes();

}
