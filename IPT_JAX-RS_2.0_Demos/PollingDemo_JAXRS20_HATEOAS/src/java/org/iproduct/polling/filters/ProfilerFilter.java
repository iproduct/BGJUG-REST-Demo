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
 *
 */
package org.iproduct.polling.filters;

import com.sun.istack.internal.logging.Logger;
import com.sun.xml.ws.security.trust.util.WSTrustUtil;
import java.util.Date;
import java.util.logging.Level;
import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;


/**
 * 
 *
 * @author Trayan Iliev, IPT [http://iproduct.org]
 */

@Provider
//@PreMatching
@Priority(500)
@Profiled
public class ProfilerFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Logger.getLogger("Profiling Resource Request >>>>> ", ProfilerFilter.class)
            .log(Level.INFO, "Profiling Request >>>>>>> Method: " + requestContext.getMethod()
                + ", URI: " + requestContext.getUriInfo().getAbsolutePath()
            );
            requestContext.setProperty("startTime", (Long)System.currentTimeMillis());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        Logger.getLogger("Profiling Resource Request >>>>> ", ProfilerFilter.class)
            .log(Level.INFO, "Profiling Request >>>>>>> Method: " + requestContext.getMethod()
                + ", URI: " + requestContext.getUriInfo().getAbsolutePath()
                + ", Execution Time: " 
                + (System.currentTimeMillis() - ((Long)requestContext.getProperty("startTime"))) 
                + " ms" 
                );
    }

}
