package com.ideiasapp.dao;

import com.ideiasapp.model.Ideia;
import com.ideiasapp.model.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IdeiaDAO extends MysqlDAO {

    public void inserir(Ideia ideia) {
        String sql = "INSERT INTO ideia (titulo, descricao, usuario_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ideia.getTitulo());
            stmt.setString(2, ideia.getDescricao());
            stmt.setInt(3, ideia.getAutor().getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ideia.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir ideia", e);
        }
    }

    public Ideia buscarPorId(int id) {
        String sql = "SELECT i.*, u.nome AS autor_nome, " +
                "(SELECT COUNT(*) FROM voto v WHERE v.ideia_id = i.id) AS total_votos " +
                "FROM ideia i JOIN usuario u ON i.usuario_id = u.id WHERE i.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar ideia por id", e);
        }
        return null;
    }

    // Lista todas as ideias ordenadas pelas mais votadas
    public List<Ideia> listarOrdenadasPorVotos() {
        List<Ideia> ideias = new ArrayList<>();
        String sql = "SELECT i.*, u.nome AS autor_nome, " +
                "(SELECT COUNT(*) FROM voto v WHERE v.ideia_id = i.id) AS total_votos " +
                "FROM ideia i JOIN usuario u ON i.usuario_id = u.id " +
                "ORDER BY total_votos DESC, i.data_criacao DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ideias.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar ideias", e);
        }
        return ideias;
    }

    public List<Ideia> listarPorUsuario(int usuarioId) {
        List<Ideia> ideias = new ArrayList<>();
        String sql = "SELECT i.*, u.nome AS autor_nome, " +
                "(SELECT COUNT(*) FROM voto v WHERE v.ideia_id = i.id) AS total_votos " +
                "FROM ideia i JOIN usuario u ON i.usuario_id = u.id " +
                "WHERE i.usuario_id = ? ORDER BY i.data_criacao DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ideias.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar ideias por usuário", e);
        }
        return ideias;
    }

    // UPDATE
    public void atualizar(Ideia ideia) {
        String sql = "UPDATE ideia SET titulo = ?, descricao = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ideia.getTitulo());
            stmt.setString(2, ideia.getDescricao());
            stmt.setInt(3, ideia.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar ideia", e);
        }
    }

    // DELETE
    public void deletar(int id) {
        String sqlComentarios = "DELETE FROM comentario WHERE ideia_id = ?";
        String sqlVotos = "DELETE FROM voto WHERE ideia_id = ?";
        String sqlIdeia = "DELETE FROM ideia WHERE id = ?";
        try {
            try (PreparedStatement stmt = connection.prepareStatement(sqlComentarios)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = connection.prepareStatement(sqlVotos)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = connection.prepareStatement(sqlIdeia)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir ideia", e);
        }
    }

    private Ideia mapear(ResultSet rs) throws SQLException {
        Usuario autor = new Usuario();
        autor.setId(rs.getInt("usuario_id"));
        autor.setNome(rs.getString("autor_nome"));

        Ideia ideia = new Ideia();
        ideia.setId(rs.getInt("id"));
        ideia.setTitulo(rs.getString("titulo"));
        ideia.setDescricao(rs.getString("descricao"));
        ideia.setAutor(autor);
        ideia.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        ideia.setTotalVotos(rs.getInt("total_votos"));
        return ideia;
    }
}