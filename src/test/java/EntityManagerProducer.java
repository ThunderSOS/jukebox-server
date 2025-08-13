


import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 *
 * @author chrisf
 */
public class EntityManagerProducer {

  @Inject
  private EntityManagerFactory emf;

  @Produces
  @RequestScoped
  public EntityManager create() {
    System.out.println("Creating entityManager");
    return emf.createEntityManager();
  }

  public void destroy(@Disposes EntityManager em) {
    System.out.println("Disposing entityManager");
    if(em.isOpen()) {
      em.close();
    }
  }
}
