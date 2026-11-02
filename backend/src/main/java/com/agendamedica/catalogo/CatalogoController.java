package com.agendamedica.catalogo;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

/** Tablas de referencia. Lectura para cualquier usuario autenticado; escritura sólo ADMIN. */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogoController {

    public record EspecialidadRequest(@NotBlank @Size(max = 100) String nombre,
                                      @Min(5) @Max(480) int duracionTurnoMin,
                                      boolean activo) {}

    public record AseguradoraRequest(@NotBlank @Size(max = 100) String nombre, boolean activo) {}

    public record ConsultorioRequest(@NotBlank @Size(max = 60) String nombre,
                                     @Size(max = 150) String ubicacion,
                                     boolean activo) {}

    private final EspecialidadRepository especialidades;
    private final AseguradoraRepository aseguradoras;
    private final ConsultorioRepository consultorios;

    // ---- Especialidades ----

    @GetMapping("/especialidades")
    public List<Especialidad> listarEspecialidades() {
        return especialidades.findAllByOrderByNombre();
    }

    @PostMapping("/especialidades")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Especialidad crearEspecialidad(@Valid @RequestBody EspecialidadRequest req) {
        return especialidades.save(aplicar(new Especialidad(), req));
    }

    @PutMapping("/especialidades/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Especialidad modificarEspecialidad(@PathVariable Long id, @Valid @RequestBody EspecialidadRequest req) {
        return aplicar(especialidades.findById(id)
                .orElseThrow(() -> new NoEncontradoException("Especialidad", id)), req);
    }

    private Especialidad aplicar(Especialidad e, EspecialidadRequest req) {
        e.setNombre(req.nombre().trim());
        e.setDuracionTurnoMin(req.duracionTurnoMin());
        e.setActivo(req.activo());
        return e;
    }

    // ---- Aseguradoras ----

    @GetMapping("/aseguradoras")
    public List<Aseguradora> listarAseguradoras() {
        return aseguradoras.findAllByOrderByNombre();
    }

    @PostMapping("/aseguradoras")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Aseguradora crearAseguradora(@Valid @RequestBody AseguradoraRequest req) {
        return aseguradoras.save(aplicar(new Aseguradora(), req));
    }

    @PutMapping("/aseguradoras/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Aseguradora modificarAseguradora(@PathVariable Long id, @Valid @RequestBody AseguradoraRequest req) {
        return aplicar(aseguradoras.findById(id)
                .orElseThrow(() -> new NoEncontradoException("Aseguradora", id)), req);
    }

    private Aseguradora aplicar(Aseguradora a, AseguradoraRequest req) {
        a.setNombre(req.nombre().trim());
        a.setActivo(req.activo());
        return a;
    }

    // ---- Consultorios ----

    @GetMapping("/consultorios")
    public List<Consultorio> listarConsultorios() {
        return consultorios.findAllByOrderByNombre();
    }

    @PostMapping("/consultorios")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Consultorio crearConsultorio(@Valid @RequestBody ConsultorioRequest req) {
        return consultorios.save(aplicar(new Consultorio(), req));
    }

    @PutMapping("/consultorios/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Consultorio modificarConsultorio(@PathVariable Long id, @Valid @RequestBody ConsultorioRequest req) {
        return aplicar(consultorios.findById(id)
                .orElseThrow(() -> new NoEncontradoException("Consultorio", id)), req);
    }

    private Consultorio aplicar(Consultorio c, ConsultorioRequest req) {
        c.setNombre(req.nombre().trim());
        c.setUbicacion(req.ubicacion());
        c.setActivo(req.activo());
        return c;
    }
}
