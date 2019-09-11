package com.utn.sear.sensores.controllaboratorio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ControlLaboratorioApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControlLaboratorioApplication.class, args);
	}

}
