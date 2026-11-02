package com.agendamedica;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class AgendaMedicaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgendaMedicaApplication.class, args);
    }

    /** Reloj de la clínica: todas las fechas/horas de la agenda son locales a esta zona. */
    @Bean
    Clock reloj(@Value("${app.zona-horaria}") String zona) {
        return Clock.system(ZoneId.of(zona));
    }
}
