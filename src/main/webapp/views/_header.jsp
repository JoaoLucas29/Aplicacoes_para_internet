<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="app-header">
    <div class="app-header__inner">
        <a class="marca" href="${pageContext.request.contextPath}/ideias">
            <span class="marca__icone">💡</span> Ideias &amp; Votos
        </a>
        <nav class="app-nav">
            <c:choose>
                <c:when test="${sessionScope.usuarioLogado != null}">
                    <a class="btn-nova" href="${pageContext.request.contextPath}/ideias/nova">+ Nova ideia</a>
                    <a href="${pageContext.request.contextPath}/perfil">👤 ${sessionScope.usuarioLogado.nome}</a>
                    <a class="link-sair" href="${pageContext.request.contextPath}/logout">Sair</a>
                </c:when>
                <c:when test="${sessionScope.visitante == true}">
                    <span class="badge-visitante">Modo visitante</span>
                    <a href="${pageContext.request.contextPath}/cadastro">Criar conta</a>
                    <a class="link-sair" href="${pageContext.request.contextPath}/logout">Sair</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Entrar</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>
