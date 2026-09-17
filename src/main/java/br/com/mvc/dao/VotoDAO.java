package br.com.mvc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VotoDAO extends MysqlDAO {

    public VotoDAO() {
        super();
    }

    public boolean existeVoto(Long ideiaId, Long usuarioId) {
        String sql = "SELECT COUNT(*) AS total FROM votos WHERE ideia_id = ? AND usuario_id = ?";
        try (ResultSet rs = super.executar(sql, ideiaId, usuarioId)) {
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar voto.", e);
        }
        return false;
    }

    public int contarPorIdeia(Long ideiaId) {
        String sql = "SELECT COUNT(*) AS total FROM votos WHERE ideia_id = ?";
        try (ResultSet rs = super.executar(sql, ideiaId)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar votos.", e);
        }
        return 0;
    }

    public void inserir(Long ideiaId, Long usuarioId) {
        String sql = "INSERT INTO votos (ideia_id, usuario_id) VALUES (?, ?)";
        try {
            super.executarUpdate(sql, ideiaId, usuarioId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar voto.", e);
        }
    }

    public void deletar(Long ideiaId, Long usuarioId) {
        String sql = "DELETE FROM votos WHERE ideia_id = ? AND usuario_id = ?";
        try {
            super.executarUpdate(sql, ideiaId, usuarioId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover voto.", e);
        }
    }

    public void deletarPorIdeia(Long ideiaId) {
        String sql = "DELETE FROM votos WHERE ideia_id = ?";
        try {
            super.executarUpdate(sql, ideiaId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover votos da ideia.", e);
        }
    }
}
