                    
CREATE TABLE room (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  nombre varchar(255),
  ip varchar(16),
  PRIMARY KEY (id),
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE lectura (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  room_id bigint(20) NOT NULL,
  temperatura varchar(255),
  humedad varchar(255),
  movimiento bit(1) NOT NULL DEFAULT 0,
  fecha datetime,
  PRIMARY KEY (id),
 FOREIGN KEY (room_id) REFERENCES room(id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE error (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  lectura_id bigint(20) NOT NULL ,
  mensaje varchar(255),
  PRIMARY KEY (id),
  FOREIGN KEY (lectura_id) REFERENCES lectura(id),
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


insert into room
(id , nombre  , ip            ) values 
(1  , 'room 1', '192.168.1.5' );


insert into lectura
(id , room_id  , temperatura,humedad,movimiento,fecha) values 
(1  , 1        , '18'       ,'64'   ,1         ,now());

