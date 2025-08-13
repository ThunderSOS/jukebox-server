

/**
 *
 * @author chrisf
 */
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.happysoft.jukebox.beans.service.entity.JBAlbum;
import jakarta.enterprise.context.RequestScoped;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;
import jakarta.transaction.Transactional;
import java.util.List;

@Stateless
public class HelloServiceImpl implements HelloService {

  @PersistenceContext(unitName = "JukeboxPU")
  private EntityManager em;

  //@EJB
  //private ArtistService artistService;

  @Resource(lookup = "openejb:Resource/jms/ConnectionFactory")
  private ConnectionFactory connectionFactory;
  
  @Resource(lookup = "openejb:Resource/jms/queue/RequestQueue")
  private Queue queue;

  @Override
  @RequestScoped
  //@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  //@Transactional
  public String getMessage() {
    JBAlbum album = new JBAlbum(null, 0L);
    
    int deleted = em.createQuery("delete from JBAlbum a where a.albumName like :name").setParameter("name", "Test%").executeUpdate();
    System.out.println("Deleted " + deleted);
    deleted = em.createQuery("delete from JBArtist a where a.artistName like :name").setParameter("name", "Test%").executeUpdate();
    System.out.println("Deleted " + deleted);
    album.setAlbumName("Test 2");
    System.out.println("EntityManager: " + em);
    em.persist(album);

    System.out.println("Connection Factory: " + connectionFactory);
    System.out.println("Queue: " + queue);
    
    List<JBAlbum> albums = em.createQuery("select a from JBAlbum a").getResultList();
    for (JBAlbum a : albums) {
      System.out.println("Album " + a.getAlbumName());
    }
    return "Hello from injected HelloService! ArtistService = "; // + artistService;
    
  }

}
