
package org.happysoft.jukebox.beans.service.entity;

/**
 *
 * @author chrisf
 */
import jakarta.persistence.*;
import org.happysoft.jukebox.model.RemoteDirectory;

@Entity
@Table(name = "jb_albums")
@NamedQueries({
  @NamedQuery(name = "album.findAllByArtistId", query="SELECT a FROM JBAlbum a WHERE a.artistId = :artistId ORDER BY a.albumName"),
  @NamedQuery(name = "album.findByArtistAndAlbumName", query="SELECT a FROM JBAlbum a WHERE a.artistId = :artistId AND a.albumName = :albumName "),
  @NamedQuery(name = "album.findById", query="SELECT a FROM JBAlbum a WHERE a.id = :id"),
})
public class JBAlbum extends AbstractJukeboxEntity {
  
  @Column(name = "artist_id")
  private Long artistId;

  @Column(name = "album_name")
  private String albumName;
      
  public JBAlbum() {    
  }
  
  public JBAlbum(RemoteDirectory remote, long ownerId) {
    setOwnerId(ownerId);
    setFoundOnLastLoad(false);
  }

  public String getAlbumName() {
    return albumName;
  }

  public void setAlbumName(String albumName) {
    this.albumName = albumName;
  }

  public Long getArtistId() {
    return artistId;
  }

  public void setArtistId(Long artistId) {
    this.artistId = artistId;
  }
    
}