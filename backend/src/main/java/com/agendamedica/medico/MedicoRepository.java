package com.agendamedica.medico;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    @EntityGraph(attributePaths = "especialidades")
    List<Medico> findAllByOrderByApellidosAscNombresAsc();

    @EntityGraph(attributePaths = "especialidades")
    Optional<Medico> findWithEspecialidadesById(Long id);
}
