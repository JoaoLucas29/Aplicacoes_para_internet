<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Ideias &amp; Votos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilo.css">
</head>
<body class="auth-body">
    <div class="auth-card">
        <div class="auth-marca"><span class="auth-marca__icone">💡</span> Ideias &amp; Votos</div>
        <h1>Bem-vindo de volta</h1>
        <p class="auth-subtitulo">Entre para votar, comentar e publicar suas ideias.</p>

        <% if (request.getAttribute("erro") != null) { %>
            <p class="erro"><%= request.getAttribute("erro") %></p>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <label for="nome">Nome</label>
            <input type="text" id="nome" name="nome" placeholder="Seu nome de usuário" required>

            <label for="senha">Senha</label>
            <input type="password" id="senha" name="senha" placeholder="Sua senha" required>

            <button type="submit">Entrar</button>
        </form>

        <p class="auth-rodape">Não tem conta? <a href="${pageContext.request.contextPath}/cadastro">Cadastre-se</a></p>

        <div class="auth-divisor">ou</div>

        <form action="${pageContext.request.contextPath}/visitante" method="get">
            <button type="submit" class="btn-secundario">Entrar como visitante</button>
        </form>
        <p class="auth-dica">No modo visitante você pode navegar e ver as ideias, mas não pode votar, comentar ou publicar.</p>
    </div>
</body>
</html>
