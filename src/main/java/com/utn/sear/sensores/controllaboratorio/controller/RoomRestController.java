package com.utn.sear.sensores.controllaboratorio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoomRestController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/rooms")
    public String obtnerTodas() {

        return "[{\n"
                + "\"ip\": 192.168.1.11"
                + "\"nombre\" : 'lab azul',\n"
                + "\"temperatura\" : 15.55,\n"
                + "\"movimiento\" : true,\n"
                + "\"humedad\" : 25.44\n"
                + "\n"
                + "},{\n"
                + "\"ip\": 192.168.1.13"
                + "\"nombre\" : 'lab amarillo',\n"
                + "\"temperatura\" : 50,\n"
                + "\"movimiento\" : false,\n"
                + "\"humedad\" : 29.44\n"
                + "\n"
                + "}]";
    }

    @GetMapping("/live/rooms/{ip}")
    public String obtenerLecturaActualPorId(@PathVariable String ip) {

        return restTemplate.getForObject(ip, String.class);

    }

}
