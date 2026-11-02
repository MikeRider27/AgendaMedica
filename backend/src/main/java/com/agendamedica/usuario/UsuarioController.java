package com.agendamedica.usuario;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agendamedica.comun.NoEncontradoException;
import com.agendamedica.comun.ReglaNegocioException;
import com.agendamedica.medico.MedicoRepository;
import com.agendamedica.seguridad.UsuarioActual;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UsuarioController {

    /** En altas la contraseña es obligatoria; en modificaciones, vacía = no cambiar. */
    public record UsuarioRequest(
            @NotBlank @Size(max = 50) @Pattern(regexp = "[a-zA-Z0-9._-]+", message = "sólo letras, números, punto, guion") String username,
            @Size(min = 8, message = "mínimo 8 caracteres") String password,
            @NotBlank @Size(max = 150) String nombreCompleto,
            @Email @Size(max = 150) String email,
            @NotNull Rol rol,
            Long medicoId,
            Boolean activo) {}

    private final UsuarioRepository usuarios;
    private final MedicoRepository medicos;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioActual usuarioActual;

    @GetMapping
    @Transactional(readOnly = true)
    public List<UsuarioDto> listar() {
        return usuarios.findAllByOrderByUsername().stream().map(UsuarioDto::de).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UsuarioDto crear(@Valid @RequestBody UsuarioRequest req) {
        if (req.password() == null || req.password().isBlank()) {
            throw new ReglaNegocioException("La contraseña es obligatoria");
        }
        Usuario u = new Usuario();
        aplicar(u, req);
        return UsuarioDto.de(usuarios.save(u));
    }

    @PutMapping("/{id}")
    @Transactional
    public UsuarioDto modificar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest req) {
        Usuario u = usuarios.findById(id).orElseThrow(() -> new NoEncontradoException("Usuario", id));
        if (id.equals(usuarioActual.id()) && (Boolean.FALSE.equals(req.activo()) || req.rol() != Rol.ADMIN)) {
            throw new ReglaNegocioException("No puede desactivarse ni quitarse el rol de administrador a sí mismo");
        }
        aplicar(u, req);
        return UsuarioDto.de(u);
    }

    private void aplicar(Usuario u, UsuarioRequest req) {
        u.setUsername(req.username().trim());
        u.setNombreCompleto(req.nombreCompleto().trim());
        u.setEmail(req.email());
        u.setRol(req.rol());
        if (req.activo() != null) {
            u.setActivo(req.activo());
        }
        if (req.password() != null && !req.password().isBlank()) {
            u.setPasswordHash(passwordEncoder.encode(req.password()));
        }
        if (req.rol() == Rol.MEDICO) {
            if (req.medicoId() == null) {
                throw new ReglaNegocioException("Un usuario con rol MEDICO debe estar vinculado a un médico");
            }
            u.setMedico(medicos.findById(req.medicoId())
                    .orElseThrow(() -> new NoEncontradoException("Médico", req.medicoId())));
        } else {
            u.setMedico(null);
        }
    }
}
