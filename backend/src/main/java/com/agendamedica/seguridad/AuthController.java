package com.agendamedica.seguridad;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agendamedica.comun.NoEncontradoException;
import com.agendamedica.usuario.Usuario;
import com.agendamedica.usuario.UsuarioDto;
import com.agendamedica.usuario.UsuarioRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

    public record LoginResponse(String token, Instant expira, UsuarioDto usuario) {}

    public record CambioClaveRequest(@NotBlank String actual, @NotBlank @Size(min = 8) String nueva) {}

    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final UsuarioActual usuarioActual;
    private final Clock reloj;
    private final Duration expiracion;

    AuthController(UsuarioRepository usuarios, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder,
                   UsuarioActual usuarioActual, Clock reloj,
                   @Value("${app.jwt.expiracion-horas}") long horas) {
        this.usuarios = usuarios;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.usuarioActual = usuarioActual;
        this.reloj = reloj;
        this.expiracion = Duration.ofHours(horas);
    }

    @PostMapping("/login")
    @Transactional
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        Usuario u = usuarios.findByUsername(req.username().trim())
                .filter(Usuario::isActivo)
                .filter(x -> passwordEncoder.matches(req.password(), x.getPasswordHash()))
                .orElseThrow(() -> new BadCredentialsException("Usuario o contraseña incorrectos"));
        u.setUltimoAcceso(LocalDateTime.now(reloj));

        Instant ahora = reloj.instant();
        Instant expira = ahora.plus(expiracion);
        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer("agenda-medica")
                .subject(u.getUsername())
                .issuedAt(ahora)
                .expiresAt(expira)
                .claim("uid", u.getId())
                .claim("roles", List.of(u.getRol().name()));
        if (u.getMedico() != null) {
            claims.claim("medicoId", u.getMedico().getId());
        }
        String token = jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims.build())).getTokenValue();
        return new LoginResponse(token, expira, UsuarioDto.de(u));
    }

    @GetMapping("/yo")
    @Transactional(readOnly = true)
    public UsuarioDto yo() {
        return UsuarioDto.de(actual());
    }

    @PutMapping("/clave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void cambiarClave(@Valid @RequestBody CambioClaveRequest req) {
        Usuario u = actual();
        if (!passwordEncoder.matches(req.actual(), u.getPasswordHash())) {
            throw new BadCredentialsException("La contraseña actual no es correcta");
        }
        u.setPasswordHash(passwordEncoder.encode(req.nueva()));
    }

    private Usuario actual() {
        return usuarios.findById(usuarioActual.id())
                .orElseThrow(() -> new NoEncontradoException("Usuario", usuarioActual.id()));
    }
}
