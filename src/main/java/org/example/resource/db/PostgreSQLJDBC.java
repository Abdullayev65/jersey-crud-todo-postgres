package org.example.resource.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgreSQLJDBC {
   public static Connection getConnection() {
      Connection c = null;
      try {
         Class.forName("org.postgresql.Driver");
         c = DriverManager
            .getConnection("jdbc:postgresql://localhost:5432/postgres",
            "postgres", "1");
      } catch (Exception e) {
         e.printStackTrace();
         System.err.println(e.getClass().getName()+": "+e.getMessage());
         System.exit(0);
      }
      System.out.println("Opened database successfully");

      return c;
   }
}