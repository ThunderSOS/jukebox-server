package org.happysoft.jukebox.beans.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author chrisf
 */
@Entity
@Table(name = "jb_filetypes")
@NamedQueries({
  @NamedQuery(name = "filetypes.findAll", query = "SELECT f FROM JBFileType f"),
})
public class JBFileType {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "filetype")
  private String fileType;

  public JBFileType() {
  }

  public Long getId() {
    return id;
  }

  public String getFileType() {
    return fileType;
  }

}
