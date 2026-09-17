package br.com.mvc.service;

import br.com.mvc.dao.UsuarioDAO;
import br.com.mvc.model.Usuario;

import java.util.regex.Pattern;

public class UsuarioService {

    private static final int SENHA_MINIMA = 6;
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario autenticar(String email, String senha) {
        email = this.normalizar(email);
        senha = this.normalizar(senha);

        if (email == null || senha == null) {
            throw new IllegalArgumentException("Informe email e senha.");
        }

        Usuario usuario = this.usuarioDAO.buscarPorEmailESenha(email, senha);
        if (usuario == null) {
            throw new IllegalArgumentException("Email ou senha invalidos.");
        }
        return usuario;
    }

    public Usuario buscarPorId(Long id) {
        if (id == null) {
            return null;
        }
        return this.usuarioDAO.buscarPorId(id);
    }

    public void cadastrar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario e obrigatorio.");
        }
        usuario.setId(null);

        this.prepararDados(usuario);
        this.validarCamposObrigatorios(usuario);
        this.validarEmail(usuario.getEmail());
        this.validarSenha(usuario.getSenha());
        this.validarEmailUnico(usuario);

        this.usuarioDAO.inserir(usuario);
    }

    public void atualizar(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException("Usuario e obrigatorio.");
        }

        this.prepararDados(usuario);
        this.validarCamposObrigatorios(usuario);
        this.validarEmail(usuario.getEmail());
        this.validarSenha(usuario.getSenha());
        this.validarEmailUnico(usuario);

        if (this.usuarioDAO.buscarPorId(usuario.getId()) == null) {
            throw new IllegalArgumentException("Usuario nao encontrado para alteracao.");
        }
        this.usuarioDAO.alterar(usuario);
    }

    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id e obrigatorio para excluir.");
        }
        if (this.usuarioDAO.buscarPorId(id) == null) {
            throw new IllegalArgumentException("Usuario nao encontrado.");
        }
        this.usuarioDAO.deletar(id);
    }

    private void prepararDados(Usuario usuario) {
        usuario.setNome(this.normalizar(usuario.getNome()));
        usuario.setEmail(this.normalizarEmail(usuario.getEmail()));
        usuario.setSenha(this.normalizar(usuario.getSenha()));
    }

    private void validarCamposObrigatorios(Usuario usuario) {
        if (usuario.getNome() == null) {
            throw new IllegalArgumentException("Nome e obrigatorio.");
        }
        if (usuario.getEmail() == null) {
            throw new IllegalArgumentException("Email e obrigatorio.");
        }
        if (usuario.getSenha() == null) {
            throw new IllegalArgumentException("Senha e obrigatoria.");
        }
    }

    private void validarEmail(String email) {
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new IllegalArgumentException("Informe um email valido.");
        }
    }

    private void validarSenha(String senha) {
        if (senha.length() < SENHA_MINIMA) {
            throw new IllegalArgumentException("Senha deve ter no minimo " + SENHA_MINIMA + " caracteres.");
        }
    }

    private void validarEmailUnico(Usuario usuario) {
        Usuario existente = this.usuarioDAO.buscarPorEmail(usuario.getEmail());
        if (existente == null) {
            return;
        }
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("Ja existe um usuario com este email.");
        }
        if (!existente.getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("Ja existe um usuario com este email.");
        }
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpo = valor.trim();
        return limpo.isEmpty() ? null : limpo;
    }

    private String normalizarEmail(String valor) {
        String limpo = this.normalizar(valor);
        return limpo == null ? null : limpo.toLowerCase();
    }
}
