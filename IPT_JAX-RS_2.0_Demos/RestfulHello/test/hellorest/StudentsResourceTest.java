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
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientConfig;
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
public class StudentsResourceTest {

    public static final String BASE_URI_STRING
            = "http://localhost:8084/hello/resources";
    public static final String USER_NAME = "RESTful BGJUG Programmer";
    public static final int[] FACULTY_NUMBERS = {
        62544, 68345, 67845, 863877, 12345
    };
    public static final String[] STUDENT_NAMES = {
        "Dimitar Gospodinov", "Samuil Petkov", "Simeon Nikolov",
        "Stefka Rakovska", "Zeliazko Kostadinov"
    };
    public static final int NEW_FACULTY_NUMBER = 654321;
    public static final String NEW_STUDENT_NAME = "Emilian Dimitrov";
    private static WebTarget baseTarget;

    @BeforeClass
    public static void setUpClass() {
        URI uri = UriBuilder.fromUri(BASE_URI_STRING).build();
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        baseTarget = client.target(uri);

        //create student records for testing
        for (int i = 0; i < FACULTY_NUMBERS.length; i++) {
            postStudent(FACULTY_NUMBERS[i], STUDENT_NAMES[i]);
        }
    }

    /**
     * Test of getStudent method, of class HelloResource.
     */
    @Test
    public void testGetStudentExisistingFN() {
        System.out.println("getStudentExisistingFN");
        int facultyNumber = FACULTY_NUMBERS[2];
        String studentName = STUDENT_NAMES[2];
        Response result = getStudent(facultyNumber);
        System.out.println(result);
        assertResponseContainsStudent(FACULTY_NUMBERS[2], STUDENT_NAMES[2], result);
    }

    /**
     * Test of getAllStudents method, of class HelloResource.
     */
    @Test
    public void testSayHelloAll() {
        System.out.println("getUser");
        for(int i = 0; i < FACULTY_NUMBERS.length; i++) {
            Response result = getStudent(FACULTY_NUMBERS[i]);
            assertResponseContainsStudent(FACULTY_NUMBERS[i], STUDENT_NAMES[i], result);
        }
    }

    /**
     * Test of addStudent method, of class HelloResource.
     */
    @Test
    public void testAddStudent() {
        System.out.println("addStudent");
        
        // Try to add new student
        boolean isException = false;
        try{
            postStudent(NEW_FACULTY_NUMBER, NEW_STUDENT_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            isException = true;
        }
        assertFalse("Posting new student not successful", isException);
        
        // Test student posted successfully by getting new student data from service
        // This is a functional not unit test and we are testing scenarios not methods! 
        Response result = getStudent(NEW_FACULTY_NUMBER);
        assertResponseContainsStudent(NEW_FACULTY_NUMBER, NEW_STUDENT_NAME, result);
    }

    /**
     * Test of addStudent method, of class HelloResource.
     */
    @Test
    public void testDeleteStudentExistingFN() {
        System.out.println("deleteStudentExistingFN");
        // Setup test by posting new student (should be created if does not exist)
        try{
            postStudent(NEW_FACULTY_NUMBER, NEW_STUDENT_NAME);
        } catch (Exception e) {}
        // Test setup (test preconditions) are successful by getting new student data from service
        // This is a functional not unit test and we are testing scenarios not methods! 
        Response result = getStudent(NEW_FACULTY_NUMBER);
        assertResponseContainsStudent(NEW_FACULTY_NUMBER, NEW_STUDENT_NAME, result);
        
        // Try to delete the created student
        Response resultResponse = null;
        boolean isException = false;
        try{
            resultResponse = deleteStudent(NEW_FACULTY_NUMBER);
            assertEquals("Staus code not 204 NO_CONTENT",
                204, resultResponse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            isException = true;
        }
        assertFalse("Deleting new student not successful", isException);
        
        // Test student deleted successfully by getting new student data from service
        // This is a functional not unit test and we are testing scenarios not methods! 
        result = getStudent(NEW_FACULTY_NUMBER);
        assertEquals("Student not deleted - status code different from 404 NOT_EXIST",
                404, result.getStatus());
    }

    // Helper methods
    private static Response postStudent(int facultyNumber, String name) {
        return baseTarget.path("students")
            .path(Integer.toString(facultyNumber)).request()
            .post(Entity.entity(name, TEXT_PLAIN));
        
    }

    private static Response deleteStudent(int facultyNumber) {
        return baseTarget.path("students")
            .path(Integer.toString(facultyNumber)).request()
            .delete();
    }

    private static Response getStudent(int facultyNumber) {
        return baseTarget.path("students")
            .path(Integer.toString(facultyNumber)).request(TEXT_HTML)
            .get();
    }

    private static Response getAllStudents() {
        return baseTarget.path("students")
            .request(TEXT_HTML).get();
    }

    private void assertResponseContainsStudent(Integer expectedFacultyNumber, 
            String expectedStudentName, 
            Response actualResponse) {
        String responseBodyHtml = actualResponse.readEntity(String.class);
        assertEquals("Staus code is not 200 OK as expected", 
            200, actualResponse.getStatus());
        assertNotNull("Get Student[" + expectedFacultyNumber + "] returns null response body",
            responseBodyHtml);
        assertThat("Faculty number not correct [" +expectedFacultyNumber + "] ",
            responseBodyHtml, containsString("<td>" +  expectedFacultyNumber + "</td>"));
        assertThat("Studen name not correct [" +expectedFacultyNumber + "] ",
            responseBodyHtml, containsString("<td>" + expectedStudentName + "</td>"));
    }


}
