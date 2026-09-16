package com.ideiasapp.service;

import com.ideiasapp.dao.UsuarioDAO;
import com.ideiasapp.model.Usuario;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // CREATE
    public void cadastrar(Usuario usuario) {
        validarNome(usuario.getNome());
        validarSenha(usuario.getSenha());

        if (usuarioDAO.buscarPorNome(usuario.getNome()) != null) {
            throw new IllegalArgumentException("Esse nome de usuário já está em uso");
        }

        // Em produção, use BCrypt em vez de armazenar a senha em texto puro
        usuarioDAO.inserir(usuario);
    }

    // READ
    public Usuario autenticar(String nome, String senha) {
        Usuario usuario = usuarioDAO.buscarPorNome(nome);
        if (usuario == null || !usuario.getSenha().equals(senha)) {
            return null; // credenciais inválidas
        }
        return usuario;
    }

    public Usuario buscarPorId(int id) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        return usuario;
    }

    // UPDATE
    public void atualizar(Usuario usuario) {
        validarNome(usuario.getNome());

        Usuario existente = usuarioDAO.buscarPorNome(usuario.getNome());
        if (existente != null && !existente.getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("Esse nome de usuário já está em uso");
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            // mantém a senha atual se o campo vier vazio na edição
            Usuario atual = usuarioDAO.buscarPorId(usuario.getId());
            usuario.setSenha(atual.getSenha());
        } else {
            validarSenha(usuario.getSenha());
        }

        usuarioDAO.atualizar(usuario);
    }

    // DELETE
    public void excluir(int id) {
        usuarioDAO.deletar(id);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (nome.trim().length() < 3) {
            throw new IllegalArgumentException("Nome deve ter no mínimo 3 caracteres");
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres");
        }
    }
}
