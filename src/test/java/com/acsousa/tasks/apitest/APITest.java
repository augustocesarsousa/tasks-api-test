package com.acsousa.tasks.apitest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        return dateFormat.format(Date.from(Instant.now()));
    }

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8001/tasks-backend";
    }
    
    @Test
    public void shouldReturnTasks() {
        RestAssured
            .given()
            .when()
                .get("/todo")
            .then()
                .statusCode(200);
    }

    @Test
    public void shouldSaveTaskWhenSendingCorrectDatas(){
        String date = getDate();
        RestAssured
            .given()
                .body("{\"task\": \"teste RestAssured\",\"dueDate\": \""+date+"\"}")
                .contentType(ContentType.JSON)
            .when()
                .post("/todo")
            .then()
                .statusCode(201);
    }

    @Test
    public void shouldNotSaveTaskWithPastDate(){
        RestAssured
            .given()
                .body("{\"task\": \"teste RestAssured\",\"dueDate\": \"2020-12-21\"}")
                .contentType(ContentType.JSON)
            .when()
                .post("/todo")
            .then()
                .statusCode(400)
                .body("message", CoreMatchers.is("Due date must not be in past"));
    }
}