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
package org.iproduct.hellorest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import static javax.ws.rs.core.MediaType.*;

/**
 * This class demonstrates a basic RESTful resource methods and annotations
 *
 * @author Trayan Iliev, IPT [http://iproduct.org]
 */
@Path("hello")
public class HelloResource {

    static final String MESSAGE_TEXT
            = "Hello BGJUG, from JAX-RS!";
    static final String MESSAGE_XML
            = "<?xml version=\"1.0\"?>"
            + "<hello>Hello BGJUG, from JAX-RS!</hello>";
    static final String MESSAGE_JSON
            = "{\"message\" : \"" + MESSAGE_TEXT + "\"}";
    static final String MESSAGE_HTML_START
            = "<html><title>Hello ";
    static final String MESSAGE_HTML_END
            = ", from JAX-RS!</title>"
            + "<body><h1>Hello BGJUG, from JAX-RS!</body></h1></html> ";
    
    private static final Map<Integer, String> students = new ConcurrentHashMap<>();

    @GET
    @Produces(TEXT_PLAIN)
    public String sayHelloText() {
        return MESSAGE_TEXT;
    }

    @GET
    @Produces(TEXT_XML)
    public String sayHelloXml() {
        return MESSAGE_XML;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public String sayHelloJson() {
        return MESSAGE_JSON;
    }

    @GET
    @Produces("text/html")
    @Path("{name}")
    public String sayHelloName(@PathParam("name") String name) {
        return MESSAGE_HTML_START + name + MESSAGE_HTML_END;
    }

}
