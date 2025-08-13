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
import org.happysoft.jukebox.model.RemoteDirectory;

@Stateless
public class AlbumServiceImpl implements AlbumService {

  @PersistenceContext(unitName = "JukeboxPU")
  private EntityManager em;

  @Override
  @RequestScoped
  public JBAlbum findOrCreateAlbum(RemoteDirectory remote, long ownerId, long artistId, String albumName) {
    JBAlbum album;
    try {
      album = findByArtistIdAndAlbumName(artistId, albumName);

    } catch (NoResultException nre) {
      album = new JBAlbum(remote, ownerId);
      album.setAlbumName(albumName);
      album.setArtistId(artistId);
      album.setOwnerId(ownerId);      
    }
    album.setFoundOnLastLoad(true);
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
  public void tidyUpAfterReload(long ownerId) {
    em.createQuery("delete from JBAlbum j where j.foundOnLastLoad = false and j.ownerId = :id")
            .setParameter("id", ownerId)
            .executeUpdate();
  }
  
  @Override
  @RequestScoped
  public JBAlbum findByArtistIdAndAlbumName(long artistId, String albumName) {
    return em.createNamedQuery("album.findByArtistAndAlbumName", JBAlbum.class)
            //findByArtistAndAlbumName
            //.setParameter("pathId", networkPathId)
            .setParameter("artistId", artistId)
            .setParameter("albumName", albumName)
            .getSingleResult();
  }

}
