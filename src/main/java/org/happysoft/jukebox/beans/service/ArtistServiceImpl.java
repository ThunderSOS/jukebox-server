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

@Stateless
public class ArtistServiceImpl implements ArtistService {

  @PersistenceContext(unitName = "JukeboxPU")
  private EntityManager em;

  @Override
  @RequestScoped
  public JBArtist findOrCreateArtist(long ownerId, String artistName) {
    JBArtist artist;  
    try {
      artist = findByOwnerAndArtistName(ownerId, artistName);
    
    } catch (NoResultException nre) {      
      artist = new JBArtist();
      artist.setArtistName(artistName);
      artist.setOwnerId(ownerId);      
    }
    artist.setFoundOnLastLoad(true);
    em.persist(artist);
    return artist;
  }
  
  @Override
  public JBArtist findById(long trackId) {
    return em.createNamedQuery("artist.findById", JBArtist.class).setParameter("id", trackId).getSingleResult();
  }

  @Override
  @RequestScoped
  public JBArtist remove(long id) {
    JBArtist artist = em.createNamedQuery("artist.findById", JBArtist.class)
            .setParameter("id", id).getSingleResult();
    em.remove(artist);
    return artist;
  }
  
  @Override
  @RequestScoped
  public void prepareForReload(long ownerId) {
    em.createQuery("update JBArtist j set j.foundOnLastLoad = false where j.ownerId = :id")
            .setParameter("id", ownerId)
            .executeUpdate();
  }
  
  @Override
  @RequestScoped
  public void tidyUpAfterReload(long ownerId) {
    em.createQuery("delete from JBArtist j where j.foundOnLastLoad = false and j.ownerId = :id")
            .setParameter("id", ownerId)
            .executeUpdate();
  }

  @Override
  @RequestScoped
  public JBArtist findByOwnerAndArtistName(long ownerId, String artistName) {
    return em.createNamedQuery("artist.findByOwnerAndArtistName", JBArtist.class)
            .setParameter("ownerId", ownerId)
            .setParameter("artistName", artistName)
            .getSingleResult();
  }
  
}
