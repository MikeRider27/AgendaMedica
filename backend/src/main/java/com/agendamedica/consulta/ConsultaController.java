package com.agendamedica.consulta;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agendamedica.comun.NoEncontradoException;
import com.agendamedica.comun.ReglaNegocioException;
import com.agendamedica.seguridad.UsuarioActual;
import com.agendamedica.turno.EstadoTurno;
import com.agendamedica.turno.Turno;
import com.agendamedica.turno.TurnoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

/**
 * Historia clínica. Sólo médicos y administradores; un médico registra consultas
 * únicamente sobre turnos de su propia agenda.
 */
@RestController
@RequestMapping("/api/consultas")
@PreAuthorize("hasAnyRole('ADMIN','MEDICO')")
@RequiredArgsConstructor
public class ConsultaController {

    public record ConsultaRequest(
            @NotBlank String motivoConsulta,
            String anamnesis,
            String examenFisico,
            String diagnostico,
            String indicaciones,
            @DecimalMin("0.1") @DecimalMax("500") BigDecimal pesoKg,
            @DecimalMin("1") @DecimalMax("300") BigDecimal tallaCm,
            @Size(max = 10) String presionArterial,
            @DecimalMin("30") @DecimalMax("45") BigDecimal temperaturaC,
            LocalDate proximoControl) {}

    public record ConsultaDto(Long id, Long turnoId, Long pacienteId, String paciente, Long medicoId, String medico,
                              String especialidad, LocalDateTime fecha, String motivoConsulta, String anamnesis,
                              String examenFisico, String diagnostico, String indicaciones, BigDecimal pesoKg,
                              BigDecimal tallaCm, String presionArterial, BigDecimal temperaturaC,
                              LocalDate proximoControl) {

        static ConsultaDto de(Consulta c) {
            return new ConsultaDto(c.getId(), c.getTurno() == null ? null : c.getTurno().getId(),
                    c.getPaciente().getId(), c.getPaciente().nombreCompleto(),
                    c.getMedico().getId(), c.getMedico().nombreCompleto(), c.getEspecialidad().getNombre(),
                    c.getFecha(), c.getMotivoConsulta(), c.getAnamnesis(), c.getExamenFisico(),
                    c.getDiagnostico(), c.getIndicaciones(), c.getPesoKg(), c.getTallaCm(),
                    c.getPresionArterial(), c.getTemperaturaC(), c.getProximoControl());
        }
    }

    private final ConsultaRepository consultas;
    private final TurnoService turnoService;
    private final UsuarioActual usuarioActual;
    private final Clock reloj;

    @GetMapping
    @Transactional(readOnly = true)
    public List<ConsultaDto> historiaClinica(@RequestParam Long pacienteId) {
        return consultas.findByPacienteIdOrderByFechaDesc(pacienteId).stream().map(ConsultaDto::de).toList();
    }

    @GetMapping("/turno/{turnoId}")
    @Transactional(readOnly = true)
    public ConsultaDto porTurno(@PathVariable Long turnoId) {
        return consultas.findByTurnoId(turnoId).map(ConsultaDto::de)
                .orElseThrow(() -> new NoEncontradoException("Consulta del turno", turnoId));
    }

    /** Registra la atención de un turno y lo marca como ATENDIDO. */
    @PostMapping("/turno/{turnoId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ConsultaDto registrar(@PathVariable Long turnoId, @Valid @RequestBody ConsultaRequest req) {
        Turno t = turnoService.buscar(turnoId);
        verificarAutor(t.getMedico().getId());
        if (t.getEstado() == EstadoTurno.CANCELADO || t.getEstado() == EstadoTurno.AUSENTE) {
            throw new ReglaNegocioException("No se puede registrar la consulta de un turno " + t.getEstado());
        }
        t.setEstado(EstadoTurno.ATENDIDO);
        Consulta c = new Consulta();
        c.setTurno(t);
        c.setPaciente(t.getPaciente());
        c.setMedico(t.getMedico());
        c.setEspecialidad(t.getEspecialidad());
        c.setFecha(LocalDateTime.now(reloj));
        aplicar(c, req);
        return ConsultaDto.de(consultas.save(c));
    }

    @PutMapping("/{id}")
    @Transactional
    public ConsultaDto modificar(@PathVariable Long id, @Valid @RequestBody ConsultaRequest req) {
        Consulta c = consultas.findById(id).orElseThrow(() -> new NoEncontradoException("Consulta", id));
        verificarAutor(c.getMedico().getId());
        aplicar(c, req);
        return ConsultaDto.de(c);
    }

    private void verificarAutor(@NotNull Long medicoId) {
        if (usuarioActual.esMedico() && !medicoId.equals(usuarioActual.medicoId())) {
            throw new ReglaNegocioException("Sólo el médico tratante puede registrar o modificar esta consulta");
        }
    }

    private void aplicar(Consulta c, ConsultaRequest req) {
        c.setMotivoConsulta(req.motivoConsulta());
        c.setAnamnesis(req.anamnesis());
        c.setExamenFisico(req.examenFisico());
        c.setDiagnostico(req.diagnostico());
        c.setIndicaciones(req.indicaciones());
        c.setPesoKg(req.pesoKg());
        c.setTallaCm(req.tallaCm());
        c.setPresionArterial(req.presionArterial());
        c.setTemperaturaC(req.temperaturaC());
        c.setProximoControl(req.proximoControl());
    }
}
