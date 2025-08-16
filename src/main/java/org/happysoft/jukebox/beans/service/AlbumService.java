package org.happysoft.jukebox.beans.service;

import org.happysoft.jukebox.beans.service.entity.JBAlbum;

/**
 *
 * @author chrisf
 */
public interface AlbumService {

  public JBAlbum findOrCreateAlbum(long ownerId, long artistId, String artistName);

  public JBAlbum removeAlbum(long artistId, String artistName);

  public JBAlbum findByArtistIdAndAlbumName(long artistId, String albumName);

  public void prepareForReload(long ownerId);

  public int tidyUpAfterReload(long ownerId);

  public JBAlbum findById(long id);

  public long countByOwner(long ownerId);

  public long countNewByOwner(long ownerId);
  
}
