/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.resources;

/**
 *
 * @author humza
 */
import com.mycompany.smartcampusapi.exceptions.SensorUnavailableException;
import com.mycompany.smartcampusapi.models.Sensor;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;

    // Shared storage: sensorId
    private static Map<String, List<Map<String, Object>>> readings = new HashMap<>();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // GET /sensors//readings
    @GET
    public List<Map<String, Object>> getReadings() {
        return readings.getOrDefault(sensorId, new ArrayList<>());
    }

    // POST /api/v1/sensors/{id}/readings
    @POST
    public Response addReading(Map<String, Object> reading) {

        // Get sensor
        Sensor sensor = SensorResource.getSensorById(sensorId);

        if (sensor == null) {
            throw new RuntimeException("Sensor not found");
        }

        //block maintenance sensors
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is under maintenance");
        }

        //Add timestamp
        reading.put("timestamp", System.currentTimeMillis());

        // Store reading
        readings.computeIfAbsent(sensorId, k -> new ArrayList<>())
                .add(reading);

        //Update sensor value
        if (reading.get("value") != null) {
            double value = Double.parseDouble(reading.get("value").toString());
            sensor.setCurrentValue(value);
        }

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}