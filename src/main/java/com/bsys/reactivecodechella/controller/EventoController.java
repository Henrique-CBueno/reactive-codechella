package com.bsys.reactivecodechella.controller;

import com.bsys.reactivecodechella.domain.Evento;
import com.bsys.reactivecodechella.dtos.EventoDTO;
import com.bsys.reactivecodechella.repository.EventoRepository;
import com.bsys.reactivecodechella.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequestMapping("eventos")
public class EventoController {

    private final EventoService eventoService;

    private final Sinks.Many<EventoDTO> eventoSynck;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
        this.eventoSynck = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping
    public Flux<EventoDTO> getAllEventos() {

        return eventoService.getAll();
    }

    @GetMapping(value = "categoria/{tipo}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EventoDTO> getByType(@PathVariable String tipo) {

        return Flux.merge(eventoService.getByTipo(tipo), eventoSynck.asFlux())
                .delayElements(Duration.ofSeconds(1));
    }

    @GetMapping("{id}")
    public Mono<EventoDTO> getEventoById(@PathVariable Long id) {

        return eventoService.getById(id);
    }

    @PostMapping
    public Mono<EventoDTO> create(@RequestBody EventoDTO dto) {

        return eventoService.create(dto)
                .doOnSuccess(eventoSynck::tryEmitNext);
    }

    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteEventoById(@PathVariable Long id) {

        return eventoService.deleteById(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @PutMapping("{id}")
    public Mono<EventoDTO> updateEventoById(@PathVariable Long id,
                                                            @RequestBody EventoDTO dto) {

        return eventoService.updateById(id, dto);
    }
}
