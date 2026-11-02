package com.agendamedica.comun;

/** Operación válida sintácticamente pero que viola una regla de la agenda. */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
