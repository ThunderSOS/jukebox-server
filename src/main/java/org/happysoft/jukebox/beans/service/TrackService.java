
package org.happysoft.jukebox.beans.service;

import org.happysoft.jukebox.beans.service.entity.JBTrack;
import org.happysoft.jukebox.model.RemoteDirectory;

/**
 *
 * @author chrisf
 */
public interface TrackService {
  public JBTrack findOrCreateTrack(RemoteDirectory remote, long ownerId, long artistId, long albumId, String trackName);
  public JBTrack findByOwnerArtistAlbumAndName(long ownerId, long artistId, long albumId, String trackName);
  public void prepareForReload(long ownerId);
  public void tidyUpAfterReload(long ownerId);
  public JBTrack findById(long trackId);
}