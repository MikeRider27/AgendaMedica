package com.agendamedica.consulta;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @EntityGraph(attributePaths = {"medico", "especialidad", "paciente"})
    List<Consulta> findByPacienteIdOrderByFechaDesc(Long pacienteId);

    @EntityGraph(attributePaths = {"medico", "especialidad", "paciente"})
    Optional<Consulta> findByTurnoId(Long turnoId);
}
