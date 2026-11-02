package com.agendamedica.turno;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agendamedica.comun.ReglaNegocioException;
import com.agendamedica.seguridad.UsuarioActual;
import com.agendamedica.turno.TurnoService.NuevoTurno;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController {

    public record TurnoRequest(@NotNull Long pacienteId, @NotNull Long medicoId, @NotNull Long especialidadId,
                               @NotNull LocalDateTime inicio, TipoTurno tipo,
                               @Size(max = 250) String motivo, String observaciones) {}

    public record ReprogramarRequest(@NotNull LocalDateTime inicio) {}

    public record EstadoRequest(@NotNull EstadoTurno estado, @Size(max = 250) String motivo) {}

    private final TurnoService servicio;
    private final TurnoRepository turnos;
    private final UsuarioActual usuarioActual;
    private final Clock reloj;

    /** Listado por rango de fechas (inclusive). Un médico sólo ve su propia agenda. */
    @GetMapping
    @Transactional(readOnly = true)
    public List<TurnoDto> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) Long medicoId,
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) EstadoTurno estado) {
        LocalDate d = desde != null ? desde : LocalDate.now(reloj);
        LocalDate h = hasta != null ? hasta : d;
        if (h.isBefore(d) || d.plusDays(366).isBefore(h)) {
            throw new ReglaNegocioException("Rango de fechas inválido (máximo un año)");
        }
        if (usuarioActual.esMedico()) {
            medicoId = usuarioActual.medicoId();
        }
        return turnos.buscar(d.atStartOfDay(), h.plusDays(1).atStartOfDay(), medicoId, pacienteId, estado)
                .stream().map(TurnoDto::de).toList();
    }

    @GetMapping("/paciente/{pacienteId}")
    @Transactional(readOnly = true)
    public List<TurnoDto> historialPaciente(@PathVariable Long pacienteId) {
        return turnos.historialPaciente(pacienteId).stream().map(TurnoDto::de).toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public TurnoDto obtener(@PathVariable Long id) {
        return TurnoDto.de(servicio.buscar(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public TurnoDto crear(@Valid @RequestBody TurnoRequest req) {
        return TurnoDto.de(servicio.crear(new NuevoTurno(req.pacienteId(), req.medicoId(), req.especialidadId(),
                req.inicio(), req.tipo(), req.motivo(), req.observaciones())));
    }

    @PutMapping("/{id}/reprogramar")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    @Transactional
    public TurnoDto reprogramar(@PathVariable Long id, @Valid @RequestBody ReprogramarRequest req) {
        return TurnoDto.de(servicio.reprogramar(id, req.inicio()));
    }

    @PatchMapping("/{id}/estado")
    @Transactional
    public TurnoDto cambiarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequest req) {
        return TurnoDto.de(servicio.cambiarEstado(id, req.estado(), req.motivo()));
    }
}
