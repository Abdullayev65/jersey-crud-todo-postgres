package org.example.resource.controller;

import org.example.resource.entity.User;
import org.example.resource.repository.UserRepo;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
public class TodoCtrl {

    private final UserRepo repo;

    @Inject
    public TodoCtrl(UserRepo repo) {
        this.repo = repo;
    }

    @GET
    @Path("sign-up")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUp(User u) {
        try {
            u = repo.insert(u);
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Problem getting data")
                    .build();
        }
        if (u == null) {
            return Response.status(404)
                    .entity("Could not find customer")
                    .build();
        }

        return Response.ok(u).build();
    }
}
