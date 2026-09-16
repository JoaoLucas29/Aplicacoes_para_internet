<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Ideia - Ideias &amp; Votos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilo.css">
</head>
<body>
    <jsp:include page="_header.jsp" />

    <div class="container">
        <a class="voltar" href="${pageContext.request.contextPath}/ideias/detalhe?id=${ideia.id}">&larr; Voltar para a ideia</a>

        <h1>Editar ideia</h1>

        <% if (request.getAttribute("erro") != null) { %>
            <p class="erro"><%= request.getAttribute("erro") %></p>
        <% } %>

        <form action="${pageContext.request.contextPath}/ideias/editar" method="post">
            <input type="hidden" name="id" value="${ideia.id}">

            <label for="titulo">Título</label>
            <input type="text" id="titulo" name="titulo" value="${ideia.titulo}" required>

            <label for="descricao">Descrição</label>
            <textarea id="descricao" name="descricao" required>${ideia.descricao}</textarea>

            <button type="submit">Salvar alterações</button>
        </form>
    </div>
</body>
</html>
