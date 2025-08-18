package org.happysoft.jukebox.beans.service;

import java.io.FileNotFoundException;
import java.util.concurrent.Future;

/**
 *
 * @author chrisf
 */
public interface UserLoadService {

  public Future<String> startLoad() throws FileNotFoundException;

  public boolean isLoadInProgress();

}
