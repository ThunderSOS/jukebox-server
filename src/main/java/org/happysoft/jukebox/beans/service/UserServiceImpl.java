package org.happysoft.jukebox.beans.service;

/**
 *
 * @author chrisf
 */
import jakarta.ejb.Stateless;
import jakarta.persistence.PersistenceContext;
import org.happysoft.jukebox.beans.service.entity.JBArtist;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.happysoft.jukebox.beans.service.entity.JBUser;

@Stateless
public class UserServiceImpl implements UserService {

  @PersistenceContext(unitName = "JukeboxPU")
  private EntityManager em;

  @Override
  @RequestScoped
  public JBUser findOrCreateUser(String username, String sharedFolder) {
    JBUser user;  
    try {
      user = findByUsername(username);
    
    } catch (NoResultException nre) {      
      user = new JBUser();
      user.setUsername(username);
      user.setSharedFolder(sharedFolder);
    }
    em.persist(user);
    return user;
  }
  
  @Override
  @RequestScoped
  public JBUser findByUserId(long userId) {
    return em.createNamedQuery("artist.findById", JBUser.class).setParameter("id", userId).getSingleResult();
  }

  @Override
  @RequestScoped
  public JBUser findByUsername(String username) {
    return em.createNamedQuery("user.findByUsername", JBUser.class)
            .setParameter("username", username)
            .getSingleResult();
  }
  
}
