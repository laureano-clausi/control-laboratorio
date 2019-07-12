package com.utn.sear.sensores.controllaboratorio.controller;

import com.utn.sear.sensores.controllaboratorio.domain.Lectura;
import com.utn.sear.sensores.controllaboratorio.domain.Room;
import com.utn.sear.sensores.controllaboratorio.repository.RoomRepository;
import com.utn.sear.sensores.controllaboratorio.service.LecturaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoomRestController {

    private final RoomRepository roomRepository;
    private final LecturaService lecturaService;
    private final RestTemplate restTemplate;

    @GetMapping("/rooms")
    public List<Room> obtnerTodas() {
        return roomRepository.findAll();
    }

    @DeleteMapping("/rooms/{id}")
    public void borrar(@PathVariable Long id) {
        roomRepository.deleteById(id);
    }

    @PostMapping("/rooms")
    public Room crear(@RequestBody Room room) {
        return roomRepository.save(room);
    }

    @GetMapping("/live/rooms/{id}")
    public Lectura obtenerLecturaActualPorId(@PathVariable Long id) {
        return lecturaService.obtenerLectura(roomRepository.findById(id).get());
    }

    @GetMapping("/live/rooms/ip/{ip}")
    public String obtenerLecturaActualPorIp(@PathVariable String ip) {
        return restTemplate.getForObject(ip, String.class);
    }

}
