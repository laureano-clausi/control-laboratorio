# control-laboratorio

Por ahora utiliza una BD en memoria.

## Requerimientos
- Maven > 3.1
- Java 11

## Links útiles
documentacion proyecto arduino: /control-laboratorio/doc/arduino/

## Para compilar 
mvn clean install

## Para levantar
mvn spring-boot:run

## autenticacion
admin:admin

## Url

port 8080

GET /api/rooms
GET /api/live/rooms/{ip}


