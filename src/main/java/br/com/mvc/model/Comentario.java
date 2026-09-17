package br.com.mvc.model;

import java.time.LocalDateTime;

public class Comentario {

    private Long id;
    private Long ideiaId;
    private Long usuarioId;
    private String texto;
    private LocalDateTime dataCriacao;

    private String autorNome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdeiaId() {
        return ideiaId;
    }

    public void setIdeiaId(Long ideiaId) {
        this.ideiaId = ideiaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getAutorNome() {
        return autorNome;
    }

    public void setAutorNome(String autorNome) {
        this.autorNome = autorNome;
    }
}
