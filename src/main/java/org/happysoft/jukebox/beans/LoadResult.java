package org.happysoft.jukebox.beans;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author chrisf
 */
public class LoadResult {

//  private long numTracksBeforeLoad = 0;
//  private long numAlbumsBeforeLoad = 0;
//  private long numArtistsBeforeLoad = 0;
  private Map<LoadType, Integer> numLoaded = new HashMap<>();
  private Map<LoadType, Integer> numRemoved = new HashMap<>();
  private Map<LoadType, Integer> numNew = new HashMap<>();
  
  
  private boolean loadFailed = false;

  public long getNumLoaded(LoadType type) {
    return numLoaded.getOrDefault(type, 0);
  }

  public long getNumRemoved(LoadType type) {
    return numRemoved.getOrDefault(type, 0);
  }

  public long getNumNew(LoadType type) {
    return numNew.getOrDefault(type, 0);
  }

  public void incNumNew(LoadType type) {
    numNew.merge(type, 1, Integer::sum);
  }  

  public void incNumRemoved(LoadType type) {
    numRemoved.merge(type, 1, Integer::sum);
  } 
  
  public void incNumLoaded(LoadType type) {
    this.numLoaded.getOrDefault(type, 0);
  }

  public void addNumRemoved(LoadType type, int removed) {
    numRemoved.merge(type, removed, Integer::sum);
  }

  public void addNumNew(LoadType type, int newStuff) {
    numNew.merge(type, newStuff, Integer::sum);
  }

  public void addNumLoaded(LoadType type, int newLoaded) {
    numLoaded.merge(type, newLoaded, Integer::sum);
  }
  
  public void setLoadFailed() {
    loadFailed = true;
  }
  
  public boolean loadFailed() {
    return loadFailed;
  }
}
