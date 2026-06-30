package com.bsys.reactivecodechella;



import com.bsys.reactivecodechella.domain.TipoEvento;
import com.bsys.reactivecodechella.dtos.EventoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ReactiveCodechellaApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void cadastraNovoEvento() {

        EventoDTO dto = new EventoDTO(null,
                TipoEvento.TEATRO,
                "Patati",
                LocalDate.parse("2025-01-01"),
                "teatro do patati");

        webTestClient.post().uri("/eventos").bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EventoDTO.class)
                .value(response -> {

                    assertNotNull(response.id());
                    assertEquals(dto.tipo(), response.tipo());
                    assertEquals(dto.nome(), response.nome());
                    assertEquals(dto.data(), response.data());
                    assertEquals(dto.descricao(), response.descricao());
                });
    }

    @Test
    void buscarEvento() {

        EventoDTO dto = new EventoDTO(13L,
                TipoEvento.SHOW,
                "The Weeknd",
                LocalDate.parse("2025-11-02"),
                "Um show eletrizante ao ar livre com muitos efeitos especiais.");

        webTestClient.get().uri("/eventos/13")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(EventoDTO.class)
                .value(response -> {

                    assertEquals(response.id(), dto.id());
                    assertEquals(dto.tipo(), response.tipo());
                    assertEquals(dto.nome(), response.nome());
                    assertEquals(dto.data(), response.data());
                    assertEquals(dto.descricao(), response.descricao());
                });
    }

    @Test
    void buscarEventos() {

        EventoDTO dto = new EventoDTO(13L,
                TipoEvento.SHOW,
                "The Weeknd",
                LocalDate.parse("2025-11-02"),
                "Um show eletrizante ao ar livre com muitos efeitos especiais.");

        webTestClient.get().uri("/eventos")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(EventoDTO.class)
                .value(responses -> {

                    EventoDTO response = responses.get(12);
                    assertEquals(response.id(), dto.id());
                    assertEquals(dto.tipo(), response.tipo());
                    assertEquals(dto.nome(), response.nome());
                    assertEquals(dto.data(), response.data());
                    assertEquals(dto.descricao(), response.descricao());
                });
    }

}
