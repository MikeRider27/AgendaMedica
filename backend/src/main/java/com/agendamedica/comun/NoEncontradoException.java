package com.agendamedica.comun;

public class NoEncontradoException extends RuntimeException {

    public NoEncontradoException(String entidad, Object id) {
        super(entidad + " no encontrado/a: " + id);
    }
}
