package br.com.mvc.controller;

import br.com.mvc.model.Ideia;
import br.com.mvc.service.IdeiaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends BaseServlet {

    private static final int TOP_IDEIAS = 5;

    private final IdeiaService ideiaService = new IdeiaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Ideia> todas = this.ideiaService.listar(this.usuarioLogadoId(req));
        List<Ideia> topIdeias = todas.subList(0, Math.min(TOP_IDEIAS, todas.size()));

        req.setAttribute("topIdeias", topIdeias);
        req.setAttribute("totalIdeias", todas.size());
        this.forward(req, resp, "/WEB-INF/jsp/home.jsp");
    }
}
