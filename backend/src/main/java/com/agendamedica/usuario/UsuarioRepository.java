package com.agendamedica.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @EntityGraph(attributePaths = "medico")
    Optional<Usuario> findByUsername(String username);

    @EntityGraph(attributePaths = "medico")
    List<Usuario> findAllByOrderByUsername();
}
