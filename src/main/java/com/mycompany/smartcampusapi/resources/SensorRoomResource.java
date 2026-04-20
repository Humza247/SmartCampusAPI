/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.resources;

/**
 *
 * @author humza
 */
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

import com.mycompany.smartcampusapi.models.Room;
import com.mycompany.smartcampusapi.exceptions.RoomNotEmptyException;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoomResource {

    public static Map<String, Room> rooms = new HashMap<>();

    // GET /rooms
    @GET
    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    // POST /rooms
    @POST
    public Response createRoom(Room room) {

        if (room.getId() == null || room.getId().isEmpty()) {
            return buildError("Room ID is required", Response.Status.BAD_REQUEST);
        }

        if (rooms.containsKey(room.getId())) {
            return buildError("Room with this ID already exists", Response.Status.CONFLICT);
        }

        rooms.put(room.getId(), room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    // GET /rooms/{id}
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {

        Room room = rooms.get(roomId);

        if (room == null) {
            return buildError("Room not found", Response.Status.NOT_FOUND);
        }

        return Response.ok(room).build();
    }

    // DELETE /rooms/{id}
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {

        Room room = rooms.get(roomId);

        // If room doesn't exist
        if (room == null) {
            return buildError("Room not found", Response.Status.NOT_FOUND);
        }

        // ❗ UPDATED: throw exception instead of returning 409 manually
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room has active sensors");
        }

        // Safe to delete
        rooms.remove(roomId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Room deleted successfully");

        return Response.ok(response).build();
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
