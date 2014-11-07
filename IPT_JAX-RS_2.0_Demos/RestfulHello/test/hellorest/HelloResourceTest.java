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
package hellorest;

import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.message.internal.MediaTypes;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * Functional tests for {@link hellorest.HelloResource} resource methods
 *
 * @author Trayan Iliev, IPT [http://iproduct.org]
 */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HelloResourceTest {

    public static final String BASE_URI_STRING
            = "http://localhost:8084/hello/resources";
    public static final String USER_NAME = "RESTful BGJUG Programmer";
    private static WebTarget baseTarget;

    @BeforeClass
    public static void setUpClass() {
        URI uri = UriBuilder.fromUri(BASE_URI_STRING).build();
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        baseTarget = client.target(uri);

     }

    /**
     * Test of sayHelloText method, of class HelloResource.
     */
    @Test
    public void testSayHelloText() {
        System.out.println("sayHelloText");
        String response = baseTarget.path("hello")
                .request(MediaType.TEXT_PLAIN).get(String.class);
        System.out.println(response);
        assertEquals(HelloResource.MESSAGE_TEXT, response);
    }

    /**
     * Test of sayHelloXml method, of class HelloResource.
     */
    @Test
    public void testSayHelloXml() {
        System.out.println("sayHelloXml");
        String response = baseTarget.path("hello")
                .request(MediaType.TEXT_XML).get(String.class);
        System.out.println(response);
        assertEquals(HelloResource.MESSAGE_XML, response);
    }

    /**
     * Test of sayHelloJson method, of class HelloResource.
     */
    @Test
    public void testSayHelloJson() {
        System.out.println("sayHelloJson");
        String response = baseTarget.path("hello")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println(response);
        assertEquals(HelloResource.MESSAGE_JSON, response);
    }

    /**
     * Test of sayHelloName method, of class HelloResource.
     */
    @Test
    public void testSayHelloName() {
        System.out.println("sayHelloName");
        String response = baseTarget.path("hello").path(USER_NAME)
                .request(MediaType.TEXT_HTML)
                .get(String.class);
        System.out.println(response);
        String expectedMessage = HelloResource.MESSAGE_HTML_START
                + USER_NAME + HelloResource.MESSAGE_HTML_END;
        assertEquals(expectedMessage, response);
    }
    
    /**
     * Test that WADL document is generated at \application.wadl.
     */
    @Test
    public void testApplicationWadl() {
        String serviceWadl = baseTarget.path("application.wadl")
                .request(MediaTypes.WADL).get(String.class);
        assertTrue(serviceWadl.length() > 0);
    }
    

}
