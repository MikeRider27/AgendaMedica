package com.agendamedica.paciente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @EntityGraph(attributePaths = "aseguradora")
    Page<Paciente> findAllBy(Pageable pageable);

    /**
     * Búsqueda por documento, nombre o apellido, sin distinguir tildes.
     * {@code patron} en minúsculas, sin tildes y con comodines.
     */
    @EntityGraph(attributePaths = "aseguradora")
    @Query("""
            select p from Paciente p
            where lower(p.nroDocumento) like :patron
               or lower(cast(function('unaccent', concat(p.nombres, ' ', p.apellidos)) as String)) like :patron
               or lower(cast(function('unaccent', concat(p.apellidos, ' ', p.nombres)) as String)) like :patron
            """)
    Page<Paciente> buscar(String patron, Pageable pageable);
}
