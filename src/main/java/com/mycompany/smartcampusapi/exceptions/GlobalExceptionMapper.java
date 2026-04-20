/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi.exceptions;

/**
 *
 * @author humza
 */
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal server error. Please contact system administrator.");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR) // 500
                .entity(error)
                .build();
    }
}
