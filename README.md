# WEB_QUIZ_ENGINE-hyperskill-project

Short description: 
REST API using HTTP Basic Auth, where you can create Quizzes and solve them. Spring JPA and H2 had been used to store data. There is User repository (you can register), Quiz repository and Completion repository. GET posiibilities: Get all quizzes, Get one quiz, Get your quizzes completions with data/time (all Get are with pagination). POST: create quizz (with conditions), solve quiz, register. DELETE - deletes quiz. 
To perform any operation (except register) the user has to be registered and authorised via HTTP Basic Auth by sending their email and password for each request. Password is encoded with BCrypt.


Below description of every stage of buliding the project.

FIRST STAGE:

About

In the Internet, you can often find sites where you need to answer questions: educational sites, sites with psychological tests, job search services, or just entertaining sites like web quests. Something they all have in common is that they permit to answer questions (or quizzes) and then see the results.

In this project, you will develop a multi-user web service for creating and solving quizzes using REST API, an embedded database, security, and other technologies. Here we will concentrate on the server side ("engine") without a user interface at all. The project stages are described in terms of the client-server model, where the client can be a browser, the curl tool, a REST client (like postman) or something else.

During the development of the web service, you will probably have to do some Google searching and additional reading. This is a normal situation, just read a few articles when implementing stages.

After you complete this project, you will have a clear understanding of backend development. You'll also know how to combine various modern technologies to get a great result. If you continue the work on the project, you can also develop a web/mobile client for this web service.

Description

At the first stage, you need to develop a simple JSON API that always returns the same quiz to be solved. The API should support only two operations: getting the quiz and solving it by passing an answer. Each operation is described in more detail below.

Once the stage is completed, you will have a working web service with an comprehensive API.

SECOND STAGE:

Description

At this stage, you will improve the web service to create, get and solve lots of quizzes, not just a single one. All quizzes should be stored in the service's memory, without an external storage.

The format of requests and responses will be similar to the first stage, but you will make the API more REST-friendly and extendable. Each of the four possible operations is described below.

To complete this stage, you may read about some Jackson serializer properties for ignoring fields. But this is not the only way to solve this stage.
Create a new quiz

To create a new quiz, the client needs to send a JSON as the request's body via POST to /api/quizzes. The JSON should contain the four fields: title (a string), text (a string), options (an array of strings) and answer (integer index of the correct option). At this moment, all the keys are optional.

Here is a new JSON quiz as an example:

{
  "title": "The Java Logo",
  "text": "What is depicted on the Java logo?",
  "options": ["Robot","Tea leaf","Cup of coffee","Bug"],
  "answer": 2
}

The answer equals 2 corresponds to the third item from the options array ("Cup of coffee").

The server response is a JSON with four fields: id, title, text and options. Here is an example.

{
  "id": 1,
  "title": "The Java Logo",
  "text": "What is depicted on the Java logo?",
  "options": ["Robot","Tea leaf","Cup of coffee","Bug"]
}

The id field is a generated unique integer identifier for the quiz. Also, the response may or may not include the answer field depending on your wishes. This is not very important for this operation.

At this moment, it is admissible if a creation request does not contain some quiz data. In the next stages, we will improve the service to avoid some server errors.
Get a quiz by id

To get a quiz by id, the client sends the GET request to /api/quizzes/{id}.

Here is a response example:

{
  "id": 1,
  "title": "The Java Logo",
  "text": "What is depicted on the Java logo?",
  "options": ["Robot","Tea leaf","Cup of coffee","Bug"]
}

The response must not include the answer field, otherwise, any user will be able to find the correct answer for any quiz.

If the specified quiz does not exist, the server should return the 404 (Not found) status code.
Get all quizzes

To get all existing quizzes in the service, the client sends the GET request to /api/quizzes.

The response contains a JSON array of quizzes like the following:

[
  {
    "id": 1,
    "title": "The Java Logo",
    "text": "What is depicted on the Java logo?",
    "options": ["Robot","Tea leaf","Cup of coffee","Bug"]
  },
  {
    "id": 2,
    "title": "The Ultimate Question",
    "text": "What is the answer to the Ultimate Question of Life, the Universe and Everything?",
    "options": ["Everything goes right","42","2+2=4","11011100"]
  }
]

The response must not include the answer field, otherwise, any user will be able to find the correct answer for any quiz.

If there are no quizzes, the service returns an empty JSON array: [].

In both cases, the status code is 200 (OK).
Solving a quiz

To solve the quiz, the client sends a POST request to /api/quizzes/{id}/solve and passes the answer parameter in the content. This parameter is the index of a chosen option from options array. As before, it starts from zero.

The service returns a JSON with two fields: success (true or false) and feedback (just a string). There are three possible responses.

    If the passed answer is correct (e.g., POST to /api/quizzes/1/solve with content answer=2):

{"success":true,"feedback":"Congratulations, you're right!"}

    If the answer is incorrect (e.g., POST to /api/quizzes/1/solve with content answer=1):

{"success":false,"feedback":"Wrong answer! Please, try again."}

    If the specified quiz does not exist, the server returns the 404 (Not found) status code.

You can write any other strings in the feedback field, but the names of fields and the true/false values must match this example.

THIRD STAGE:

Description

Currently, your service allows creating new quizzes, but there may be problems if the client didn't provide all the quiz data. In such cases, the service will create an incorrect unsolvable quiz which is very frustrating for those who are trying to solve it.

At this stage, you should fix this so that the service does not accept incorrect quizzes. Another task is to make quizzes more interesting by supporting the arbitrary number of correct options (from zero to all). It means that to solve a quiz, the client needs to send all correct options at once, or zero if all options are wrong.

Here is a few resources where you can read how to validate data in the API:

    Bean validation with Spring Boot
    Spring Boot bean validation

There are only two modified operations for creating and solving quizzes. All other operations should not be changed or deleted.
Create a new quiz

To create a new quiz, the client needs to send a JSON as the request's body via POST to /api/quizzes. The JSON should contain the four fields:

    title: a string, required;
    text: a string, required;
    options: an array of strings, required, should contain at least 2 items;
    answer: an array of indexes of correct options, optional, since all options can be wrong.

Here is a new JSON quiz as an example:

{
  "title": "Coffee drinks",
  "text": "Select only coffee drinks.",
  "options": ["Americano","Tea","Cappuccino","Sprite"],
  "answer": [0,2]
}

The answer equals [0,2] corresponds to the first and the third item from the options array ("Americano" and "Cappuccino").

The server response is a JSON with four fields: id, title, text and options. Here is an example:

{
  "id": 1,
  "title": "Coffee drinks",
  "text": "Select only coffee drinks.",
  "options": ["Americano","Tea","Cappuccino","Sprite"]
}

The id field is a generated unique integer identifier for the quiz. Also, the response may or may not include the answer field depending on your wishes. This is not very important for this operation.

If the request JSON does not contain title or text, or they are empty strings (""), then the server should respond with the 400 (Bad request) status code. If the number of options in the quiz is less than 2, the server returns the same status code.
Solving a quiz

To solve a quiz, the client sends the POST request to /api/quizzes/{id}/solve with a JSON that contains the indexes of all chosen options as the answer. This looks like a regular JSON object with key "answer" and value as the array: {"answer": [0,2]}. As before, indexes start from zero.

It is also possible to send an empty array [] since some quizzes may not have correct options.

The service returns a JSON with two fields: success (true or false) and feedback (just a string). There are three possible responses.

    If the passed answer is correct:

{"success":true,"feedback":"Congratulations, you're right!"}

    If the answer is incorrect:

{"success":false,"feedback":"Wrong answer! Please, try again."}

    If the specified quiz does not exist, the server returns the 404 (Not found) status code.

You can write any other strings in the feedback field, but the names of fields and the true/false values must match this example.

FORTH STAGE

Description

At this stage, you will permanently store the data in a database, so that after restarting the service you will not lose all quizzes created by the users. You don't need to change the API of your service at this stage.

Here are some articles that explain how to work with databases in Spring Boot:

    Spring Data JPA with H2 database
    Sprint Boot CRUD REST API + Spring Data JPA + H2 DB Example

If that doesn't feel like enough, you can google other articles on the same topics (Spring Data, Repositories, JPA, H2).

We recommend you use the H2 database in the disk-based storage mode (not in-memory).

To start working with it, just add a couple of new dependencies in your build.gradle file:

dependencies {
    // ...
    runtimeOnly 'com.h2database:h2'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    // ...
}

The first dependency will allow using the H2 database in your application, and the second will allow using Spring Data JPA.

You also need to configure the database inside the application.properties file. Do not change the database path.

spring.datasource.url=jdbc:h2:file:../quizdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

spring.h2.console.enabled=true
spring.h2.console.settings.trace=false
spring.h2.console.settings.web-allow-others=false

This config will automatically create the database and update tables inside it.

You should use exactly the same name for DB: quizdb.

If you want to see SQL statements generated by Spring ORM, just add the following line in the properties file:

spring.jpa.show-sql=true

To start using this database, you need to map your classes to database tables using the JPA annotations and Spring Repositories.

You can use any tables in your database to complete this stage. The main thing is that when you restart the service, quizzes should not be lost. Our tests will create and get them via the API developed at the previous stages.

FIFT STAGE:

Description

Your service already has a well-designed API and stores all the quizzes in the database. At this stage, you will improve the service to support users and the authorization process. This will allow you to provide different privileges to the users and understand what do they do in the service.

Here are two operations to be added:

    register a new user, which accepts an email as the login and a password;
    deleting a quiz created by the current user.

All the previously developed operations should not be changed. As before, when creating a new quiz, the service checks the following rules: the fields title and text exist and they are not empty, and the options array has two or more items. If at least one of these conditions is not satisfied, the service returns the 400 (Bad request) status code. As before, server responses for getting quizzes should not include answers for the quizzes.

For the testing reasons, make POST /actuator/shutdown endpoint accessible without authentication.

The main change is the accessibility of these operations. Now, to perform any operations with quizzes (create, solve, get one, get all, delete), the user has to be registered and then authorized via HTTP Basic Auth by sending their email and password for each request. Otherwise, the service returns the 401 (Unauthorized) status code. Thus, the only operation that does not require authorization is the registration.

Here are some articles about spring security:

    securing rest services;
    security rest basic auth example;
    spring boot and basic authentication;

Do not store the actual password in the database! Instead, configure password encryption using BCrypt or some other algorithm via Spring Security.

Register a user

To register a new user, the client needs to send a JSON with email and password via POST request to /api/register:

{
  "email": "test@gmail.com",
  "password": "secret"
}

The service returns 200 (OK) status code if the registration has been completed successfully.

If the email is already taken by another user, the service will return the 400 (Bad request) status code.

Here are some additional restrictions to the format of user credentials:

    the email must have a valid format (with @ and .);
    the password must have at least five characters.

If any of them is not satisfied, the service will also return the 400 (Bad request) status code.

All the following operations need a registered user to be successfully completed.
Delete a quiz

A user can delete their quiz by sending the DELETE request to /api/quizzes/{id}.

If the operation was successful, the service returns the 204 (No content) status code without any content.

If the specified quiz does not exist, the server returns 404 (Not found). If the specified user is not the author of this quiz, the response is the 403 (Forbidden) status code.

SIXTH STAGE:

Description

At this last stage, your service will be improved to perform some trickier requests and return paginated responses. From the client's point of view, only a small part of API will be changed here.

Here are two articles worth reading before we begin:

    Spring Data @Query annotation for custom queries;
    Spring Boot Pagination and sorting examples.

Get all quizzes with paging (MODIFIED)

To get all existing quizzes in the service, the client sends the GET request to /api/quizzes as before. But here is the problem: the number of stored quizzes can be very large since your service is so popular among so many users.

In this regard, your API should return only 10 quizzes at once and supports the ability to specify which portion of quizzes is needed.

Please, use the standard libraries for the pagination.

The response contains a JSON with quizzes (inside content) and some additional metadata:

{
  "totalPages":1,
  "totalElements":3,
  "last":true,
  "first":true,
  "sort":{ },
  "number":0,
  "numberOfElements":3,
  "size":10,
  "empty":false,
  "pageable": { },
  "content":[
    {"id":102,"title":"Test 1","text":"Text 1","options":["a","b","c"]},
    {"id":103,"title":"Test 2","text":"Text 2","options":["a", "b", "c", "d"]},
    {"id":202,"title":"The Java Logo","text":"What is depicted on the Java logo?",
     "options":["Robot","Tea leaf","Cup of coffee","Bug"]}
  ]
}

We've simplified JSON a bit, but you can keep it in the same format it is generated by the framework. Our tests will validate only the essential fields.

The API should support the navigation through pages by passing the page parameter ( /api/quizzes?page=1). The first page is 0 since pages start from zero, just like our quizzes.

If there are no quizzes, content is empty []. If the user is authorized, the status code is 200 (OK); otherwise, it's 401 (Unauthorized).
Get all completions of quizzes with paging (NEW)

Your service must provide a new operation for getting all completions of quizzes for a specified user by sending the GET request to /api/quizzes/completed together with the user auth data. All the completions should be sorted from the most recent to the oldest.

A response is separated by pages since the service may return a lot of data. It contains a JSON with quizzes (inside content) and some additional metadata as in the previous operation.

Here is a response example:

{
  "totalPages":1,
  "totalElements":5,
  "last":true,
  "first":true,
  "empty":false,
  "content":[
    {"id":103,"completedAt":"2019-10-29T21:13:53.779542"},
    {"id":102,"completedAt":"2019-10-29T21:13:52.324993"},
    {"id":101,"completedAt":"2019-10-29T18:59:58.387267"},
    {"id":101,"completedAt":"2019-10-29T18:59:55.303268"},
    {"id":202,"completedAt":"2019-10-29T18:59:54.033801"}
  ]
}

Since it is allowed to solve a quiz multiple times, the response may contain duplicate quizzes, but with the different completion date.

We removed some metadata keys from the response to keep it comprehensible.

If there are no quizzes, content is empty []. If the user is authorized, the status code is 200 (OK); otherwise, it's 401 (Unauthorized).
