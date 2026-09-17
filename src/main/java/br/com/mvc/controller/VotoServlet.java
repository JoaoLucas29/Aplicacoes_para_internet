package br.com.mvc.controller;

import br.com.mvc.service.VotoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/votos")
public class VotoServlet extends BaseServlet {

    private final VotoService votoService = new VotoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long ideiaId = this.paramLong(req, "ideiaId");
        String origem = this.param(req, "origem");

        try {
            this.votoService.alternarVoto(ideiaId, this.usuarioLogadoId(req));
        } catch (IllegalArgumentException e) {
            req.getSession(true).setAttribute("erroFlash", e.getMessage());
        }

        if ("detalhe".equals(origem)) {
            this.redirect(req, resp, "/ideias?acao=detalhe&id=" + ideiaId);
        } else {
            this.redirect(req, resp, "/ideias");
        }
    }
}
