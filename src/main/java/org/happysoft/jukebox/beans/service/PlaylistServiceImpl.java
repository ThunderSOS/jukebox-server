package org.happysoft.jukebox.beans.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.happysoft.jukebox.beans.service.entity.JBAlbum;
import org.happysoft.jukebox.beans.service.entity.JBArtist;
import org.happysoft.jukebox.beans.service.entity.JBPlaylistRequest;
import org.happysoft.jukebox.beans.service.entity.JBTrack;
import org.happysoft.jukebox.model.RemoteDirectory;
import org.happysoft.jukebox.model.Request;
import org.happysoft.jukebox.model.RequestStatus;
import org.happysoft.jukebox.model.Track;

/**
 *
 * @author chrisf
 */
@Stateless
public class PlaylistServiceImpl implements PlaylistService {

  @PersistenceContext(unitName = "JukeboxPU")
  private EntityManager em;

  @EJB
  private ArtistService artistService;

  @EJB
  private AlbumService albumService;

  @EJB
  private TrackService trackService;

  @Override
  @RequestScoped
  public JBPlaylistRequest addToPlaylist(long trackId, long requestedBy) {
    JBPlaylistRequest request = new JBPlaylistRequest();
    request.setTrackId(trackId);
    request.setRequestedBy(requestedBy);
    request.setStatus(RequestStatus.QUEUED);
    em.persist(request);
    return request;
  }

  @Override
  public List<JBPlaylistRequest> getQueued() {
    return em.createNamedQuery("playlist.findAllQueued").getResultList();
  }

  @Override
  public List<JBPlaylistRequest> getPlayingNow() {
    return em.createNamedQuery("playlist.findPlaying", JBPlaylistRequest.class).getResultList();
  }

  @Override
  public void setRequestStatus(long requestId, RequestStatus status) {
    em.createQuery("update JBPlaylistRequest r set r.status = :status where r.id = :id")
            .setParameter("status", status)
            .setParameter("id", requestId)
            .executeUpdate();
  }
  
  @Override
  public void clearPlaying() {
    em.createQuery("update JBPlaylistRequest r set r.status = 'PLAYED' where r.status = 'PLAYING'")
            .executeUpdate();
  }
  
  @Override
  public void cancel(long requestId) {
    em.createQuery("delete from JBPlaylistRequest r where r.requestId = :id")
            .setParameter("id", requestId)
            .executeUpdate();
  }
  
  @Override
  public Request buildRequest(long requestId, long trackId) {
    JBTrack t = trackService.findById(trackId);
    Request r = null;

    //some id's can change after a reload
    if (t != null) {
      String albumName = "";
      String artistName = "";

      long alId = t.getAlbumId();
      long arId = t.getArtistId();

      if (alId != 0) {
        JBAlbum al = albumService.findById(t.getAlbumId());
        albumName = al.getAlbumName();
      }
      if (arId != 0) {
        JBArtist ar = artistService.findById(t.getArtistId());
        artistName = ar.getArtistName();
      }

      RemoteDirectory remote = new RemoteDirectory(null, t.getSharedFolder());
      Track tr = new Track(artistName, albumName, t.getTrackName(), trackId);
      r = new Request(remote, tr, t.getOwnerId(), requestId);
    }
    return r;
  }

}
