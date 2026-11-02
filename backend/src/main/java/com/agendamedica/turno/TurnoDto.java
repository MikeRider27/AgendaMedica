package com.agendamedica.turno;

import java.time.LocalDateTime;

public record TurnoDto(Long id, LocalDateTime inicio, LocalDateTime fin, EstadoTurno estado, TipoTurno tipo,
                       Long pacienteId, String paciente, String pacienteDocumento, String pacienteTelefono,
                       Long medicoId, String medico, String medicoColor,
                       Long especialidadId, String especialidad,
                       Long consultorioId, String consultorio,
                       String motivo, String observaciones, String motivoCancelacion) {

    public static TurnoDto de(Turno t) {
        var p = t.getPaciente();
        var m = t.getMedico();
        var c = t.getConsultorio();
        return new TurnoDto(t.getId(), t.getInicio(), t.getFin(), t.getEstado(), t.getTipo(),
                p.getId(), p.nombreCompleto(), p.getNroDocumento(), p.getTelefono(),
                m.getId(), m.nombreCompleto(), m.getColor(),
                t.getEspecialidad().getId(), t.getEspecialidad().getNombre(),
                c == null ? null : c.getId(), c == null ? null : c.getNombre(),
                t.getMotivo(), t.getObservaciones(), t.getMotivoCancelacion());
    }
}
