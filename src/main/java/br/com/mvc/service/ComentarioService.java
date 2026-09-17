package br.com.mvc.service;

import br.com.mvc.dao.ComentarioDAO;
import br.com.mvc.dao.IdeiaDAO;
import br.com.mvc.model.Comentario;
import br.com.mvc.model.Ideia;

import java.util.List;

public class ComentarioService {

    private static final int TEXTO_MAXIMO = 500;

    private final ComentarioDAO comentarioDAO;
    private final IdeiaDAO ideiaDAO;

    public ComentarioService() {
        this.comentarioDAO = new ComentarioDAO();
        this.ideiaDAO = new IdeiaDAO();
    }

    public List<Comentario> listarPorIdeia(Long ideiaId) {
        if (ideiaId == null) {
            throw new IllegalArgumentException("Ideia e obrigatoria.");
        }
        return this.comentarioDAO.listarPorIdeia(ideiaId);
    }

    public void comentar(Comentario comentario) {
        if (comentario == null) {
            throw new IllegalArgumentException("Comentario e obrigatorio.");
        }
        if (comentario.getUsuarioId() == null) {
            throw new IllegalArgumentException("Usuario logado e obrigatorio.");
        }

        comentario.setTexto(this.normalizar(comentario.getTexto()));
        if (comentario.getTexto() == null) {
            throw new IllegalArgumentException("Comentario nao pode ser vazio.");
        }
        if (comentario.getTexto().length() > TEXTO_MAXIMO) {
            throw new IllegalArgumentException("Comentario deve ter no maximo " + TEXTO_MAXIMO + " caracteres.");
        }

        Ideia ideia = this.ideiaDAO.buscarPorId(comentario.getIdeiaId());
        if (ideia == null) {
            throw new IllegalArgumentException("Ideia nao encontrada.");
        }

        this.comentarioDAO.inserir(comentario);
    }

    public void deletar(Long id, Long usuarioLogadoId) {
        if (id == null) {
            throw new IllegalArgumentException("Id e obrigatorio para excluir.");
        }
        Comentario existente = this.comentarioDAO.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("Comentario nao encontrado.");
        }
        if (usuarioLogadoId == null || !existente.getUsuarioId().equals(usuarioLogadoId)) {
            throw new IllegalArgumentException("Somente o autor pode excluir este comentario.");
        }
        this.comentarioDAO.deletar(id);
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpo = valor.trim();
        return limpo.isEmpty() ? null : limpo;
    }
}
