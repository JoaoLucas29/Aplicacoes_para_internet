package com.ideiasapp.controller;

import com.ideiasapp.model.Usuario;
import com.ideiasapp.service.UsuarioService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet({"/cadastro", "/login", "/logout", "/visitante", "/perfil", "/perfil/excluir"})
public class UsuarioController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        if (path.equals("/logout")) {
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (path.equals("/visitante")) {
            // Entrar sem cadastro: sessão apenas de leitura, sem usuário associado
            HttpSession session = req.getSession();
            session.setAttribute("visitante", Boolean.TRUE);
            resp.sendRedirect(req.getContextPath() + "/ideias");
            return;
        }

        if (path.equals("/cadastro")) {
            req.getRequestDispatcher("/views/cadastroUsuario.jsp").forward(req, resp);
            return;
        }

        if (path.equals("/perfil")) {
            Usuario logado = (Usuario) req.getSession().getAttribute("usuarioLogado");
            if (logado == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            req.setAttribute("usuario", logado);
            req.getRequestDispatcher("/views/perfil.jsp").forward(req, resp);
            return;
        }

        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        // CREATE - cadastro de usuário
        if (path.equals("/cadastro")) {
            try {
                Usuario usuario = new Usuario(
                        req.getParameter("nome"),
                        req.getParameter("senha")
                );
                usuarioService.cadastrar(usuario);
                resp.sendRedirect(req.getContextPath() + "/login");
            } catch (IllegalArgumentException e) {
                req.setAttribute("erro", e.getMessage());
                req.getRequestDispatcher("/views/cadastroUsuario.jsp").forward(req, resp);
            }
            return;
        }

        // READ - autenticação
        if (path.equals("/login")) {
            String nome = req.getParameter("nome");
            String senha = req.getParameter("senha");
            Usuario usuario = usuarioService.autenticar(nome, senha);

            if (usuario == null) {
                req.setAttribute("erro", "Nome ou senha inválidos");
                req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
                return;
            }

            HttpSession session = req.getSession();
            session.removeAttribute("visitante");
            session.setAttribute("usuarioLogado", usuario);
            resp.sendRedirect(req.getContextPath() + "/ideias");
            return;
        }

        // UPDATE - edição do próprio perfil
        if (path.equals("/perfil")) {
            Usuario logado = (Usuario) req.getSession().getAttribute("usuarioLogado");
            if (logado == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            try {
                Usuario dados = new Usuario();
                dados.setId(logado.getId());
                dados.setNome(req.getParameter("nome"));
                dados.setSenha(req.getParameter("senha")); // pode vir vazio: mantém a senha atual
                usuarioService.atualizar(dados);

                Usuario atualizado = usuarioService.buscarPorId(logado.getId());
                req.getSession().setAttribute("usuarioLogado", atualizado);
                req.setAttribute("sucesso", "Perfil atualizado com sucesso");
                req.setAttribute("usuario", atualizado);
                req.getRequestDispatcher("/views/perfil.jsp").forward(req, resp);
            } catch (IllegalArgumentException e) {
                req.setAttribute("erro", e.getMessage());
                req.setAttribute("usuario", logado);
                req.getRequestDispatcher("/views/perfil.jsp").forward(req, resp);
            }
            return;
        }

        // DELETE - exclusão da própria conta
        if (path.equals("/perfil/excluir")) {
            Usuario logado = (Usuario) req.getSession().getAttribute("usuarioLogado");
            if (logado == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            usuarioService.excluir(logado.getId());
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
