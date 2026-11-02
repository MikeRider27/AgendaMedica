package com.agendamedica.medico;

import java.time.LocalDate;
import java.time.LocalTime;

import com.agendamedica.catalogo.Consultorio;
import com.agendamedica.catalogo.Especialidad;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** Bloque semanal en que un médico atiende una especialidad (p. ej. lunes 08:00-12:00, turnos de 20 min). */
@Entity
@Table(name = "horarios_atencion")
@Getter
@Setter
public class HorarioAtencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultorio_id")
    private Consultorio consultorio;

    /** ISO-8601: 1 = lunes ... 7 = domingo. */
    private int diaSemana;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private int duracionTurnoMin;

    private LocalDate vigenteDesde;

    private LocalDate vigenteHasta;

    private boolean activo = true;

    public boolean vigenteEn(LocalDate fecha) {
        return activo && !fecha.isBefore(vigenteDesde) && (vigenteHasta == null || !fecha.isAfter(vigenteHasta));
    }

    public boolean seSuperponeCon(HorarioAtencion otro) {
        boolean vigenciasCruzadas = (otro.vigenteHasta == null || !vigenteDesde.isAfter(otro.vigenteHasta))
                && (vigenteHasta == null || !otro.vigenteDesde.isAfter(vigenteHasta));
        return diaSemana == otro.diaSemana && vigenciasCruzadas
                && horaInicio.isBefore(otro.horaFin) && otro.horaInicio.isBefore(horaFin);
    }
}
