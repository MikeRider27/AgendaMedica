package com.agendamedica.consulta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.agendamedica.catalogo.Especialidad;
import com.agendamedica.medico.Medico;
import com.agendamedica.paciente.Paciente;
import com.agendamedica.turno.Turno;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/** Registro de una atención médica: forma la historia clínica del paciente. */
@Entity
@Table(name = "consultas")
@Getter
@Setter
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turno_id")
    private Turno turno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    private LocalDateTime fecha;

    private String motivoConsulta;

    private String anamnesis;

    private String examenFisico;

    private String diagnostico;

    private String indicaciones;

    private BigDecimal pesoKg;

    private BigDecimal tallaCm;

    private String presionArterial;

    @Column(name = "temperatura_c")
    private BigDecimal temperaturaC;

    private LocalDate proximoControl;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creadoEn;
}
