package com.agendamedica.catalogo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AseguradoraRepository extends JpaRepository<Aseguradora, Long> {

    List<Aseguradora> findAllByOrderByNombre();
}
