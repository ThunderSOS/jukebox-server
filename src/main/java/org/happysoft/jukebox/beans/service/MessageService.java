
package org.happysoft.jukebox.beans.service;

import jakarta.jms.JMSException;
import org.happysoft.jukebox.messaging.JukeboxMessage;

/**
 *
 * @author chrisf
 */
public interface MessageService {
  public void sendMessage(JukeboxMessage message) throws JMSException;
}
