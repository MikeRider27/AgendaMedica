package com.agendamedica.comun;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorErrores {

    /** Mensajes legibles para las restricciones definidas en la base de datos. */
    private static final Map<String, String> RESTRICCIONES = Map.ofEntries(
            Map.entry("ex_turnos_medico_solapado", "El médico ya tiene un turno en ese horario"),
            Map.entry("ex_turnos_paciente_solapado", "El paciente ya tiene un turno en ese horario"),
            Map.entry("uq_pacientes_documento", "Ya existe un paciente con ese documento"),
            Map.entry("uq_medicos_documento", "Ya existe un médico con ese documento"),
            Map.entry("uq_medicos_matricula", "Ya existe un médico con esa matrícula"),
            Map.entry("uq_usuarios_username", "El nombre de usuario ya está en uso"),
            Map.entry("uq_usuarios_medico", "Ese médico ya tiene un usuario asignado"),
            Map.entry("ck_usuarios_medico", "Un usuario con rol MEDICO debe estar vinculado a un médico"),
            Map.entry("uq_especialidades_nombre", "Ya existe una especialidad con ese nombre"),
            Map.entry("uq_aseguradoras_nombre", "Ya existe una aseguradora con ese nombre"),
            Map.entry("uq_consultorios_nombre", "Ya existe un consultorio con ese nombre"),
            Map.entry("uq_consultas_turno", "El turno ya tiene una consulta registrada"),
            Map.entry("fk_horarios_medico_especialidad", "El médico no tiene asignada esa especialidad"),
            Map.entry("medico_especialidades_especialidad_id_fkey", "Especialidad inexistente"));

    @ExceptionHandler(NoEncontradoException.class)
    ProblemDetail noEncontrado(NoEncontradoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ReglaNegocioException.class)
    ProblemDetail reglaNegocio(ReglaNegocioException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    ProblemDetail credenciales(BadCredentialsException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validacion(MethodArgumentNotValidException e) {
        Map<String, String> campos = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(err -> campos.putIfAbsent(err.getField(), err.getDefaultMessage()));
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Datos inválidos");
        pd.setProperty("campos", campos);
        return pd;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail integridad(DataIntegrityViolationException e) {
        String detalle = String.valueOf(e.getMostSpecificCause().getMessage());
        String mensaje = RESTRICCIONES.entrySet().stream()
                .filter(r -> detalle.contains(r.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("La operación viola una restricción de integridad de datos");
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, mensaje);
    }
}
