package com.utn.sear.sensores.controllaboratorio.domain;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import lombok.Data;
import javax.persistence.Id;

@Entity
@Data
public class Lectura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long roomId;
    private String temperatura;
    private Boolean movimiento;
    private String humedad;
    private LocalDateTime fecha;

    @OneToMany(mappedBy = "lectura")
    private Set<Error> errores;
}