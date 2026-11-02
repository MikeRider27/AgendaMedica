package com.agendamedica.turno;

import java.util.Set;

public enum EstadoTurno {
    PENDIENTE,
    CONFIRMADO,
    EN_ATENCION,
    ATENDIDO,
    CANCELADO,
    AUSENTE;

    /** Transiciones permitidas desde este estado. */
    public Set<EstadoTurno> siguientes() {
        return switch (this) {
            case PENDIENTE -> Set.of(CONFIRMADO, EN_ATENCION, CANCELADO, AUSENTE);
            case CONFIRMADO -> Set.of(PENDIENTE, EN_ATENCION, ATENDIDO, CANCELADO, AUSENTE);
            case EN_ATENCION -> Set.of(CONFIRMADO, ATENDIDO);
            case ATENDIDO, CANCELADO, AUSENTE -> Set.of();
        };
    }

    public boolean puedePasarA(EstadoTurno destino) {
        return siguientes().contains(destino);
    }

    /** Estados en que el turno todavía puede reprogramarse. */
    public boolean esReprogramable() {
        return this == PENDIENTE || this == CONFIRMADO;
    }
}
