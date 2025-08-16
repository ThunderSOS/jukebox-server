
package org.happysoft.jukebox.beans.service.entity;

/**
 *
 * @author chrisf
 */
import jakarta.persistence.*;

@Entity
@Table(name = "jb_artists")
@NamedQueries({
  @NamedQuery(name = "artist.findAllMatching", query="SELECT a FROM JBArtist a WHERE UPPER(a.artistName) LIKE UPPER('%' || :search || '%') ORDER BY a.artistName"),
  @NamedQuery(name = "artist.findAllStartingWith", query="SELECT a FROM JBArtist a WHERE UPPER(a.artistName) LIKE UPPER(:search || '%') ORDER BY a.artistName"),
  @NamedQuery(name = "artist.findById", query="SELECT a FROM JBArtist a WHERE a.id = :id"),
  @NamedQuery(name = "artist.findByOwnerAndArtistName", query="SELECT a FROM JBArtist a WHERE a.ownerId = :ownerId AND a.artistName = :artistName"),  
  @NamedQuery(name = "artist.countByOwner", query="SELECT COUNT(a) FROM JBArtist a WHERE a.ownerId = :ownerId"),  
  @NamedQuery(name = "artist.countNewByOwner", query="SELECT COUNT(a) FROM JBArtist a WHERE a.ownerId = :ownerId AND a.foundOnLastLoad = false"),  
})
public class JBArtist extends AbstractJukeboxEntity {
  
  public JBArtist() {  
    setFoundOnLastLoad(false);
  }

  @Column(name = "artist_name")
  private String artistName;
  

  public String getArtistName() {
    return artistName;
  }

  public void setArtistName(String artistName) {
    this.artistName = artistName;
  }
      
}