import { useState } from 'react';
import { Link } from 'react-router-dom';
import { api, qs } from '../api';
import { useAuth } from '../auth';
import AccionesTurno from '../components/AccionesTurno';
import ReprogramarModal from '../components/ReprogramarModal';
import { Alerta, EstadoBadge, useDatos } from '../components/comunes';
import type { Medico, Turno } from '../types';
import { ESTADOS, fecha, hora, hoyISO, sumarDias, TIPOS } from '../util';

export default function Turnos() {
  const { tiene } = useAuth();
  const [desde, setDesde] = useState(hoyISO());
  const [hasta, setHasta] = useState(sumarDias(hoyISO(), 7));
  const [medicoId, setMedicoId] = useState('');
  const [estado, setEstado] = useState('');
  const [reprogramando, setReprogramando] = useState<Turno>();

  const { datos: medicos } = useDatos(
    () => (tiene('MEDICO') ? Promise.resolve([] as Medico[]) : api.get<Medico[]>('/medicos')),
    [],
  );
  const { datos: turnos, error, cargando, recargar } = useDatos(
    () => api.get<Turno[]>(`/turnos${qs({ desde, hasta, medicoId, estado })}`),
    [desde, hasta, medicoId, estado],
  );

  return (
    <section>
      <div className="titulo-pagina">
        <h1>Turnos</h1>
      </div>
      <div className="filtros tarjeta">
        <label className="campo">
          <span>Desde</span>
          <input type="date" value={desde} onChange={(e) => e.target.value && setDesde(e.target.value)} />
        </label>
        <label className="campo">
          <span>Hasta</span>
          <input type="date" value={hasta} onChange={(e) => e.target.value && setHasta(e.target.value)} />
        </label>
        {!tiene('MEDICO') && (
          <label className="campo">
            <span>Médico</span>
            <select value={medicoId} onChange={(e) => setMedicoId(e.target.value)}>
              <option value="">Todos</option>
              {medicos?.map((m) => (
                <option key={m.id} value={m.id}>
                  {m.apellidos}, {m.nombres}
                </option>
              ))}
            </select>
          </label>
        )}
        <label className="campo">
          <span>Estado</span>
          <select value={estado} onChange={(e) => setEstado(e.target.value)}>
            <option value="">Todos</option>
            {Object.entries(ESTADOS).map(([v, t]) => (
              <option key={v} value={v}>
                {t}
              </option>
            ))}
          </select>
        </label>
      </div>

      <Alerta error={error} />

      <div className="tarjeta">
        {turnos && turnos.length === 0 && !cargando && <p className="tenue">No hay turnos con esos filtros.</p>}
        {turnos && turnos.length > 0 && (
          <table>
            <thead>
              <tr>
                <th>Fecha</th>
                <th>Hora</th>
                <th>Paciente</th>
                <th>Médico</th>
                <th>Especialidad</th>
                <th>Tipo</th>
                <th>Estado</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {turnos.map((t) => (
                <tr key={t.id}>
                  <td>{fecha(t.inicio)}</td>
                  <td>{hora(t.inicio)}</td>
                  <td>
                    <Link to={`/pacientes/${t.pacienteId}`}>{t.paciente}</Link>
                    <div className="tenue pequeno">
                      {t.pacienteDocumento}
                      {t.pacienteTelefono ? ` · ${t.pacienteTelefono}` : ''}
                    </div>
                  </td>
                  <td>
                    <span className="punto" style={{ background: t.medicoColor }} />
                    {t.medico}
                  </td>
                  <td>{t.especialidad}</td>
                  <td>{TIPOS[t.tipo]}</td>
                  <td>
                    <EstadoBadge estado={t.estado} />
                    {t.motivoCancelacion && <div className="tenue pequeno">{t.motivoCancelacion}</div>}
                  </td>
                  <td>
                    <div className="acciones">
                      <AccionesTurno turno={t} onCambio={recargar} />
                      {tiene('ADMIN', 'RECEPCION') && ['PENDIENTE', 'CONFIRMADO'].includes(t.estado) && (
                        <button className="btn chico" onClick={() => setReprogramando(t)}>
                          Reprogramar
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {reprogramando && (
        <ReprogramarModal
          turno={reprogramando}
          onCerrar={() => setReprogramando(undefined)}
          onHecho={() => {
            setReprogramando(undefined);
            recargar();
          }}
        />
      )}
    </section>
  );
}
