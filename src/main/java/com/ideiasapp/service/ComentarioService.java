package com.ideiasapp.service;

import com.ideiasapp.dao.ComentarioDAO;
import com.ideiasapp.model.Comentario;

import java.util.List;

public class ComentarioService {

    private final ComentarioDAO comentarioDAO = new ComentarioDAO();

    public void comentar(Comentario comentario) {
        if (comentario.getTexto() == null || comentario.getTexto().trim().isEmpty()) {
            throw new IllegalArgumentException("Comentário não pode ser vazio");
        }
        if (comentario.getTexto().length() > 500) {
            throw new IllegalArgumentException("Comentário muito longo (máx. 500 caracteres)");
        }
        if (comentario.getAutor() == null || comentario.getAutor().getId() == null) {
            throw new IllegalStateException("Usuário precisa estar logado para comentar");
        }
        comentarioDAO.inserir(comentario);
    }

    public List<Comentario> listarPorIdeia(int ideiaId) {
        return comentarioDAO.listarPorIdeia(ideiaId);
    }

    public Comentario buscarPorId(int id) {
        Comentario comentario = comentarioDAO.buscarPorId(id);
        if (comentario == null) {
            throw new IllegalArgumentException("Comentário não encontrado");
        }
        return comentario;
    }

    // UPDATE
    public void atualizar(int id, String novoTexto, int usuarioLogadoId) {
        Comentario existente = buscarPorId(id);
        if (!existente.getAutor().getId().equals(usuarioLogadoId)) {
            throw new SecurityException("Você só pode editar seus próprios comentários");
        }
        if (novoTexto == null || novoTexto.trim().isEmpty()) {
            throw new IllegalArgumentException("Comentário não pode ser vazio");
        }
        if (novoTexto.length() > 500) {
            throw new IllegalArgumentException("Comentário muito longo (máx. 500 caracteres)");
        }
        existente.setTexto(novoTexto);
        comentarioDAO.atualizar(existente);
    }

    // DELETE
    public void excluir(int id, int usuarioLogadoId) {
        Comentario existente = buscarPorId(id);
        if (!existente.getAutor().getId().equals(usuarioLogadoId)) {
            throw new SecurityException("Você só pode excluir seus próprios comentários");
        }
        comentarioDAO.deletar(id);
    }
}