<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>
        <c:choose>
            <c:when test="${empty ideia.id}">Nova ideia</c:when>
            <c:otherwise>Editar ideia</c:otherwise>
        </c:choose>
        - IdeiaViva
    </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<header class="topbar">
    <div class="container">
        <strong>IdeiaViva</strong>
        <nav>
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/ideias">Ideias</a>
            <a href="${pageContext.request.contextPath}/perfil">Meu perfil</a>
            <a href="${pageContext.request.contextPath}/logout">Sair</a>
        </nav>
    </div>
</header>

<main class="container">
    <div class="page-header">
        <h1>
            <c:choose>
                <c:when test="${empty ideia.id}">Nova ideia</c:when>
                <c:otherwise>Editar ideia</c:otherwise>
            </c:choose>
        </h1>
    </div>

    <div class="card" style="max-width: 640px;">
        <c:if test="${not empty erro}">
            <div class="alert alert-erro">${erro}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/ideias">
            <input type="hidden" name="id" value="${ideia.id}">

            <div class="form-group">
                <label for="titulo">Titulo</label>
                <input type="text" id="titulo" name="titulo" value="${ideia.titulo}"
                       maxlength="150" required autofocus>
            </div>

            <div class="form-group">
                <label for="descricao">Descricao</label>
                <textarea id="descricao" name="descricao" rows="6" required>${ideia.descricao}</textarea>
            </div>

            <div class="actions">
                <button type="submit" class="btn">Salvar</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/ideias">Cancelar</a>
            </div>
        </form>
    </div>
</main>
</body>
</html>
