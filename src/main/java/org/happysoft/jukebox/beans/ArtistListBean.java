
package org.happysoft.jukebox.beans;

import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import org.happysoft.jukebox.beans.service.SearchService;
import org.happysoft.jukebox.beans.service.entity.JBAlbum;
import org.happysoft.jukebox.beans.service.entity.JBArtist;

/**
 *
 * @author chrisf
 */
@Named(value = "artistListBean")
@ViewScoped
public class ArtistListBean implements Serializable {

  @EJB
  private SearchService searchService;
  
  private List<JBArtist> artists = List.of();
  private List<JBAlbum> albums = null;
  
  private String letter;
  private String artistId;
  private String artistName;
  private boolean shouldRenderArtists =  true;
  
  @Inject
  private AlbumListBean albumBean;
  
  @Inject
  private TrackListBean trackBean;
  
  public void loadArtistsStartingWith() {
    FacesContext context = FacesContext.getCurrentInstance();
    letter = context.getExternalContext().getRequestParameterMap().get("letter");
    shouldRenderArtists = true;
    albumBean.setShouldRenderAlbums(false);
    trackBean.setShouldRenderTracks(false);
    artists = searchService.findArtistsStartingWith(letter);
    System.out.println("Loaded artist list");
  }
  
  public List<JBArtist> getArtists() {
    return artists;
  }
  
  public boolean isLetterSet() {
    return letter != null;
  }
  
  public boolean shouldRenderArtists() {
    return shouldRenderArtists;
  }
  
  public void setShouldRenderArtists(boolean should) {
    this.shouldRenderArtists = should;
  }
  
}
