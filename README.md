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

