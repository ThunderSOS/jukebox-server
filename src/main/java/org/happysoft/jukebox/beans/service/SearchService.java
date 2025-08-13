
package org.happysoft.jukebox.beans.service;

import java.util.List;
import org.happysoft.jukebox.beans.service.entity.JBAlbum;
import org.happysoft.jukebox.beans.service.entity.JBArtist;
import org.happysoft.jukebox.beans.service.entity.JBTrack;

/**
 *
 * @author chrisf
 */
public interface SearchService {
  
  public List<JBArtist> findArtistsBySearchString(String search);
  
  public List<JBArtist> findArtistsStartingWith(String search);
  
  public JBArtist findArtistById(long id);
  
  public List<JBAlbum> findAlbumsByArtistId(long artistId);
  
  public List<JBTrack> findTracksForAlbum(long albumId);
  
  public List<JBTrack> findLooseTracksForArtist(long artistId);
  
  public List<JBTrack> findLooseTracks();
  
}
