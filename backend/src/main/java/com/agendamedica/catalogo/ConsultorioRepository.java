package com.agendamedica.catalogo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {

    List<Consultorio> findAllByOrderByNombre();
}
