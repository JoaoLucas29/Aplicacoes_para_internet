package com.ideiasapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class MysqlDAO {

    protected Connection connection;

    public MysqlDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    protected void closeStatement(PreparedStatement stmt) {
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    protected void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void close() {
        ConnectionFactory.closeConnection(connection);
    }
}