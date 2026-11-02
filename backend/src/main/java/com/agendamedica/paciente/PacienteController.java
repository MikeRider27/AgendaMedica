package com.agendamedica.paciente;

import java.text.Normalizer;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agendamedica.catalogo.AseguradoraRepository;
import com.agendamedica.comun.NoEncontradoException;
import com.agendamedica.paciente.Paciente.Sexo;
import com.agendamedica.paciente.Paciente.TipoDocumento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    public record PacienteRequest(
            @NotNull TipoDocumento tipoDocumento,
            @NotBlank @Size(max = 20) String nroDocumento,
            @NotBlank @Size(max = 100) String nombres,
            @NotBlank @Size(max = 100) String apellidos,
            @NotNull @Past LocalDate fechaNacimiento,
            @NotNull Sexo sexo,
            @Size(max = 30) String telefono,
            @Email @Size(max = 150) String email,
            @Size(max = 200) String direccion,
            @Size(max = 100) String ciudad,
            Long aseguradoraId,
            @Size(max = 50) String nroAfiliado,
            @Pattern(regexp = "(A|B|AB|O)[+-]") String grupoSanguineo,
            String alergias,
            String observaciones,
            Boolean activo) {}

    private final PacienteRepository pacientes;
    private final AseguradoraRepository aseguradoras;

    @GetMapping
    @Transactional(readOnly = true)
    public Page<PacienteDto> listar(@RequestParam(required = false) String q,
                                    @PageableDefault(size = 20, sort = {"apellidos", "nombres"}) Pageable pageable) {
        Page<Paciente> pagina = (q == null || q.isBlank())
                ? pacientes.findAllBy(pageable)
                : pacientes.buscar("%" + sinTildes(q.trim().toLowerCase()) + "%", pageable);
        return pagina.map(PacienteDto::de);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public PacienteDto obtener(@PathVariable Long id) {
        return PacienteDto.de(buscar(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public PacienteDto crear(@Valid @RequestBody PacienteRequest req) {
        Paciente p = new Paciente();
        aplicar(p, req);
        return PacienteDto.de(pacientes.save(p));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    @Transactional
    public PacienteDto modificar(@PathVariable Long id, @Valid @RequestBody PacienteRequest req) {
        Paciente p = buscar(id);
        aplicar(p, req);
        return PacienteDto.de(pacientes.saveAndFlush(p));
    }

    private Paciente buscar(Long id) {
        return pacientes.findById(id).orElseThrow(() -> new NoEncontradoException("Paciente", id));
    }

    private void aplicar(Paciente p, PacienteRequest req) {
        p.setTipoDocumento(req.tipoDocumento());
        p.setNroDocumento(req.nroDocumento().trim());
        p.setNombres(req.nombres().trim());
        p.setApellidos(req.apellidos().trim());
        p.setFechaNacimiento(req.fechaNacimiento());
        p.setSexo(req.sexo());
        p.setTelefono(req.telefono());
        p.setEmail(blankANull(req.email()));
        p.setDireccion(req.direccion());
        p.setCiudad(req.ciudad());
        p.setAseguradora(req.aseguradoraId() == null ? null : aseguradoras.findById(req.aseguradoraId())
                .orElseThrow(() -> new NoEncontradoException("Aseguradora", req.aseguradoraId())));
        p.setNroAfiliado(req.nroAfiliado());
        p.setGrupoSanguineo(blankANull(req.grupoSanguineo()));
        p.setAlergias(req.alergias());
        p.setObservaciones(req.observaciones());
        if (req.activo() != null) {
            p.setActivo(req.activo());
        }
    }

    private static String sinTildes(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    private static String blankANull(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}
