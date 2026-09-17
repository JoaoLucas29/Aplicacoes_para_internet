<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${ideia.titulo} - IdeiaViva</title>
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
        <h1>${ideia.titulo}</h1>
    </div>

    <c:if test="${not empty sessionScope.erroFlash}">
        <div class="alert alert-erro">${sessionScope.erroFlash}</div>
        <c:remove var="erroFlash" scope="session"/>
    </c:if>

    <div class="card ideia-detalhe">
        <div class="ideia-meta" style="margin-bottom: 0.75rem;">
            <span>
                por <strong>${ideia.autorNome}</strong>
                em ${fn:replace(fn:substring(ideia.dataCriacao, 0, 16), 'T', ' ')}
            </span>
            <span class="badge-votos">${ideia.totalVotos} voto(s)</span>
        </div>

        <p class="descricao">${ideia.descricao}</p>

        <div class="actions">
            <c:choose>
                <c:when test="${ideia.usuarioId == usuarioLogado.id}">
                    <span class="btn-voto desabilitado">Voce e o autor desta ideia</span>
                </c:when>
                <c:when test="${ideia.jaVotou}">
                    <a class="btn-voto votado"
                       href="${pageContext.request.contextPath}/votos?ideiaId=${ideia.id}&origem=detalhe">
                        Desvotar
                    </a>
                </c:when>
                <c:otherwise>
                    <a class="btn-voto"
                       href="${pageContext.request.contextPath}/votos?ideiaId=${ideia.id}&origem=detalhe">
                        Votar nesta ideia
                    </a>
                </c:otherwise>
            </c:choose>

            <c:if test="${ideia.usuarioId == usuarioLogado.id}">
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/ideias?acao=editar&id=${ideia.id}">
                    Editar
                </a>
                <a class="btn btn-danger" href="${pageContext.request.contextPath}/ideias?acao=excluir&id=${ideia.id}"
                   onclick="return confirm('Excluir esta ideia?');">
                    Excluir
                </a>
            </c:if>

            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/ideias">Voltar</a>
        </div>
    </div>

    <div class="page-header" style="margin-top: 2rem;">
        <h1>Comentarios (${fn:length(comentarios)})</h1>
    </div>

    <div class="card" style="margin-bottom: 1.5rem;">
        <form method="post" action="${pageContext.request.contextPath}/comentarios">
            <input type="hidden" name="ideiaId" value="${ideia.id}">
            <div class="form-group">
                <label for="texto">Deixe seu comentario</label>
                <textarea id="texto" name="texto" rows="3" maxlength="500" required></textarea>
            </div>
            <div class="actions">
                <button type="submit" class="btn">Comentar</button>
            </div>
        </form>
    </div>

    <div class="card">
        <c:choose>
            <c:when test="${empty comentarios}">
                <p class="empty">Nenhum comentario ainda. Seja o primeiro a comentar!</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="comentario" items="${comentarios}">
                    <div class="comentario">
                        <div class="comentario-cabecalho">
                            <span class="comentario-autor">${comentario.autorNome}</span>
                            <span class="comentario-data">
                                ${fn:replace(fn:substring(comentario.dataCriacao, 0, 16), 'T', ' ')}
                            </span>
                        </div>
                        <p class="comentario-texto">${comentario.texto}</p>
                        <c:if test="${comentario.usuarioId == usuarioLogado.id}">
                            <a class="link-excluir"
                               href="${pageContext.request.contextPath}/comentarios?acao=excluir&id=${comentario.id}&ideiaId=${ideia.id}"
                               onclick="return confirm('Excluir este comentario?');">
                                Excluir
                            </a>
                        </c:if>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>
