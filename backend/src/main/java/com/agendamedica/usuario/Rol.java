package com.agendamedica.usuario;

public enum Rol {
    /** Acceso total, incluida la configuración. */
    ADMIN,
    /** Gestiona pacientes y turnos. No accede a la historia clínica. */
    RECEPCION,
    /** Ve su agenda y registra consultas. */
    MEDICO
}
