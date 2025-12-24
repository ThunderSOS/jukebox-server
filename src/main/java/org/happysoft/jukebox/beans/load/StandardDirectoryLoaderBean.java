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

  public StandardDirectoryLoaderBean() {
    exclude.add("incoming");
    exclude.add("test");
  }
  
  @RequestScoped
  public void startLoad() throws FileNotFoundException {
    remote = new RemoteDirectory(null, sessionBean.getDirectory());
    ownerId = sessionBean.getOwnerId();
    loadInProgress = true;

    artistService.prepareForReload(ownerId);
    albumService.prepareForReload(ownerId);
    trackService.prepareForReload(ownerId);
    //sessionBean.prepareForLoad();

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
    List<JBTrack> allTracks = new ArrayList();

    for (var artistDirectory : directories) {
      String artistName = artistDirectory.getName();
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
      JBTrack t = trackService.findOrCreateTrack(remote, ownerId, 0L, 0L, tr.getName());
      allTracks.add(t);
    }
    return allTracks;
  }

  private List<JBTrack> loadTracksForAlbum(JBArtist artist, JBAlbum album) {
    String subDirectory = artist.getArtistName() + "/" + album.getAlbumName();
    File f = new File(remote.toString(), subDirectory);
    File[] trackList = f.listFiles(filter);
    List<JBTrack> list = new ArrayList();

    for (File t : trackList) {
      JBTrack tr = trackService.findOrCreateTrack(remote, ownerId, artist.getId(), album.getId(), t.getName());
      list.add(tr);
    }
    return list;
  }

  private List<JBTrack> loadLooseTracksForArtist(long artistId, File[] looseTracks) {
    List<JBTrack> list = new ArrayList();

    for (File looseTrack : looseTracks) {
      String trackName = looseTrack.getName();
      JBTrack tr = trackService.findOrCreateTrack(remote, ownerId, artistId, 0L, trackName);
      list.add(tr);
    }
    return list;
  }
  
}
