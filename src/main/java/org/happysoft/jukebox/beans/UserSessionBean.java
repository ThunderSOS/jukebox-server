package org.happysoft.jukebox.beans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIComponent;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.concurrent.Future;
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
  
  private Future<LoadResult> loadResult;
  
  private UIComponent hiddenButton;

  @EJB
  private UserService userService;

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
      loadResult = null;
    }
    return loading;
  }
  
  public UIComponent getHiddenButton() {
    return hiddenButton;
  }

  public void setHiddenButton(UIComponent hiddenButton) {
    this.hiddenButton = hiddenButton;
  }

  public void startLoad() {
    loadResult = userLoadService.startLoad();    
  }

}
