/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "jb_users")
@NamedQueries({
  @NamedQuery(name = "user.findByUsername", query="SELECT u FROM JBUser u WHERE u.username = :username"),
  @NamedQuery(name = "user.findByUserId", query="SELECT u FROM JBUser u WHERE u.userId = :userId"),
})
public class JBUser {
  
  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId; 
    
  @Column(name = "username")
  private String username;
  
  @Column(name = "shared_folder")
  private String sharedFolder;
  
  @Column (name = "date_created")
  private final Timestamp dateCreated;

  public JBUser() {  
    dateCreated = new Timestamp(System.currentTimeMillis());
  }

  public Long getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getSharedFolder() {
    return sharedFolder;
  }

  public void setSharedFolder(String sharedFolder) {
    this.sharedFolder = sharedFolder;
  }

  
  
  public final Timestamp getDateCreated() {
    return dateCreated;
  }
  
}
