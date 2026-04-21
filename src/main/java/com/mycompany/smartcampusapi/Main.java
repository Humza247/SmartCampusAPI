/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.smartcampusapi;

/**
 *
 * @author humza
 */
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import java.net.URI;
import com.mycompany.smartcampusapi.exceptions.RoomNotEmptyExceptionMapper;
import com.mycompany.smartcampusapi.exceptions.LinkedResourceNotFoundExceptionMapper;
import com.mycompany.smartcampusapi.exceptions.GlobalExceptionMapper;
import com.mycompany.smartcampusapi.exceptions.SensorUnavailableExceptionMapper;
import com.mycompany.smartcampusapi.filters.ApiLoggingFilter;
public class Main {

    public static void main(String[] args) {

        URI baseUri = URI.create("http://localhost:8080/");

        ResourceConfig config = new ResourceConfig()
                .packages("com.mycompany.smartcampusapi.resources")

               
                .register(RoomNotEmptyExceptionMapper.class)
                .register(LinkedResourceNotFoundExceptionMapper.class)
                .register(SensorUnavailableExceptionMapper.class)
                .register(GlobalExceptionMapper.class)
                .register(ApiLoggingFilter.class);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);

        System.out.println("Server running at " + baseUri);
    }
}
