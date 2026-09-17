<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Home - IdeiaViva</title>
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
            <span>Ola, ${usuarioLogado.nome}</span>
            <a href="${pageContext.request.contextPath}/logout">Sair</a>
        </nav>
    </div>
</header>

<main class="container">
    <div class="page-header">
        <h1>Painel</h1>
    </div>

    <div class="grid-cards">
        <a class="menu-card" href="${pageContext.request.contextPath}/ideias">
            <strong>Ideias</strong>
            <span>Ver todas as ideias, sugerir novas, votar e comentar.</span>
        </a>
        <a class="menu-card" href="${pageContext.request.contextPath}/ideias?acao=novo">
            <strong>Nova ideia</strong>
            <span>Sugira uma nova ideia para a comunidade.</span>
        </a>
        <a class="menu-card" href="${pageContext.request.contextPath}/perfil">
            <strong>Meu perfil</strong>
            <span>Atualize seus dados de cadastro.</span>
        </a>
    </div>

    <div class="page-header" style="margin-top: 2rem;">
        <h1>Ideias mais votadas</h1>
    </div>

    <div class="card">
        <c:choose>
            <c:when test="${empty topIdeias}">
                <p class="empty">Ainda nao ha ideias cadastradas. Seja o primeiro a sugerir uma!</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="ideia" items="${topIdeias}" varStatus="posicao">
                    <div class="ranking-item">
                        <span class="ranking-posicao">${posicao.index + 1}</span>
                        <span class="ranking-titulo">
                            <a href="${pageContext.request.contextPath}/ideias?acao=detalhe&id=${ideia.id}">
                                ${ideia.titulo}
                            </a>
                            <small>por ${ideia.autorNome}</small>
                        </span>
                        <span class="badge-votos">${ideia.totalVotos} voto(s)</span>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <p style="margin-top: 1rem; color: var(--muted);">
        Total de ideias cadastradas: ${totalIdeias}
    </p>
</main>
</body>
</html>
