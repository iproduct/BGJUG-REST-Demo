/**
 *
 */
package org.iproduct.polling.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import javax.ws.rs.core.UriInfo;
import org.iproduct.polling.controller.AlternativeController;

import org.iproduct.polling.entity.Alternative;
import org.iproduct.polling.entity.Poll;
import org.iproduct.polling.jpacontroller.exceptions.IllegalOrphanException;
import org.iproduct.polling.jpacontroller.exceptions.NonexistentEntityException;
import org.iproduct.polling.jpacontroller.exceptions.RollbackFailureException;

/**
 * Resource class for {@link org.iproduct.polling.model.Alternative Alternative} resources
 *
 * @author Trayan Iliev
 * @author IPT [http://iproduct.org]
 *
 */
@Path("/alternatives")
public class AlternativesResource {
    private Long pollId;
    private Poll poll;
    private UriInfo uriInfo;

    @Inject 
    private AlternativeController altrnativeController;

    /**
     * Public constructor with parent Poll resource identifier as argument
     * @param pollId the parent Poll resource identifier
     * @param parent the parent Poll entity
     * @param uriInfo the UriInfo instance from parent resource
     */
    public AlternativesResource(Long pollId, Poll parent, UriInfo uriInfo){
        this.pollId = pollId;
        this.poll = parent;
        this.uriInfo = uriInfo;
    }           
 
    /**
     * Get all available polls as a collection
     *
     * @return Collection of Alternative JAXB XML/JSON representations
     */
    @GET
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Collection<Alternative> getAllAlternatives() {
        return altrnativeController.findAlternativeEntitiesByPollId(pollId);
    }

    /**
     * Get range of available polls with identifiers statring from
     * {@code fromId}, and with maximal size of {@code numberItems}
     *
     * @param firstResult the sequential number of the first poll to be returned
     * @param maxResults the maximal number of polls to be returned
     * @return Collection of Alternative JAXB XML/JSON representations
     */
    @GET
    @Path("/range")
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Collection<Alternative> getAlternativesRange(
        @QueryParam("first") int firstResult, @QueryParam("max") int maxResults) {
        return altrnativeController.findAlternativeEntitiesByPollId(pollId, maxResults, firstResult);
    }

    /**
     * Get count of all available polls
     *
     * @return polls total count as plain text string
     */
    @GET
    @Path("/count")
    @Produces(TEXT_PLAIN)
    public String getAlternativesCount() {
        return Integer.toString(altrnativeController.getAlternativeCount());
    }

    /**
     * Receive particular resource with given identifier or status code 404
     * NOT_FOUND if the resource does not exist.
     *
     * @param id the poll identifier
     * @return Alternative JAXB XML/JSON representation
     */
    @GET
    @Path("/{id}")
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Alternative getAlternativeById(@PathParam("id") Long id) {
        Alternative poll = altrnativeController.findAlternative(id);
        if (poll == null) {
            throw new WebApplicationException("Alternative with Id = " 
                    + id + " does not exist", NOT_FOUND);
        }
//			throw new NotFoundException("Entity with resourceId = " + id + " does not exist");
        return poll;
    }

    /**
     * Create new resource with identifier automatically assigned by polls
     * container resource using 'Container-Item' RESTful resource pattern The
     * status code returned by this method is 201 CREATED with
     * <a href="https://tools.ietf.org/html/rfc5988">RFC 5988 Link header</a>,
     * and entity body containing representation of newly created resource with
     * auto assigned identifier
     * <p>
     * The method is not idempotent (see {@link #updateAlternative(Long, Alternative)} for a
     * discussion)</p>
     *
     * @param alt the new alternative to be added
     * @return HHTP response with entity body containing Alternative JAXB XML/JSON
     * representation
     */
    @POST
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    public Response addAlternative(Alternative alt) {
        try {
            altrnativeController.create(pollId, alt);
        } catch (Exception ex) {
            Logger.getLogger(AlternativesResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(ex, Response.Status.BAD_REQUEST);
        }
        Response response = Response.created(uriInfo.getAbsolutePath().resolve("/" + poll.getId()))
                .entity(poll)
                .build();
        return response;
    }

    /**
     * Modifies existing resource identified by last path segment in the
     * resource uri, which should be the same as the poll identifier in the
     * resource representation.
     * <ul>
     * <li>If the poll identifier and uri segment do not match an error response
     * is returned with status code 400 BAD_REQUEST.</li>
     * <li>If there is no resource with given identifier on the server, then
     * response 404 NOT_FOUND is returned. </li>
     * <li>If the resource on the server has been concurrently modified by
     * another client (which is detected by comparing the previous ETag =
     * hashcode of the resource being updated with the currently computed for
     * the resource one on the server) then error response 409 CONFLICT is
     * returned</li>
     * </ul>
     * All above mentioned error responses are accompanied by entity body
     * containing plain text message describing the problem.
     *
     * <p>
     * If the update has been successful 204 NO_CONTENT status code is returned
     * with empty body (an opportunity described in
     * <a href"http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html">RFC 2616
     * Section 9</a>)</p>
     * <p>
     * The method is idempotent - meaning that it can be safely repeated
     * multiple times by the client with no negative side effects (in contrast
     * with POST method, which is not idempotent)
     *
     * @param id poll identifier
     * @param poll poll entity to be updated
     * @return HTTP Response
     */
    @PUT
    @Path("/{id}")
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    public Response updateAlternative(@PathParam("id") Long id, Alternative poll) {
        Response response;
        if (id.equals(poll.getId())) {
            try {
                altrnativeController.edit(poll); //TODO Etag comaprison
                response = Response.noContent().build(); //More appropriate than 200OK
            } catch (ConcurrentModificationException e) {
                response = Response.status(Response.Status.CONFLICT)
                    .type(TEXT_PLAIN).entity(e.getMessage()).build();
            } catch (NonexistentEntityException e) {
                response = Response.status(Response.Status.NOT_FOUND)
                    .type(TEXT_PLAIN).entity(e.getMessage()).build();
            } catch(IllegalOrphanException | RollbackFailureException e){
                response = Response.status(Response.Status.BAD_REQUEST)
                    .type(TEXT_PLAIN).entity(e.getMessage()).build();
            } catch (Exception e){
                Logger.getLogger(AlternativesResource.class.getName())
                    .log(Level.SEVERE, null, e);
                throw new WebApplicationException(e, BAD_REQUEST);
            }
        } else {
            response = Response.status(Response.Status.BAD_REQUEST)
                    .type(TEXT_PLAIN).entity("Resource identifier " + id
                    + " can not be changed to " + poll.getId() 
                    + ". Resource identifiers are immutable.").build();
        }
        return response;
    }

    /**
     * Deletes existing resource identified by last path segment in the resource
     * uri
     * <ul>
     * <li>If there is no resource with given identifier on the server, then
     * response 404 NOT_FOUND is returned. </li>
     * </ul>
     * The above mentioned error response is accompanied by entity body
     * containing plain text message describing the problem.
     *
     * <p>
     * If the DELETE has been successful 200 OK status code is returned with
     * entity body containing the deleted entity representation (see
     * <a href"http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html">RFC 2616
     * Section 9</a> for more information)</p>
     * <p>
     * The method is not idempotent (see {@link #updateAlternative(Long, Alternative)} for a
     * discussion)</p>
     *
     * @param id the poll identifier
     * @return HTTP Response
     */
    @DELETE
    @Path("/{id}")
    public Response deleteAlternative(@PathParam("id") Long id) {
        Alternative deletedItem;
        Response response;
        try {
            deletedItem = altrnativeController.findAlternative(id);
            altrnativeController.destroy(id);
            response = Response.ok().entity(deletedItem).build();
            } catch (NonexistentEntityException e) {
                response = Response.status(Response.Status.NOT_FOUND)
                    .type(TEXT_PLAIN).entity(e.getMessage()).build();
            } catch(IllegalOrphanException | RollbackFailureException e){
                response = Response.status(Response.Status.BAD_REQUEST)
                    .type(TEXT_PLAIN).entity(e.getMessage()).build();
            } catch (Exception e){
                Logger.getLogger(AlternativesResource.class.getName())
                    .log(Level.SEVERE, null, e);
                throw new WebApplicationException(e, BAD_REQUEST);
            }
        return response;
    }

}
