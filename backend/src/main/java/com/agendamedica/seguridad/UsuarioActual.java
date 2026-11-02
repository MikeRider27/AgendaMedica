package com.agendamedica.seguridad;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.agendamedica.comun.ReglaNegocioException;
import com.agendamedica.usuario.Rol;

/** Datos del usuario autenticado, leídos del token JWT de la petición en curso. */
@Component
public class UsuarioActual {

    public Long id() {
        return jwt().getClaim("uid");
    }

    public Rol rol() {
        return Rol.valueOf(jwt().getClaimAsStringList("roles").getFirst());
    }

    public boolean esMedico() {
        return rol() == Rol.MEDICO;
    }

    /** Id del médico vinculado; sólo válido para usuarios con rol MEDICO. */
    public Long medicoId() {
        Long medicoId = jwt().getClaim("medicoId");
        if (medicoId == null) {
            throw new ReglaNegocioException("El usuario no está vinculado a un médico");
        }
        return medicoId;
    }

    private Jwt jwt() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
