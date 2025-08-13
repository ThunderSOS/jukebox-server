
package org.happysoft.jukebox.beans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
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
  
  @EJB
  private UserService userService;        
  
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
  
}
