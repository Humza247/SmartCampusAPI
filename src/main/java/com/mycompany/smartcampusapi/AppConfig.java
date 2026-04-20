/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampusapi;

/**
 *
 * @author humza
 */

import com.mycompany.smartcampusapi.resources.SensorRoomResource;
import com.mycompany.smartcampusapi.resources.SensorResource;
import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1")
public class AppConfig extends ResourceConfig {
}