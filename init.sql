CREATE DATABASE IF NOT EXISTS ideiaviva
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE ideiaviva;

-- =========================================
-- TABELA DE USUARIOS
-- =========================================
-- Qualquer usuario cadastrado pode sugerir ideias, votar e comentar.

CREATE TABLE usuarios (
    id             BIGINT NOT NULL AUTO_INCREMENT,
    nome           VARCHAR(150) NOT NULL,
    email          VARCHAR(150) NOT NULL,
    senha          VARCHAR(255) NOT NULL,
    data_cadastro  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT uk_usuario_email
        UNIQUE (email)
);

-- =========================================
-- TABELA DE IDEIAS
-- =========================================
-- Cada ideia pertence a (foi sugerida por) um usuario.

CREATE TABLE ideias (
    id             BIGINT NOT NULL AUTO_INCREMENT,
    titulo         VARCHAR(150) NOT NULL,
    descricao      TEXT NOT NULL,
    usuario_id     BIGINT NOT NULL,
    data_criacao   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT fk_ideia_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
);

-- =========================================
-- TABELA DE VOTOS
-- =========================================
-- Relaciona um usuario a uma ideia. A restricao de unicidade abaixo
-- garante que cada usuario vote no maximo uma vez em cada ideia.

CREATE TABLE votos (
    id             BIGINT NOT NULL AUTO_INCREMENT,
    ideia_id       BIGINT NOT NULL,
    usuario_id     BIGINT NOT NULL,
    data_voto      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT uk_voto_ideia_usuario
        UNIQUE (ideia_id, usuario_id),

    CONSTRAINT fk_voto_ideia
        FOREIGN KEY (ideia_id)
        REFERENCES ideias(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_voto_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
);

-- =========================================
-- TABELA DE COMENTARIOS
-- =========================================
-- Nao fazia parte da lista minima de tabelas do enunciado (Usuarios,
-- Ideias, Votos), mas foi adicionada para viabilizar a funcionalidade de
-- "comentar em cada ideia" pedida no tema do trabalho.

CREATE TABLE comentarios (
    id             BIGINT NOT NULL AUTO_INCREMENT,
    ideia_id       BIGINT NOT NULL,
    usuario_id     BIGINT NOT NULL,
    texto          VARCHAR(500) NOT NULL,
    data_criacao   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT fk_comentario_ideia
        FOREIGN KEY (ideia_id)
        REFERENCES ideias(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_comentario_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
);

-- =========================================
-- DADOS PARA TESTE
-- =========================================
-- Todos os usuarios de teste usam a senha "123456".

INSERT INTO usuarios (nome, email, senha)
VALUES
    ('Ana Souza',    'ana@ideiaviva.com',   '123456'),
    ('Bruno Lima',   'bruno@ideiaviva.com', '123456'),
    ('Carla Mendes', 'carla@ideiaviva.com', '123456');

INSERT INTO ideias (titulo, descricao, usuario_id)
VALUES
    (
        'Ponto de reciclagem no campus',
        'Instalar coletores separados de papel, plastico, vidro e metal na entrada de cada bloco, com campanhas de conscientizacao sobre descarte correto.',
        1
    ),
    (
        'App de caronas entre alunos',
        'Criar um aplicativo simples para alunos que moram perto combinarem caronas para a faculdade, reduzindo custo de transporte e emissao de carbono.',
        2
    ),
    (
        'Biblioteca 24 horas em epoca de provas',
        'Manter a biblioteca aberta durante a madrugada nas duas semanas que antecedem as provas finais, com sala de estudo em grupo e silenciosa.',
        3
    );

INSERT INTO votos (ideia_id, usuario_id)
VALUES
    (1, 2),
    (1, 3),
    (2, 1),
    (2, 3),
    (3, 1);

INSERT INTO comentarios (ideia_id, usuario_id, texto)
VALUES
    (1, 2, 'Otima ideia! Podemos comecar com o predio principal e expandir depois.'),
    (1, 3, 'Seria legal ter um mural mostrando quanto material ja foi reciclado.'),
    (2, 3, 'Eu moro perto do campus e adoraria participar do app de caronas.'),
    (3, 1, 'Apoio total, principalmente na semana de provas finais.');

-- =========================================
-- CONSULTA DE EXEMPLO
-- =========================================
-- Ranking das ideias mais votadas, com nome do autor e total de votos.

SELECT
    i.id,
    i.titulo,
    u.nome AS autor,
    COUNT(v.id) AS total_votos
FROM ideias i
INNER JOIN usuarios u
    ON u.id = i.usuario_id
LEFT JOIN votos v
    ON v.ideia_id = i.id
GROUP BY i.id, i.titulo, u.nome
ORDER BY total_votos DESC;
