package org.happysoft.jukebox.beans.web;

/**
 *
 * @author chrisf
 */
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import org.happysoft.jukebox.beans.service.PlaylistService;
import org.happysoft.jukebox.beans.service.TrackService;
import org.happysoft.jukebox.beans.service.entity.JBPlaylistRequest;
import org.happysoft.jukebox.beans.service.entity.JBTrack;
import org.happysoft.jukebox.model.Request;
import org.happysoft.jukebox.model.RequestStatus;

@Path("/playlist")
public class PlaylistResource {

  @EJB
  private PlaylistService playlistService;
  
  @EJB
  private TrackService trackService;
  
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Response setRequestStatus(@QueryParam("requestId") long requestId, @QueryParam("status") RequestStatus status) {
    System.out.println("In service setRequestStatus()");
    playlistService.setRequestStatus(requestId, status);
    return Response.noContent().build();
  }
  
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/clearPlaying")
  public Response clearPlaying() {
    System.out.println("In service setRequestStatus()");
    playlistService.clearPlaying();
    return Response.noContent().build();
  }

  @GET
  @Path("/getQueue")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getQueue() {
    System.out.println("In service getQueue()");
    List<JBPlaylistRequest> queue = playlistService.getQueued();
    
    List<Request> reqs = new ArrayList<>();
    
    for(JBPlaylistRequest req : queue) {
      JBTrack track = trackService.findById(req.getTrackId());
      reqs.add(playlistService.buildRequest(req.getRequestId(), track.getId()));
    }    
    return Response.ok().entity(reqs).build();
  }
  
  

}
