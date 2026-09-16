<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${ideia.titulo} - Ideias &amp; Votos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilo.css">
</head>
<body>
    <jsp:include page="_header.jsp" />

    <div class="container">
        <a class="voltar" href="${pageContext.request.contextPath}/ideias">&larr; Voltar para ideias</a>

        <div class="ideia-cabecalho">
            <h1>${ideia.titulo}</h1>
            <p>${ideia.descricao}</p>
            <div class="meta-ideia">
                <span class="autor">por <strong>${ideia.autor.nome}</strong></span>
                <span class="votos">👍 ${ideia.totalVotos} voto<c:if test="${ideia.totalVotos != 1}">s</c:if></span>
            </div>

            <c:if test="${sessionScope.usuarioLogado != null && sessionScope.usuarioLogado.id == ideia.autor.id}">
                <div class="acoes">
                    <a href="${pageContext.request.contextPath}/ideias/editar?id=${ideia.id}">✏️ Editar ideia</a>
                    <form action="${pageContext.request.contextPath}/ideias/excluir" method="post" class="form-inline"
                          onsubmit="return confirm('Tem certeza que deseja excluir esta ideia?');">
                        <input type="hidden" name="id" value="${ideia.id}">
                        <button type="submit">🗑️ Excluir ideia</button>
                    </form>
                </div>
            </c:if>
        </div>

        <c:if test="${sessionScope.visitante == true}">
            <p class="dica">Você está no modo visitante. Entre com uma conta para votar e comentar.</p>
        </c:if>

        <c:if test="${sessionScope.usuarioLogado != null}">
            <c:if test="${sessionScope.erroVoto != null}">
                <p class="erro">${sessionScope.erroVoto}</p>
                <% session.removeAttribute("erroVoto"); %>
            </c:if>

            <div class="bloco-votar">
                <c:choose>
                    <c:when test="${usuarioJaVotou}">
                        <span class="ja-votou">✓ Você já votou nessa ideia</span>
                    </c:when>
                    <c:otherwise>
                        <span>Essa ideia parece boa?</span>
                        <form action="${pageContext.request.contextPath}/votar" method="post">
                            <input type="hidden" name="ideiaId" value="${ideia.id}">
                            <button type="submit">👍 Votar nesta ideia</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <h2>Comentários</h2>

        <c:if test="${sessionScope.usuarioLogado != null}">
            <c:if test="${sessionScope.erroComentario != null}">
                <p class="erro">${sessionScope.erroComentario}</p>
                <% session.removeAttribute("erroComentario"); %>
            </c:if>

            <form class="form-comentario" action="${pageContext.request.contextPath}/comentar" method="post">
                <input type="hidden" name="ideiaId" value="${ideia.id}">
                <textarea name="texto" maxlength="500" required placeholder="Escreva um comentário..."></textarea>
                <button type="submit">Comentar</button>
            </form>
        </c:if>

        <ul class="lista-comentarios">
            <c:forEach var="comentario" items="${comentarios}">
                <li>
                    <strong>${comentario.autor.nome}</strong>
                    <p>${comentario.texto}</p>

                    <c:if test="${sessionScope.usuarioLogado != null && sessionScope.usuarioLogado.id == comentario.autor.id}">
                        <div class="acoes">
                            <a href="${pageContext.request.contextPath}/comentar/editar?id=${comentario.id}">✏️ Editar</a>
                            <form action="${pageContext.request.contextPath}/comentar/excluir" method="post" class="form-inline"
                                  onsubmit="return confirm('Tem certeza que deseja excluir este comentário?');">
                                <input type="hidden" name="id" value="${comentario.id}">
                                <input type="hidden" name="ideiaId" value="${ideia.id}">
                                <button type="submit">🗑️ Excluir</button>
                            </form>
                        </div>
                    </c:if>
                </li>
            </c:forEach>
        </ul>

        <c:if test="${empty comentarios}">
            <div class="vazio">
                <span class="vazio__icone">💬</span>
                <p>Nenhum comentário ainda. Seja o primeiro a opinar!</p>
            </div>
        </c:if>
    </div>

    <script src="${pageContext.request.contextPath}/resources/js/script.js"></script>
</body>
</html>
