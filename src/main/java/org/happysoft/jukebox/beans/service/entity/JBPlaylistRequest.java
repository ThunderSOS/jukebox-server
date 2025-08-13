
package org.happysoft.jukebox.beans.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import org.happysoft.jukebox.model.RequestStatus;

@Entity
@Table(name = "jb_requests")
@NamedQueries({
  @NamedQuery(name = "playlist.findPlaying", query="SELECT p FROM JBPlaylistRequest p WHERE p.status = 'PLAYING'"),
  @NamedQuery(name = "playlist.findAllQueued", query="SELECT p FROM JBPlaylistRequest p WHERE p.status = 'QUEUED' ORDER BY p.dateCreated ASC"),
})
public class JBPlaylistRequest {
  
  @Id
  @Column(name = "request_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long requestId; 
    
  @Column(name = "track_id")
  private Long trackId;
  
  @Enumerated(EnumType.STRING)
  @Column (name = "status")
  private RequestStatus status;

  @Column(name = "requested_by")
  private Long requestedBy;
  
  @Column (name = "date_created")
  private final Timestamp dateCreated;

  public JBPlaylistRequest() {  
    dateCreated = new Timestamp(System.currentTimeMillis());
  }

  public Long getRequestId() {
    return requestId;
  }

  public void setRequestId(Long requestId) {
    this.requestId = requestId;
  }

  public Long getTrackId() {
    return trackId;
  }

  public void setTrackId(Long trackId) {
    this.trackId = trackId;
  }

  public Long getRequestedBy() {
    return requestedBy;
  }

  public void setRequestedBy(Long requestedBy) {
    this.requestedBy = requestedBy;
  }
  
  public final Timestamp getDateCreated() {
    return dateCreated;
  }

  public RequestStatus getStatus() {
    return status;
  }

  public void setStatus(RequestStatus status) {
    this.status = status;
  }
  
}
