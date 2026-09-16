package com.ideiasapp.dao;

import com.ideiasapp.model.Comentario;
import com.ideiasapp.model.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComentarioDAO extends MysqlDAO {

    public void inserir(Comentario comentario) {
        String sql = "INSERT INTO comentario (texto, ideia_id, usuario_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, comentario.getTexto());
            stmt.setInt(2, comentario.getIdeiaId());
            stmt.setInt(3, comentario.getAutor().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir comentário", e);
        }
    }

    public Comentario buscarPorId(int id) {
        String sql = "SELECT c.*, u.nome AS autor_nome FROM comentario c " +
                "JOIN usuario u ON c.usuario_id = u.id WHERE c.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar comentário por id", e);
        }
        return null;
    }

    public List<Comentario> listarPorIdeia(int ideiaId) {
        List<Comentario> comentarios = new ArrayList<>();
        String sql = "SELECT c.*, u.nome AS autor_nome FROM comentario c " +
                "JOIN usuario u ON c.usuario_id = u.id " +
                "WHERE c.ideia_id = ? ORDER BY c.data_comentario ASC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ideiaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comentarios.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar comentários", e);
        }
        return comentarios;
    }

    // UPDATE
    public void atualizar(Comentario comentario) {
        String sql = "UPDATE comentario SET texto = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, comentario.getTexto());
            stmt.setInt(2, comentario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar comentário", e);
        }
    }

    // DELETE
    public void deletar(int id) {
        String sql = "DELETE FROM comentario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir comentário", e);
        }
    }

    private Comentario mapear(ResultSet rs) throws SQLException {
        Usuario autor = new Usuario();
        autor.setId(rs.getInt("usuario_id"));
        autor.setNome(rs.getString("autor_nome"));

        Comentario c = new Comentario();
        c.setId(rs.getInt("id"));
        c.setTexto(rs.getString("texto"));
        c.setIdeiaId(rs.getInt("ideia_id"));
        c.setAutor(autor);
        c.setDataComentario(rs.getTimestamp("data_comentario").toLocalDateTime());
        return c;
    }
}