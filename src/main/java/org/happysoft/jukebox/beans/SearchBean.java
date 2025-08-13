
package org.happysoft.jukebox.beans;

import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import org.happysoft.jukebox.beans.service.entity.JBAlbum;
import org.happysoft.jukebox.beans.service.entity.JBArtist;
import org.happysoft.jukebox.beans.service.SearchService;
import org.happysoft.jukebox.beans.service.entity.JBTrack;

/**
 *
 * @author chrisf
 */
@Named(value = "searchBean")
@ViewScoped
public class SearchBean implements Serializable {
  
  @EJB
  private SearchService searchService;
  
  public List<JBArtist> getArtistsBySearchString(String search) {
    return searchService.findArtistsBySearchString(search);
  }
  
  public List<JBArtist> getArtistsStartingWith(String search) {
    return searchService.findArtistsStartingWith(search);
  }
  
  public JBArtist getArtistById(Long id) {
    return searchService.findArtistById(id);
  }
  
  public List<JBAlbum> getAlbumsByArtistId(Long id) {
    return searchService.findAlbumsByArtistId(id);
  }

  public List<JBTrack> getTracksForAlbum(Long id) {
    return searchService.findTracksForAlbum(id);
  }

  public List<JBTrack> getLooseTracks() {
    return searchService.findLooseTracks();
  }

}
