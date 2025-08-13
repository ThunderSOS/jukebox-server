
package org.happysoft.jukebox.beans.service;

import org.happysoft.jukebox.beans.service.entity.JBUser;

/**
 *
 * @author chrisf
 */
public interface UserService {
  
  public JBUser findOrCreateUser(String username, String sharedFolder);
  
  public JBUser findByUserId(long userId);
  
  public JBUser findByUsername(String username);
  
}