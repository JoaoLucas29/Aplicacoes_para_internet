<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Meu perfil - Ideias &amp; Votos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/estilo.css">
</head>
<body>
    <jsp:include page="_header.jsp" />

    <div class="container">
        <a class="voltar" href="${pageContext.request.contextPath}/ideias">&larr; Voltar para ideias</a>

        <h1>Meu perfil</h1>

        <% if (request.getAttribute("erro") != null) { %>
            <p class="erro"><%= request.getAttribute("erro") %></p>
        <% } %>
        <% if (request.getAttribute("sucesso") != null) { %>
            <p class="sucesso"><%= request.getAttribute("sucesso") %></p>
        <% } %>

        <div class="cartao">
            <form action="${pageContext.request.contextPath}/perfil" method="post">
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" minlength="3" value="${usuario.nome}" required>

                <label for="senha">Nova senha</label>
                <input type="password" id="senha" name="senha" minlength="6" placeholder="Deixe em branco para manter a senha atual">

                <button type="submit">Salvar alterações</button>
            </form>
        </div>

        <div class="cartao cartao--perigo">
            <h2>Excluir conta</h2>
            <p>Essa ação é permanente e apaga também suas ideias, votos e comentários.</p>
            <form action="${pageContext.request.contextPath}/perfil/excluir" method="post"
                  onsubmit="return confirm('Tem certeza que deseja excluir sua conta? Essa ação não pode ser desfeita.');">
                <button type="submit" class="btn-perigo">Excluir minha conta</button>
            </form>
        </div>
    </div>
</body>
</html>
