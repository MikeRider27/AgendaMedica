package com.agendamedica.turno;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agendamedica.catalogo.Especialidad;
import com.agendamedica.catalogo.EspecialidadRepository;
import com.agendamedica.comun.NoEncontradoException;
import com.agendamedica.comun.ReglaNegocioException;
import com.agendamedica.medico.BloqueoAgendaRepository;
import com.agendamedica.medico.HorarioAtencion;
import com.agendamedica.medico.HorarioAtencionRepository;
import com.agendamedica.medico.Medico;
import com.agendamedica.medico.MedicoRepository;
import com.agendamedica.paciente.Paciente;
import com.agendamedica.paciente.PacienteRepository;
import com.agendamedica.seguridad.UsuarioActual;
import com.agendamedica.usuario.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TurnoService {

    /** Estados que un médico puede asignar a sus propios turnos. */
    private static final Set<EstadoTurno> ESTADOS_MEDICO =
            Set.of(EstadoTurno.EN_ATENCION, EstadoTurno.ATENDIDO, EstadoTurno.AUSENTE, EstadoTurno.CONFIRMADO);

    public record NuevoTurno(Long pacienteId, Long medicoId, Long especialidadId, LocalDateTime inicio,
                             TipoTurno tipo, String motivo, String observaciones) {}

    private final TurnoRepository turnos;
    private final PacienteRepository pacientes;
    private final MedicoRepository medicos;
    private final EspecialidadRepository especialidades;
    private final HorarioAtencionRepository horarios;
    private final BloqueoAgendaRepository bloqueos;
    private final UsuarioRepository usuarios;
    private final UsuarioActual usuarioActual;
    private final Clock reloj;

    @Transactional
    public Turno crear(NuevoTurno req) {
        Paciente paciente = pacientes.findById(req.pacienteId())
                .orElseThrow(() -> new NoEncontradoException("Paciente", req.pacienteId()));
        if (!paciente.isActivo()) {
            throw new ReglaNegocioException("El paciente está inactivo");
        }
        Medico medico = medicos.findWithEspecialidadesById(req.medicoId())
                .orElseThrow(() -> new NoEncontradoException("Médico", req.medicoId()));
        Especialidad especialidad = especialidades.findById(req.especialidadId())
                .orElseThrow(() -> new NoEncontradoException("Especialidad", req.especialidadId()));

        Turno t = new Turno();
        t.setPaciente(paciente);
        t.setTipo(req.tipo() == null ? TipoTurno.CONSULTA : req.tipo());
        t.setMotivo(req.motivo());
        t.setObservaciones(req.observaciones());
        t.setCreadoPor(usuarios.getReferenceById(usuarioActual.id()));
        ubicarEnAgenda(t, medico, especialidad, req.inicio());
        return turnos.save(t);
    }

    @Transactional
    public Turno reprogramar(Long id, LocalDateTime nuevoInicio) {
        Turno t = buscar(id);
        if (!t.getEstado().esReprogramable()) {
            throw new ReglaNegocioException("Sólo se reprograman turnos pendientes o confirmados");
        }
        ubicarEnAgenda(t, medicos.findWithEspecialidadesById(t.getMedico().getId()).orElseThrow(),
                t.getEspecialidad(), nuevoInicio);
        t.setEstado(EstadoTurno.PENDIENTE);
        return turnos.saveAndFlush(t);
    }

    @Transactional
    public Turno cambiarEstado(Long id, EstadoTurno nuevo, String motivo) {
        Turno t = buscar(id);
        if (usuarioActual.esMedico()) {
            if (!t.getMedico().getId().equals(usuarioActual.medicoId())) {
                throw new ReglaNegocioException("Sólo puede modificar turnos de su propia agenda");
            }
            if (!ESTADOS_MEDICO.contains(nuevo)) {
                throw new ReglaNegocioException("Un médico no puede pasar un turno a " + nuevo);
            }
        }
        if (!t.getEstado().puedePasarA(nuevo)) {
            throw new ReglaNegocioException("No se puede pasar un turno de " + t.getEstado() + " a " + nuevo);
        }
        if (nuevo == EstadoTurno.CANCELADO) {
            if (motivo == null || motivo.isBlank()) {
                throw new ReglaNegocioException("Indique el motivo de la cancelación");
            }
            t.setMotivoCancelacion(motivo.trim());
        }
        t.setEstado(nuevo);
        return t;
    }

    public Turno buscar(Long id) {
        return turnos.findById(id).orElseThrow(() -> new NoEncontradoException("Turno", id));
    }

    /**
     * Valida que el turno caiga dentro de un horario de atención vigente del médico, fuera de
     * bloqueos y sin superponerse con otros turnos, y completa médico, fin y consultorio.
     */
    private void ubicarEnAgenda(Turno t, Medico medico, Especialidad especialidad, LocalDateTime inicio) {
        if (!medico.isActivo()) {
            throw new ReglaNegocioException("El médico está inactivo");
        }
        if (!medico.atiende(especialidad)) {
            throw new ReglaNegocioException("El médico no atiende " + especialidad.getNombre());
        }
        if (inicio.isBefore(LocalDateTime.now(reloj))) {
            throw new ReglaNegocioException("No se pueden dar turnos en el pasado");
        }
        LocalTime hora = inicio.toLocalTime();
        HorarioAtencion horario = horarios
                .findByMedicoIdAndDiaSemanaAndActivoTrueOrderByHoraInicio(medico.getId(),
                        inicio.getDayOfWeek().getValue())
                .stream()
                .filter(h -> h.vigenteEn(inicio.toLocalDate()))
                .filter(h -> h.getEspecialidad().getId().equals(especialidad.getId()))
                .filter(h -> !hora.isBefore(h.getHoraInicio())
                        && !hora.plusMinutes(h.getDuracionTurnoMin()).isAfter(h.getHoraFin())
                        // evita que hora + duración "dé la vuelta" pasada la medianoche
                        && hora.plusMinutes(h.getDuracionTurnoMin()).isAfter(hora))
                .findFirst()
                .orElseThrow(() -> new ReglaNegocioException(
                        "El horario elegido está fuera de la atención del médico para " + especialidad.getNombre()));

        LocalDateTime fin = inicio.plusMinutes(horario.getDuracionTurnoMin());
        if (!bloqueos.solapados(medico.getId(), inicio, fin).isEmpty()) {
            throw new ReglaNegocioException("El médico tiene la agenda bloqueada en ese horario");
        }
        if (turnos.medicoOcupado(medico.getId(), inicio, fin, t.getId())) {
            throw new ReglaNegocioException("El médico ya tiene un turno en ese horario");
        }
        if (turnos.pacienteOcupado(t.getPaciente().getId(), inicio, fin, t.getId())) {
            throw new ReglaNegocioException("El paciente ya tiene un turno en ese horario");
        }
        t.setMedico(medico);
        t.setEspecialidad(especialidad);
        t.setConsultorio(horario.getConsultorio());
        t.setInicio(inicio);
        t.setFin(fin);
    }
}
