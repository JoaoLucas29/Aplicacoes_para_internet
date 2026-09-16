package com.ideiasapp.service;

import com.ideiasapp.dao.VotoDAO;
import com.ideiasapp.model.Voto;

public class VotoService {

    private final VotoDAO votoDAO = new VotoDAO();

    public void votar(int ideiaId, int usuarioId) {
        if (votoDAO.jaVotou(ideiaId, usuarioId)) {
            throw new IllegalStateException("Usuário já votou nessa ideia");
        }
        votoDAO.inserir(new Voto(ideiaId, usuarioId));
    }

    public void removerVoto(int ideiaId, int usuarioId) {
        if (!votoDAO.jaVotou(ideiaId, usuarioId)) {
            throw new IllegalStateException("Usuário ainda não votou nessa ideia");
        }
        votoDAO.remover(ideiaId, usuarioId);
    }

    public boolean usuarioJaVotou(int ideiaId, int usuarioId) {
        return votoDAO.jaVotou(ideiaId, usuarioId);
    }
}