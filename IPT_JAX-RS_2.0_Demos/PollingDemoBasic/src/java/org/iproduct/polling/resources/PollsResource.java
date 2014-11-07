/**
 * 
 */
package org.iproduct.polling.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.util.Collection;
import java.util.ConcurrentModificationException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.iproduct.polling.common.EntityDoesNotExistException;
import org.iproduct.polling.model.Poll;
import org.iproduct.polling.repository.Repository;
import org.iproduct.polling.repository.impl.PollRepositorySingleton;

/**
 * Resource class for {@link org.iproduct.polling.model.Poll Poll} resources
 * 
 * @author Trayan Iliev
 * @author IPT [http://iproduct.org]
 * 
 */
@Path("/polls")
public class PollsResource {
	
	private Repository<Long, Poll> pollRepository = PollRepositorySingleton.getInstance();
	
	@Context
	private UriInfo uriInfo;
	
	/**
	 * Get all available polls as a collection
	 * @return Collection of Poll JAXB XML/JSON representations
	 */
	@GET
	@Produces({APPLICATION_XML, APPLICATION_JSON})
	public Collection<Poll> getAllPolls(){
		return pollRepository.getAllItems();
	}
	
	/**
	 * Get range of available polls with identifiers statring from {@code fromId}, 
	 * and with maximal size of {@code numberItems}
	 * 
	 * @param fromId the next poll identifier to start from
	 * @param numberItems the maximal number of items to be returned
	 * @return Collection of Poll JAXB XML/JSON representations
	 */
	@GET
	@Path("/{fromId}/{maxSize}")
	@Produces({APPLICATION_XML, APPLICATION_JSON})
	public Collection<Poll> getRangePolls(
			@PathParam ("fromId") Long fromId, @PathParam ("maxSize") Integer maxSize){
		return pollRepository.getRangeItems(fromId, maxSize);
	}
	
	/**
	 * Get count of all available polls
	 * 
	 * @return polls total count as plain text string
	 */
	@GET
	@Path("/count")
	@Produces(TEXT_PLAIN)
	public String getPollsCount() {
		return pollRepository.getItemsCount();
	}
	
	/**
	 * Receive particular resource with given identifier or 
	 * status code 404 NOT_FOUND if the resource does not exist.
	 * @return Poll JAXB XML/JSON representation
	 */
	@GET
	@Path("/{id}")
	@Produces({APPLICATION_XML, APPLICATION_JSON})
	public Poll getPollById(@PathParam("id") Long id){
		Poll poll = pollRepository.getItemById(id);
		if (poll == null)
			throw new NotFoundException(
				Response.status(Response.Status.NOT_FOUND).type(TEXT_PLAIN)
				.entity("Entity with resourceId = " + id + " does not exist").build());
//			throw new NotFoundException("Entity with resourceId = " + id + " does not exist");
		return poll;
	}
	
	/**
	 * Create new resource with identifier automatically assigned 
	 * by polls container resource using 'Container-Item' RESTful resource pattern
	 * The status code returned by this method is 201 CREATED 
	 * with <a href="https://tools.ietf.org/html/rfc5988">RFC 5988 Link header</a>,
	 * and entity body containing representation
	 * of newly created resource with auto assigned identifier 
	 * <p>The method is not idempotent (see {@link #updatePoll(Long, Poll)} for a discussion)</p>

	 * @return HHTP response with entity body containing Poll JAXB XML/JSON representation
	 */
	@POST
	@Consumes({APPLICATION_XML, APPLICATION_JSON})
	public Response addPoll(Poll poll){
		poll = pollRepository.addItem(poll);
		Response response = Response.created(uriInfo.getAbsolutePath().resolve("/" + poll.getId()))
				.entity(poll)
				.build();
		return response;
	}
	
	/**
	 * Modifies existing resource identified by last path segment
	 * in the resource uri, which should be the same as the poll identifier 
	 * in the resource representation. 
	 * <ul>
	 * <li>If the poll identifier and uri segment 
	 * do not match an error response is returned with status code 400 BAD_REQUEST.</li>
	 * <li>If there is no resource with given identifier on the server, then response  
	 * 404 NOT_FOUND is returned. </li>
	 * <li>If the resource on the server has been concurrently modified by another 
	 * client (which is detected by comparing the previous ETag = hashcode of the resource 
	 * being updated with the currently computed for the resource one on the server)
	 * then error response 409 CONFLICT is returned</li>
	 * </ul>
	 * All above mentioned error responses are accompanied by entity body
	 * containing plain text message describing the problem.
	 * 
	 * <p>If the update has been successful 204 NO_CONTENT status code is
	 * returned with empty body (an opportunity described in 
	 * <a href"http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html">RFC 2616 Section 9</a>)</p>
	 * <p>The method is idempotent - meaning that it can be safely repeated multiple times by the client
	 * with no negative side effects (in contrast with POST method, which is not idempotent)
	 * @return HTTP Response 
	 */
	@PUT
	@Path("/{id}")
	@Consumes({APPLICATION_XML, APPLICATION_JSON})
	public Response updatePoll(@PathParam("id") Long id, Poll poll){
		Response response;
		if(id.equals(poll.getId())){
			try{
				poll = pollRepository.updateItem(poll, null);
				response = Response.noContent().build(); //More appropriate than 200OK
			} catch (ConcurrentModificationException e){
				response = Response.status(Response.Status.CONFLICT).type(TEXT_PLAIN).entity(e.getMessage()).build();
			} catch (EntityDoesNotExistException e){
				response = Response.status(Response.Status.NOT_FOUND).type(TEXT_PLAIN).entity(e.getMessage()).build();
			}
		} else {
			response = Response.status(Response.Status.BAD_REQUEST).type(TEXT_PLAIN).entity("Resource identifier " + id 
					+ " can not be changed to " + poll.getId() +". Resource identifiers are immutable.").build();
		}
		return response;
	}
	
	/**
	 * Deletes existing resource identified by last path segment
	 * in the resource uri
	 * <ul>
	 * <li>If there is no resource with given identifier on the server, then response  
	 * 404 NOT_FOUND is returned. </li>
	 * </ul>
	 * The above mentioned error response is accompanied by entity body
	 * containing plain text message describing the problem.
	 * 
	 * <p>If the DELETE has been successful 200 OK status code is
	 * returned with entity body containing the deleted entity representation (see 
	 * <a href"http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html">RFC 2616 Section 9</a> for more information)</p>
	 * <p>The method is not idempotent (see {@link #updatePoll(Long, Poll)} for a discussion)</p>
	 * @return HTTP Response 
	 */
	@DELETE
	@Path("/{id}")
	public Response deletePoll(@PathParam("id") Long id){
		Poll deletedItem;
		Response response;
		try{
			deletedItem = pollRepository.deleteItem(id);
			response = Response.ok().entity(deletedItem).build(); 
		} catch (EntityDoesNotExistException e){
			response = Response.status(Response.Status.NOT_FOUND).type(TEXT_PLAIN).entity(e.getMessage()).build();
		}
		return response;
	}
	
}
