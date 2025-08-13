
package org.happysoft.jukebox.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.util.List;

/**
 *
 * @author chrisf
 */
@Named(value = "utilBean")
@ApplicationScoped
public class UtilBean {
  
  public List<Character> getAlphabet() {
    return List.of(
            'A','B','C','D','E','F','G','H','I','J','K','L','M',
            'N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
  }
  
}
