package com.agendamedica.medico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public final class MedicoDtos {

    private MedicoDtos() {}

    public record EspecialidadRef(Long id, String nombre) {}

    public record MedicoDto(Long id, String nroDocumento, String nombres, String apellidos, String nombreCompleto,
                            String matricula, String telefono, String email, String color, boolean activo,
                            List<EspecialidadRef> especialidades) {

        public static MedicoDto de(Medico m) {
            return new MedicoDto(m.getId(), m.getNroDocumento(), m.getNombres(), m.getApellidos(),
                    m.nombreCompleto(), m.getMatricula(), m.getTelefono(), m.getEmail(), m.getColor(), m.isActivo(),
                    m.getEspecialidades().stream()
                            .map(e -> new EspecialidadRef(e.getId(), e.getNombre()))
                            .sorted(Comparator.comparing(EspecialidadRef::nombre))
                            .toList());
        }
    }

    public record HorarioDto(Long id, Long especialidadId, String especialidad, Long consultorioId,
                             String consultorio, int diaSemana, LocalTime horaInicio, LocalTime horaFin,
                             int duracionTurnoMin, LocalDate vigenteDesde, LocalDate vigenteHasta, boolean activo) {

        public static HorarioDto de(HorarioAtencion h) {
            var c = h.getConsultorio();
            return new HorarioDto(h.getId(), h.getEspecialidad().getId(), h.getEspecialidad().getNombre(),
                    c == null ? null : c.getId(), c == null ? null : c.getNombre(), h.getDiaSemana(),
                    h.getHoraInicio(), h.getHoraFin(), h.getDuracionTurnoMin(), h.getVigenteDesde(),
                    h.getVigenteHasta(), h.isActivo());
        }
    }

    public record BloqueoDto(Long id, LocalDateTime inicio, LocalDateTime fin, String motivo) {

        public static BloqueoDto de(BloqueoAgenda b) {
            return new BloqueoDto(b.getId(), b.getInicio(), b.getFin(), b.getMotivo());
        }
    }
}
