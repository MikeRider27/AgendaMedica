package com.agendamedica.medico;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agendamedica.catalogo.ConsultorioRepository;
import com.agendamedica.catalogo.Especialidad;
import com.agendamedica.catalogo.EspecialidadRepository;
import com.agendamedica.comun.NoEncontradoException;
import com.agendamedica.comun.ReglaNegocioException;
import com.agendamedica.medico.MedicoDtos.BloqueoDto;
import com.agendamedica.medico.MedicoDtos.HorarioDto;
import com.agendamedica.medico.MedicoDtos.MedicoDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    public record MedicoRequest(
            @NotBlank @Size(max = 20) String nroDocumento,
            @NotBlank @Size(max = 100) String nombres,
            @NotBlank @Size(max = 100) String apellidos,
            @NotBlank @Size(max = 30) String matricula,
            @Size(max = 30) String telefono,
            @Email @Size(max = 150) String email,
            @Pattern(regexp = "#[0-9a-fA-F]{6}") String color,
            @NotEmpty(message = "debe tener al menos una especialidad") Set<Long> especialidadIds,
            Boolean activo) {}

    public record HorarioRequest(
            @NotNull Long especialidadId,
            Long consultorioId,
            @Min(1) @Max(7) int diaSemana,
            @NotNull LocalTime horaInicio,
            @NotNull LocalTime horaFin,
            @Min(5) @Max(480) int duracionTurnoMin,
            LocalDate vigenteDesde,
            LocalDate vigenteHasta,
            Boolean activo) {}

    public record BloqueoRequest(@NotNull LocalDateTime inicio, @NotNull LocalDateTime fin,
                                 @Size(max = 200) String motivo) {}

    private final MedicoRepository medicos;
    private final EspecialidadRepository especialidades;
    private final ConsultorioRepository consultorios;
    private final HorarioAtencionRepository horarios;
    private final BloqueoAgendaRepository bloqueos;
    private final Clock reloj;

    // ---- Médicos ----

    @GetMapping
    @Transactional(readOnly = true)
    public List<MedicoDto> listar(@RequestParam(required = false) Long especialidadId,
                                  @RequestParam(defaultValue = "false") boolean soloActivos) {
        return medicos.findAllByOrderByApellidosAscNombresAsc().stream()
                .filter(m -> !soloActivos || m.isActivo())
                .filter(m -> especialidadId == null
                        || m.getEspecialidades().stream().anyMatch(e -> e.getId().equals(especialidadId)))
                .map(MedicoDto::de)
                .toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public MedicoDto obtener(@PathVariable Long id) {
        return MedicoDto.de(buscar(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public MedicoDto crear(@Valid @RequestBody MedicoRequest req) {
        Medico m = new Medico();
        aplicar(m, req);
        return MedicoDto.de(medicos.save(m));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public MedicoDto modificar(@PathVariable Long id, @Valid @RequestBody MedicoRequest req) {
        Medico m = buscar(id);
        aplicar(m, req);
        return MedicoDto.de(medicos.saveAndFlush(m));
    }

    private void aplicar(Medico m, MedicoRequest req) {
        m.setNroDocumento(req.nroDocumento().trim());
        m.setNombres(req.nombres().trim());
        m.setApellidos(req.apellidos().trim());
        m.setMatricula(req.matricula().trim());
        m.setTelefono(req.telefono());
        m.setEmail(req.email() == null || req.email().isBlank() ? null : req.email());
        m.setColor(req.color() == null ? "#2563eb" : req.color());
        if (req.activo() != null) {
            m.setActivo(req.activo());
        }
        List<Especialidad> nuevas = especialidades.findAllById(req.especialidadIds());
        if (nuevas.size() != req.especialidadIds().size()) {
            throw new ReglaNegocioException("Alguna de las especialidades no existe");
        }
        if (m.getId() != null) {
            Set<Long> ids = req.especialidadIds();
            boolean quitaConHorario = horarios.findByMedicoIdOrderByDiaSemanaAscHoraInicioAsc(m.getId()).stream()
                    .anyMatch(h -> !ids.contains(h.getEspecialidad().getId()));
            if (quitaConHorario) {
                throw new ReglaNegocioException(
                        "No se puede quitar una especialidad que tiene horarios de atención cargados");
            }
        }
        m.setEspecialidades(new HashSet<>(nuevas));
    }

    private Medico buscar(Long id) {
        return medicos.findWithEspecialidadesById(id).orElseThrow(() -> new NoEncontradoException("Médico", id));
    }

    // ---- Horarios de atención ----

    @GetMapping("/{id}/horarios")
    @Transactional(readOnly = true)
    public List<HorarioDto> horarios(@PathVariable Long id) {
        return horarios.findByMedicoIdOrderByDiaSemanaAscHoraInicioAsc(id).stream().map(HorarioDto::de).toList();
    }

    @PostMapping("/{id}/horarios")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public HorarioDto crearHorario(@PathVariable Long id, @Valid @RequestBody HorarioRequest req) {
        HorarioAtencion h = new HorarioAtencion();
        h.setMedico(buscar(id));
        aplicar(h, req);
        return HorarioDto.de(horarios.save(h));
    }

    @PutMapping("/{id}/horarios/{horarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public HorarioDto modificarHorario(@PathVariable Long id, @PathVariable Long horarioId,
                                       @Valid @RequestBody HorarioRequest req) {
        HorarioAtencion h = buscarHorario(id, horarioId);
        aplicar(h, req);
        return HorarioDto.de(h);
    }

    @DeleteMapping("/{id}/horarios/{horarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void eliminarHorario(@PathVariable Long id, @PathVariable Long horarioId) {
        // Los turnos ya dados no dependen del horario: se conservan.
        horarios.delete(buscarHorario(id, horarioId));
    }

    private HorarioAtencion buscarHorario(Long medicoId, Long horarioId) {
        return horarios.findById(horarioId)
                .filter(h -> h.getMedico().getId().equals(medicoId))
                .orElseThrow(() -> new NoEncontradoException("Horario", horarioId));
    }

    private void aplicar(HorarioAtencion h, HorarioRequest req) {
        if (!req.horaFin().isAfter(req.horaInicio())) {
            throw new ReglaNegocioException("La hora de fin debe ser posterior a la de inicio");
        }
        Especialidad esp = especialidades.findById(req.especialidadId())
                .orElseThrow(() -> new NoEncontradoException("Especialidad", req.especialidadId()));
        if (!h.getMedico().atiende(esp)) {
            throw new ReglaNegocioException("El médico no tiene asignada la especialidad " + esp.getNombre());
        }
        h.setEspecialidad(esp);
        h.setConsultorio(req.consultorioId() == null ? null : consultorios.findById(req.consultorioId())
                .orElseThrow(() -> new NoEncontradoException("Consultorio", req.consultorioId())));
        h.setDiaSemana(req.diaSemana());
        h.setHoraInicio(req.horaInicio());
        h.setHoraFin(req.horaFin());
        h.setDuracionTurnoMin(req.duracionTurnoMin());
        h.setVigenteDesde(req.vigenteDesde() != null ? req.vigenteDesde()
                : h.getVigenteDesde() != null ? h.getVigenteDesde() : LocalDate.now(reloj));
        h.setVigenteHasta(req.vigenteHasta());
        if (req.activo() != null) {
            h.setActivo(req.activo());
        }
        if (h.getVigenteHasta() != null && h.getVigenteHasta().isBefore(h.getVigenteDesde())) {
            throw new ReglaNegocioException("La vigencia hasta no puede ser anterior a la vigencia desde");
        }
        if (h.isActivo()) {
            boolean solapa = horarios.findByMedicoIdOrderByDiaSemanaAscHoraInicioAsc(h.getMedico().getId()).stream()
                    .filter(o -> o.isActivo() && !o.getId().equals(h.getId()))
                    .anyMatch(h::seSuperponeCon);
            if (solapa) {
                throw new ReglaNegocioException("El horario se superpone con otro horario activo del médico");
            }
        }
    }

    // ---- Bloqueos de agenda ----

    @GetMapping("/{id}/bloqueos")
    @Transactional(readOnly = true)
    public List<BloqueoDto> bloqueos(@PathVariable Long id) {
        return bloqueos.findByMedicoIdAndFinAfterOrderByInicio(id, LocalDate.now(reloj).atStartOfDay()).stream()
                .map(BloqueoDto::de).toList();
    }

    @PostMapping("/{id}/bloqueos")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public BloqueoDto crearBloqueo(@PathVariable Long id, @Valid @RequestBody BloqueoRequest req) {
        if (!req.fin().isAfter(req.inicio())) {
            throw new ReglaNegocioException("El fin del bloqueo debe ser posterior al inicio");
        }
        BloqueoAgenda b = new BloqueoAgenda();
        b.setMedico(buscar(id));
        b.setInicio(req.inicio());
        b.setFin(req.fin());
        b.setMotivo(req.motivo());
        return BloqueoDto.de(bloqueos.save(b));
    }

    @DeleteMapping("/{id}/bloqueos/{bloqueoId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void eliminarBloqueo(@PathVariable Long id, @PathVariable Long bloqueoId) {
        bloqueos.delete(bloqueos.findById(bloqueoId)
                .filter(b -> b.getMedico().getId().equals(id))
                .orElseThrow(() -> new NoEncontradoException("Bloqueo", bloqueoId)));
    }
}
