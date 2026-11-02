package com.agendamedica.usuario;

import java.time.LocalDateTime;

public record UsuarioDto(Long id, String username, String nombreCompleto, String email, Rol rol,
                         Long medicoId, String medico, boolean activo, LocalDateTime ultimoAcceso) {

    public static UsuarioDto de(Usuario u) {
        var m = u.getMedico();
        return new UsuarioDto(u.getId(), u.getUsername(), u.getNombreCompleto(), u.getEmail(), u.getRol(),
                m == null ? null : m.getId(), m == null ? null : m.nombreCompleto(),
                u.isActivo(), u.getUltimoAcceso());
    }
}
