package com.utn.sear.sensores.controllaboratorio.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import lombok.Data;
import javax.persistence.Id;
import javax.persistence.Transient;

@Data
public class LecturaVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

        ArrayList<Error> erroresLectura = new ArrayList<>();

        for (String error : errores) {
            erroresLectura.add(new Error(null, null, error));
        }
        return lectura;
    }
}
