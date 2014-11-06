/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hellorest;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author admin
 */
@Path("hello")
public class HelloResource {
    private static List<String> users = new ArrayList<String>();
   
    @GET
    @Produces("text/plain")
    public String sayHello(){
        return "Hello from RESTful Service!";
    }
    
    @GET
    @Produces("text/html")
    @Path("{name}")
    public String sayHelloName(@PathParam("name") String name){
        return "<h1>Hello " + name + ", from RESTful Service!</h1>";
    }
    
    @GET
    @Produces("text/html")
    @Path("user/{name}")
    public String getUser(@PathParam("name") String name){
        return "<h1>Hello " + name + ", from RESTful Service!</h1>";
    }
    
    @GET
    @Produces("text/html")
    @Path("all")
    public String sayHelloAll(){
        StringBuilder sb = new StringBuilder("<h1>Hello from RESTful Service to </h1><ul>");
        for(String user: users){
            sb.append("<li>").append(user).append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
    
    @POST
    @Consumes("text/plain")
    @Path("user")
    public Response addUser(String userName){
        users.add(userName);
        System.out.println(users);
        URI newUserURI = UriBuilder.fromResource(HelloResource.class)
                .path("user/{name}").build(userName);
        return Response.created(newUserURI).build();
    }
    
}
