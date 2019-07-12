package com.utn.sear.sensores.controllaboratorio.repository;

import com.utn.sear.sensores.controllaboratorio.domain.Lectura;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturaRepository extends JpaRepository<Lectura, Long> {

    List<Lectura> findByRoomId(Long roomId);
}
