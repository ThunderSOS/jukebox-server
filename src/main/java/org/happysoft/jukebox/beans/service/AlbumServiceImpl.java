package org.happysoft.jukebox.beans.service;

/**
 *
 * @author chrisf
 */
import jakarta.ejb.Stateless;
import jakarta.persistence.PersistenceContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.happysoft.jukebox.beans.service.entity.JBAlbum;

@Stateless
public class AlbumServiceImpl implements AlbumService {

  @PersistenceContext(unitName = "JukeboxPU")
  private EntityManager em;

  @Override
  @RequestScoped
  public JBAlbum findOrCreateAlbum(long ownerId, long artistId, String albumName) {
    JBAlbum album;
    try {
      album = findByArtistIdAndAlbumName(artistId, albumName);      
      album.setFoundOnLastLoad(true);

    } catch (NoResultException nre) {
      album = new JBAlbum(ownerId);
      album.setAlbumName(albumName);
      album.setArtistId(artistId);
      album.setOwnerId(ownerId);    
    }
    em.persist(album);
    return album;
  }
  
  
  public JBAlbum findById(long id) {
    return em.createNamedQuery("album.findById", JBAlbum.class).setParameter("id", id).getSingleResult();
  }

  @Override
  @RequestScoped
  public JBAlbum removeAlbum(long artistId, String albumName) {
    JBAlbum album = findByArtistIdAndAlbumName(artistId, albumName);
    em.remove(album);
    return album;
  }

  @Override
  @RequestScoped
  public void prepareForReload(long ownerId) {
    em.createQuery("update JBAlbum j set j.foundOnLastLoad = false where j.ownerId = :id")
            .setParameter("id", ownerId)
            .executeUpdate();
  }
  
  @Override
  @RequestScoped
  public int tidyUpAfterReload(long ownerId) {
    return em.createQuery("delete from JBAlbum j where j.foundOnLastLoad = false and j.ownerId = :id")
            .setParameter("id", ownerId)
            .executeUpdate();
  }
  
  @Override
  @RequestScoped
  public JBAlbum findByArtistIdAndAlbumName(long artistId, String albumName) {
    return em.createNamedQuery("album.findByArtistAndAlbumName", JBAlbum.class)
            .setParameter("artistId", artistId)
            .setParameter("albumName", albumName)
            .getSingleResult();
  }
  
  @Override
  @RequestScoped
  public long countByOwner(long ownerId) {
    return em.createNamedQuery("album.countByOwner", Long.class)
            .setParameter("ownerId", ownerId)
            .getSingleResult();
  }  
  
  @Override
  @RequestScoped
  public long countNewByOwner(long ownerId) {
    return em.createNamedQuery("album.countNewByOwner", Long.class)
            .setParameter("ownerId", ownerId)
            .getSingleResult();
  }

}
