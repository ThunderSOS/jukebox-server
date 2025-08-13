
package org.happysoft.jukebox.beans.service;

import org.happysoft.jukebox.beans.service.entity.JBAlbum;
import org.happysoft.jukebox.model.RemoteDirectory;

/**
 *
 * @author chrisf
 */
public interface AlbumService {
    public JBAlbum findOrCreateAlbum(RemoteDirectory remote, long ownerId, long artistId, String artistName);
    public JBAlbum removeAlbum(long artistId, String artistName);
    public JBAlbum findByArtistIdAndAlbumName(long artistId, String albumName);
    public void prepareForReload(long ownerId);
    public void tidyUpAfterReload(long ownerId);
    public JBAlbum findById(long id);
}