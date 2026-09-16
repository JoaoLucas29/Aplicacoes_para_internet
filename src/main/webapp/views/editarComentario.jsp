<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Comentário - Ideias &amp; Votos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilo.css">
</head>
<body>
    <jsp:include page="_header.jsp" />

    <div class="container">
        <a class="voltar" href="${pageContext.request.contextPath}/ideias/detalhe?id=${comentario.ideiaId}">&larr; Voltar para a ideia</a>

        <h1>Editar comentário</h1>

        <% if (request.getAttribute("erro") != null) { %>
            <p class="erro"><%= request.getAttribute("erro") %></p>
        <% } %>

        <form action="${pageContext.request.contextPath}/comentar/editar" method="post">
            <input type="hidden" name="id" value="${comentario.id}">

            <label for="texto">Comentário</label>
            <textarea id="texto" name="texto" maxlength="500" required>${comentario.texto}</textarea>

            <button type="submit">Salvar alterações</button>
        </form>
    </div>
</body>
</html>
