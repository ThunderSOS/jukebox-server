package org.happysoft.jukebox.beans;

import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import org.happysoft.jukebox.beans.service.SearchService;
import org.happysoft.jukebox.beans.entity.JBTrack;

/**
 *
 * @author chrisf
 */
@Named(value = "trackListBean")
@ViewScoped
public class TrackListBean implements Serializable {

  private static final long serialVersionUID = 1L;

  @EJB
  private SearchService searchService;

  @Inject
  private ArtistListBean artistBean;

  @Inject
  private AlbumListBean albumBean;

  private List<JBTrack> tracks = List.of();

  private String albumId;
  private String albumName;
  private String artistId;
  private String artistName;
  private String lastSearch = "";
  private boolean shouldRenderTracks = false;
  private boolean shouldRenderSearchResults = false;

  public void loadTracksByAlbumId() {
    FacesContext context = FacesContext.getCurrentInstance();
    albumId = context.getExternalContext().getRequestParameterMap().get("albumId");
    albumName = context.getExternalContext().getRequestParameterMap().get("albumName");
    artistId = context.getExternalContext().getRequestParameterMap().get("artistId");
    artistName = context.getExternalContext().getRequestParameterMap().get("artistName");
    tracks = searchService.findTracksForAlbum(Long.parseLong(albumId));
    artistBean.setShouldRenderArtists(false);
    albumBean.setShouldRenderAlbums(false);
    shouldRenderSearchResults = false;
    shouldRenderTracks = true;
  }

  public void loadLooseTracks() {
    tracks = searchService.findLooseTracks();
    artistBean.setShouldRenderArtists(false);
    albumBean.setShouldRenderAlbums(false);
    shouldRenderSearchResults = false;
    shouldRenderTracks = true;
  }

  public List<JBTrack> getTracks() {
    return tracks == null ? List.of() : tracks;
  }

  public String getAlbumId() {
    return albumId;
  }

  public String getAlbumName() {
    return albumName;
  }
  
  public String getLastSearch() {
    return lastSearch;
  }

  public boolean shouldRenderTracks() {
    return shouldRenderTracks;
  }

  public void setShouldRenderTracks(boolean should) {
    this.shouldRenderTracks = should;
  }
  
  public boolean shouldRenderSearchResults() {
    return shouldRenderSearchResults;
  }

  public void setShouldRenderSearchResults(boolean should) {
    this.shouldRenderSearchResults = should;
  }

  public void searchTracks() {
    FacesContext context = FacesContext.getCurrentInstance();
    String searchString = context.getExternalContext().getRequestParameterMap().get("searchForm:searchBox");
    tracks = searchService.searchTracks(searchString);
    System.out.println("Found " + tracks.size() + " tracks");
    lastSearch = searchString;
    artistBean.setShouldRenderArtists(false);
    albumBean.setShouldRenderAlbums(false);
    shouldRenderTracks = false;
    shouldRenderSearchResults = true;
  }

  public void reloadAlbumList() {
    shouldRenderTracks = false;
    albumBean.setArtistId(artistId);
    albumBean.setArtistName(artistName);
    albumBean.loadAlbumsInternal();
  }

}
