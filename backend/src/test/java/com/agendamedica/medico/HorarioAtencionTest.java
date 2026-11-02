package com.agendamedica.medico;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class HorarioAtencionTest {

    private static HorarioAtencion horario(int dia, String desde, String hasta, LocalDate vigDesde, LocalDate vigHasta) {
        HorarioAtencion h = new HorarioAtencion();
        h.setDiaSemana(dia);
        h.setHoraInicio(LocalTime.parse(desde));
        h.setHoraFin(LocalTime.parse(hasta));
        h.setVigenteDesde(vigDesde);
        h.setVigenteHasta(vigHasta);
        return h;
    }

    private static final LocalDate ENE = LocalDate.of(2026, 1, 1);
    private static final LocalDate JUN = LocalDate.of(2026, 6, 1);

    @Test
    void mismoDiaYHorasCruzadasSeSuperponen() {
        assertThat(horario(1, "08:00", "12:00", ENE, null)
                .seSuperponeCon(horario(1, "11:00", "13:00", ENE, null))).isTrue();
    }

    @Test
    void horariosContiguosNoSeSuperponen() {
        assertThat(horario(1, "08:00", "12:00", ENE, null)
                .seSuperponeCon(horario(1, "12:00", "16:00", ENE, null))).isFalse();
    }

    @Test
    void vigenciasSinCruceNoSeSuperponen() {
        assertThat(horario(1, "08:00", "12:00", ENE, JUN.minusDays(1))
                .seSuperponeCon(horario(1, "08:00", "12:00", JUN, null))).isFalse();
    }

    @Test
    void vigenteEnRespetaRangoYActivo() {
        HorarioAtencion h = horario(1, "08:00", "12:00", ENE, JUN);
        assertThat(h.vigenteEn(JUN)).isTrue();
        assertThat(h.vigenteEn(JUN.plusDays(1))).isFalse();
        h.setActivo(false);
        assertThat(h.vigenteEn(ENE)).isFalse();
    }
}
