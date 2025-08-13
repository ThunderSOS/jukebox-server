
package org.happysoft.jukebox.beans.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import org.happysoft.jukebox.messaging.JukeboxMessage;

/**
 *
 * @author chrisf
 */
@Stateless
public class MessageServiceImpl implements MessageService {
  
  @Resource(lookup = "openejb:Resource/jms/ConnectionFactory")
  private ConnectionFactory connectionFactory;

  @Resource(lookup = "openejb:Resource/jms/queue/RequestQueue")
  private Queue queue;
  
  private Connection connection;
  private Session session;
  private MessageProducer producer;
  
  @PostConstruct
  public void init() throws JMSException {
    connection = connectionFactory.createConnection();
    session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
    producer = session.createProducer(queue);
    connection.start();
  }
  
  @RequestScoped
  public void sendMessage(final JukeboxMessage message) throws JMSException {
      final Message jmsMessage = session.createObjectMessage(message);
      producer.send(jmsMessage);
      System.out.println("Message sent to queue: " + queue.getQueueName());
  }
  
}
