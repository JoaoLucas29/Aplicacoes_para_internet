package br.com.mvc.dao;

import br.com.mvc.model.Ideia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class IdeiaDAO extends MysqlDAO {

    private static final String SELECT_BASE =
            "SELECT i.id, i.titulo, i.descricao, i.usuario_id, i.data_criacao, "
                    + "u.nome AS autor_nome, "
                    + "(SELECT COUNT(*) FROM votos v WHERE v.ideia_id = i.id) AS total_votos, "
                    + "EXISTS(SELECT 1 FROM votos v2 WHERE v2.ideia_id = i.id AND v2.usuario_id = ?) AS ja_votou "
                    + "FROM ideias i "
                    + "INNER JOIN usuarios u ON u.id = i.usuario_id ";

    public IdeiaDAO() {
        super();
    }

    public List<Ideia> listarTodos(Long usuarioLogadoId) {
        String sql = SELECT_BASE + "ORDER BY total_votos DESC, i.data_criacao DESC";
        List<Ideia> lista = new ArrayList<>();
        try (ResultSet rs = super.executar(sql, this.idOuZero(usuarioLogadoId))) {
            while (rs.next()) {
                lista.add(this.mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar ideias.", e);
        }
        return lista;
    }

    public Ideia buscarPorId(Long id, Long usuarioLogadoId) {
        String sql = SELECT_BASE + "WHERE i.id = ?";
        try (ResultSet rs = super.executar(sql, this.idOuZero(usuarioLogadoId), id)) {
            if (rs.next()) {
                return this.mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar ideia por id.", e);
        }
        return null;
    }

    public Ideia buscarPorId(Long id) {
        return this.buscarPorId(id, null);
    }

    public void inserir(Ideia ideia) {
        String sql = "INSERT INTO ideias (titulo, descricao, usuario_id) VALUES (?, ?, ?)";
        try {
            super.executarUpdate(sql, ideia.getTitulo(), ideia.getDescricao(), ideia.getUsuarioId());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir ideia.", e);
        }
    }

    public void alterar(Ideia ideia) {
        String sql = "UPDATE ideias SET titulo = ?, descricao = ? WHERE id = ?";
        try {
            super.executarUpdate(sql, ideia.getTitulo(), ideia.getDescricao(), ideia.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar ideia.", e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM ideias WHERE id = ?";
        try {
            super.executarUpdate(sql, id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar ideia.", e);
        }
    }

    private long idOuZero(Long id) {
        return id == null ? 0L : id;
    }

    private Ideia mapear(ResultSet rs) throws SQLException {
        Ideia ideia = new Ideia();
        ideia.setId(rs.getLong("id"));
        ideia.setTitulo(rs.getString("titulo"));
        ideia.setDescricao(rs.getString("descricao"));
        ideia.setUsuarioId(rs.getLong("usuario_id"));
        ideia.setAutorNome(rs.getString("autor_nome"));
        ideia.setTotalVotos(rs.getInt("total_votos"));
        ideia.setJaVotou(rs.getBoolean("ja_votou"));

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            ideia.setDataCriacao(dataCriacao.toLocalDateTime());
        }
        return ideia;
    }
}
