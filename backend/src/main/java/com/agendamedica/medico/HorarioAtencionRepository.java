package com.agendamedica.medico;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencion, Long> {

    @EntityGraph(attributePaths = {"especialidad", "consultorio"})
    List<HorarioAtencion> findByMedicoIdOrderByDiaSemanaAscHoraInicioAsc(Long medicoId);

    @EntityGraph(attributePaths = {"especialidad", "consultorio"})
    List<HorarioAtencion> findByMedicoIdAndDiaSemanaAndActivoTrueOrderByHoraInicio(Long medicoId, int diaSemana);
}
