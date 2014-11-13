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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientConfig;
import static org.hamcrest.CoreMatchers.containsString;

import org.iproduct.polling.entity.Poll;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Functional tests for {@link org.iproduct.polling.resources.PollsResource PollsResource} resources
 * 
 * @author Trayan Iliev, IPT [http://iproduct.org]
 * 
 */
public class PollsResourceTest {
    public static final String BASE_URI_STRING
        = "http://localhost:8080/polling/resources/polls";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static Poll[] SAMPLE_POLLS;

    private static final Long NONEXISTING_POLL_ID = 100000L;
    private static Poll NONEXISTING_POLL;

    private static final int EXISTING_POLL_INDEX  = 2;
    private static final String 
                    TITLE_UPDATE = "Updated Title", 
                    QUESTION_UPDATE = "Updated question";

    private final List<Poll> toBeDeleted = new ArrayList<Poll>();

    private static WebTarget target;

    @BeforeClass
    public static void setUpClass() throws ParseException{
        // Initialize base REST client and target
        URI uri = UriBuilder.fromUri(BASE_URI_STRING).build();
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        target = client.target(uri);
        
        // Initialize sample data
        SAMPLE_POLLS = new Poll[]{
            new Poll("Next BGJUG Presentation Topic", 
                "Which topic is most interesting for you?", 
                sdf.parse("07.11.2014 00:00"),
                sdf.parse("12.11.2014 00:00")
             ),
            new Poll("Favourite Color Poll", 
                "Which is your favourite color?", 
                sdf.parse("07.11.2014 00:00"),
                sdf.parse("12.11.2014 00:00")
            ),
            new Poll("Favourite Pet", 
                "Which pet would you prefer to care about?", 
                sdf.parse("07.11.2014 00:00"),
                sdf.parse("12.11.2014 00:00")
            ),
            new Poll("JAVA Enterprise Application Platforms", 
                "Which JAVA Enterprise Application Platform do you prefer?", 
                sdf.parse("07.11.2014 00:00"),
                sdf.parse("12.11.2014 00:00")
            ),
            new Poll("JAVA Web Frameworks", 
                "Which JAVA Web Framework is your fsvourite?", 
                sdf.parse("07.11.2014 00:00"),
                sdf.parse("12.11.2014 00:00")
            ),		
        };
        NONEXISTING_POLL = new Poll(
                NONEXISTING_POLL_ID, 
                "Non existing poll", 
                "Non exisiting question?", 
                sdf.parse("07.11.2014 00:00"),
                sdf.parse("12.11.2014 00:00"),
                new ArrayList<>()
            );
    }

    /**
     * Remove all posted polls during the test
     */
    @After
    public void cleanPostedPolls() {
        toBeDeleted.stream().forEach(poll -> {
            try{
                target.path(Long.toString(poll.getId())).request().delete();
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
            List<Poll> allPolls = target.request(APPLICATION_JSON).get(new GenericType<List<Poll>>(){});	
            assertNotNull(allPolls);
    }

    /**
     * Get all polls by using {@link org.iproduct.polling.resources.PollsResource#getAllPolls()}
     * in XML format, and check that returned result is not null.
     */
    @Test
    public void testGetAllPollsXMLReturnsNotNull() {
            List<Poll> allPolls = target.request(APPLICATION_XML).get(new GenericType<List<Poll>>(){});	
            assertNotNull(allPolls);
    }

    /**
     * Tests that when given non-exisitng poll identified service returns 
     * 404 NOT_FOUND with appropriate message. 
     */
    @Test
    public void testGetPollByIdReturns404ForNonexisitingResource() {
            Response resp = target.path(Long.toString(NONEXISTING_POLL_ID))
                            .request(APPLICATION_JSON).get();
            assertEquals("Status code not 404", 404, resp.getStatus());
            assertThat("Response does not contain espected string: '" ,
                resp.readEntity(String.class), containsString( 
                        NONEXISTING_POLL_ID + " does not exist"));
    }

    /**
     * Tests that when invoked with valid poll id REST service returns its 
     * XML representation
     */
    @Test
    public void testGetPollByIdReturnsPollXML() {
            // create some polls and return them with updated ids
            List<Poll> updatedPolls = setupResources(); 
//            List<Poll> updatedPolls = new ArrayList<>(); 
//            try {
//                updatedPolls = setupResourcesAsync();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(PollsResourceTest.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (ExecutionException ex) {
//                Logger.getLogger(PollsResourceTest.class.getName()).log(Level.SEVERE, null, ex);
//            }
            Poll testedPoll = updatedPolls.get(EXISTING_POLL_INDEX);

            Response response = target.path(
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

            Response response = target.path(
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
            Response resp = target.request(APPLICATION_XML)
                            .post(Entity.entity(SAMPLE_POLLS[0], APPLICATION_XML));
            assertEquals("Status code not 201 CREATED.", 201, resp.getStatus());
            Poll actualPoll =  resp.readEntity(Poll.class);
            assertNotNull("The POST response should not return null result", actualPoll);
            forCleanup(actualPoll);
            
            String actualLocation = resp.getHeaderString("Location");
            assertNotNull("Header Location missing", actualLocation);
            assertEquals("Location", target.getUriBuilder()
                .path(Long.toString(actualPoll.getId())).build().toString(), 
                actualLocation);
            
            assertEquals("Poll title is different then posted", 
                SAMPLE_POLLS[0].getTitle(), actualPoll.getTitle());
            assertEquals("Poll question is different then posted", 
                SAMPLE_POLLS[0].getQuestion(), actualPoll.getQuestion());
    }

    /**
     * Test adding poll using JSON marshaling 
     * (method {@link org.iproduct.polling.resources.PollsResource#addPoll(org.iproduct.polling.model.Poll)}).
     */
    @Test
    public void testAddPollJSON() {
            Response resp = target.request(APPLICATION_JSON)
                            .post(Entity.entity(SAMPLE_POLLS[0], APPLICATION_JSON));
            assertEquals("Status code not 201 CREATED.", 201, resp.getStatus());
            Poll actualPoll =  resp.readEntity(Poll.class);
            assertNotNull("The POST response should not return null result", actualPoll);
            forCleanup(actualPoll);
            
            String actualLocation = resp.getHeaderString("Location");
            assertNotNull("Header Location missing", actualLocation);
            assertEquals("Location", target.getUriBuilder()
                .path(Long.toString(actualPoll.getId())).build().toString(), 
                actualLocation);
            
            assertEquals("Poll title is different then posted", 
                SAMPLE_POLLS[0].getTitle(), actualPoll.getTitle());
            assertEquals("Poll question is different then posted", 
                SAMPLE_POLLS[0].getQuestion(), actualPoll.getQuestion());
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
            testedPoll.setTitle(TITLE_UPDATE);
            testedPoll.setQuestion(QUESTION_UPDATE);

            // make PUT update to the server resource
            Response response = target.path(Long.toString(testedPoll.getId()))
                            .request(APPLICATION_JSON)
                            .put(Entity.entity(testedPoll, APPLICATION_JSON));
            
            // test that correct response code is returned
            assertEquals("Status code is not 204 NO CONTENT", 204, response.getStatus());

            // try to get it and test the update was successful
            response = target.path(Long.toString(testedPoll.getId()))
                            .request(APPLICATION_JSON).get();
            assertEquals("Status code is not 200 OK", 200, response.getStatus());
            Poll responsePoll  = response.readEntity(Poll.class);
            assertEquals("GET response does not contain correct title",
                            testedPoll.getTitle(),
                            responsePoll.getTitle());
            assertEquals("GRT response does not contain correct question", 
                            testedPoll.getQuestion(),
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

            //Call REST service
            Response resp = target.path(Long.toString(NONEXISTING_POLL_ID)).request()
                .put(Entity.entity(NONEXISTING_POLL, APPLICATION_JSON));
            
            //Assert
            assertEquals("After update status code not 404", 404, resp.getStatus());
            assertThat("Response does not contain espected string: '" ,
                resp.readEntity(String.class), containsString( 
                        NONEXISTING_POLL_ID + " does not exist"));
    }

    /**
     * Test polls count is returned correctly implemented by method 
     * {@link org.iproduct.polling.resources.PollsResource#getPollsCount()}.
     */
    @Test
    public void testGetPollsCount() {
            // get the count before adding new polls
            Response beforeResponse = target.path("count")
                            .request(TEXT_PLAIN).get();
            assertEquals("Status code is not 200 OK", 200, beforeResponse.getStatus());
            int before  = Integer.parseInt(beforeResponse.readEntity(String.class));

            // create some polls and return them with updated ids
            List<Poll> updatedPolls = setupResources(); 

            // get the count after adding new polls
            Response afterResponse = target.path("count")
                            .request(TEXT_PLAIN).get();
            assertEquals("Status code is not 200 OK", 200, afterResponse.getStatus());
            int after  = Integer.parseInt(afterResponse.readEntity(String.class));
            
            // Assert difference is equal to number of new polls added
            assertEquals("Count is not correct", updatedPolls.size(),
                            after - before);
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

            Response response = target.path(Long.toString(testedPoll.getId()))
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
            Response resp = target.path(Long.toString(NONEXISTING_POLL_ID))
                            .request(APPLICATION_JSON).get();
            assertEquals("After deletion status code not 404", 404, resp.getStatus());
            assertThat("Response does not contain espected string: '" ,
                resp.readEntity(String.class), containsString( 
                        NONEXISTING_POLL_ID + " does not exist"));

    }

    /**
     * Test delete poll by non-existing id scenario implemented by method 
     * {@link org.iproduct.polling.resources.PollsResource#deletePoll(Long)}.
     */
    @Test
    public void testDeletePollByNonExisitngId() {
            // create some polls and return them with updated ids
            List<Poll> updatedPolls = setupResources(); 

            Response resp = target.path(Long.toString(NONEXISTING_POLL_ID))
                            .request(APPLICATION_JSON).delete();
            assertEquals("Status code is not 404", 404, resp.getStatus());
            assertThat("Response does not contain espected string: '" ,
                resp.readEntity(String.class), containsString( 
                        NONEXISTING_POLL_ID + " does not exist"));
   }

    /**
     * Test adding multiple polls and getting them all using {@link org.iproduct.polling.resources.PollsResource#getAllPolls()}
     * complex scenario.
     */
    @Test
    public void testPostTwoPollsAndGetAllPolls() {
            int initialCount = target.path("count").request(TEXT_PLAIN).get(Integer.class);

            //add a Poll
            Poll pollResponse = target.request(APPLICATION_XML)
                            .post(Entity.entity(SAMPLE_POLLS[0], APPLICATION_XML), Poll.class);
            assertNotNull("The POST response should not return null result", pollResponse);
            forCleanup(pollResponse);
            assertEquals("Poll title is different then posted", SAMPLE_POLLS[0].getTitle(), pollResponse.getTitle());
            assertEquals("Poll question is different then posted", SAMPLE_POLLS[0].getQuestion(), pollResponse.getQuestion());

            //count should be incremented by 1
            int count1 = target.path("count").request(TEXT_PLAIN).get(Integer.class);
            assertEquals("After ading poll count not incremented by 1", 1, count1-initialCount);

            //add another Poll
            Poll pollResponse2 = target.request(APPLICATION_JSON)
                            .post(Entity.entity(SAMPLE_POLLS[1], APPLICATION_JSON), Poll.class);
            assertNotNull("The POST response should not return null result", pollResponse);
            toBeDeleted.add(pollResponse2);
            assertEquals("Poll title is different then posted", SAMPLE_POLLS[1].getTitle(), pollResponse2.getTitle());
            assertEquals("Poll question is different then posted", SAMPLE_POLLS[1].getQuestion(), pollResponse2.getQuestion());

            //count should be incremented by 1
            int count2 = target.path("count").request(TEXT_PLAIN).get(Integer.class);
            assertEquals("After ading poll count not incremented by 1", 1, count2-count1);
            assertTrue("There should be atleast two records created but " + count2 + " were reported", count2 >= 2);

            //get all polls and check that the two newly created polls are returned
            List<Poll> allPolls = target.request(APPLICATION_JSON).get(new GenericType<List<Poll>>(){});

            assertTrue("Some created items: " + pollResponse + " are not returned by getAllPolls()", allPolls.contains(pollResponse));
            assertTrue("Some created items: " + pollResponse2 + " are not returned by getAllPolls()", allPolls.contains(pollResponse2));
    }


    /**
     * REST resource setup for testing
     * 
     * @return list of updated polls with actual automatically assigned identifiers
     */
    protected List<Poll> setupResources(){
        List<Poll> updatedPolls = Arrays.asList(SAMPLE_POLLS).stream()
        .map(poll -> {
            try{
                Poll updatedPoll = target.request(APPLICATION_JSON)
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
     * REST resource setup for testing - async implementation
     * 
     * @return list of updated polls with actual automatically assigned identifiers
     */
    protected List<Poll> setupResourcesAsync() throws InterruptedException, ExecutionException{
        List<Future<Poll>> updateFutures = Arrays.asList(SAMPLE_POLLS).stream()
        .map(poll -> {
            Future<Poll> updateFuture = target.request(APPLICATION_JSON)
                .async()
                .post(Entity.entity(poll, APPLICATION_XML), 
                    new InvocationCallback<Poll>(){
                        @Override
                        public void completed(Poll updatedPoll) {
                            forCleanup(updatedPoll);
                        }
                        @Override
                        public void failed(Throwable throwable) {
                        }                            
                });
            return updateFuture;
        }).collect(Collectors.toList());
        do{
            Thread.sleep(100);
        } while(! updateFutures.stream().allMatch((Future<Poll> fp) -> fp.isDone()));
        List<Poll> results = new ArrayList<>();
        for(Future<Poll> fp: updateFutures){
            Poll resPoll = fp.get();
            if(resPoll != null)
                results.add(resPoll);
        }  
        return results;
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
