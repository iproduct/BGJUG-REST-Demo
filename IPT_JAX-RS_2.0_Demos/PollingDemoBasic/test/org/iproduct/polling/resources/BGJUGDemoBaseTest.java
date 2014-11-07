/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2003-2014 IPT - Intellectual Products & Technologies.
 * All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 with Classpath Exception only ("GPL"). 
 * You may use this file only in compliance with GPL. You can find a copy 
 * of GPL in the root directory of this project in the file named LICENSE.txt.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the GPL file named LICENSE.txt in the root directory of 
 * the project.
 *
 * GPL Classpath Exception:
 * IPT - Intellectual Products & Technologies (IPT) designates this particular 
 * file as subject to the "Classpath" exception as provided by IPT in the GPL 
 * Version 2 License file that accompanies this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 */
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

