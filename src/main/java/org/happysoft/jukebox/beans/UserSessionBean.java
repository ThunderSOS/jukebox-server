
package org.happysoft.jukebox.beans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import org.happysoft.jukebox.beans.service.AlbumService;
import org.happysoft.jukebox.beans.service.ArtistService;
import org.happysoft.jukebox.beans.service.TrackService;
import org.happysoft.jukebox.beans.service.UserService;
import org.happysoft.jukebox.beans.service.entity.JBUser;

/**
 *
 * @author chrisf
 */
@SessionScoped
@Named(value = "sessionBean")
public class UserSessionBean implements Serializable {
  
  private Long ownerId;
  private String username;
  private String directory;
  private boolean loggedIn = false;
  private boolean loaded = false;
  
  private long numTracksBeforeLoad = 0;
  private long numAlbumsBeforeLoad = 0;
  private long numArtistsBeforeLoad = 0;
  
  private long numLoaded = 0;
  private long numRemoved = 0;
  private long numNew = 0;
  
  @EJB
  private UserService userService;  
  
  @EJB
  private TrackService trackService;
  
  @EJB
  private AlbumService albumService;
    
  @EJB
  private ArtistService artistService;
  
  public void login() {
    JBUser user = userService.findByUsername("chris");
    directory = user.getSharedFolder();
    ownerId = user.getUserId();
    loggedIn = true;    
    loaded = false;
  }
  
  public void logout() {
    loggedIn = false;
    loaded = false;
    directory = null;
    ownerId = 0L;
  }
  
  public Long getOwnerId() {
    return ownerId;
  }
  
  public String getUsername() {
    return username;
  }
  
  public String getDirectory() {
    return directory;            
  }
  
  public boolean isLoggedIn() {
    return loggedIn;
  }
  
  public boolean loadingStatsAvailable() {
    return loaded;
  }

  public long getNumLoaded() {
    return numLoaded;
  }

  public long getNumRemoved() {
    return numRemoved;
  }

  public long getNumNew() {
    return numNew;
  }
  
  public void loadStats() {
    loaded = true;
  }
  
  public void prepareForLoad() {
    numTracksBeforeLoad = trackService.countByOwner(ownerId);
    numAlbumsBeforeLoad = albumService.countByOwner(ownerId);
    numArtistsBeforeLoad = artistService.countByOwner(ownerId);
  }
  
}
