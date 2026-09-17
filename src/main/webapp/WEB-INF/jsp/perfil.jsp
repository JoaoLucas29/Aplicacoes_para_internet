<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Meu perfil - IdeiaViva</title>
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
        <h1>Meu perfil</h1>
    </div>

    <div class="card" style="max-width: 480px;">
        <c:if test="${not empty erro}">
            <div class="alert alert-erro">${erro}</div>
        </c:if>
        <c:if test="${not empty sucesso}">
            <div class="alert alert-sucesso">${sucesso}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/perfil">
            <div class="form-group">
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" value="${usuario.nome}" required>
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="text" id="email" name="email" value="${usuario.email}" required>
            </div>
            <div class="form-group">
                <label for="senha">Senha</label>
                <input type="text" id="senha" name="senha" value="${usuario.senha}" required>
            </div>
            <div class="actions">
                <button type="submit" class="btn">Salvar alteracoes</button>
            </div>
        </form>
    </div>

    <div class="card" style="max-width: 480px; margin-top: 1.5rem; border-color: #fecdca;">
        <h3 style="margin-top: 0;">Zona de risco</h3>
        <p style="color: var(--muted);">
            Excluir sua conta remove permanentemente seu cadastro, suas ideias,
            votos e comentarios.
        </p>
        <a class="btn btn-danger"
           href="${pageContext.request.contextPath}/perfil?acao=excluir"
           onclick="return confirm('Tem certeza que deseja excluir sua conta? Esta acao nao pode ser desfeita.');">
            Excluir minha conta
        </a>
    </div>
</main>
</body>
</html>
