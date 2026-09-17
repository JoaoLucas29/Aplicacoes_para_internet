package br.com.mvc.dao;

import br.com.mvc.model.Comentario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ComentarioDAO extends MysqlDAO {

    public ComentarioDAO() {
        super();
    }

    public List<Comentario> listarPorIdeia(Long ideiaId) {
        String sql = "SELECT c.id, c.ideia_id, c.usuario_id, c.texto, c.data_criacao, "
                + "u.nome AS autor_nome "
                + "FROM comentarios c "
                + "INNER JOIN usuarios u ON u.id = c.usuario_id "
                + "WHERE c.ideia_id = ? "
                + "ORDER BY c.data_criacao ASC";
        List<Comentario> lista = new ArrayList<>();
        try (ResultSet rs = super.executar(sql, ideiaId)) {
            while (rs.next()) {
                lista.add(this.mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar comentarios.", e);
        }
        return lista;
    }

    public Comentario buscarPorId(Long id) {
        String sql = "SELECT c.id, c.ideia_id, c.usuario_id, c.texto, c.data_criacao, "
                + "u.nome AS autor_nome "
                + "FROM comentarios c "
                + "INNER JOIN usuarios u ON u.id = c.usuario_id "
                + "WHERE c.id = ?";
        try (ResultSet rs = super.executar(sql, id)) {
            if (rs.next()) {
                return this.mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar comentario.", e);
        }
        return null;
    }

    public void inserir(Comentario comentario) {
        String sql = "INSERT INTO comentarios (ideia_id, usuario_id, texto) VALUES (?, ?, ?)";
        try {
            super.executarUpdate(sql, comentario.getIdeiaId(), comentario.getUsuarioId(), comentario.getTexto());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir comentario.", e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM comentarios WHERE id = ?";
        try {
            super.executarUpdate(sql, id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar comentario.", e);
        }
    }

    private Comentario mapear(ResultSet rs) throws SQLException {
        Comentario comentario = new Comentario();
        comentario.setId(rs.getLong("id"));
        comentario.setIdeiaId(rs.getLong("ideia_id"));
        comentario.setUsuarioId(rs.getLong("usuario_id"));
        comentario.setTexto(rs.getString("texto"));
        comentario.setAutorNome(rs.getString("autor_nome"));

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            comentario.setDataCriacao(dataCriacao.toLocalDateTime());
        }
        return comentario;
    }
}
