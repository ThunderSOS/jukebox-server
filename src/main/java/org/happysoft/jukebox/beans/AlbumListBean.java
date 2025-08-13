
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
import org.happysoft.jukebox.beans.service.entity.JBTrack;

/**
 *
 * @author chrisf
 */
@Named(value = "albumListBean")
@ViewScoped
public class AlbumListBean implements Serializable {
  
  @EJB
  private SearchService searchService;
  
  @Inject 
  private ArtistListBean artistBean;
  
  @Inject
  private TrackListBean trackBean;
  
  private List<JBAlbum> albums = List.of();
  private List<JBTrack> looseTracks = List.of();
  
  private String artistId;
  private String artistName;
  private boolean shouldRenderAlbums = true;
        
  public void loadAlbumsByArtistId() { 
    FacesContext context = FacesContext.getCurrentInstance();
    artistId = context.getExternalContext().getRequestParameterMap().get("artistId");
    artistName = context.getExternalContext().getRequestParameterMap().get("artistName");
    loadAlbumsInternal();
  }
  
  void loadAlbumsInternal() { 
    albums = searchService.findAlbumsByArtistId(Long.parseLong(artistId));
    looseTracks = searchService.findLooseTracksForArtist(Long.parseLong(artistId));
    artistBean.setShouldRenderArtists(false);   
    trackBean.setShouldRenderTracks(false);
    shouldRenderAlbums = true;
  }
    
  public List<JBAlbum> getAlbums() {
    return albums == null ? List.of() : albums;
  }
  
  public List<JBTrack> getLooseTracks() {
    return looseTracks == null ? List.of() : looseTracks;
  }
  
  public String getArtistId() {
    return artistId;
  }    

  public String getArtistName() {
    return artistName;
  }

  public void setArtistId(String artistId) {
    this.artistId = artistId;
  }

  public void setArtistName(String artistName) {
    this.artistName = artistName;
  }
  
  public boolean shouldRenderAlbums() {
    return shouldRenderAlbums;
  }
  
  public void setShouldRenderAlbums(boolean should) {
    this.shouldRenderAlbums = should;
  }
  
}
