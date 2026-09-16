package com.ideiasapp.dao;

import com.ideiasapp.model.Voto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VotoDAO extends MysqlDAO {

    public void inserir(Voto voto) {
        String sql = "INSERT INTO voto (ideia_id, usuario_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, voto.getIdeiaId());
            stmt.setInt(2, voto.getUsuarioId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar voto", e);
        }
    }

    public boolean jaVotou(int ideiaId, int usuarioId) {
        String sql = "SELECT 1 FROM voto WHERE ideia_id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ideiaId);
            stmt.setInt(2, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar voto", e);
        }
    }

    public int contarVotos(int ideiaId) {
        String sql = "SELECT COUNT(*) FROM voto WHERE ideia_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ideiaId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar votos", e);
        }
        return 0;
    }

    public void remover(int ideiaId, int usuarioId) {
        String sql = "DELETE FROM voto WHERE ideia_id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ideiaId);
            stmt.setInt(2, usuarioId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover voto", e);
        }
    }
}