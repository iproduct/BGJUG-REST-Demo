package hellorest;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * The JAX-RS bootstrap class that configures the polling application
 * 
 * @author Trayan Iliev
 * @author IPT [http://iproduct.org]
 * 
 */
@ApplicationPath("/resources")
public class BGJUGDemoApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
	    final Set<Class<?>> classes = new HashSet<>();
	    // register root resources
	    classes.add(HelloResource.class);
	    classes.add(StudentsResource.class);
	    return classes;
	}
}