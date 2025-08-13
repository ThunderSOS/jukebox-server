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
import org.happysoft.jukebox.beans.service.entity.JBTrack;
import org.happysoft.jukebox.model.RemoteDirectory;

@Stateless
public class TrackServiceImpl implements TrackService {

  @PersistenceContext(unitName = "JukeboxPU")
  private EntityManager em;

  @Override
  @RequestScoped
  public JBTrack findOrCreateTrack(RemoteDirectory remote, long ownerId, long artistId, long albumId, String trackName) {
    JBTrack track;
    try {
      track = findByOwnerArtistAlbumAndName(ownerId, artistId, albumId, trackName);
    } catch (NoResultException nre) {
      track = new JBTrack(remote, ownerId, artistId, albumId, trackName);
      track.setOwnerId(ownerId);
    }
    track.setFoundOnLastLoad(true);
    em.persist(track);
    return track;
  }

  @Override
  public JBTrack findById(long trackId) {
    JBTrack t = null;
    try {
      t = em.createNamedQuery("track.findById", JBTrack.class).setParameter("id", trackId).getSingleResult();
    } catch (NoResultException nre) {
      System.out.println("Track not found " + trackId);
    }
    return t;
  }

  @Override
  @RequestScoped
  public void prepareForReload(long ownerId) {
    em.createQuery("update JBTrack j set j.foundOnLastLoad = false where j.ownerId = :id")
            .setParameter("id", ownerId)
            .executeUpdate();
  }

  @Override
  @RequestScoped
  public void tidyUpAfterReload(long ownerId) {
    em.createQuery("delete from JBTrack j where j.foundOnLastLoad = false and j.ownerId = :id")
            .setParameter("id", ownerId)
            .executeUpdate();
  }

  @Override
  @RequestScoped
  public JBTrack findByOwnerArtistAlbumAndName(long ownerId, long artistId, long albumId, String trackName) {
    return em.createNamedQuery("track.findByOwnerArtistAlbumAndName", JBTrack.class)
            .setParameter("ownerId", ownerId)
            .setParameter("albumId", albumId)
            .setParameter("artistId", artistId)
            .setParameter("trackName", trackName)
            .getSingleResult();
  }

}
