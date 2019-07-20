package com.utn.sear.sensores.controllaboratorio.domain;

import lombok.Data;

@Data
public class RoomConLectura {

    private Lectura ultimaLectura;
    private Room room;
}
