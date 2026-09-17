package br.com.mvc.service;

import br.com.mvc.dao.IdeiaDAO;
import br.com.mvc.dao.VotoDAO;
import br.com.mvc.model.Ideia;

public class VotoService {

    private final VotoDAO votoDAO;
    private final IdeiaDAO ideiaDAO;

    public VotoService() {
        this.votoDAO = new VotoDAO();
        this.ideiaDAO = new IdeiaDAO();
    }

    public boolean alternarVoto(Long ideiaId, Long usuarioLogadoId) {
        if (ideiaId == null) {
            throw new IllegalArgumentException("Ideia e obrigatoria.");
        }
        if (usuarioLogadoId == null) {
            throw new IllegalArgumentException("Usuario logado e obrigatorio.");
        }

        Ideia ideia = this.ideiaDAO.buscarPorId(ideiaId);
        if (ideia == null) {
            throw new IllegalArgumentException("Ideia nao encontrada.");
        }
        if (ideia.getUsuarioId().equals(usuarioLogadoId)) {
            throw new IllegalArgumentException("Voce nao pode votar na propria ideia.");
        }

        if (this.votoDAO.existeVoto(ideiaId, usuarioLogadoId)) {
            this.votoDAO.deletar(ideiaId, usuarioLogadoId);
            return false;
        }

        this.votoDAO.inserir(ideiaId, usuarioLogadoId);
        return true;
    }
}
