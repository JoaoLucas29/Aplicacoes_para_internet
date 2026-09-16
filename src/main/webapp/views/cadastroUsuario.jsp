<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro - Ideias &amp; Votos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilo.css">
</head>
<body class="auth-body">
    <div class="auth-card">
        <div class="auth-marca"><span class="auth-marca__icone">💡</span> Ideias &amp; Votos</div>
        <h1>Criar conta</h1>
        <p class="auth-subtitulo">Cadastre-se para votar, comentar e publicar ideias.</p>

        <% if (request.getAttribute("erro") != null) { %>
            <p class="erro"><%= request.getAttribute("erro") %></p>
        <% } %>

        <form action="${pageContext.request.contextPath}/cadastro" method="post">
            <label for="nome">Nome</label>
            <input type="text" id="nome" name="nome" minlength="3" placeholder="Escolha um nome de usuário" required>

            <label for="senha">Senha</label>
            <input type="password" id="senha" name="senha" minlength="6" placeholder="Pelo menos 6 caracteres" required>

            <button type="submit">Cadastrar</button>
        </form>

        <p class="auth-rodape">Já tem conta? <a href="${pageContext.request.contextPath}/login">Entrar</a></p>
        <p class="auth-rodape">Ou <a href="${pageContext.request.contextPath}/visitante">entre como visitante</a>.</p>
    </div>
</body>
</html>
