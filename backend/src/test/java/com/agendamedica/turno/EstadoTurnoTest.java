package com.agendamedica.turno;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EstadoTurnoTest {

    @Test
    void estadosFinalesNoTienenSalida() {
        assertThat(EstadoTurno.ATENDIDO.siguientes()).isEmpty();
        assertThat(EstadoTurno.CANCELADO.siguientes()).isEmpty();
        assertThat(EstadoTurno.AUSENTE.siguientes()).isEmpty();
    }

    @Test
    void pendientePuedeConfirmarseYCancelarse() {
        assertThat(EstadoTurno.PENDIENTE.puedePasarA(EstadoTurno.CONFIRMADO)).isTrue();
        assertThat(EstadoTurno.PENDIENTE.puedePasarA(EstadoTurno.CANCELADO)).isTrue();
        assertThat(EstadoTurno.CANCELADO.puedePasarA(EstadoTurno.PENDIENTE)).isFalse();
    }
}
