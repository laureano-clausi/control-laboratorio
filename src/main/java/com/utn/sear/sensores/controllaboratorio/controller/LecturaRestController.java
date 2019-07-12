package com.utn.sear.sensores.controllaboratorio.controller;

import com.utn.sear.sensores.controllaboratorio.domain.Lectura;
import com.utn.sear.sensores.controllaboratorio.repository.LecturaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LecturaRestController {

    private final LecturaRepository lecturaRepository;

    @GetMapping("/rooms/{roomId}/lectura")
    public List<Lectura> obtenerLecturaActualPorId(@PathVariable Long roomId) {
        return lecturaRepository.findByRoomId(roomId);
    }
}
