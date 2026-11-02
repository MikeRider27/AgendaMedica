package com.agendamedica.medico;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BloqueoAgendaRepository extends JpaRepository<BloqueoAgenda, Long> {

    /** Bloqueos del médico que se cruzan con el intervalo [desde, hasta). */
    @Query("""
            select b from BloqueoAgenda b
            where b.medico.id = :medicoId and b.inicio < :hasta and b.fin > :desde
            order by b.inicio
            """)
    List<BloqueoAgenda> solapados(Long medicoId, LocalDateTime desde, LocalDateTime hasta);

    List<BloqueoAgenda> findByMedicoIdAndFinAfterOrderByInicio(Long medicoId, LocalDateTime desde);
}
