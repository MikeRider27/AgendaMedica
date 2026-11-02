package com.agendamedica.seguridad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.agendamedica.usuario.Rol;
import com.agendamedica.usuario.Usuario;
import com.agendamedica.usuario.UsuarioRepository;

/** Crea el administrador inicial si la tabla de usuarios está vacía. */
@Component
class InicializadorAdmin implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(InicializadorAdmin.class);

    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String password;

    InicializadorAdmin(UsuarioRepository usuarios, PasswordEncoder passwordEncoder,
                       @Value("${app.admin.username}") String username,
                       @Value("${app.admin.password}") String password) {
        this.usuarios = usuarios;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (usuarios.count() > 0) {
            return;
        }
        Usuario admin = new Usuario();
        admin.setUsername(username);
        admin.setPasswordHash(passwordEncoder.encode(password));
        admin.setNombreCompleto("Administrador");
        admin.setRol(Rol.ADMIN);
        usuarios.save(admin);
        log.warn("Usuario administrador '{}' creado. Cambie la contraseña tras el primer ingreso.", username);
    }
}
