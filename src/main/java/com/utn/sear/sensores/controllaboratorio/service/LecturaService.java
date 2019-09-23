package com.utn.sear.sensores.controllaboratorio.service;

import com.utn.sear.sensores.controllaboratorio.domain.Error;
import com.utn.sear.sensores.controllaboratorio.domain.Lectura;
import com.utn.sear.sensores.controllaboratorio.domain.LecturaVo;
import com.utn.sear.sensores.controllaboratorio.domain.Room;
import com.utn.sear.sensores.controllaboratorio.repository.ErrorRepository;
import com.utn.sear.sensores.controllaboratorio.repository.LecturaRepository;
import com.utn.sear.sensores.controllaboratorio.repository.RoomRepository;
import java.time.LocalDateTime;
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
    private final ErrorRepository errorRepository;
    private final RestTemplate restTemplate;

    @Scheduled(fixedRate = 60000)
    public void obtenerLecturas() {

        List<Room> rooms = roomRepository.findAll();

        for (Room room : rooms) {
            obtenerLectura(room);
        }

    }

    public Lectura obtenerLectura(Room room) throws RestClientException {
        Lectura lectura = null;

        try {
            LecturaVo lecturaVo = restTemplate.getForObject("http://" + room.getIp(), LecturaVo.class);

            lecturaVo.setRoomId(room.getId());
            lectura = lecturaVo.toLectura();
            lectura = lecturaRepository.save(lectura);

            List<Error> errores = lecturaVo.toErrores(lectura.getId());

            for (Error error : errores) {
                errorRepository.save(error);
            }
        } catch (Exception ex) {
            errorRepository.save(new Error(null, room.getId(), null, LocalDateTime.now(), "[conexion fallida] - no se puedo conectar al arduino."));
        }

        return lectura;
    }
}
