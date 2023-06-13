package org.example.resource.controller;

import org.example.resource.entity.Todo;
import org.example.resource.repository.TodoRepo;
import org.example.resource.util.Consts;
import org.example.resource.util.JWTDemo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("todo")
public class TodoCtrl {

    private final TodoRepo repo;

    @Inject
    public TodoCtrl(TodoRepo repo) {
        this.repo = repo;
    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@HeaderParam(Consts.token) String token, Todo t) {
        Integer userId = JWTDemo.decodeJWT(token);
        if (userId == null) {
            return Responses.invalidToken;
        }

        t.setCreatedBy(userId);

        try {
            t = repo.insert(t);
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Problem getting data")
                    .build();
        }
        if (t == null) {
            return Response.status(404)
                    .entity("something gone wrong!")
                    .build();
        }

        return Response.ok(t).build();
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@HeaderParam(Consts.token) String token,
                         @QueryParam("limit") Integer limit,
                         @QueryParam("offset") Integer offset) {
        Integer userId = JWTDemo.decodeJWT(token);
        {
            if (userId == null) {
                return Responses.invalidToken;
            }

            if (limit == null)
                limit = 10;
            if (offset == null)
                offset = 0;
        }

        List<Todo> todos = null;
        try {
            todos = repo.list(userId, limit, offset);
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Problem getting data")
                    .build();
        }

        return Response.ok(todos).build();
    }

    @PUT
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@HeaderParam(Consts.token) String token,
                           @PathParam("id") Integer id,
                           Todo todo) {
        Integer userId = JWTDemo.decodeJWT(token);
        if (userId == null) {
            return Responses.invalidToken;
        }
        todo.setId(id);

        try {
            Todo todoDb = repo.getById(id);
            if (!todoDb.getCreatedBy().equals(userId)) {
                return Response.status(400)
                        .entity("permission denied")
                        .build();
            }
            repo.update(todo);
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Problem deleting data")
                    .build();
        }

        return Response.ok(todo).build();
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@HeaderParam(Consts.token) String token, @PathParam("id") Integer id) {
        Integer userId = JWTDemo.decodeJWT(token);
        if (userId == null) {
            return Responses.invalidToken;
        }

        try {
            Todo t = repo.getById(id);
            if (!t.getCreatedBy().equals(userId)) {
                return Response.status(400)
                        .entity("permission denied")
                        .build();
            }
            repo.delete(id);
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Problem deleting data")
                    .build();
        }

        return Response.ok("DELETED").build();
    }

}
