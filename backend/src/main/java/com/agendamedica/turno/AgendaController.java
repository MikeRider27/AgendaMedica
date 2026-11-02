package com.agendamedica.turno;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agendamedica.comun.NoEncontradoException;
import com.agendamedica.medico.BloqueoAgenda;
import com.agendamedica.medico.BloqueoAgendaRepository;
import com.agendamedica.medico.HorarioAtencion;
import com.agendamedica.medico.HorarioAtencionRepository;
import com.agendamedica.medico.MedicoDtos.BloqueoDto;
import com.agendamedica.medico.MedicoRepository;

import lombok.RequiredArgsConstructor;

/** Vista diaria de la agenda de un médico: grilla de horarios libres/ocupados. */
@RestController
@RequestMapping("/api/agenda")
@RequiredArgsConstructor
public class AgendaController {

    public record Hueco(LocalDateTime inicio, LocalDateTime fin, Long especialidadId, String especialidad,
                        Long consultorioId, String consultorio, boolean disponible, Long turnoId) {}

    public record AgendaDia(LocalDate fecha, Long medicoId, String medico,
                            List<Hueco> huecos, List<TurnoDto> turnos, List<BloqueoDto> bloqueos) {}

    private final MedicoRepository medicos;
    private final HorarioAtencionRepository horarios;
    private final BloqueoAgendaRepository bloqueos;
    private final TurnoRepository turnos;
    private final Clock reloj;

    @GetMapping("/dia")
    @Transactional(readOnly = true)
    public AgendaDia dia(@RequestParam Long medicoId,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                         @RequestParam(required = false) Long especialidadId) {
        var medico = medicos.findById(medicoId).orElseThrow(() -> new NoEncontradoException("Médico", medicoId));
        LocalDateTime desde = fecha.atStartOfDay();
        LocalDateTime hasta = fecha.plusDays(1).atStartOfDay();
        LocalDateTime ahora = LocalDateTime.now(reloj);

        List<Turno> delDia = turnos.buscar(desde, hasta, medicoId, null, null);
        List<Turno> activos = delDia.stream().filter(t -> t.getEstado() != EstadoTurno.CANCELADO).toList();
        List<BloqueoAgenda> bloqueosDia = bloqueos.solapados(medicoId, desde, hasta);

        List<Hueco> huecos = new ArrayList<>();
        for (HorarioAtencion h : horarios.findByMedicoIdAndDiaSemanaAndActivoTrueOrderByHoraInicio(
                medicoId, fecha.getDayOfWeek().getValue())) {
            if (!h.vigenteEn(fecha)
                    || (especialidadId != null && !h.getEspecialidad().getId().equals(especialidadId))) {
                continue;
            }
            LocalDateTime inicio = fecha.atTime(h.getHoraInicio());
            LocalDateTime finHorario = fecha.atTime(h.getHoraFin());
            while (!inicio.plusMinutes(h.getDuracionTurnoMin()).isAfter(finHorario)) {
                LocalDateTime fin = inicio.plusMinutes(h.getDuracionTurnoMin());
                LocalDateTime ini = inicio;
                Turno ocupante = activos.stream()
                        .filter(t -> t.getInicio().isBefore(fin) && t.getFin().isAfter(ini))
                        .findFirst().orElse(null);
                boolean bloqueado = bloqueosDia.stream()
                        .anyMatch(b -> b.getInicio().isBefore(fin) && b.getFin().isAfter(ini));
                var c = h.getConsultorio();
                huecos.add(new Hueco(ini, fin, h.getEspecialidad().getId(), h.getEspecialidad().getNombre(),
                        c == null ? null : c.getId(), c == null ? null : c.getNombre(),
                        ocupante == null && !bloqueado && ini.isAfter(ahora),
                        ocupante == null ? null : ocupante.getId()));
                inicio = fin;
            }
        }
        huecos.sort((a, b) -> a.inicio().compareTo(b.inicio()));

        return new AgendaDia(fecha, medicoId, medico.nombreCompleto(), huecos,
                delDia.stream().map(TurnoDto::de).toList(),
                bloqueosDia.stream().map(BloqueoDto::de).toList());
    }
}
