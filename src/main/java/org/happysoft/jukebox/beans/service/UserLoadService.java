package org.happysoft.jukebox.beans.service;

import java.util.concurrent.Future;
import org.happysoft.jukebox.beans.LoadResult;

/**
 *
 * @author chrisf
 */
public interface UserLoadService {

  public Future<LoadResult> startLoad();

  public boolean isLoadInProgress();

}
