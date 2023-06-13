package org.example.resource.controller;

import org.example.resource.entity.User;
import org.example.resource.repository.UserRepo;
import org.example.resource.util.Consts;
import org.example.resource.util.JWTDemo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("user")
public class UserCtrl {

    private final UserRepo repo;

    @Inject
    public UserCtrl(UserRepo repo) {
        this.repo = repo;
    }

    @POST
    @Path("sign-up")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUp(User u) {
        try {
            u = repo.insert(u);
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("email already taken")
                    .build();
        }
        if (u == null) {
            return Response.status(404)
                    .entity("Could not find customer")
                    .build();
        }

        String jwt = JWTDemo.createJWT(u.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("user", u);
        map.put("token", jwt);

        return Response.ok(map).build();
    }

    @POST
    @Path("log-in")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logIn(User u) {
        if (u.getEmail() == null || u.getPassword() == null) {
            return Response.status(400)
                    .entity("email and password not given!")
                    .build();
        }

        User userDb = null;
        try {
            userDb = repo.getByEmail(u.getEmail());
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("User not found")
                    .build();
        }
        if (userDb == null || !userDb.getPassword().equals(u.getPassword())) {
            return Response.status(404)
                    .entity("email or password gone wrong")
                    .build();
        }
        u = userDb;

        String jwt = JWTDemo.createJWT(u.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("user", u);
        map.put("token", jwt);

        return Response.ok(map).build();
    }

    @GET
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response me(@HeaderParam(Consts.token) String token) {
        Integer userId = JWTDemo.decodeJWT(token);
        if (userId == null)
            return Response.status(400)
                    .entity("Token invalid").build();
        User u = null;

        try {
            u = repo.getById(userId);
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
