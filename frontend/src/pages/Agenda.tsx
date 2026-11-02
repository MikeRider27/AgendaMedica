import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api, qs } from '../api';
import { useAuth } from '../auth';
import AccionesTurno from '../components/AccionesTurno';
import NuevoTurnoModal from '../components/NuevoTurnoModal';
import { Alerta, EstadoBadge, useDatos } from '../components/comunes';
import type { AgendaDia, Especialidad, Hueco, Medico } from '../types';
import { diaSemanaLargo, hoyISO, hora, sumarDias, TIPOS } from '../util';

export default function Agenda() {
  const { usuario, tiene } = useAuth();
  const puedeAgendar = tiene('ADMIN', 'RECEPCION');

  const [especialidadId, setEspecialidadId] = useState('');
  const [medicoId, setMedicoId] = useState(usuario?.medicoId ? String(usuario.medicoId) : '');
  const [dia, setDia] = useState(hoyISO());
  const [reservando, setReservando] = useState<Hueco>();

  const { datos: especialidades } = useDatos(() => api.get<Especialidad[]>('/especialidades'), []);
  const { datos: medicos } = useDatos(
    () => api.get<Medico[]>(`/medicos${qs({ especialidadId, soloActivos: 'true' })}`),
    [especialidadId],
  );

  // Si el médico elegido no atiende la especialidad filtrada, se limpia la selección.
  useEffect(() => {
    if (medicos && medicoId && !medicos.some((m) => String(m.id) === medicoId)) setMedicoId('');
  }, [medicos, medicoId]);

  const { datos: agenda, error, recargar } = useDatos(
    () =>
      medicoId
        ? api.get<AgendaDia>(`/agenda/dia${qs({ medicoId, fecha: dia, especialidadId })}`)
        : Promise.resolve(undefined),
    [medicoId, dia, especialidadId],
  );

  const turnoPorId = new Map(agenda?.turnos.map((t) => [t.id, t]));
  const enHueco = new Set(agenda?.huecos.map((h) => h.turnoId).filter(Boolean));
  const fueraDeGrilla = agenda?.turnos.filter((t) => !enHueco.has(t.id)) ?? [];

  return (
    <section>
      <div className="titulo-pagina">
        <h1>Agenda</h1>
      </div>

      <div className="filtros tarjeta">
        <label className="campo">
          <span>Especialidad</span>
          <select value={especialidadId} onChange={(e) => setEspecialidadId(e.target.value)}>
            <option value="">Todas</option>
            {especialidades
              ?.filter((e) => e.activo)
              .map((e) => (
                <option key={e.id} value={e.id}>
                  {e.nombre}
                </option>
              ))}
          </select>
        </label>
        <label className="campo">
          <span>Médico</span>
          <select value={medicoId} onChange={(e) => setMedicoId(e.target.value)}>
            <option value="">Seleccione…</option>
            {medicos?.map((m) => (
              <option key={m.id} value={m.id}>
                {m.apellidos}, {m.nombres}
              </option>
            ))}
          </select>
        </label>
        <div className="campo">
          <span>Fecha</span>
          <div className="navegador-fecha">
            <button className="btn" onClick={() => setDia(sumarDias(dia, -1))} aria-label="Día anterior">
              ←
            </button>
            <input type="date" value={dia} onChange={(e) => e.target.value && setDia(e.target.value)} />
            <button className="btn" onClick={() => setDia(sumarDias(dia, 1))} aria-label="Día siguiente">
              →
            </button>
            <button className="btn" onClick={() => setDia(hoyISO())}>
              Hoy
            </button>
          </div>
        </div>
      </div>

      <Alerta error={error} />

      {!medicoId && <p className="tenue">Seleccione un médico para ver su agenda.</p>}

      {agenda && (
        <div className="tarjeta">
          <div className="titulo-pagina">
            <h2>{agenda.medico}</h2>
            <span className="tenue">{diaSemanaLargo(agenda.fecha)}</span>
          </div>

          {agenda.bloqueos.map((b) => (
            <div key={b.id} className="aviso">
              Agenda bloqueada {b.inicio.replace('T', ' ').slice(0, 16)} → {b.fin.replace('T', ' ').slice(0, 16)}
              {b.motivo ? ` (${b.motivo})` : ''}
            </div>
          ))}

          {agenda.huecos.length === 0 ? (
            <p className="tenue">El médico no tiene horario de atención este día.</p>
          ) : (
            <ul className="grilla-agenda">
              {agenda.huecos.map((h) => {
                const t = h.turnoId ? turnoPorId.get(h.turnoId) : undefined;
                return (
                  <li key={h.inicio + h.especialidadId} className={t ? 'ocupado' : h.disponible ? 'libre' : 'no-disp'}>
                    <span className="hora">{hora(h.inicio)}</span>
                    {t ? (
                      <div className="contenido">
                        <div>
                          <Link to={`/pacientes/${t.pacienteId}`}>
                            <strong>{t.paciente}</strong>
                          </Link>{' '}
                          <EstadoBadge estado={t.estado} /> <span className="tenue">{TIPOS[t.tipo]}</span>
                          {t.motivo && <div className="tenue pequeno">{t.motivo}</div>}
                        </div>
                        <AccionesTurno turno={t} onCambio={recargar} />
                      </div>
                    ) : (
                      <div className="contenido">
                        <span className="tenue">
                          {h.especialidad}
                          {h.consultorio ? ` · ${h.consultorio}` : ''}
                        </span>
                        {h.disponible && puedeAgendar ? (
                          <button className="btn chico" onClick={() => setReservando(h)}>
                            + Agendar
                          </button>
                        ) : (
                          <span className="tenue pequeno">{h.disponible ? 'Libre' : 'No disponible'}</span>
                        )}
                      </div>
                    )}
                  </li>
                );
              })}
            </ul>
          )}

          {fueraDeGrilla.length > 0 && (
            <>
              <h3>Otros turnos del día</h3>
              <ul className="grilla-agenda">
                {fueraDeGrilla.map((t) => (
                  <li key={t.id} className="ocupado">
                    <span className="hora">{hora(t.inicio)}</span>
                    <div className="contenido">
                      <div>
                        <strong>{t.paciente}</strong> <EstadoBadge estado={t.estado} />
                        {t.motivoCancelacion && <div className="tenue pequeno">{t.motivoCancelacion}</div>}
                      </div>
                      <AccionesTurno turno={t} onCambio={recargar} />
                    </div>
                  </li>
                ))}
              </ul>
            </>
          )}
        </div>
      )}

      {reservando && agenda && (
        <NuevoTurnoModal
          medicoId={agenda.medicoId}
          medico={agenda.medico}
          hueco={reservando}
          onCerrar={() => setReservando(undefined)}
          onCreado={() => {
            setReservando(undefined);
            recargar();
          }}
        />
      )}
    </section>
  );
}
