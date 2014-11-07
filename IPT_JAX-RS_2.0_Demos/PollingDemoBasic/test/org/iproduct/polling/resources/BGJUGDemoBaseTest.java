package org.iproduct.polling.resources;

import java.net.URI;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.iproduct.polling.BGJUGDemoApplication;

/**
 * Base test extending JerseyTest class that should be extended by other 
 * functional tests in BGJUG Demo project
 *
 * @author Trayan Iliev, IPT [http://iproduct.org]
 */
public class BGJUGDemoBaseTest extends JerseyTest {

    /**
     * Configures Jersey tests and returns Application to be tested
     * 
     * @return Application to be tested 
     */
	@Override
    protected Application configure() {
    	set(TestProperties.LOG_TRAFFIC, true);
    	set(TestProperties.DUMP_ENTITY, true);
        return new BGJUGDemoApplication();
    }

	/**
	 * Sets the base Uri for all resource to be accessed using 
	 * {@link org.iproduct.polling.resources.PollsResourceTest#target()} method.
	 * 
	 * @return the base URI for all resources to be tested
	 */
    @Override
    protected URI getBaseUri() {
        return UriBuilder.fromUri(super.getBaseUri()).path("rest-jaxrs/resources").build();
    }
}

