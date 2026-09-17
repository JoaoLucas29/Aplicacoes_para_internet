package br.com.mvc.controller;

import br.com.mvc.model.Ideia;
import br.com.mvc.service.ComentarioService;
import br.com.mvc.service.IdeiaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/ideias")
public class IdeiaServlet extends BaseServlet {

    private static final String LISTA = "/WEB-INF/jsp/ideias/lista.jsp";
    private static final String FORM = "/WEB-INF/jsp/ideias/form.jsp";
    private static final String DETALHE = "/WEB-INF/jsp/ideias/detalhe.jsp";

    private final IdeiaService ideiaService = new IdeiaService();
    private final ComentarioService comentarioService = new ComentarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long usuarioLogadoId = this.usuarioLogadoId(req);

        switch (this.acao(req)) {
            case "novo" -> this.form(req, resp, null);
            case "editar" -> this.editar(req, resp, usuarioLogadoId);
            case "detalhe" -> this.detalhe(req, resp, usuarioLogadoId);
            case "excluir" -> this.excluir(req, resp, usuarioLogadoId);
            default -> {
                req.setAttribute("ideias", this.ideiaService.listar(usuarioLogadoId));
                this.forward(req, resp, LISTA);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        Ideia ideia = this.fromRequest(req);

        try {
            this.ideiaService.salvar(ideia, this.usuarioLogadoId(req));
            this.redirect(req, resp, "/ideias");
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("ideia", ideia);
            this.forward(req, resp, FORM);
        }
    }

    private void form(HttpServletRequest req, HttpServletResponse resp, Ideia ideia)
            throws ServletException, IOException {
        req.setAttribute("ideia", ideia);
        this.forward(req, resp, FORM);
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp, Long usuarioLogadoId)
            throws ServletException, IOException {

        Ideia ideia = this.ideiaService.buscarPorId(this.paramLong(req, "id"), usuarioLogadoId);
        if (ideia == null) {
            this.redirect(req, resp, "/ideias");
            return;
        }
        if (!ideia.getUsuarioId().equals(usuarioLogadoId)) {
            req.setAttribute("erro", "Somente o autor pode editar esta ideia.");
            req.setAttribute("ideias", this.ideiaService.listar(usuarioLogadoId));
            this.forward(req, resp, LISTA);
            return;
        }
        this.form(req, resp, ideia);
    }

    private void detalhe(HttpServletRequest req, HttpServletResponse resp, Long usuarioLogadoId)
            throws ServletException, IOException {

        Ideia ideia = this.ideiaService.buscarPorId(this.paramLong(req, "id"), usuarioLogadoId);
        if (ideia == null) {
            this.redirect(req, resp, "/ideias");
            return;
        }
        req.setAttribute("ideia", ideia);
        req.setAttribute("comentarios", this.comentarioService.listarPorIdeia(ideia.getId()));
        this.forward(req, resp, DETALHE);
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp, Long usuarioLogadoId)
            throws ServletException, IOException {
        try {
            this.ideiaService.deletar(this.paramLong(req, "id"), usuarioLogadoId);
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("ideias", this.ideiaService.listar(usuarioLogadoId));
            this.forward(req, resp, LISTA);
            return;
        }
        this.redirect(req, resp, "/ideias");
    }

    private Ideia fromRequest(HttpServletRequest req) {
        Ideia ideia = new Ideia();
        ideia.setId(this.paramLong(req, "id"));
        ideia.setTitulo(this.param(req, "titulo"));
        ideia.setDescricao(this.param(req, "descricao"));
        return ideia;
    }
}
