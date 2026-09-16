CREATE DATABASE IF NOT EXISTS ideias_votos;
USE ideias_votos;

CREATE TABLE usuario (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL,
                         data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ideia (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       titulo VARCHAR(150) NOT NULL,
                       descricao TEXT NOT NULL,
                       usuario_id INT NOT NULL,
                       data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE voto (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      ideia_id INT NOT NULL,
                      usuario_id INT NOT NULL,
                      data_voto TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      FOREIGN KEY (ideia_id) REFERENCES ideia(id) ON DELETE CASCADE,
                      FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
                      UNIQUE KEY uk_voto_unico (ideia_id, usuario_id) -- impede voto duplicado
);

CREATE TABLE comentario (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            texto TEXT NOT NULL,
                            ideia_id INT NOT NULL,
                            usuario_id INT NOT NULL,
                            data_comentario TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (ideia_id) REFERENCES ideia(id) ON DELETE CASCADE,
                            FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);
