package com.ideiasapp.model;

import java.time.LocalDateTime;

public class Comentario {

    private Integer id;
    private String texto;
    private Integer ideiaId;
    private Usuario autor;
    private LocalDateTime dataComentario;

    public Comentario() {}

    public Comentario(String texto, Integer ideiaId, Usuario autor) {
        this.texto = texto;
        this.ideiaId = ideiaId;
        this.autor = autor;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Integer getIdeiaId() { return ideiaId; }
    public void setIdeiaId(Integer ideiaId) { this.ideiaId = ideiaId; }

    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }

    public LocalDateTime getDataComentario() { return dataComentario; }
    public void setDataComentario(LocalDateTime dataComentario) { this.dataComentario = dataComentario; }
}