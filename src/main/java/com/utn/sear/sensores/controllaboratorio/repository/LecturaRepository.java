package com.utn.sear.sensores.controllaboratorio.repository;

import com.utn.sear.sensores.controllaboratorio.domain.Lectura;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LecturaRepository extends JpaRepository<Lectura, Long> {

    List<Lectura> findByRoomId(Long roomId);

    List<Lectura> findByRoomIdOrderByIdDesc(Long roomId);

    @Query("select MAX(l.fecha) from Lectura l where l.id = ?1 and l.movimiento = 1")
    LocalDateTime obtenerFechaUltimoMovimiento(Long roomId);

}
