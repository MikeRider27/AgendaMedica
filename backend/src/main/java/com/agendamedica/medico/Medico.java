package com.agendamedica.medico;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.agendamedica.catalogo.Especialidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "medicos")
@Getter
@Setter
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nroDocumento;

    private String nombres;

    private String apellidos;

    private String matricula;

    private String telefono;

    private String email;

    /** Color con el que se pinta al médico en la agenda. */
    private String color;

    private boolean activo = true;

    @ManyToMany
    @JoinTable(name = "medico_especialidades",
            joinColumns = @JoinColumn(name = "medico_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidad_id"))
    private Set<Especialidad> especialidades = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    private LocalDateTime actualizadoEn;

    public String nombreCompleto() {
        return "Dr/a. " + nombres + " " + apellidos;
    }

    public boolean atiende(Especialidad especialidad) {
        return especialidades.stream().anyMatch(e -> e.getId().equals(especialidad.getId()));
    }
}
