package com.ideiasapp.model;

import java.time.LocalDateTime;

public class Ideia {

    private Integer id;
    private String titulo;
    private String descricao;
    private Usuario autor;
    private LocalDateTime dataCriacao;
    private int totalVotos; // preenchido via JOIN/COUNT quando necessário

    public Ideia() {}

    public Ideia(String titulo, String descricao, Usuario autor) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.autor = autor;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public int getTotalVotos() { return totalVotos; }
    public void setTotalVotos(int totalVotos) { this.totalVotos = totalVotos; }
}