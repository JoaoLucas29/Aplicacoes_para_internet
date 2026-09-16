<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ideias - Ideias &amp; Votos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilo.css">
</head>
<body>
    <jsp:include page="_header.jsp" />

    <div class="container">
        <div class="topo">
            <div>
                <h1>Ideias da comunidade</h1>
                <p>As mais votadas aparecem primeiro. Vote e comente para ajudar a priorizar.</p>
            </div>
        </div>

        <ul class="lista-ideias">
            <c:forEach var="ideia" items="${ideias}">
                <li>
                    <a href="${pageContext.request.contextPath}/ideias/detalhe?id=${ideia.id}">
                        <h2>${ideia.titulo}</h2>
                        <p class="ideia-descricao">${ideia.descricao}</p>
                    </a>
                    <div class="meta-ideia">
                        <span class="autor">por <strong>${ideia.autor.nome}</strong></span>
                        <span class="votos">👍 ${ideia.totalVotos} voto<c:if test="${ideia.totalVotos != 1}">s</c:if></span>
                    </div>

                    <c:if test="${sessionScope.usuarioLogado != null && sessionScope.usuarioLogado.id == ideia.autor.id}">
                        <div class="acoes">
                            <a href="${pageContext.request.contextPath}/ideias/editar?id=${ideia.id}">✏️ Editar</a>
                            <form action="${pageContext.request.contextPath}/ideias/excluir" method="post" class="form-inline"
                                  onsubmit="return confirm('Tem certeza que deseja excluir esta ideia?');">
                                <input type="hidden" name="id" value="${ideia.id}">
                                <button type="submit">🗑️ Excluir</button>
                            </form>
                        </div>
                    </c:if>
                </li>
            </c:forEach>
        </ul>

        <c:if test="${empty ideias}">
            <div class="vazio">
                <span class="vazio__icone">💡</span>
                <p>Nenhuma ideia cadastrada ainda.</p>
                <c:if test="${sessionScope.usuarioLogado != null}">
                    <a class="btn" href="${pageContext.request.contextPath}/ideias/nova">Publicar a primeira ideia</a>
                </c:if>
            </div>
        </c:if>
    </div>

    <script src="${pageContext.request.contextPath}/resources/js/script.js"></script>
</body>
</html>
