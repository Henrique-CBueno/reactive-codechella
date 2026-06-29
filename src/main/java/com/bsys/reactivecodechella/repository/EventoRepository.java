package com.bsys.reactivecodechella.repository;

import com.bsys.reactivecodechella.domain.Evento;
import com.bsys.reactivecodechella.domain.TipoEvento;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface EventoRepository extends ReactiveCrudRepository<Evento, Long> {

    Flux<Evento> findAllByOrderByIdAsc();

    Flux<Evento> findByTipo(TipoEvento tipo);
}
