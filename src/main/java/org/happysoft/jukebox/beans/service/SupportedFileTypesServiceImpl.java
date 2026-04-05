
package org.happysoft.jukebox.beans.service;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.happysoft.jukebox.beans.entity.JBFileType;

/**
 *
 * @author chrisf
 */
@Stateless
public class SupportedFileTypesServiceImpl implements SupportedFileTypesService {

  @PersistenceContext(unitName = "JukeboxPU")
  private EntityManager em;

  /* Artist search */
  @Override
  @RequestScoped
  public List<JBFileType> getSupportedTypes() {
    return em.createNamedQuery("filetypes.findAll").getResultList();
  }
}