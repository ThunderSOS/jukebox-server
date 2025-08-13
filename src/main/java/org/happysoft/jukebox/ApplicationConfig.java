
package org.happysoft.jukebox;

import jakarta.faces.annotation.FacesConfig;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@FacesConfig
@ApplicationPath("/api") // Not required for Servlets but can be used for JAX-RS if you need.
public class ApplicationConfig extends Application {

}
  

