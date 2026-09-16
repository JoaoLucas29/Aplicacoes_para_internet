package com.ideiasapp.model;

import java.time.LocalDateTime;

public class Usuario {

    private Integer id;
    private String nome;
    private String senha;
    private LocalDateTime dataCadastro;

    public Usuario() {}

    public Usuario(String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
    }

    // getters e setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}
