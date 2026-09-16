# Ideias & Votos

Aplicação Java (Servlet + JSP) para publicar ideias, votar e comentar, com persistência em MySQL.

## Sobre o projeto

Qualquer pessoa pode navegar e ver as ideias publicadas (inclusive em **modo visitante**, sem criar conta). Para votar, comentar ou publicar uma ideia, é preciso ter uma conta (login simples com **nome** e **senha**).

| Entidade   | Create | Read | Update | Delete |
|------------|--------|------|--------|--------|
| Usuário    | ✅ cadastro | ✅ login/perfil | ✅ `/perfil` (editar nome/senha) | ✅ `/perfil/excluir` (exclui conta e cascata) |
| Ideia      | ✅ `/ideias/nova` | ✅ `/ideias`, `/ideias/detalhe` | ✅ `/ideias/editar` (só o autor) | ✅ `/ideias/excluir` (só o autor) |
| Comentário | ✅ `/comentar` | ✅ listado na ideia | ✅ `/comentar/editar` (só o autor) | ✅ `/comentar/excluir` (só o autor) |
| Voto       | ✅ `/votar` | ✅ contagem/verificação | — (não se aplica) | ✅ remoção de voto (camada de serviço) |

Regras de permissão: só o autor de uma ideia ou comentário pode editá-lo ou excluí-lo; a tentativa por outro usuário é bloqueada no `Service` (`SecurityException`).

Ao excluir a própria conta, o banco remove em cascata (`ON DELETE CASCADE`) as ideias, comentários e votos daquele usuário.

## Rotas principais

```
GET/POST /login            - login (nome + senha)
GET      /visitante         - entra em modo visitante (somente leitura)
GET/POST /cadastro          - cadastro de novo usuário
GET      /logout            - encerra a sessão
GET/POST /perfil            - ver/editar o próprio perfil
POST     /perfil/excluir    - excluir a própria conta

GET      /ideias            - lista de ideias (mais votadas primeiro)
GET/POST /ideias/nova       - criar ideia (requer login)
GET      /ideias/detalhe    - detalhe da ideia + comentários
GET/POST /ideias/editar     - editar ideia (só o autor)
POST     /ideias/excluir    - excluir ideia (só o autor)

POST     /votar             - votar numa ideia (requer login)

POST     /comentar          - comentar numa ideia (requer login)
GET/POST /comentar/editar   - editar comentário (só o autor)
POST     /comentar/excluir  - excluir comentário (só o autor)
```

## Ambiente testado

Versões usadas para rodar o projeto localmente com Docker (Windows 10):

| Componente             |   Versão | Situação |
| ---------------------- | -------: | -------- |
| Windows                |       10 | ✅       |
| Java local             | 25.0.4.1 | ⚠️       |
| Maven                  |   3.9.16 | ✅       |
| Docker                 |   29.8.0 | ✅       |
| Docker Compose         |    5.5.1 | ✅       |
| Tomcat (imagem)        |  9.0.122 | ✅       |
| Java dentro do Tomcat  |  11.0.32 | ✅       |
| Servlet API            |    4.0.1 | ✅       |
| JSTL                   |      1.2 | ✅       |
| MySQL                  |      8.0 | ✅       |
| MySQL Connector/J      |   8.0.33 | ⚠️       |

**Sobre os itens com ⚠️:**

- **Java local 25.0.4.1** — o `pom.xml` compila com `maven.compiler.source`/`target` = `11`. Ter um JDK mais novo instalado (ex.: 25) na máquina normalmente não é problema, desde que o Maven use esse JDK apenas para *compilar visando* a versão 11 (o que as propriedades do `pom.xml` já garantem). Se aparecer erro de build relacionado à versão do compilador, confirme com `java -version` e `mvn -v` qual JDK o Maven está usando, e considere instalar um JDK 11 dedicado via `jenv`/`sdkman`/variável `JAVA_HOME` caso o build falhe.
- **MySQL Connector/J 8.0.33** — compatível com o MySQL 8.0 usado no `docker-compose.yml`. Não é necessária nenhuma alteração na string de conexão além da já usada em `ConnectionFactory.java` (`useTimezone=true&serverTimezone=UTC`). Só fica marcado como atenção porque é a peça mais sensível a mudanças de versão do MySQL — se um dia trocar a imagem do MySQL, revalide a versão do connector.

O restante dos componentes rodou sem ajustes.

## Rodando localmente com Docker

```bash
mvn clean package
docker-compose up --build
```

A aplicação sobe em:

```
http://localhost:8080/ideias-votos/
```

> O caminho `/ideias-votos/` é obrigatório: ele vem do nome do artefato (`finalName` no `pom.xml` → `ideias-votos.war`), que o Tomcat usa como contexto da aplicação. Acessar só `http://localhost:8080/` **não** funciona, pois a aplicação não fica na raiz do Tomcat.

Para derrubar e subir do zero (útil depois de qualquer mudança no código ou no banco):

```bash
docker-compose down
mvn clean package
docker-compose up --build
```

## Solução de problemas

**Erro 404 ao abrir o navegador**
- Confirme que a URL é `http://localhost:8080/ideias-votos/` (com o nome do contexto e a barra final), não apenas `http://localhost:8080/`.
- Verifique se `target/ideias-votos.war` foi gerado (`mvn clean package` precisa rodar sem erro antes do `docker-compose up`).
- Veja o log do container com `docker logs ideias-votos-app` e procure por `Deploying web application archive ideias-votos.war`.

**IDE mostra aviso no `web.xml`** (`Cannot resolve symbol '...web-app_4_0.xsd'` / `URI is not registered`)
- É um aviso apenas do editor, tentando validar o XML contra o schema pela internet — não afeta o build nem o funcionamento da aplicação no Tomcat.
- Não é necessário trocar o namespace para `jakarta.ee`: o projeto usa `javax.servlet-api` (Jakarta EE 8 / Servlet 4.0), então o namespace `xmlns.jcp.org` atual está correto.

## Observações

- A senha continua armazenada em texto puro no banco, como no projeto original — para produção, use BCrypt.
- O `script.sql` foi atualizado: a coluna `email` foi removida, `nome` agora é `UNIQUE`, e as chaves estrangeiras têm `ON DELETE CASCADE`.
