package com.hotel.gerenciador.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {
    protected Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    protected abstract String getTableName();
    protected abstract T fromResultSet(ResultSet rs) throws SQLException;

    public List<T> findAll() throws SQLException {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(fromResultSet(rs));
            }
        }
        return list;
    }

    public T findById(int id) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getTableName() + "ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return fromResultSet(rs);
                }
            }
        }
        return null;
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getTableName() + "ID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}