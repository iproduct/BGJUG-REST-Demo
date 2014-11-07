/**
 * 
 */
package org.iproduct.polling.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.iproduct.polling.model.Poll;
import org.junit.After;
import org.junit.Test;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * Functional tests for {@link org.iproduct.polling.resources.PollsResource PollsResource} resources
 * 
 * @author Trayan Iliev, IPT [http://iproduct.org]
 * 
 */
public class PollsResourceTest extends BGJUGDemoBaseTest{
	private static final Poll[] SAMPLE_POLLS = {
		new Poll(1L, "Next BGJUG Presentation Topic", "Which topic is most interesting for you?"),
		new Poll(2L, "Choose JSON Hypermedia Serialization Format", "Which JSON hypermedia serialization format do you prefer?"),
		new Poll(3L, "Favourite Color Poll", "Which is your favourite color?"),
		new Poll(4L, "Favourite Pet", "Which pet would you prefer to care about?"),
		new Poll(5L, "JAVA Enterprise Application Platforms", "Which JAVA Enterprise Application Platform do you prefer?"),
		new Poll(6L, "JAVA Web Frameworks", "Which JAVA Web Framework is your fsvourite?"),		
	};
	private static final Long NONEXISTING_POLL_ID = 100000L;
	private static int EXISTING_POLL_INDEX  = 2;
	private static final String 
			TITLE_UPDATE = "Updated Title", 
			QUESTION_UPDATE = "Updated question";
	
	private List<Poll> toBeDeleted = new ArrayList<Poll>();
	
	/**
	 * Remove all posted polls during the test
	 */
	@After
	public void cleanPostedPolls() {
		toBeDeleted.stream().forEach(poll -> {
			try{
				target("polls").path(Long.toString(poll.getId())).request().delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Get all polls by using {@link org.iproduct.polling.resources.PollsResource#getAllPolls()} 
	 * in JSON format, and check that returned result is not null.
	 */
	@Test
	public void testGetAllPollsJSONReturnsNotNull() {
		List<Poll> allPolls = target("polls").request(APPLICATION_JSON).get(new GenericType<List<Poll>>(){});	
		assertNotNull(allPolls);
	}

	/**
	 * Get all polls by using {@link org.iproduct.polling.resources.PollsResource#getAllPolls()}
	 * in XML format, and check that returned result is not null.
	 */
	@Test
	public void testGetAllPollsXMLReturnsNotNull() {
		List<Poll> allPolls = target("polls").request(APPLICATION_XML).get(new GenericType<List<Poll>>(){});	
		assertNotNull(allPolls);
	}

	/**
	 * Tests that when given non-exisitng poll identified service returns 
	 * 404 NOT_FOUND with appropriate message. 
	 */
	@Test
	public void testGetPollByIdReturns404ForNonexisitingResource() {
		Response resp = target("polls").path(Long.toString(NONEXISTING_POLL_ID))
				.request(APPLICATION_JSON).get();
		assertEquals("Status code not 404", 404, resp.getStatus());
		assertThat("Response does not contain: '" + NONEXISTING_POLL_ID +  " does not exist'",
				resp.readEntity(String.class), containsString( NONEXISTING_POLL_ID +  " does not exist"));
	}

	/**
	 * Tests that when invoked with valid poll id REST service returns its 
	 * XML representation
	 */
	@Test
	public void testGetPollByIdReturnsPollXML() {
		// create some polls and return them with updated ids
		List<Poll> updatedPolls = setupResources(); 
		Poll testedPoll = updatedPolls.get(EXISTING_POLL_INDEX);
		
		Response response = target("polls").path(
				Long.toString(testedPoll.getId()))
				.request(APPLICATION_XML).get();
		assertEquals("Status code is not 200 OK", 200, response.getStatus());
		Poll responsePoll  = response.readEntity(Poll.class);
		assertEquals("Response does not contain correct title",
				testedPoll.getTitle(), responsePoll.getTitle());
		assertEquals("Response does not contain correct question", 
				testedPoll.getQuestion(), responsePoll.getQuestion());
	}

	/**
	 * Tests that when invoked with valid poll id REST service returns its 
	 * JSON representation
	 */
	@Test
	public void testGetPollByIdReturnsPollJSON() {
		// create some polls and return them with updated ids
		List<Poll> updatedPolls = setupResources(); 
		Poll testedPoll = updatedPolls.get(EXISTING_POLL_INDEX);
		
		Response response = target("polls").path(
				Long.toString(testedPoll.getId()))
				.request(APPLICATION_JSON).get();
		assertEquals("Status code is not 200 OK", 200, response.getStatus());
		Poll responsePoll  = response.readEntity(Poll.class);
		assertEquals("Response does not contain correct title",
				testedPoll.getTitle(), responsePoll.getTitle());
		assertEquals("Response does not contain correct question", 
				testedPoll.getQuestion(), responsePoll.getQuestion());
	}

	/**
	 * Test adding poll using XML marshaling 
	 * (method {@link org.iproduct.polling.resources.PollsResource#addPoll(org.iproduct.polling.model.Poll)}).
	 */
	@Test
	public void testAddPollXML() {
		Poll pollResponse = target("polls").request(APPLICATION_XML)
				.post(Entity.entity(SAMPLE_POLLS[0], APPLICATION_XML), Poll.class);
		assertNotNull("The POST response should not return null result", pollResponse);
		forCleanup(pollResponse);
		assertEquals("Poll title is different then posted", SAMPLE_POLLS[0].getTitle(), pollResponse.getTitle());
		assertEquals("Poll question is different then posted", SAMPLE_POLLS[0].getQuestion(), pollResponse.getQuestion());
	}

	/**
	 * Test adding poll using JSON marshaling 
	 * (method {@link org.iproduct.polling.resources.PollsResource#addPoll(org.iproduct.polling.model.Poll)}).
	 */
	@Test
	public void testAddPollJSON() {
		Poll pollResponse = target("polls").request(APPLICATION_JSON)
				.post(Entity.entity(SAMPLE_POLLS[0], APPLICATION_JSON), Poll.class);
		assertNotNull("The POST response should not return null result", pollResponse);
		forCleanup(pollResponse);
		assertEquals("Poll title is different then posted", SAMPLE_POLLS[0].getTitle(), pollResponse.getTitle());
		assertEquals("Poll question is different then posted", SAMPLE_POLLS[0].getQuestion(), pollResponse.getQuestion());
	}

	/**
	 * Test update poll with existing id scenario implemented by method 
	 * {@link org.iproduct.polling.resources.PollsResource#updatePoll(Long, Poll)}.
	 */
	@Test
	public void testUpdatePollWithExistingId() {
		// create some polls and return them with updated ids
		List<Poll> updatedPolls = setupResources(); 
		Poll testedPoll = updatedPolls.get(EXISTING_POLL_INDEX);
		Poll pollUpdate = new Poll(testedPoll.getId(), TITLE_UPDATE, QUESTION_UPDATE);
		
		// make PUT update to the server resource
		Response response = target("polls").path(Long.toString(pollUpdate.getId()))
				.request(APPLICATION_JSON)
				.put(Entity.entity(pollUpdate, APPLICATION_JSON));
		
		// test that correct response code is returned
		assertEquals("Status code is not 204 NO CONTENT", 204, response.getStatus());
		
		// try to get it and test the update was successful
		response = target("polls").path(Long.toString(pollUpdate.getId()))
				.request(APPLICATION_JSON).get();
		assertEquals("Status code is not 200 OK", 200, response.getStatus());
		Poll responsePoll  = response.readEntity(Poll.class);
		assertEquals("GET response does not contain correct title",
				pollUpdate.getTitle(),
				responsePoll.getTitle());
		assertEquals("GRT response does not contain correct question", 
				pollUpdate.getQuestion(),
				responsePoll.getQuestion());
			
	}

	/**
	 * Test update poll with existing id scenario implemented by method 
	 * {@link org.iproduct.polling.resources.PollsResource#updatePoll(Long, Poll)}.
	 */
	@Test
	public void testUpdatePollWithNonexistingId() {
		// create some polls and return them with updated ids
		List<Poll> updatedPolls = setupResources(); 
		Poll testedPoll = updatedPolls.get(EXISTING_POLL_INDEX);
		Poll pollUpdate = new Poll(NONEXISTING_POLL_ID, TITLE_UPDATE, QUESTION_UPDATE);

		Response resp = target("polls").path(Long.toString(NONEXISTING_POLL_ID)).request()
				.put(Entity.entity(pollUpdate, APPLICATION_JSON));
		assertEquals("After deletion status code not 404", 404, resp.getStatus());
		assertThat("Response does not contain: '" + NONEXISTING_POLL_ID +  " does not exist'",
				resp.readEntity(String.class), containsString( NONEXISTING_POLL_ID +  " does not exist"));
	}

	/**
	 * Test polls count is returned correctly implemented by method 
	 * {@link org.iproduct.polling.resources.PollsResource#getPollsCount()}.
	 */
	@Test
	public void testGetPollsCount() {
		// create some polls and return them with updated ids
		List<Poll> updatedPolls = setupResources(); 
		
		Response response = target("polls").path("count")
				.request(TEXT_PLAIN).get();
		assertEquals("Status code is not 200 OK", 200, response.getStatus());
		String responseStr  = response.readEntity(String.class);
		assertEquals("Count is not correct",
				updatedPolls.size(),
				Integer.parseInt(responseStr));
	}

	/**
	 * Test delete poll by existing id scenario implemented by method 
	 * {@link org.iproduct.polling.resources.PollsResource#deletePoll(Long)}.
	 */
	@Test
	public void testDeletePollById() {
		// create some polls and return them with updated ids
		List<Poll> updatedPolls = setupResources(); 
		Poll testedPoll = updatedPolls.get(EXISTING_POLL_INDEX);
		
		Response response = target("polls").path(Long.toString(testedPoll.getId()))
				.request(APPLICATION_JSON).delete();
		assertEquals("Status code is not 200 OK", 200, response.getStatus());
		Poll responsePoll  = response.readEntity(Poll.class);
		assertEquals("Response does not contain correct title",
				testedPoll.getTitle(),
				responsePoll.getTitle());
		assertEquals("Response does not contain correct question", 
				testedPoll.getQuestion(),
				responsePoll.getQuestion());
		
		// test poll was successfully deleted on the server
		Response resp = target("polls").path(Long.toString(NONEXISTING_POLL_ID))
				.request(APPLICATION_JSON).get();
		assertEquals("After deletion status code not 404", 404, resp.getStatus());
		assertThat("Response does not contain: '" + NONEXISTING_POLL_ID +  " does not exist'",
				resp.readEntity(String.class), containsString( NONEXISTING_POLL_ID +  " does not exist"));

	}
	
	/**
	 * Test delete poll by non-existing id scenario implemented by method 
	 * {@link org.iproduct.polling.resources.PollsResource#deletePoll(Long)}.
	 */
	@Test
	public void testDeletePollByNonExisitngId() {
		// create some polls and return them with updated ids
		List<Poll> updatedPolls = setupResources(); 
		
		Response response = target("polls").path(Long.toString(NONEXISTING_POLL_ID))
				.request(APPLICATION_JSON).delete();
		assertEquals("Status code is not 404", 404, response.getStatus());
		assertThat("Response does not contain: '" + NONEXISTING_POLL_ID +  " does not exist'",
				response.readEntity(String.class), 
				containsString( NONEXISTING_POLL_ID +  " does not exist"));
	}
	
	/**
	 * Test adding multiple polls and getting them all using {@link org.iproduct.polling.resources.PollsResource#getAllPolls()}
	 * complex scenario.
	 */
	@Test
	public void testPostTwoPollsAndGetAllPolls() {
		int initialCount = target("polls").path("count").request(TEXT_PLAIN).get(Integer.class);

		//add a Poll
		Poll pollResponse = target("polls").request(APPLICATION_XML)
				.post(Entity.entity(SAMPLE_POLLS[0], APPLICATION_XML), Poll.class);
		assertNotNull("The POST response should not return null result", pollResponse);
		forCleanup(pollResponse);
		assertEquals("Poll title is different then posted", SAMPLE_POLLS[0].getTitle(), pollResponse.getTitle());
		assertEquals("Poll question is different then posted", SAMPLE_POLLS[0].getQuestion(), pollResponse.getQuestion());
		
		//count should be incremented by 1
		int count1 = target("polls").path("count").request(TEXT_PLAIN).get(Integer.class);
		assertEquals("After ading poll count not incremented by 1", 1, count1-initialCount);
		
		//add another Poll
		Poll pollResponse2 = target("polls").request(APPLICATION_JSON)
				.post(Entity.entity(SAMPLE_POLLS[1], APPLICATION_JSON), Poll.class);
		assertNotNull("The POST response should not return null result", pollResponse);
		toBeDeleted.add(pollResponse2);
		assertEquals("Poll title is different then posted", SAMPLE_POLLS[1].getTitle(), pollResponse2.getTitle());
		assertEquals("Poll question is different then posted", SAMPLE_POLLS[1].getQuestion(), pollResponse2.getQuestion());
		
		//count should be incremented by 1
		int count2 = target("polls").path("count").request(TEXT_PLAIN).get(Integer.class);
		assertEquals("After ading poll count not incremented by 1", 1, count2-count1);
		assertTrue("There should be atleast two records created but " + count2 + " were reported", count2 >= 2);
		
		//get all polls and check that the two newly created polls are returned
		List<Poll> allPolls = target("polls").request(APPLICATION_JSON).get(new GenericType<List<Poll>>(){});
		
		assertTrue("Some created items: " + pollResponse + " are not returned by getAllPolls()", allPolls.contains(pollResponse));
		assertTrue("Some created items: " + pollResponse2 + " are not returned by getAllPolls()", allPolls.contains(pollResponse2));
	}

	
	/**
	 * REST resource setup for testing
	 * 
	 * @return list of updated polls with actual automatically assigned identifiers
	 */
	protected List<Poll> setupResources() {
		List<Poll> updatedPolls = Arrays.asList(SAMPLE_POLLS).stream()
		.map(poll -> {
			try{
				Poll updatedPoll = target("polls").request(APPLICATION_JSON)
					.post(Entity.entity(poll, APPLICATION_XML), Poll.class);
				if(updatedPoll != null)
					forCleanup(updatedPoll);
				return updatedPoll;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
		return updatedPolls;
	}
	
	/**
	 * Add a resource for automatic cleanup after test
	 * 
	 * @param poll poll to be deleted
	 */
	protected void forCleanup(Poll poll){
		toBeDeleted.add(poll);
	}


}
