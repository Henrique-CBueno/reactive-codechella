package com.bsys.reactivecodechella.dtos;

import com.bsys.reactivecodechella.domain.Evento;
import com.bsys.reactivecodechella.domain.TipoEvento;

import java.time.LocalDate;

public record EventoDTO(Long id,
                        TipoEvento tipo,
                        String nome,
                        LocalDate data,
                        String descricao) {

    public static EventoDTO toDTO(Evento evento) {

        return new EventoDTO(evento.getId(),
                evento.getTipo(),
                evento.getNome(),
                evento.getData(),
                evento.getDescricao());
    }

    public Evento toEntity() {

        return new Evento(id(),
                tipo(),
                nome(),
                data(),
                descricao());
    }
}
