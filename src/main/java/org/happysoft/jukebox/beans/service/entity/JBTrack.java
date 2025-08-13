
package org.happysoft.jukebox.beans.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import org.happysoft.jukebox.model.RemoteDirectory;

/**
 *
 * @author chrisf
 */
@Entity
@Table(name = "jb_tracks")
@NamedQueries({
  @NamedQuery(name = "track.findByOwnerArtistAlbumAndName", query="SELECT t FROM JBTrack t WHERE t.ownerId = :ownerId "
          + "AND t.artistId = :artistId "
          + "AND t.albumId = :albumId "
          + "AND t.trackName = :trackName"),
  @NamedQuery(name = "track.findTracksForAlbum", query="SELECT t FROM JBTrack t WHERE t.albumId = :albumId ORDER BY t.trackName"),
  @NamedQuery(name = "track.findLooseTracksForArtist", query="SELECT t FROM JBTrack t WHERE t.albumId = 0 AND t.artistId = :artistId ORDER BY t.trackName"),
  @NamedQuery(name = "track.findLooseTracks", query="SELECT t FROM JBTrack t WHERE t.albumId = 0 AND t.artistId = 0 ORDER BY t.trackName"),
  @NamedQuery(name = "track.findById", query="SELECT t FROM JBTrack t WHERE t.id = :id"),
})
public class JBTrack extends AbstractJukeboxEntity {

  @Column(name = "artist_id")
  private Long artistId;
  
  @Column(name = "album_id")
  private Long albumId;
  
  @Column(name = "track_name")
  private String trackName;  
      
  @Column(name = "shared_folder")
  private String sharedFolder;

  public JBTrack() {    
  }
  
  public JBTrack(RemoteDirectory remote, long ownerId, long artistId, long albumId, String trackName) {
    this.albumId = albumId;
    this.artistId = artistId;
    this.trackName = trackName;
    this.sharedFolder = remote.toString();
    setOwnerId(ownerId);
    setFoundOnLastLoad(false);    
  }

  public Long getArtistId() {
    return artistId;
  }

  public void setArtistId(Long artistId) {
    this.artistId = artistId;
  }

  public Long getAlbumId() {
    return albumId;
  }

  public void setAlbumId(Long albumId) {
    this.albumId = albumId;
  }

  public String getTrackName() {
    return trackName;
  }

  public void setTrackName(String trackName) {
    this.trackName = trackName;
  }
    
  public final String getSharedFolder() {
    return sharedFolder;
  }

  public final void setSharedFolder(String sharedFolder) {
    this.sharedFolder = sharedFolder;
  }
  
}
