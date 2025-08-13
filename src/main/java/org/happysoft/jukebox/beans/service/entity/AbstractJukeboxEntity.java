/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.happysoft.jukebox.beans.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author chrisf
 */
@MappedSuperclass
public class AbstractJukeboxEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; 
  
  @Column (name = "date_created")
  @jakarta.json.bind.annotation.JsonbTransient
  private final Timestamp dateCreated;
  
  @Column (name = "found_on_last_load")
  private boolean foundOnLastLoad = false;    
      
  @Column(name = "owner_id")
  private Long ownerId;    


  public AbstractJukeboxEntity() {
    dateCreated = new Timestamp(System.currentTimeMillis());
  }
    
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public final Timestamp getDateCreated() {
    return dateCreated;
  }
  
  public final Long getOwnerId() {
    return ownerId;
  }

  public final void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }
  
  public final boolean foundOnLastLoad() {
    return foundOnLastLoad;
  }

  public final void setFoundOnLastLoad(boolean foundOnLastLoad) {
    this.foundOnLastLoad = foundOnLastLoad;
  }  
  
}
