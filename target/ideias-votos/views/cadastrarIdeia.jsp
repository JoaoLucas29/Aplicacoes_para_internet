<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nova Ideia - Ideias &amp; Votos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilo.css">
</head>
<body>
    <jsp:include page="_header.jsp" />

    <div class="container">
        <a class="voltar" href="${pageContext.request.contextPath}/ideias">&larr; Voltar para ideias</a>

        <h1>Nova ideia</h1>
        <p style="color: var(--cor-texto-suave); margin-bottom: 20px;">Compartilhe uma ideia para a comunidade votar e comentar.</p>

        <% if (request.getAttribute("erro") != null) { %>
            <p class="erro"><%= request.getAttribute("erro") %></p>
        <% } %>

        <form action="${pageContext.request.contextPath}/ideias" method="post">
            <label for="titulo">Título</label>
            <input type="text" id="titulo" name="titulo" placeholder="Um resumo direto da sua ideia" required>

            <label for="descricao">Descrição</label>
            <textarea id="descricao" name="descricao" placeholder="Explique a ideia com mais detalhes..." required></textarea>

            <button type="submit">Publicar ideia</button>
        </form>
    </div>
</body>
</html>
