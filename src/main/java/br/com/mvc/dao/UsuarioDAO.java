package br.com.mvc.dao;

import br.com.mvc.model.Usuario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends MysqlDAO {

    public UsuarioDAO() {
        super();
    }

    public Usuario buscarPorEmailESenha(String email, String senha) {
        String sql = "SELECT id, nome, email, senha, data_cadastro "
                + "FROM usuarios WHERE email = ? AND senha = ?";
        try (ResultSet rs = super.executar(sql, email, senha)) {
            if (rs.next()) {
                return this.mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuario.", e);
        }
        return null;
    }

    public List<Usuario> listarTodos() {
        String sql = "SELECT id, nome, email, senha, data_cadastro "
                + "FROM usuarios ORDER BY nome";
        List<Usuario> lista = new ArrayList<>();
        try (ResultSet rs = super.executar(sql)) {
            while (rs.next()) {
                lista.add(this.mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuarios.", e);
        }
        return lista;
    }

    public Usuario buscarPorId(Long id) {
        String sql = "SELECT id, nome, email, senha, data_cadastro "
                + "FROM usuarios WHERE id = ?";
        try (ResultSet rs = super.executar(sql, id)) {
            if (rs.next()) {
                return this.mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por id.", e);
        }
        return null;
    }

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT id, nome, email, senha, data_cadastro "
                + "FROM usuarios WHERE email = ?";
        try (ResultSet rs = super.executar(sql, email)) {
            if (rs.next()) {
                return this.mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por email.", e);
        }
        return null;
    }

    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";
        try {
            super.executarUpdate(sql, usuario.getNome(), usuario.getEmail(), usuario.getSenha());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuario.", e);
        }
    }

    public void alterar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";
        try {
            super.executarUpdate(
                    sql,
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getSenha(),
                    usuario.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar usuario.", e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try {
            super.executarUpdate(sql, id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuario.", e);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));

        Timestamp dataCadastro = rs.getTimestamp("data_cadastro");
        if (dataCadastro != null) {
            usuario.setDataCadastro(dataCadastro.toLocalDateTime());
        }
        return usuario;
    }
}
