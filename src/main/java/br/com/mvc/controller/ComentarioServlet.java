package br.com.mvc.controller;

import br.com.mvc.model.Comentario;
import br.com.mvc.service.ComentarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/comentarios")
public class ComentarioServlet extends BaseServlet {

    private final ComentarioService comentarioService = new ComentarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long ideiaId = this.paramLong(req, "ideiaId");

        if ("excluir".equals(this.acao(req))) {
            try {
                this.comentarioService.deletar(this.paramLong(req, "id"), this.usuarioLogadoId(req));
            } catch (IllegalArgumentException e) {
                req.getSession(true).setAttribute("erroFlash", e.getMessage());
            }
        }

        this.redirect(req, resp, "/ideias?acao=detalhe&id=" + ideiaId);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        Long ideiaId = this.paramLong(req, "ideiaId");

        Comentario comentario = new Comentario();
        comentario.setIdeiaId(ideiaId);
        comentario.setUsuarioId(this.usuarioLogadoId(req));
        comentario.setTexto(this.param(req, "texto"));

        try {
            this.comentarioService.comentar(comentario);
        } catch (IllegalArgumentException e) {
            req.getSession(true).setAttribute("erroFlash", e.getMessage());
        }

        this.redirect(req, resp, "/ideias?acao=detalhe&id=" + ideiaId);
    }
}
