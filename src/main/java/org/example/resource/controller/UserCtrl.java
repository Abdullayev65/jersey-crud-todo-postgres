package org.example.resource.controller;

import org.example.resource.domain.Customer;
import org.example.resource.entity.User;
import org.example.resource.repository.CustomerRepository;
import org.example.resource.repository.UserRepo;
import org.example.resource.resource.CustomerResource;
import org.example.resource.util.JWTDemo;

import javax.inject.Inject;
import javax.validation.constraints.Positive;
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
                    .entity("Problem getting data")
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

    @GET
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response me(@HeaderParam("token") String token) {
//        try {
//            u = repo.insert(u);
//        } catch (Exception ex) {
//            return Response.serverError()
//                    .entity("Problem getting data")
//                    .build();
//        }
//        if (u == null) {
//            return Response.status(404)
//                    .entity("Could not find customer")
//                    .build();
//        }
//
//        return Response.ok(u).build();
        return null;
    }


}
