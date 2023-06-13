package org.example.resource.controller;

import javax.ws.rs.core.Response;

public interface Responses {
    Response invalidToken = Response.status(404)
            .entity("invalid token!")
            .build();
}
