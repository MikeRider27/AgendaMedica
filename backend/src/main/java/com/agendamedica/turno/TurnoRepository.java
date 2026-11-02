package com.agendamedica.turno;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

    /**
     * Turnos en el intervalo [desde, hasta) con filtros opcionales. Trae las asociaciones
     * necesarias para armar el DTO en una sola consulta.
     */
    @Query("""
            select t from Turno t
              join fetch t.paciente join fetch t.medico join fetch t.especialidad
              left join fetch t.consultorio
            where t.inicio >= :desde and t.inicio < :hasta
              and (:medicoId is null or t.medico.id = :medicoId)
              and (:pacienteId is null or t.paciente.id = :pacienteId)
              and (:estado is null or t.estado = :estado)
            order by t.inicio, t.medico.apellidos
            """)
    List<Turno> buscar(LocalDateTime desde, LocalDateTime hasta, Long medicoId, Long pacienteId, EstadoTurno estado);

    @Query("""
            select t from Turno t
              join fetch t.paciente join fetch t.medico join fetch t.especialidad
              left join fetch t.consultorio
            where t.paciente.id = :pacienteId
            order by t.inicio desc
            """)
    List<Turno> historialPaciente(Long pacienteId);

    @Query("""
            select count(t) > 0 from Turno t
            where t.medico.id = :medicoId and t.estado <> com.agendamedica.turno.EstadoTurno.CANCELADO
              and t.inicio < :fin and t.fin > :inicio and (:excluirId is null or t.id <> :excluirId)
            """)
    boolean medicoOcupado(Long medicoId, LocalDateTime inicio, LocalDateTime fin, Long excluirId);

    @Query("""
            select count(t) > 0 from Turno t
            where t.paciente.id = :pacienteId and t.estado <> com.agendamedica.turno.EstadoTurno.CANCELADO
              and t.inicio < :fin and t.fin > :inicio and (:excluirId is null or t.id <> :excluirId)
            """)
    boolean pacienteOcupado(Long pacienteId, LocalDateTime inicio, LocalDateTime fin, Long excluirId);
}
