package br.com.mvc.controller;

import br.com.mvc.model.Usuario;
import br.com.mvc.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/cadastro")
public class CadastroServlet extends BaseServlet {

    private static final String VIEW = "/WEB-INF/jsp/cadastro.jsp";

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (this.usuarioLogado(req) != null) {
            this.redirect(req, resp, "/home");
            return;
        }
        this.forward(req, resp, VIEW);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        Usuario usuario = this.fromRequest(req);

        try {
            this.usuarioService.cadastrar(usuario);
            req.setAttribute("sucesso", "Cadastro realizado com sucesso! Faca login para continuar.");
            this.forward(req, resp, "/WEB-INF/jsp/login.jsp");
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("usuario", usuario);
            this.forward(req, resp, VIEW);
        }
    }

    private Usuario fromRequest(HttpServletRequest req) {
        Usuario usuario = new Usuario();
        usuario.setNome(this.param(req, "nome"));
        usuario.setEmail(this.param(req, "email"));
        usuario.setSenha(this.param(req, "senha"));
        return usuario;
    }
}
