
package org.happysoft.jukebox.beans.service;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.happysoft.jukebox.beans.entity.JBAlbum;
import org.happysoft.jukebox.beans.entity.JBArtist;
import org.happysoft.jukebox.beans.entity.JBTrack;

/**
 *
 * @author chrisf
 */
@Stateless
public class SearchServiceImpl implements SearchService {

  @PersistenceContext(unitName = "JukeboxPU")
  private EntityManager em;

  /* Artist search */
  @Override
  @RequestScoped
  public List<JBArtist> findArtistsBySearchString(String search) {
    return em.createNamedQuery("artist.findAllMatching").setParameter("search", search).getResultList();
  }

  @Override
  @RequestScoped
  public List<JBArtist> findArtistsStartingWith(String search) {
    return em.createNamedQuery("artist.findAllStartingWith").setParameter("search", search).getResultList();
  }

  @Override
  @RequestScoped
  public JBArtist findArtistById(long id) {
    System.out.println("Find artist " + id);
    return em.createNamedQuery("artist.findById", JBArtist.class).setParameter("id", id).getSingleResult();
  }

  /* Album search */
  @Override
  @RequestScoped
  public List<JBAlbum> findAlbumsByArtistId(long artistId) {
    System.out.println("Finding albums for artist " + artistId);
    return em.createNamedQuery("album.findAllByArtistId").setParameter("artistId", artistId).getResultList();
  }
  
  @Override
  @RequestScoped
  public List<JBTrack> findTracksForAlbum(long albumId) {
    System.out.println("Finding tracks for album " + albumId);
    return em.createNamedQuery("track.findTracksForAlbum").setParameter("albumId", albumId).getResultList();
  }
  
  @Override
  @RequestScoped
  public List<JBTrack> findLooseTracksForArtist(long artistId) {
    System.out.println("Finding tracks for artist " + artistId);
    return em.createNamedQuery("track.findLooseTracksForArtist").setParameter("artistId", artistId).getResultList();
  }
  
  @Override
  @RequestScoped
  public List<JBTrack> findLooseTracks() {
    System.out.println("Finding loose tracks");
    return em.createNamedQuery("track.findLooseTracks").getResultList();
  }

}
