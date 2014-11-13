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

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import javax.ws.rs.core.UriBuilder;

/**
 * This class demonstrates a basic RESTful resource methods and annotations
 *
 * @author Trayan Iliev, IPT [http://iproduct.org]
 */
@Path("students")
public class StudentsResource {
    private static final Map<Integer, String> students = new ConcurrentHashMap<>();

    @GET
    @Produces("text/html")
    @Path("{fn}")
    public String getStudent(@PathParam("fn") Integer facultyNumber) {
        Response response;
        String studentName = students.get(facultyNumber);
        if(studentName == null ){
             throw new WebApplicationException("Student with FN = " + facultyNumber
                    +" does not exist.", NOT_FOUND);
        }
        return "<table><tr><th>FN</th><th>Name</th></tr>"
                +"<tr><td>" + facultyNumber + "</td><td>"
                + students.get(facultyNumber) + "</td></tr></table>";
    }

    @GET
    @Produces("text/html")
    @Path("students")
    public String getAllStudents() {
        StringBuilder sb = new StringBuilder("<h1>BGJUG RestfulHello Demo - List of All Students</h1>");
        sb.append("<table><tr><th>FN</th><th>Name</th></tr>");
        for (Integer facultyNumber : students.keySet()) {
            sb.append("<tr><td>").append(facultyNumber).append("</td><td>")
                .append(students.get(facultyNumber)).append("</td></tr>");
        }
        sb.append("<table>");
        return sb.toString();
    }

    @POST
    @Consumes("text/plain")
    @Path("{fn}")
    public Response addStudent(@PathParam("fn") Integer facultyNumber, String userName) {
        students.put(facultyNumber, userName);
        System.out.println("Studens now: " + students);
        URI newUserURI = UriBuilder.fromResource(StudentsResource.class)
                .path("{facultyNumber}").build(facultyNumber);
        return Response.created(newUserURI).build();
    }

    @DELETE
    @Consumes("text/plain")
    @Path("{fn}")
    public Response deleteStudent(@PathParam("fn") Integer facultyNumber) {
        String removedStudentName = students.remove(facultyNumber);
        Response response;
        if(removedStudentName != null){
                System.out.println("Student with " + removedStudentName
                + ": " + facultyNumber + " removed succesfully.");
                response = Response.noContent().build();
        }
        if(removedStudentName != null){
                System.out.println("Student with " + removedStudentName
                + ": " + facultyNumber + " removed succesfully.");
                response = Response.noContent().build();
        } else {
       
                System.out.println("Student with FN = " + facultyNumber
                    + " does not exist.");
                response = Response.noContent().build();
        }
        return response;
    }

}
