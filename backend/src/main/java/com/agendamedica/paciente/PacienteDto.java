package com.agendamedica.paciente;

import java.time.LocalDate;
import java.time.Period;

import com.agendamedica.paciente.Paciente.Sexo;
import com.agendamedica.paciente.Paciente.TipoDocumento;

public record PacienteDto(Long id, TipoDocumento tipoDocumento, String nroDocumento, String nombres,
                          String apellidos, LocalDate fechaNacimiento, Integer edad, Sexo sexo,
                          String telefono, String email, String direccion, String ciudad,
                          Long aseguradoraId, String aseguradora, String nroAfiliado,
                          String grupoSanguineo, String alergias, String observaciones, boolean activo) {

    public static PacienteDto de(Paciente p) {
        var a = p.getAseguradora();
        return new PacienteDto(p.getId(), p.getTipoDocumento(), p.getNroDocumento(), p.getNombres(),
                p.getApellidos(), p.getFechaNacimiento(),
                Period.between(p.getFechaNacimiento(), LocalDate.now()).getYears(), p.getSexo(),
                p.getTelefono(), p.getEmail(), p.getDireccion(), p.getCiudad(),
                a == null ? null : a.getId(), a == null ? null : a.getNombre(), p.getNroAfiliado(),
                p.getGrupoSanguineo(), p.getAlergias(), p.getObservaciones(), p.isActivo());
    }
}
