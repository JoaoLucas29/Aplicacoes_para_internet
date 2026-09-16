package com.ideiasapp.controller;

import com.ideiasapp.model.Comentario;
import com.ideiasapp.model.Usuario;
import com.ideiasapp.service.ComentarioService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet({"/comentar", "/comentar/editar", "/comentar/excluir"})
public class ComentarioController extends HttpServlet {

    private final ComentarioService comentarioService = new ComentarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        // UPDATE - formulário de edição
        if (path.equals("/comentar/editar")) {
            Usuario logado = (Usuario) req.getSession().getAttribute("usuarioLogado");
            if (logado == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            int id = Integer.parseInt(req.getParameter("id"));
            Comentario comentario = comentarioService.buscarPorId(id);
            if (!comentario.getAutor().getId().equals(logado.getId())) {
                req.getSession().setAttribute("erroComentario", "Você só pode editar seus próprios comentários");
                resp.sendRedirect(req.getContextPath() + "/ideias/detalhe?id=" + comentario.getIdeiaId());
                return;
            }
            req.setAttribute("comentario", comentario);
            req.getRequestDispatcher("/views/editarComentario.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession();
        Usuario logado = (Usuario) session.getAttribute("usuarioLogado");

        if (logado == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // UPDATE
        if (path.equals("/comentar/editar")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Comentario comentario = comentarioService.buscarPorId(id);
            try {
                comentarioService.atualizar(id, req.getParameter("texto"), logado.getId());
                resp.sendRedirect(req.getContextPath() + "/ideias/detalhe?id=" + comentario.getIdeiaId());
            } catch (IllegalArgumentException | SecurityException e) {
                req.setAttribute("erro", e.getMessage());
                req.setAttribute("comentario", comentario);
                req.getRequestDispatcher("/views/editarComentario.jsp").forward(req, resp);
            }
            return;
        }

        // DELETE
        if (path.equals("/comentar/excluir")) {
            int id = Integer.parseInt(req.getParameter("id"));
            int ideiaId = Integer.parseInt(req.getParameter("ideiaId"));
            try {
                comentarioService.excluir(id, logado.getId());
            } catch (SecurityException | IllegalArgumentException e) {
                session.setAttribute("erroComentario", e.getMessage());
            }
            resp.sendRedirect(req.getContextPath() + "/ideias/detalhe?id=" + ideiaId);
            return;
        }

        // CREATE
        int ideiaId = Integer.parseInt(req.getParameter("ideiaId"));
        String texto = req.getParameter("texto");

        try {
            Comentario comentario = new Comentario(texto, ideiaId, logado);
            comentarioService.comentar(comentario);
        } catch (IllegalArgumentException e) {
            session.setAttribute("erroComentario", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/ideias/detalhe?id=" + ideiaId);
    }
}
