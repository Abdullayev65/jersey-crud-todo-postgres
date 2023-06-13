package org.example.resource.repository;

import org.example.resource.db.PostgreSQLJDBC;
import org.example.resource.entity.Todo;
import org.example.resource.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TodoRepo {

    public Todo insert(Todo t) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = PostgreSQLJDBC.getConnection();
            ps = conn.prepareStatement(
                    "Insert Into todos(task, done, created_by) VALUES(?,?,?) RETURNING id");
            ps.setString(1, t.getTask());
            ps.setBoolean(2, t.getDone());
            ps.setInt(3, t.getCreatedBy());
            rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                t.setId(id);
            }
        } finally {
            if (conn != null) conn.close();
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        }

        return t;
    }

    public Todo getById(Integer id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Todo t = new Todo();
        t.setId(id);
        try {
            conn = PostgreSQLJDBC.getConnection();
            ps = conn.prepareStatement(
                    "SELECT task, done, created_by From todos where id=?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                t.setTask(rs.getString(1));
                t.setDone(rs.getBoolean(2));
                t.setCreatedBy(rs.getInt(3));
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (conn != null) conn.close();
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        }

        return t;
    }


    public List<Todo> list(Integer userId, Integer limit, Integer offset) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Todo> todos = new ArrayList<>();

        try {
            conn = PostgreSQLJDBC.getConnection();
            ps = conn.prepareStatement(
                    "SELECT id, task, done, created_by From todos where created_by=? " +
                            "limit ? offset ?");
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            rs = ps.executeQuery();
            while (rs.next()) {
                Todo t = new Todo();
                t.setId(rs.getInt(1));
                t.setTask(rs.getString(2));
                t.setDone(rs.getBoolean(3));
                t.setCreatedBy(rs.getInt(4));

                todos.add(t);
            }
        } finally {
            if (conn != null) conn.close();
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        }

        return todos;
    }

    public void delete(Integer id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = PostgreSQLJDBC.getConnection();
            ps = conn.prepareStatement("DELETE From todos where id=?");
            ps.setInt(1, id);
            ps.execute();
        } finally {
            if (conn != null) conn.close();
            if (ps != null) ps.close();
        }
    }

    public void update(Todo t) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = PostgreSQLJDBC.getConnection();
            ps = conn.prepareStatement(
                    "UPDATE  todos SET task=?, done=? Where id=?");
            ps.setString(1, t.getTask());
            ps.setBoolean(2, t.getDone());
            ps.setInt(3, t.getId());
            ps.execute();
        } finally {
            if (conn != null) conn.close();
            if (ps != null) ps.close();
        }

    }
}
