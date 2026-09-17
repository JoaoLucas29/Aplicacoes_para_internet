<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Ideias - IdeiaViva</title>
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
        <h1>Ideias</h1>
        <a class="btn" href="${pageContext.request.contextPath}/ideias?acao=novo">Sugerir ideia</a>
    </div>

    <c:if test="${not empty erro}">
        <div class="alert alert-erro">${erro}</div>
    </c:if>
    <c:if test="${not empty sessionScope.erroFlash}">
        <div class="alert alert-erro">${sessionScope.erroFlash}</div>
        <c:remove var="erroFlash" scope="session"/>
    </c:if>

    <c:choose>
        <c:when test="${empty ideias}">
            <p class="empty">Nenhuma ideia cadastrada ainda. Que tal sugerir a primeira?</p>
        </c:when>
        <c:otherwise>
            <div class="grid-ideias">
                <c:forEach var="ideia" items="${ideias}">
                    <c:set var="descricaoResumida" value="${ideia.descricao}"/>
                    <div class="ideia-card">
                        <h3>
                            <a href="${pageContext.request.contextPath}/ideias?acao=detalhe&id=${ideia.id}">
                                ${ideia.titulo}
                            </a>
                        </h3>
                        <c:choose>
                            <c:when test="${fn:length(descricaoResumida) > 140}">
                                <p>${fn:substring(descricaoResumida, 0, 140)}...</p>
                            </c:when>
                            <c:otherwise>
                                <p>${descricaoResumida}</p>
                            </c:otherwise>
                        </c:choose>
                        <div class="ideia-meta">
                            <span>por ${ideia.autorNome}</span>
                            <span class="badge-votos">${ideia.totalVotos} voto(s)</span>
                        </div>

                        <c:choose>
                            <c:when test="${ideia.usuarioId == usuarioLogado.id}">
                                <span class="btn-voto desabilitado">Sua propria ideia</span>
                            </c:when>
                            <c:when test="${ideia.jaVotou}">
                                <a class="btn-voto votado"
                                   href="${pageContext.request.contextPath}/votos?ideiaId=${ideia.id}&origem=lista">
                                    Desvotar
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a class="btn-voto"
                                   href="${pageContext.request.contextPath}/votos?ideiaId=${ideia.id}&origem=lista">
                                    Votar
                                </a>
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${ideia.usuarioId == usuarioLogado.id}">
                            <div class="links">
                                <a href="${pageContext.request.contextPath}/ideias?acao=editar&id=${ideia.id}">Editar</a>
                                <a class="link-excluir"
                                   href="${pageContext.request.contextPath}/ideias?acao=excluir&id=${ideia.id}"
                                   onclick="return confirm('Excluir esta ideia?');">Excluir</a>
                            </div>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
