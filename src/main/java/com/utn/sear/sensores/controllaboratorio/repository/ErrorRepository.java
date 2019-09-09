package com.utn.sear.sensores.controllaboratorio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ErrorRepository extends JpaRepository<Error, Long> {

//    @Query(value = "select error.mensaje from error e,lectura l where l.room_id = ?1 and l.id = e.lectura_id", nativeQuery = true)
//    List<String> findByRoomId(long roomId);

}
