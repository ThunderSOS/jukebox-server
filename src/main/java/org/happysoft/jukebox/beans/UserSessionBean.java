package org.happysoft.jukebox.beans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIComponent;
import jakarta.inject.Named;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.concurrent.Future;
import org.happysoft.jukebox.beans.service.AlbumService;
import org.happysoft.jukebox.beans.service.ArtistService;
import org.happysoft.jukebox.beans.service.TrackService;
import org.happysoft.jukebox.beans.service.UserLoadService;
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

  private long numTracksBeforeLoad = 0;
  private long numAlbumsBeforeLoad = 0;
  private long numArtistsBeforeLoad = 0;

  private long numLoaded = 0;
  private long numRemoved = 0;
  private long numNew = 0;
  
  private Future<String> loadResult;
  
  private UIComponent hiddenButton;

  @EJB
  private UserService userService;

  @EJB
  private TrackService trackService;

  @EJB
  private AlbumService albumService;

  @EJB
  private ArtistService artistService;

  @EJB
  private UserLoadService userLoadService;

  public void login() {
    JBUser user = userService.findByUsername("chris");
    directory = user.getSharedFolder();
    ownerId = user.getUserId();
    loggedIn = true;
  }

  public void logout() {
    loggedIn = false;
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

  public boolean isLoadInProgress() {
    boolean loading = loadResult == null ? false : !loadResult.isDone();
    if(loadResult != null && loadResult.isDone()) {
      System.out.println("Load result cleared");
      loadResult = null;
    }
    System.out.println("Loading in progress " + loading);
    return loading;
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
  
  public UIComponent getHiddenButton() {
    return hiddenButton;
  }

  public void setHiddenButton(UIComponent hiddenButton) {
    this.hiddenButton = hiddenButton;
  }

  public void startLoad() {
    try {
      loadResult = userLoadService.startLoad();
      
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
    }
  }

}
