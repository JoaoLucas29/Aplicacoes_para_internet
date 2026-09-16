package com.ideiasapp.controller;

import com.ideiasapp.model.Usuario;
import com.ideiasapp.service.VotoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/votar")
public class VotoController extends HttpServlet {

    private final VotoService votoService = new VotoService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        Usuario logado = (Usuario) session.getAttribute("usuarioLogado");

        if (logado == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int ideiaId = Integer.parseInt(req.getParameter("ideiaId"));

        try {
            votoService.votar(ideiaId, logado.getId());
        } catch (IllegalStateException e) {
            req.getSession().setAttribute("erroVoto", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/ideias/detalhe?id=" + ideiaId);
    }
}