package com.agendamedica.tablero;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agendamedica.seguridad.UsuarioActual;
import com.agendamedica.turno.EstadoTurno;
import com.agendamedica.turno.Turno;
import com.agendamedica.turno.TurnoDto;
import com.agendamedica.turno.TurnoRepository;

import lombok.RequiredArgsConstructor;

/** Resumen del día para la pantalla de inicio. */
@RestController
@RequestMapping("/api/tablero")
@RequiredArgsConstructor
public class TableroController {

    public record Tablero(LocalDate fecha, int total, Map<EstadoTurno, Long> porEstado, List<TurnoDto> proximos) {}

    private final TurnoRepository turnos;
    private final UsuarioActual usuarioActual;
    private final Clock reloj;

    @GetMapping
    @Transactional(readOnly = true)
    public Tablero hoy() {
        LocalDate hoy = LocalDate.now(reloj);
        LocalDateTime ahora = LocalDateTime.now(reloj);
        Long medicoId = usuarioActual.esMedico() ? usuarioActual.medicoId() : null;
        List<Turno> delDia = turnos.buscar(hoy.atStartOfDay(), hoy.plusDays(1).atStartOfDay(), medicoId, null, null);

        Map<EstadoTurno, Long> porEstado = new EnumMap<>(EstadoTurno.class);
        for (EstadoTurno e : EstadoTurno.values()) {
            porEstado.put(e, delDia.stream().filter(t -> t.getEstado() == e).count());
        }
        List<TurnoDto> proximos = delDia.stream()
                .filter(t -> t.getEstado() == EstadoTurno.PENDIENTE
                        || t.getEstado() == EstadoTurno.CONFIRMADO
                        || t.getEstado() == EstadoTurno.EN_ATENCION)
                .filter(t -> t.getFin().isAfter(ahora))
                .limit(15)
                .map(TurnoDto::de)
                .toList();
        return new Tablero(hoy, delDia.size(), porEstado, proximos);
    }
}
