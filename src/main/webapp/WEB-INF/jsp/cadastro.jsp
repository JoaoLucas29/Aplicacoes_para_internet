<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Criar conta - IdeiaViva</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<div class="login-page">
    <div class="card login-card">
        <h1>Criar conta</h1>
        <p>Cadastre-se para sugerir ideias, votar e comentar.</p>

        <c:if test="${not empty erro}">
            <div class="alert alert-erro">${erro}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/cadastro">
            <div class="form-group">
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" value="${usuario.nome}" required autofocus>
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="text" id="email" name="email" value="${usuario.email}" required>
            </div>
            <div class="form-group">
                <label for="senha">Senha</label>
                <input type="password" id="senha" name="senha" required>
                <small style="color: var(--muted);">Minimo de 6 caracteres.</small>
            </div>
            <div class="actions">
                <button type="submit" class="btn">Cadastrar</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/login">Cancelar</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
