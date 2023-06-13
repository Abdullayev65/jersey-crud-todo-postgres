package org.example.resource.repository;

import org.example.resource.db.PostgreSQLJDBC;
import org.example.resource.domain.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRepository {

    public Customer getCustomer(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Customer customer = null;
        try {
            conn = PostgreSQLJDBC.getConnection();
            ps = conn.prepareStatement("SELECT * FROM users WHERE ID=?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                int cId = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                customer = new Customer();
                customer.setId(cId);
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
            }
        } finally {
            if (conn != null) conn.close();
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        }

        return customer;
    }
}
