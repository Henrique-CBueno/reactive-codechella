package com.bsys.reactivecodechella.service;

import com.bsys.reactivecodechella.domain.TipoEvento;
import com.bsys.reactivecodechella.dtos.EventoDTO;
import com.bsys.reactivecodechella.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public Flux<EventoDTO> getAll() {

        return eventoRepository.findAllByOrderByIdAsc()
                .map(EventoDTO::toDTO);
    }

    public Mono<EventoDTO> getById(Long id) {

        return eventoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(404))))
                .map(EventoDTO::toDTO);
    }

    public Mono<EventoDTO> create(EventoDTO dto) {

        return eventoRepository.save(dto.toEntity()).map(EventoDTO::toDTO);
    }

    public Mono<Void> deleteById(Long id) {

        return eventoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(404))))
                .flatMap(eventoRepository::delete);
    }

    public Mono<EventoDTO> updateById(Long id, EventoDTO dto) {

        return eventoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(404))))
                .flatMap(evento -> {

                    evento.setNome(dto.nome());
                    evento.setTipo(dto.tipo());
                    evento.setDescricao(dto.descricao());
                    evento.setData(dto.data());

                    return eventoRepository.save(evento);
                }).map(EventoDTO::toDTO);
    }

    public Flux<EventoDTO> getByTipo(String tipo) {

        TipoEvento tipoEvento = TipoEvento.valueOf(tipo.toUpperCase());

        return eventoRepository.findByTipo(tipoEvento)
                .map(EventoDTO::toDTO);
    }
}
