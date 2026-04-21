/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.resources;

/**
 *
 * @author humza
 */
import com.mycompany.smartcampusapi.models.Sensor;
import com.mycompany.smartcampusapi.models.Room;
import com.mycompany.smartcampusapi.exceptions.LinkedResourceNotFoundException;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private static Map<String, Sensor> sensors = new HashMap<>();

    // POST sensors
    @POST
    public Response createSensor(Sensor sensor) {

        // Validate ID
        if (sensor.getId() == null || sensor.getId().isEmpty()) {
            return buildError("Sensor ID is required", Response.Status.BAD_REQUEST);
        }

        // Check duplicate
        if (sensors.containsKey(sensor.getId())) {
            return buildError("Sensor already exists", Response.Status.CONFLICT);
        }

        //Check if room exists
        Room room = SensorRoomResource.rooms.get(sensor.getRoomId());

        if (room == null) {
            throw new LinkedResourceNotFoundException("Room does not exist");
}
        // Add sensor
        sensors.put(sensor.getId(), sensor);

        // Link sensor to room
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    
    @GET
    public Collection<Sensor> getAllSensors(@QueryParam("type") String type) {

        // If no filter → return all
        if (type == null || type.isEmpty()) {
            return sensors.values();
        }

        // Filtered list
        List<Sensor> filtered = new ArrayList<>();

        for (Sensor sensor : sensors.values()) {
            if (sensor.getType() != null &&
                sensor.getType().equalsIgnoreCase(type)) {
                filtered.add(sensor);
            }
        }

        return filtered;
    }
    @Path("/{sensorId}/readings")
        public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
            return new SensorReadingResource(sensorId);
        }
    public static Sensor getSensorById(String sensorId) {
        return sensors.get(sensorId);
    }

    // Helper method
    private Response buildError(String message, Response.Status status) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);

        return Response.status(status)
                .entity(error)
                .build();
    }
}
