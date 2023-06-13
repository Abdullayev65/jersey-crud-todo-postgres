package org.example.resource.repository;

import org.example.resource.db.PostgreSQLJDBC;
import org.example.resource.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepo {

    public User insert(User u) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = PostgreSQLJDBC.getConnection();
            ps = conn.prepareStatement(
                    "Insert Into users(name, email, password) VALUES(?,?,?) RETURNING id");
            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                u.setId(id);
            }
        } finally {
            if (conn != null) conn.close();
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        }

        return u;
    }

    public User getById(Integer id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User u = new User();
        u.setId(id);
        try {
            conn = PostgreSQLJDBC.getConnection();
            ps = conn.prepareStatement(
                    "SELECT name, email, password From users where id=?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                u.setName(rs.getString(1));
                u.setEmail(rs.getString(2));
                u.setPassword(rs.getString(3));
            }
        } finally {
            if (conn != null) conn.close();
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        }

        return u;
    }

    public User getByEmail(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User u = new User();
        u.setEmail(email);
        try {
            conn = PostgreSQLJDBC.getConnection();
            ps = conn.prepareStatement(
                    "SELECT name, id, password From users where email=?");
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                u.setName(rs.getString(1));
                u.setId(rs.getInt(2));
                u.setPassword(rs.getString(3));
            }
        } finally {
            if (conn != null) conn.close();
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        }

        return u;
    }


}
