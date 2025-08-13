
package org.happysoft.jukebox.beans.service;

import org.happysoft.jukebox.beans.service.entity.JBArtist;

/**
 *
 * @author chrisf
 */
public interface ArtistService {
    public JBArtist findOrCreateArtist(long ownerId, String artistName);
    public JBArtist findByOwnerAndArtistName(long ownerId, String artistName);
    public JBArtist remove(long artistId);
    public void prepareForReload(long ownerId);
    public void tidyUpAfterReload(long ownerId);
    public JBArtist findById(long trackId);
}