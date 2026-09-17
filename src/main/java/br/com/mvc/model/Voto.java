package br.com.mvc.model;

import java.time.LocalDateTime;

public class Voto {

    private Long id;
    private Long ideiaId;
    private Long usuarioId;
    private LocalDateTime dataVoto;

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

    public LocalDateTime getDataVoto() {
        return dataVoto;
    }

    public void setDataVoto(LocalDateTime dataVoto) {
        this.dataVoto = dataVoto;
    }
}
