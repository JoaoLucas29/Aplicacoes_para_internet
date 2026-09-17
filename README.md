# IdeiaViva

Aplicacao web MVC (Java + Servlet/JSP + MySQL) desenvolvida como trabalho academico
sobre o padrao de arquitetura **MVC (Model-View-Controller)**, complementado pelos
padroes **DAO (Data Access Object)** e **Service**.

## Tema

Uma plataforma onde os usuarios podem:

- **Sugerir ideias** para a comunidade (ex.: melhorias no campus, na empresa, na cidade);
- **Votar** nas ideias que consideram melhores (um voto por usuario, por ideia);
- **Comentar** em qualquer ideia, discutindo e complementando a sugestao.

## Evolução em relação à versão anterior (`ideias-votos`)

Este projeto substitui o protótipo `ideias-votos`. As mudanças mais relevantes entre uma versão e outra:

- **Renomeação do projeto**: `ideias-votos` → `ideiaviva`; pacote Java `com.ideiasapp` → `br.com.mvc`; artefato Maven, containers Docker (`ideias-votos-db`/`ideias-votos-app` → `ideiaviva-mysql`/`ideiaviva-tomcat`) e nome do banco (`ideias_votos` → `ideiaviva`) atualizados junto.
- **Atualização de stack**: Java 11 → 17, `javax.servlet-api` 4.0.1 → `jakarta.servlet-api` 6.0.0, Tomcat 9/JDK 11 → Tomcat 10.1/JDK 21, MySQL 8.0 → 8.4, `mysql-connector-java` 8.0.33 → `mysql-connector-j` 8.4.0.
- **Controllers divididos por rota**: a versão anterior tinha 4 classes `*Controller`, cada uma mapeando vários paths num único `@WebServlet` (ex.: `UsuarioController` cobria `/cadastro`, `/login`, `/logout`, `/visitante`, `/perfil` e `/perfil/excluir`). Agora cada rota tem sua própria `Servlet` (`LoginServlet`, `LogoutServlet`, `CadastroServlet`, `PerfilServlet`, `HomeServlet`, `IdeiaServlet`, `VotoServlet`, `ComentarioServlet`), todas herdando de uma nova classe `BaseServlet` (helpers de parâmetro, forward/redirect e usuário logado).
- **Roteamento por ação**: rotas antes distintas como `/ideias/nova`, `/ideias/editar`, `/ideias/detalhe` e `/ideias/excluir` foram unificadas em `/ideias?acao=...`; o mesmo padrão passou a valer para `/comentarios`.
- **Autenticação centralizada**: foi adicionado um `AuthFilter` (Servlet Filter) protegendo `/home`, `/perfil`, `/ideias`, `/votos` e `/comentarios`, redirecionando para `/login` quando não há sessão — antes, cada controller verificava isso manualmente.
- **Fim do modo visitante**: a versão anterior tinha uma rota `/visitante` que permitia ver as ideias sem login. Essa opção foi removida; agora é preciso ter conta para acessar qualquer área além de `/login` e `/cadastro`.
- **Login por e-mail**: a autenticação passou a usar `email` + `senha` em vez de `nome` + `senha`. A coluna `email` (que tinha sido removida na versão anterior) voltou à tabela de usuários, agora como `UNIQUE` — e `nome` deixou de ser obrigatoriamente único.
- **Nova regra de negócio**: o usuário não pode mais votar na própria ideia (validação adicionada no `VotoService`); a versão anterior não tinha essa restrição.
- **Nova tela inicial**: rota `/home`, com um ranking das ideias mais votadas — antes, `/ideias` acumulava esse papel de página de entrada.
- **Camada de conexão renomeada**: `ConnectionFactory` deu lugar a `MysqlSingleton`, agora num pacote próprio `config`.
- **Modelo simplificado**: a classe utilitária `Mapeavel` foi removida; o campo de texto do `Comentario` passou a ter limite de 500 caracteres (antes era `TEXT` sem limite).
- **Views reorganizadas**: saíram de `webapp/views/*.jsp` e foram para `webapp/WEB-INF/jsp/`, com nomes mais curtos (`listarIdeias.jsp` → `ideias/lista.jsp`; `cadastrarIdeia.jsp` e `editarIdeia.jsp` foram unificadas em `ideias/form.jsp`; `detalheIdeia.jsp` → `ideias/detalhe.jsp`; `cadastroUsuario.jsp` → `cadastro.jsp`). A tela dedicada de edição de comentário (`editarComentario.jsp`) foi removida — comentários continuam podendo ser excluídos, mas não mais editados.
- **Empacotamento e deploy**: o `.war` passou a ser gerado na pasta `deploy/` (configurado no `maven-war-plugin`), e o `docker-compose.yml` monta essa pasta inteira como `webapps/` do Tomcat, em vez de mapear o `.war` individualmente.
- **Banco de dados**: `script.sql` deu lugar a `init.sql`, com colunas em `BIGINT`, charset `utf8mb4`, tabelas no plural (`usuarios`, `ideias`, `votos`, `comentarios`) e dados de teste (3 usuários, ideias, votos e comentários) já incluídos — a versão anterior subia o banco vazio.
- Foram adicionados um `.gitignore` (inexistente antes) e uma `index.jsp` que redireciona para `/login`.

## Integrantes

| Nome | Papel |
|---|---|
| João Lucas F.M. Rodrigues | Desenvolvedor |
| Gabriel Pereira de Queiroz | Desenvolvedor |

## Tecnologias utilizadas

- Java 17
- Jakarta Servlet 6.0 / JSP + JSTL
- MySQL 8.4
- Maven (empacotamento em `.war`)
- Docker e Docker Compose (Tomcat 10 + MySQL)

## Arquitetura

O projeto segue o padrao **MVC** com duas camadas auxiliares (**DAO** e **Service**),
mantendo cada camada com uma unica responsabilidade:

```
Requisicao HTTP
      |
      v
  Controller (Servlet)  -> so le parametros, chama o Service e decide a view (JSP)
      |
      v
   Service               -> regras de negocio (validacoes, autorizacao, calculos)
      |
      v
     DAO                 -> executa SQL e converte ResultSet em objetos Model
      |
      v
   MySQL
```

- **Model** (`br.com.mvc.model`): `Usuario`, `Ideia`, `Voto`, `Comentario` — classes
  simples (POJOs) que representam as tabelas do banco.
- **View** (`src/main/webapp/WEB-INF/jsp`): paginas JSP + JSTL, sem logica de negocio.
- **Controller** (`br.com.mvc.controller`): `Servlet`s que tratam as rotas, chamam os
  `Service`s e encaminham (`forward`) ou redirecionam (`redirect`) para a view correta.
- **DAO** (`br.com.mvc.dao`): unica camada que conhece SQL. Cada DAO cuida de uma
  tabela e devolve objetos `Model` prontos.
- **Service** (`br.com.mvc.service`): concentra as regras de negocio (ex.: "so o autor
  pode editar/excluir uma ideia", "nao pode votar na propria ideia", "email deve ser
  unico"). O Controller nunca acessa o DAO diretamente.

## Modelagem do banco de dados

O enunciado do trabalho pedia as tabelas **Usuarios, Ideias e Votos**. Para viabilizar
a funcionalidade de **comentar em cada ideia** (tambem pedida no tema), foi adicionada
uma quarta tabela, **Comentarios** — essa e a unica tabela que vai alem da lista minima
do enunciado.

```
usuarios (id, nome, email, senha, data_cadastro)
    |
    | 1:N
    v
ideias (id, titulo, descricao, usuario_id, data_criacao)
    |
    |-- 1:N --> votos       (id, ideia_id, usuario_id, data_voto)
    |                       UNIQUE (ideia_id, usuario_id)  -> 1 voto por usuario/ideia
    |
    '-- 1:N --> comentarios (id, ideia_id, usuario_id, texto, data_criacao)
```

Todas as chaves estrangeiras usam `ON DELETE CASCADE`: ao excluir uma ideia, seus
votos e comentarios sao removidos junto; ao excluir um usuario, suas ideias (e os
votos/comentarios delas) tambem sao removidos.

O script completo de criacao das tabelas e de dados de teste esta em [`init.sql`](init.sql).

## CRUDs implementados

| Entidade | Create | Read | Update | Delete | Regra de autorizacao |
|---|---|---|---|---|---|
| **Ideia** (CRUD principal) | Sim | Sim (lista + detalhe) | Sim | Sim | Somente o autor edita/exclui |
| **Usuario** | Sim (cadastro) | Sim (perfil) | Sim (perfil) | Sim (excluir conta) | Somente o proprio usuario |
| **Voto** | Sim (votar) | Sim (contagem/estado) | — | Sim (desvotar) | Nao pode votar na propria ideia |
| **Comentario** | Sim | Sim (lista na ideia) | — | Sim | Somente o autor do comentario |

## Rotas (Controllers)

| Rota | Metodo | Acao | Protegida por login? |
|---|---|---|---|
| `/cadastro` | GET/POST | Criar conta | Nao |
| `/login` | GET/POST | Autenticar | Nao |
| `/logout` | GET | Encerrar sessao | Nao |
| `/home` | GET | Dashboard com ranking das ideias mais votadas | Sim |
| `/perfil` | GET/POST | Ver/editar dados da conta; `?acao=excluir` exclui a conta | Sim |
| `/ideias` | GET | Listar (padrao), `?acao=novo`, `?acao=editar&id=`, `?acao=excluir&id=`, `?acao=detalhe&id=` | Sim |
| `/ideias` | POST | Salvar (criar ou atualizar) | Sim |
| `/votos?ideiaId=&origem=` | GET | Alternar voto (votar/desvotar) | Sim |
| `/comentarios` | POST | Adicionar comentario (`ideiaId`, `texto`) | Sim |
| `/comentarios?acao=excluir&id=&ideiaId=` | GET | Excluir comentario proprio | Sim |

O `AuthFilter` protege todas as rotas acima marcadas como "Sim", redirecionando para
`/login` quando nao ha usuario autenticado na sessao.

## Como executar o projeto

### Pre-requisitos

- JDK 17+
- Maven 3.8+
- Docker e Docker Compose

### Passo a passo

1. **Compilar o projeto** (gera o `.war` na pasta `deploy/`):

   ```bash
   mvn clean package
   ```

2. **Subir o banco de dados e o servidor** com Docker Compose:

   ```bash
   docker compose up -d
   ```

   Isso sobe dois containers:
   - `ideiaviva-mysql`: MySQL 8.4, banco `ideiaviva`, populado automaticamente pelo
     `init.sql` na primeira inicializacao.
   - `ideiaviva-tomcat`: Tomcat 10, servindo o `.war` gerado (pasta `deploy/` e
     montada como `webapps/` do Tomcat).

3. **Acessar a aplicacao**:

   ```
   http://localhost:8080/ideiaviva
   ```

4. **Login com um usuario de teste** (senha `123456` para todos):

   | Email | Senha |
   |---|---|
   | ana@ideiaviva.com | 123456 |
   | bruno@ideiaviva.com | 123456 |
   | carla@ideiaviva.com | 123456 |

   Ou clique em "Cadastre-se" na tela de login para criar uma conta nova.

5. **Parar os containers**:

   ```bash
   docker compose down
   ```

   Para apagar tambem os dados do banco: `docker compose down -v`.

### Rodando sem Docker (ambiente local)

Alternativamente, instale um MySQL local, rode o script `init.sql` manualmente e
ajuste usuario/senha/URL em `MysqlSingleton.java` (por padrao aponta para o host
`mysql`, nome do container Docker). Depois faca o deploy do `.war` gerado pelo Maven
em qualquer servlet container compativel com Jakarta EE 10 (Tomcat 10.1+, por exemplo).

## Estrutura de pastas

```
ideiaviva/
├── docker-compose.yml
├── init.sql
├── pom.xml
├── README.md
└── src/main/
    ├── java/br/com/mvc/
    │   ├── config/       -> MysqlSingleton (conexao JDBC)
    │   ├── model/         -> Usuario, Ideia, Voto, Comentario
    │   ├── dao/            -> MysqlDAO (base) + DAO de cada entidade
    │   ├── service/         -> regras de negocio de cada entidade
    │   ├── controller/       -> Servlets (rotas)
    │   └── filter/            -> AuthFilter (protege rotas autenticadas)
    └── webapp/
        ├── WEB-INF/
        │   ├── web.xml
        │   └── jsp/            -> views (login, cadastro, home, perfil, ideias/*)
        ├── css/estilo.css
        └── index.jsp            -> redireciona para /login
```

## Boas praticas aplicadas

- **Separacao de responsabilidades**: Controller nao acessa banco; DAO nao decide
  regra de negocio; Service nao conhece Servlet/HttpRequest.
- **Reuso de codigo**: `BaseServlet` centraliza leitura de parametros, forward,
  redirect e identificacao do usuario logado; `MysqlDAO` centraliza a execucao de
  SQL parametrizado (evitando SQL Injection via `PreparedStatement`).
- **Autorizacao por regra de negocio**: verificacoes como "somente o autor pode
  editar/excluir" ficam no Service, nao espalhadas pelas views ou controllers.
- **Consultas otimizadas**: a listagem de ideias traz total de votos e se o usuario
  logado ja votou em uma unica consulta SQL (subselects), evitando N+1 queries.

## Solução do Erro 404

- Caso isso ocorra: `dcker compose down` 
- Depois faça: `rmdir /s /q target` em caso de erro use `Remove-Item -Recurse -Force target`
- Logo em seguida use: `mvn clean package`
- Por fim: `docker compose up -d`
- Se necessároi confira: `docker ps`
  
## IMPORTANTE 
- Se http://localhost:8080/, der erro
- Use http://localhost:8080/ideiaviva/ 

## Possiveis evolucoes futuras

- Hash de senha (ex.: BCrypt) em vez de texto plano — mantido simples aqui por ser
  um projeto didatico, assim como no template original do professor.
- Paginacao na listagem de ideias.
- Categorias/tags para as ideias.
- Notificacoes quando a propria ideia recebe um novo voto ou comentario.

