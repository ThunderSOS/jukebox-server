
package org.happysoft.jukebox.beans.service;

import java.util.List;
import org.happysoft.jukebox.beans.service.entity.JBPlaylistRequest;
import org.happysoft.jukebox.model.Request;
import org.happysoft.jukebox.model.RequestStatus;

/**
 *
 * @author chrisf
 */
public interface PlaylistService {

  public JBPlaylistRequest addToPlaylist(long trackId, long requestedBy);

  public List<JBPlaylistRequest> getQueued();

  public List<JBPlaylistRequest> getPlayingNow();

  public void setRequestStatus(long requestId, RequestStatus status);

  public Request buildRequest(long requestId, long trackId);
  
  public void clearPlaying();
  
  public void cancel(long requestId);

}
