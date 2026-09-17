package br.com.mvc.service;

import br.com.mvc.dao.ComentarioDAO;
import br.com.mvc.dao.IdeiaDAO;
import br.com.mvc.dao.VotoDAO;
import br.com.mvc.model.Ideia;

import java.util.List;

public class IdeiaService {

    private static final int TITULO_MAXIMO = 150;

    private final IdeiaDAO ideiaDAO;
    private final VotoDAO votoDAO;
    private final ComentarioDAO comentarioDAO;

    public IdeiaService() {
        this.ideiaDAO = new IdeiaDAO();
        this.votoDAO = new VotoDAO();
        this.comentarioDAO = new ComentarioDAO();
    }

    public List<Ideia> listar(Long usuarioLogadoId) {
        return this.ideiaDAO.listarTodos(usuarioLogadoId);
    }

    public Ideia buscarPorId(Long id, Long usuarioLogadoId) {
        if (id == null) {
            return null;
        }
        return this.ideiaDAO.buscarPorId(id, usuarioLogadoId);
    }

    public void salvar(Ideia ideia, Long usuarioLogadoId) {
        if (ideia == null) {
            throw new IllegalArgumentException("Ideia e obrigatoria.");
        }
        if (usuarioLogadoId == null) {
            throw new IllegalArgumentException("Usuario logado e obrigatorio.");
        }

        this.prepararDados(ideia);
        this.validarCamposObrigatorios(ideia);

        if (ideia.getId() == null) {
            ideia.setUsuarioId(usuarioLogadoId);
            this.ideiaDAO.inserir(ideia);
            return;
        }

        Ideia existente = this.ideiaDAO.buscarPorId(ideia.getId());
        if (existente == null) {
            throw new IllegalArgumentException("Ideia nao encontrada para alteracao.");
        }
        if (!existente.getUsuarioId().equals(usuarioLogadoId)) {
            throw new IllegalArgumentException("Somente o autor pode editar esta ideia.");
        }

        this.ideiaDAO.alterar(ideia);
    }

    public void deletar(Long id, Long usuarioLogadoId) {
        if (id == null) {
            throw new IllegalArgumentException("Id e obrigatorio para excluir.");
        }
        Ideia existente = this.ideiaDAO.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("Ideia nao encontrada.");
        }
        if (usuarioLogadoId == null || !existente.getUsuarioId().equals(usuarioLogadoId)) {
            throw new IllegalArgumentException("Somente o autor pode excluir esta ideia.");
        }

        this.votoDAO.deletarPorIdeia(id);
        this.comentarioDAO.listarPorIdeia(id).forEach(c -> this.comentarioDAO.deletar(c.getId()));
        this.ideiaDAO.deletar(id);
    }

    private void prepararDados(Ideia ideia) {
        ideia.setTitulo(this.normalizar(ideia.getTitulo()));
        ideia.setDescricao(this.normalizar(ideia.getDescricao()));
    }

    private void validarCamposObrigatorios(Ideia ideia) {
        if (ideia.getTitulo() == null) {
            throw new IllegalArgumentException("Titulo e obrigatorio.");
        }
        if (ideia.getTitulo().length() > TITULO_MAXIMO) {
            throw new IllegalArgumentException("Titulo deve ter no maximo " + TITULO_MAXIMO + " caracteres.");
        }
        if (ideia.getDescricao() == null) {
            throw new IllegalArgumentException("Descricao e obrigatoria.");
        }
    }

    private String normalizar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpo = valor.trim();
        return limpo.isEmpty() ? null : limpo;
    }
}
