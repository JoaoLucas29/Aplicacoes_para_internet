package com.ideiasapp.model;

import java.time.LocalDateTime;

public class Voto {

    private Integer id;
    private Integer ideiaId;
    private Integer usuarioId;
    private LocalDateTime dataVoto;

    public Voto() {}

    public Voto(Integer ideiaId, Integer usuarioId) {
        this.ideiaId = ideiaId;
        this.usuarioId = usuarioId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdeiaId() { return ideiaId; }
    public void setIdeiaId(Integer ideiaId) { this.ideiaId = ideiaId; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public LocalDateTime getDataVoto() { return dataVoto; }
    public void setDataVoto(LocalDateTime dataVoto) { this.dataVoto = dataVoto; }
}