package org.happysoft.jukebox.beans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.jms.JMSException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.happysoft.jukebox.beans.service.MessageService;
import org.happysoft.jukebox.beans.service.PlaylistService;
import org.happysoft.jukebox.beans.service.TrackService;
import org.happysoft.jukebox.beans.service.entity.JBPlaylistRequest;
import org.happysoft.jukebox.beans.service.entity.JBTrack;
import org.happysoft.jukebox.messaging.AddToQueueMessage;
import org.happysoft.jukebox.messaging.PauseMessage;
import org.happysoft.jukebox.messaging.PlayMessage;
import org.happysoft.jukebox.messaging.StopMessage;
import org.happysoft.jukebox.model.Request;

/**
 *
 * @author chrisf
 */
@Named(value = "playlistBean")
@ApplicationScoped
public class PlaylistBean implements Serializable {

  @EJB
  private PlaylistService playlistService;

  @EJB
  private TrackService trackService;

  @EJB
  private MessageService messageService;

  private boolean isPaused = false;

  private UIComponent hiddenButton;

  public JBPlaylistRequest addToPlaylist() {
    FacesContext context = FacesContext.getCurrentInstance();
    String tId = context.getExternalContext().getRequestParameterMap().get("trackId");

    long trackId = Long.parseLong(tId);
    JBPlaylistRequest plReq = playlistService.addToPlaylist(trackId, 0);
    JBTrack t = trackService.findById(trackId);

    //some id's can change after a reload
    if (t != null) {
      Request r = playlistService.buildRequest(plReq.getRequestId(), trackId);
      AddToQueueMessage message = new AddToQueueMessage();
      message.setRequest(r);

      try {
        messageService.sendMessage(message);

      } catch (JMSException e) {
        e.printStackTrace();
      }
    }
    return plReq;
  }

  public void cancel() {
    FacesContext context = FacesContext.getCurrentInstance();
    String rId = context.getExternalContext().getRequestParameterMap().get("requestId");
    long requestId = Long.parseLong(rId);
    playlistService.cancel(requestId);
  }

  public void pause() {
    isPaused = true;
    PauseMessage p = new PauseMessage();
    try {
      messageService.sendMessage(p);

    } catch (JMSException e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    isPaused = false;
    StopMessage stop = new StopMessage();
    try {
      messageService.sendMessage(stop);

    } catch (JMSException e) {
      e.printStackTrace();
    }
  }

  public void play() {
    isPaused = false;
    PlayMessage p = new PlayMessage();
    try {
      messageService.sendMessage(p);

    } catch (JMSException e) {
      e.printStackTrace();
    }
  }

  public boolean isPaused() {
    return isPaused;
  }

  @RequestScoped
  public List<Request> getNowPlaying() {
    List<JBPlaylistRequest> plReqs = playlistService.getPlayingNow();
    List<Request> reqs = new ArrayList<>();
    for (JBPlaylistRequest plReq : plReqs) {
      Request req = playlistService.buildRequest(plReq.getRequestId(), plReq.getTrackId());
      reqs.add(req);
    }
    return reqs;
  }

  @RequestScoped
  public List<Request> getQueued() {
    List<JBPlaylistRequest> plReqs = playlistService.getQueued();
    List<Request> reqs = new ArrayList<>();

    for (JBPlaylistRequest plReq : plReqs) {
      Request req = playlistService.buildRequest(plReq.getRequestId(), plReq.getTrackId());
      reqs.add(req);
    }
    return reqs;
  }

  public UIComponent getHiddenButton() {
    return hiddenButton;
  }

  public void setHiddenButton(UIComponent hiddenButton) {
    this.hiddenButton = hiddenButton;
  }

}
