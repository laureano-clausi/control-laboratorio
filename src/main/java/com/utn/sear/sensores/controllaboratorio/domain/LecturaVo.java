package com.utn.sear.sensores.controllaboratorio.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Data;
import javax.persistence.Transient;

@Data
public class LecturaVo {

    private Long id;
    private Long roomId;
    private String temperatura;
    private Boolean movimiento;
    @Transient
    private LocalDateTime movimientoUltimaFecha;
    private String humedad;
    private LocalDateTime fecha;

    private Set<String> errores;

    public Lectura toLectura() {

        Lectura lectura = new Lectura();

        lectura.setRoomId(roomId);
        lectura.setTemperatura(temperatura);
        lectura.setMovimiento(movimiento);
        lectura.setMovimientoUltimaFecha(movimientoUltimaFecha);
        lectura.setHumedad(humedad);
        lectura.setFecha(fecha);

        return lectura;
    }

    public List<Error> toErrores(Long lecturaId) {

        ArrayList<Error> erroresLectura = new ArrayList<>();

        for (String error : errores) {
            erroresLectura.add(new Error(null, roomId, lecturaId, fecha, error));
        }

        return erroresLectura;

    }
}
