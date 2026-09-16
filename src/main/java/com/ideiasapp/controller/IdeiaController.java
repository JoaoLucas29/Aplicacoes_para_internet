package com.ideiasapp.controller;

import com.ideiasapp.model.Ideia;
import com.ideiasapp.model.Usuario;
import com.ideiasapp.service.ComentarioService;
import com.ideiasapp.service.IdeiaService;
import com.ideiasapp.service.VotoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet({"/ideias", "/ideias/nova", "/ideias/detalhe", "/ideias/editar", "/ideias/excluir"})
public class IdeiaController extends HttpServlet {

    private final IdeiaService ideiaService = new IdeiaService();
    private final VotoService votoService = new VotoService();
    private final ComentarioService comentarioService = new ComentarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        if (path.equals("/ideias/nova")) {
            Usuario logado = (Usuario) req.getSession().getAttribute("usuarioLogado");
            if (logado == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            req.getRequestDispatcher("/views/cadastrarIdeia.jsp").forward(req, resp);
            return;
        }

        // UPDATE - formulário de edição
        if (path.equals("/ideias/editar")) {
            Usuario logado = (Usuario) req.getSession().getAttribute("usuarioLogado");
            if (logado == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            int id = Integer.parseInt(req.getParameter("id"));
            Ideia ideia = ideiaService.buscarPorId(id);
            if (!ideia.getAutor().getId().equals(logado.getId())) {
                req.getSession().setAttribute("erroVoto", "Você só pode editar suas próprias ideias");
                resp.sendRedirect(req.getContextPath() + "/ideias/detalhe?id=" + id);
                return;
            }
            req.setAttribute("ideia", ideia);
            req.getRequestDispatcher("/views/editarIdeia.jsp").forward(req, resp);
            return;
        }

        if (path.equals("/ideias/detalhe")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Ideia ideia = ideiaService.buscarPorId(id);
            req.setAttribute("ideia", ideia);
            req.setAttribute("comentarios", comentarioService.listarPorIdeia(id));

            HttpSession session = req.getSession();
            Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
            if (logado != null) {
                req.setAttribute("usuarioJaVotou", votoService.usuarioJaVotou(id, logado.getId()));
            }

            req.getRequestDispatcher("/views/detalheIdeia.jsp").forward(req, resp);
            return;
        }

        // /ideias -> listagem (READ)
        req.setAttribute("ideias", ideiaService.listarTodas());
        req.getRequestDispatcher("/views/listarIdeias.jsp").forward(req, resp);
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
        if (path.equals("/ideias/editar")) {
            int id = Integer.parseInt(req.getParameter("id"));
            try {
                Ideia ideia = new Ideia();
                ideia.setId(id);
                ideia.setTitulo(req.getParameter("titulo"));
                ideia.setDescricao(req.getParameter("descricao"));
                ideiaService.atualizar(ideia, logado.getId());
                resp.sendRedirect(req.getContextPath() + "/ideias/detalhe?id=" + id);
            } catch (IllegalArgumentException | SecurityException e) {
                Ideia ideia = ideiaService.buscarPorId(id);
                req.setAttribute("erro", e.getMessage());
                req.setAttribute("ideia", ideia);
                req.getRequestDispatcher("/views/editarIdeia.jsp").forward(req, resp);
            }
            return;
        }

        // DELETE
        if (path.equals("/ideias/excluir")) {
            int id = Integer.parseInt(req.getParameter("id"));
            try {
                ideiaService.excluir(id, logado.getId());
            } catch (SecurityException e) {
                session.setAttribute("erroVoto", e.getMessage());
            }
            resp.sendRedirect(req.getContextPath() + "/ideias");
            return;
        }

        // CREATE
        try {
            Ideia ideia = new Ideia(
                    req.getParameter("titulo"),
                    req.getParameter("descricao"),
                    logado
            );
            ideiaService.cadastrar(ideia);
            resp.sendRedirect(req.getContextPath() + "/ideias");
        } catch (IllegalArgumentException | IllegalStateException e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/views/cadastrarIdeia.jsp").forward(req, resp);
        }
    }
}
