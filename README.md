# Smart Campus REST API

## Overview

This project is a RESTful API built using JAX-RS (Jersey) to manage a Smart Campus system. It allows management of rooms, sensors, and sensor readings.

The API follows REST principles including resource-based URLs, proper HTTP status codes, and structured JSON responses.

---

## Features

- Room management (create, list, delete)
- Sensor management with validation
- Sensor readings with historical tracking
- Filtering sensors by type
- Sub-resource architecture (/sensors/{id}/readings)
- Exception handling (409, 422, 403, 500)
- Logging filter for all requests and responses

---

## Technologies Used

- Java 17
- JAX-RS (Jersey)
- Grizzly HTTP Server
- Maven

---

## How to Run the Project

1. Open project in NetBeans
2. Ensure Maven dependencies are installed
3. Run the Main class
4. Server will start at:http://localhost:8080


---


## Curl commands

1. Create a room : POST http://localhost:8080/rooms \
 H "Content-Type: application/json" \
 d '{"id":"LIB-301","name":"Library","capacity":100}'
2. Get all rooms : GET http://localhost:8080/sensors?type=CO2
3. Add a sensor reading: POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
 H "Content-Type: application/json" \
 d '{"value":25}'
4. Get sensor reading : GET http://localhost:8080/api/v1/sensors/TEMP-001/readings
5. Delete Room : DELETE http://localhost:8080/api/v1/rooms/LIB-301

# Report – Humza Hassan w2115898

---

## Part 1: Service Architecture & Setup

---

### 1. Project & Application Configuration:

In the JAX-RS, resource classes are by the default request-scoped. This means a new instance of the class is made every time an http request comes in. This makes sure that each request is handled independently. It also makes sure that data doesn't get shared accidently between clients.

This lifecycle has important implications for managing memory data structures. As instance variables are not shared across requests. This means storing data in non-static fields would cause data loss after each request completes. To make sure that it is in a persistent state when recieving requests, shared data structures like static collections should be used as then it would cause negative effects.

Also, as multiple clients may access or modify these structures, it is important to make sure that there is thread safety. This can be done by using thread-safe collections such as Concurrent Hash Map. This would help maintain data consistency.

---

### 2. The Discovery Endpoint:

The HATEOAS is a key principle of RESTFUL API design. As it is where responses include hyperlinks that guide the client on how to use the API. This is used as it makes it easier as the client doesn't need to use external documentation. Clients can just use the links provided in the response to get their way around the API.

This improves flexibility and reduces tight coupling between client and server. If the endpoints were to change the server can just update the links in the responses without breaking existing clients. This also makes it easier for the developer as the APIs become more self-explanatory and making easier to navigate. This gets rid of the need to refer to static documentation.

---

## Part 2: Room Management

---

### 1. Room Resource Implementation:

When designing an API, returning only resource identifiers instead of the object can be a simple way to reduce network bandwidth usage. This can be especially helpful when dealing with larger datasets. Also, smaller payloads can improve performance and reduce latency of the API. This makes the API much more efficient for the clients due to its benefits.

However, as we are returning only the identifiers this increases the number of requests the client must make to get the full details, this can produce a large overhead on the client side. However, returning full object does provide all necessary information in a single response, which improves the usability and reduces the need to call the API.

Therefore, the choice depends on the use case. As returning just the identfiers is more efficient for small listings or when dealing with large data sets. In contrast, returning the full object is more convenient for the client and is also more user friendly as provides more information.

---

### 2. Room Deletion & Safety Logic:

The delete function in this implementation is idempotent which means making the same request multiple times still results in the same final state on the server. When the delete request is sent for a room that exists but has no sensors. This room is them removed from the system. This means if the same delete request is sent again for the same room because the room doesn't exist anymore the server will return a 404 not found response.

Therefore, the repeated delete requests will not cause any side effects that effect the program. The resource remains deleted; this ensures that the API abides by the idempotency property defined by the RESTFUL principles.

---

## Part 3: Sensor Operations & Linking

---

### 1.Sensor Resource & Integrity:

The @Consumes(MediaType.APPLICATION_JSON) annotation specifies that the API endpoint only accepts requests with a Content-Type of application/json. If a client sends data in a different format, such as plain text the JAR RS will not be able to find the right body reader in order to process it. This would mean that the framework would automatically reject the request and return a 415 response. This is put in place to make sure that the server only processes data in the right format. This maintains consistency and prevents parsing error or even data corruption.

This mechanism is put into place to improve the robustness of the API	asit  enforces strict input validation at the protocol level. As this contrasts with relying soley on manual checks within the code.

---

### 2.Filtered Retrieval & Search

Using @QueryParam for filtering is preferred over embedding filter criteria in the URL path. This is because generally query parameters are mostly designed for optional filtering and when using searching operation for collections. Query parameters allow clients to dynamically refine results without having to change the structure of the resource endpoint. However, using a path that treats the filter as a hierarchical resource leads to an API design that is much less flexible. This also makes it difficult to support multiple filters without having to create complex and hard to maintain URL structures.

Therefore, using a query parameter would be the better option as it would be more suitable for filtering because they provide more flexibility and also aligh better with RESTFUL principles for querying collections.

---

## Part 4: Deep Nesting with Sub - Resources

---

### 1. The Sub-Resource Locator Pattern:

The Sub-Resource locator pattern is used to improve the maintenance of the API by letting you hand off responsibility for nested resources to their own seperate classes. Instead of handling all nested endpoints within a single large controller, such as managing sensors and their readings in one class, the logic is seperated into dedicated resource classes.

This way massively makes the API less complicated. Each resource class has a clear focused responsibility. This makes the code much more easy to read and it also makes it easier to maintain. Also, it improves the scalability as new nested behaviours can be added without changing the main class which is the sensor resource class.

However, when we place all the nested paths inside a single controller it can lead to tightly coupled code. This makes it difficult to manage as the system grows. The sub-Resource locator pattern aligns more with a cleaner architecture principle as it promotes the separation of concerns.

---

### 2. Historical Data Management:

The SensorReadingResource implements both a retrieval of historical sensor data and the creation of new readings. This makes sure each sensor maintains a complete time-series record of its measurements. The GET method returns the full history of readings that is linked to a sensor. Then the POST method appends a new reading to this dataset.

A key design requirement is data consistency between the entity sensor and what it reads. To make this happen every successful POST operation not only stores the new reading, but it also updates the currentValue field of the parent sensor object. This ensures that the sensor always reflects the most recent measurement without requiring additional computation.

This design improves efficiency as it allows quick access to the most latest sensor state while still having full historical data. It also ensures consistency across the API, as both real time values and historical records remain.

---

## Part 5: Advanced Error Handling, Exception Mapping & Logging:

---

### 2. Dependency Validation (422 Unprocessable Entity):

The Http 422 entity is more accurate that the 404 not found in this scenario because the request is syntactically valid and the endpoint exists. But the data within the request contains a wrong reference. When the client is trying to create a resource using a valid JSON structure the room id provided does not correspond to an existing resource.

A 404 status is used when the requested endpoint is not found. While the 422 lets the client know that the server understands the request, but it cannot process it due to validation errors.

---

### 4. The Global Safety Net:

Exposing internal java stack traces to API consumers creates a big cybersecurity risk. Stack traces often reveal sensitive implementation details such as class names, package structure, databases queries, file paths, internal system logic.

Attackers can use this information to identify weaknesses in the system and understand system architecture, and craft targeted attacked such as SQL injection or even path traversal. However, this can be prevented by hiding stack traces and returning generic error messages through the class global Exception Mapper. This way the API minimizing the amount of data leaked and improves security. This way the system details are not exposed to external users.

---

### 5. API Request & Response Logging Filters:

JAX-RS filters provide a centralized and reusable approach to handling cross-cutting concern such as logging, authentication and monitoring. By implementing ContainerRequestFilter and ContainerResponseFilter, logging is applied autommatically to all incoming requests and outgoing responses without modifying the resource method.

This improves the code maintainability as it avoids duplication of logging logic. It also makes sure there is less human error, and it also creates a distinction between business logic and infrastructure. However, manually inserting logger statements in every resource method leads to repetitive and the code becomes harder to contain.
