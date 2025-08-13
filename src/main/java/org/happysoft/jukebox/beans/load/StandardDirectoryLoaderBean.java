
package org.happysoft.jukebox.beans.load;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.*;
import java.util.*;

import org.happysoft.jukebox.beans.service.entity.JBTrack;
import org.happysoft.jukebox.beans.service.entity.JBArtist;
import org.happysoft.jukebox.beans.service.entity.JBAlbum;
import org.happysoft.jukebox.beans.UserSessionBean;
import org.happysoft.jukebox.beans.service.ArtistService;
import org.happysoft.jukebox.beans.service.AlbumService;
import org.happysoft.jukebox.beans.service.TrackService;
import org.happysoft.jukebox.model.RemoteDirectory;

@Named(value = "loaderBean")
@RequestScoped
public class StandardDirectoryLoaderBean {

  private JBFilenameFilter filter = new JBFilenameFilter();
  // list of directories to exclude whilst scanning
  private List<String> exclude = new ArrayList<>();

  private int numLoaded = 0;
  private int numRemoved = 0;
  private int numNew = 0;
  private int total = 0;
  
  private long ownerId;
  private RemoteDirectory remote;
  
  @Inject
  private UserSessionBean sessionBean;

  @EJB
  private ArtistService artistService;

  @EJB
  private AlbumService albumService;

  @EJB
  private TrackService trackService;

  public StandardDirectoryLoaderBean() {
    exclude.add("incoming");
  }

  public List<JBTrack> loadAll() throws FileNotFoundException {
    List<JBTrack> allTracks = new ArrayList();
   
    var fl = new FileList(remote, null, exclude);
    File[] unsortedTracks = fl.getLooseFiles();
    File[] directories = fl.getDirectories(); // should be artists

    allTracks.addAll(loadLooseTracks(unsortedTracks));
    allTracks.addAll(loadArtists(directories, ownerId, remote));

    for (JBTrack t : allTracks) {
      trackService.findOrCreateTrack(remote, ownerId, t.getArtistId(), t.getAlbumId(), t.getTrackName());
    }
    return allTracks;
  }

  private List<JBTrack> loadArtists(File[] directories, long ownerId, RemoteDirectory remote) throws FileNotFoundException {
    System.out.println("Loading artists");
    List<JBTrack> allTracks = new ArrayList();
    
    for (var artistDirectory : directories) {
      String artistName = artistDirectory.getName();
      System.out.println("Loading artist: " + artistName);
      JBArtist artist = artistService.findOrCreateArtist(ownerId, artistName);
      FileList artistAlbums = new FileList(remote, artist.getArtistName(), exclude);

      File[] albumList = artistAlbums.getDirectories();

      List<JBTrack> albumTracks = loadAlbums(albumList, remote, ownerId, artist);
      List<JBTrack> loose = loadLooseTracksForArtist(artist.getId(), artistAlbums.getLooseFiles());
      if (albumTracks.isEmpty() && loose.isEmpty()) {
        artistService.remove(artist.getId());
      }
      allTracks.addAll(albumTracks);      
      allTracks.addAll(loose);
    }
    return allTracks;
  }

  private List<JBTrack> loadAlbums(File[] albumList, RemoteDirectory remote, long ownerId, JBArtist artist) {
    List<JBTrack> allTracks = new ArrayList();
    for (File al : albumList) {
      String albumName = al.getName();
      System.out.println("Loading album: " + albumName);
      JBAlbum album = albumService.findOrCreateAlbum(remote, ownerId, artist.getId(), albumName);
      List<JBTrack> albumTracks = loadTracksForAlbum(artist, album);
      
      if (albumTracks.isEmpty()) {
        albumService.removeAlbum(artist.getId(), album.getAlbumName());
      }
      allTracks.addAll(albumTracks);
    }
    return allTracks;
  }

  private List<JBTrack> loadLooseTracks(File[] unsortedTracks) {
    List<JBTrack> allTracks = new ArrayList();
    
    for (var track : unsortedTracks) {
      System.out.println("Unsorted track: " + track.getAbsolutePath());
      JBTrack t = new JBTrack(remote, ownerId, 0L, 0L, track.getName());
      t.setFoundOnLastLoad(true);
      allTracks.add(t);
    }
    return allTracks;
  }

  private List<JBTrack> loadTracksForAlbum(JBArtist artist, JBAlbum album) {
    String subDirectory = artist.getArtistName() + "/" + album.getAlbumName();
    File f = new File(remote.toString(), subDirectory);
    System.out.println("Listing tracks in folder: " + f.getAbsolutePath());
    File[] trackList = f.listFiles(filter);
    List<JBTrack> list = new ArrayList();
    
    for (File t : trackList) {
      JBTrack tr = new JBTrack(remote, ownerId, artist.getId(), album.getId(), t.getName());
      list.add(tr);
      System.out.println("Found album track: " + tr.getTrackName());
    }
    return list;
  }

  private List<JBTrack> loadLooseTracksForArtist(long artistId, File[] looseTracks) {
    List<JBTrack> list = new ArrayList();

    for (File looseTrack : looseTracks) {
      String trackName = looseTrack.getName();
      JBTrack tr = new JBTrack(remote, ownerId, artistId, 0L, trackName);
      list.add(tr);
      System.out.println("Found loose track: " + tr.getTrackName());
    }
    return list;
  }

  @RequestScoped
  public void startLoad() throws FileNotFoundException {
    remote = new RemoteDirectory(null, sessionBean.getDirectory());
    ownerId = sessionBean.getOwnerId();

    artistService.prepareForReload(ownerId);
    albumService.prepareForReload(ownerId);
    trackService.prepareForReload(ownerId);

    loadAll();

    trackService.tidyUpAfterReload(ownerId);
    albumService.tidyUpAfterReload(ownerId);
    artistService.tidyUpAfterReload(ownerId);
  }

  public int getNumLoaded() {
    return numLoaded;
  }

  public int getTotal() {
    return total;
  }

  public int getNumNew() {
    return numNew;
  }

}
