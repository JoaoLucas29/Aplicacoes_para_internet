package com.ideiasapp.service;

import com.ideiasapp.dao.IdeiaDAO;
import com.ideiasapp.model.Ideia;

import java.util.List;

public class IdeiaService {

    private final IdeiaDAO ideiaDAO = new IdeiaDAO();

    public void cadastrar(Ideia ideia) {
        if (ideia.getTitulo() == null || ideia.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }
        if (ideia.getDescricao() == null || ideia.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição é obrigatória");
        }
        if (ideia.getAutor() == null || ideia.getAutor().getId() == null) {
            throw new IllegalStateException("Usuário precisa estar logado para criar uma ideia");
        }
        ideiaDAO.inserir(ideia);
    }

    public List<Ideia> listarTodas() {
        return ideiaDAO.listarOrdenadasPorVotos();
    }

    public Ideia buscarPorId(int id) {
        Ideia ideia = ideiaDAO.buscarPorId(id);
        if (ideia == null) {
            throw new IllegalArgumentException("Ideia não encontrada");
        }
        return ideia;
    }

    // UPDATE
    public void atualizar(Ideia ideia, int usuarioLogadoId) {
        Ideia existente = buscarPorId(ideia.getId());
        if (!existente.getAutor().getId().equals(usuarioLogadoId)) {
            throw new SecurityException("Você só pode editar suas próprias ideias");
        }
        if (ideia.getTitulo() == null || ideia.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }
        if (ideia.getDescricao() == null || ideia.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição é obrigatória");
        }
        ideiaDAO.atualizar(ideia);
    }

    // DELETE
    public void excluir(int id, int usuarioLogadoId) {
        Ideia existente = buscarPorId(id);
        if (!existente.getAutor().getId().equals(usuarioLogadoId)) {
            throw new SecurityException("Você só pode excluir suas próprias ideias");
        }
        ideiaDAO.deletar(id);
    }
}