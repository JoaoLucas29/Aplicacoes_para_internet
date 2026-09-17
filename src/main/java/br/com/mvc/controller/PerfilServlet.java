package br.com.mvc.controller;

import br.com.mvc.model.Usuario;
import br.com.mvc.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/perfil")
public class PerfilServlet extends BaseServlet {

    private static final String VIEW = "/WEB-INF/jsp/perfil.jsp";

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if ("excluir".equals(this.acao(req))) {
            try {
                this.usuarioService.deletar(this.usuarioLogadoId(req));
                HttpSession session = req.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                this.redirect(req, resp, "/login");
            } catch (IllegalArgumentException e) {
                req.setAttribute("erro", e.getMessage());
                req.setAttribute("usuario", this.usuarioLogado(req));
                this.forward(req, resp, VIEW);
            }
            return;
        }

        req.setAttribute("usuario", this.usuarioService.buscarPorId(this.usuarioLogadoId(req)));
        this.forward(req, resp, VIEW);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        Usuario usuario = this.fromRequest(req);

        try {
            this.usuarioService.atualizar(usuario);

            // atualiza tambem os dados guardados na sessao
            req.getSession(true).setAttribute("usuarioLogado", usuario);

            req.setAttribute("sucesso", "Dados atualizados com sucesso.");
            req.setAttribute("usuario", usuario);
            this.forward(req, resp, VIEW);
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("usuario", usuario);
            this.forward(req, resp, VIEW);
        }
    }

    private Usuario fromRequest(HttpServletRequest req) {
        Usuario usuario = new Usuario();
        usuario.setId(this.usuarioLogadoId(req));
        usuario.setNome(this.param(req, "nome"));
        usuario.setEmail(this.param(req, "email"));
        usuario.setSenha(this.param(req, "senha"));
        return usuario;
    }
}
