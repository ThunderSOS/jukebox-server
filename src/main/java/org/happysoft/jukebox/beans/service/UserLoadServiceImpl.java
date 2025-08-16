
package org.happysoft.jukebox.beans.service;

import jakarta.ejb.Asynchronous;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.happysoft.jukebox.beans.UserSessionBean;
import org.happysoft.jukebox.beans.load.FileList;
import org.happysoft.jukebox.beans.load.JBFilenameFilter;
import org.happysoft.jukebox.beans.service.entity.JBAlbum;
import org.happysoft.jukebox.beans.service.entity.JBArtist;
import org.happysoft.jukebox.beans.service.entity.JBTrack;
import org.happysoft.jukebox.model.RemoteDirectory;

/**
 *
 * @author chrisf
 */
@Stateless
public class UserLoadServiceImpl implements Serializable {
  
  private final JBFilenameFilter filter = new JBFilenameFilter();
  // list of directories to exclude whilst scanning
  private final List<String> exclude = new ArrayList<>();

  private volatile boolean loadInProgress = false;

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

  public UserLoadServiceImpl() {
    exclude.add("incoming");
    exclude.add("test");
  }
  
  @RequestScoped
  @Asynchronous
  public void startLoad() throws FileNotFoundException {
    remote = new RemoteDirectory(null, sessionBean.getDirectory());
    ownerId = sessionBean.getOwnerId();
    loadInProgress = true;

    artistService.prepareForReload(ownerId);
    albumService.prepareForReload(ownerId);
    trackService.prepareForReload(ownerId);
    sessionBean.prepareForLoad();

    loadAll();

    trackService.tidyUpAfterReload(ownerId);
    albumService.tidyUpAfterReload(ownerId);
    artistService.tidyUpAfterReload(ownerId);

    loadInProgress = false;
  }

  private void loadAll() throws FileNotFoundException {
    var fl = new FileList(remote, null, exclude);

    File[] unsortedTracks = fl.getLooseFiles();
    File[] directories = fl.getDirectories(); // should be artists

    loadLooseTracks(unsortedTracks);
    loadArtists(directories, ownerId);
  }

  private List<JBTrack> loadArtists(File[] directories, long ownerId) throws FileNotFoundException {
    System.out.println("Loading artists");
    List<JBTrack> allTracks = new ArrayList();

    for (var artistDirectory : directories) {
      String artistName = artistDirectory.getName();
      System.out.println("Loading artist: " + artistName);
      JBArtist artist = artistService.findOrCreateArtist(ownerId, artistName);

      FileList artistAlbums = new FileList(remote, artist.getArtistName(), exclude);
      File[] albumList = artistAlbums.getDirectories();

      List<JBTrack> albumTracks = loadAlbums(albumList, ownerId, artist);
      List<JBTrack> loose = loadLooseTracksForArtist(artist.getId(), artistAlbums.getLooseFiles());

      if (albumTracks.isEmpty() && loose.isEmpty()) {
        artistService.remove(artist.getId());
      }
      allTracks.addAll(albumTracks);
      allTracks.addAll(loose);
    }
    return allTracks;
  }

  private List<JBTrack> loadAlbums(File[] albumList, long ownerId, JBArtist artist) {
    List<JBTrack> allTracks = new ArrayList();
    for (File al : albumList) {
      String albumName = al.getName();
      System.out.println("Loading album: " + albumName);
      JBAlbum album = albumService.findOrCreateAlbum(ownerId, artist.getId(), albumName);
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

    for (var tr : unsortedTracks) {
      System.out.println("Unsorted track: " + tr.getAbsolutePath());
      JBTrack t = trackService.findOrCreateTrack(remote, ownerId, 0L, 0L, tr.getName());
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
      JBTrack tr = trackService.findOrCreateTrack(remote, ownerId, artist.getId(), album.getId(), t.getName());
      list.add(tr);
      System.out.println("Found album track: " + tr.getTrackName());
    }
    return list;
  }

  private List<JBTrack> loadLooseTracksForArtist(long artistId, File[] looseTracks) {
    List<JBTrack> list = new ArrayList();

    for (File looseTrack : looseTracks) {
      String trackName = looseTrack.getName();
      JBTrack tr = trackService.findOrCreateTrack(remote, ownerId, artistId, 0L, trackName);
      list.add(tr);
      System.out.println("Found loose track: " + tr.getTrackName());
    }
    return list;
  }
 
  public boolean isLoadInProgress() {
    return loadInProgress;
  }
  
}
