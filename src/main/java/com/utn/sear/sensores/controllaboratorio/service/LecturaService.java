package com.utn.sear.sensores.controllaboratorio.service;

import com.utn.sear.sensores.controllaboratorio.domain.Lectura;
import com.utn.sear.sensores.controllaboratorio.domain.LecturaVo;
import com.utn.sear.sensores.controllaboratorio.domain.Room;
import com.utn.sear.sensores.controllaboratorio.repository.LecturaRepository;
import com.utn.sear.sensores.controllaboratorio.repository.RoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LecturaService {

    private final RoomRepository roomRepository;
    private final LecturaRepository lecturaRepository;
    private final RestTemplate restTemplate;

    @Scheduled(fixedRate = 60000)
    public void obtenerLecturas() {

        List<Room> rooms = roomRepository.findAll();

        for (Room room : rooms) {
            obtenerLectura(room);
        }

    }

    public Lectura obtenerLectura(Room room) throws RestClientException {
        LecturaVo lecturaVo = restTemplate.getForObject("http://" + room.getIp(), LecturaVo.class);
        lecturaVo.setRoomId(room.getId());
        Lectura lectura = lecturaVo.toLectura();
        lecturaRepository.save(lectura);
        return lectura;
    }
}
