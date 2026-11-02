import { useState, type FormEvent } from 'react';
import { Link, useParams } from 'react-router-dom';
import { api } from '../api';
import { useAuth } from '../auth';
import { Alerta, Campo, Modal, useDatos, useEnvio } from '../components/comunes';
import type { Bloqueo, Consultorio, Horario, Medico } from '../types';
import { DIAS, fecha, opt } from '../util';

/** Configuración de la agenda de un médico: horarios semanales y bloqueos. */
export default function MedicoAgenda() {
  const { id } = useParams();
  const { tiene } = useAuth();
  const esAdmin = tiene('ADMIN');
  const [editando, setEditando] = useState<Horario | 'nuevo'>();
  const [bloqueando, setBloqueando] = useState(false);

  const { datos: medico, error } = useDatos(() => api.get<Medico>(`/medicos/${id}`), [id]);
  const { datos: horarios, recargar: recargarHorarios } = useDatos(
    () => api.get<Horario[]>(`/medicos/${id}/horarios`),
    [id],
  );
  const { datos: bloqueos, recargar: recargarBloqueos } = useDatos(
    () => api.get<Bloqueo[]>(`/medicos/${id}/bloqueos`),
    [id],
  );
  const accion = useEnvio();

  const eliminarHorario = (h: Horario) =>
    confirm(`¿Eliminar el horario del ${DIAS[h.diaSemana]} ${h.horaInicio.slice(0, 5)}?`) &&
    accion.enviar(() => api.del(`/medicos/${id}/horarios/${h.id}`)).then(recargarHorarios);

  const eliminarBloqueo = (b: Bloqueo) =>
    confirm('¿Eliminar el bloqueo?') &&
    accion.enviar(() => api.del(`/medicos/${id}/bloqueos/${b.id}`)).then(recargarBloqueos);

  if (error) return <Alerta error={error} />;
  if (!medico) return null;

  return (
    <section>
      <div className="titulo-pagina">
        <h1>
          <span className="punto grande" style={{ background: medico.color }} />
          {medico.nombreCompleto}
        </h1>
        <Link to="/medicos">← Médicos</Link>
      </div>
      <Alerta error={accion.error} />

      <div className="tarjeta">
        <div className="titulo-pagina">
          <h2>Horarios de atención</h2>
          {esAdmin && (
            <button className="btn primario" onClick={() => setEditando('nuevo')}>
              + Agregar horario
            </button>
          )}
        </div>
        {horarios?.length === 0 && <p className="tenue">Sin horarios cargados: no se le pueden dar turnos.</p>}
        {horarios && horarios.length > 0 && (
          <table>
            <thead>
              <tr>
                <th>Día</th>
                <th>Horario</th>
                <th>Especialidad</th>
                <th>Turnos de</th>
                <th>Consultorio</th>
                <th>Vigencia</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {horarios.map((h) => (
                <tr key={h.id} className={h.activo ? '' : 'inactivo'}>
                  <td>{DIAS[h.diaSemana]}</td>
                  <td>
                    {h.horaInicio.slice(0, 5)}–{h.horaFin.slice(0, 5)}
                  </td>
                  <td>{h.especialidad}</td>
                  <td>{h.duracionTurnoMin} min</td>
                  <td>{h.consultorio ?? '—'}</td>
                  <td>
                    {fecha(h.vigenteDesde)} → {h.vigenteHasta ? fecha(h.vigenteHasta) : 'sin fin'}
                  </td>
                  <td>
                    {esAdmin && (
                      <div className="acciones">
                        <button className="btn chico" onClick={() => setEditando(h)}>
                          Editar
                        </button>
                        <button className="btn chico peligro" onClick={() => eliminarHorario(h)}>
                          Eliminar
                        </button>
                      </div>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <div className="tarjeta">
        <div className="titulo-pagina">
          <h2>Bloqueos de agenda (vacaciones, ausencias)</h2>
          <button className="btn primario" onClick={() => setBloqueando(true)}>
            + Bloquear período
          </button>
        </div>
        {bloqueos?.length === 0 && <p className="tenue">Sin bloqueos vigentes.</p>}
        <ul className="lista">
          {bloqueos?.map((b) => (
            <li key={b.id}>
              {b.inicio.replace('T', ' ').slice(0, 16)} → {b.fin.replace('T', ' ').slice(0, 16)}
              {b.motivo && <span className="tenue"> · {b.motivo}</span>}
              <button className="btn-link" onClick={() => eliminarBloqueo(b)}>
                Eliminar
              </button>
            </li>
          ))}
        </ul>
      </div>

      {editando && (
        <HorarioForm
          medico={medico}
          horario={editando === 'nuevo' ? undefined : editando}
          onCerrar={() => setEditando(undefined)}
          onGuardado={() => {
            setEditando(undefined);
            recargarHorarios();
          }}
        />
      )}
      {bloqueando && (
        <BloqueoForm
          medicoId={medico.id}
          onCerrar={() => setBloqueando(false)}
          onGuardado={() => {
            setBloqueando(false);
            recargarBloqueos();
          }}
        />
      )}
    </section>
  );
}

function HorarioForm({
  medico,
  horario,
  onCerrar,
  onGuardado,
}: {
  medico: Medico;
  horario?: Horario;
  onCerrar: () => void;
  onGuardado: () => void;
}) {
  const [f, setF] = useState({
    especialidadId: String(horario?.especialidadId ?? medico.especialidades[0]?.id ?? ''),
    consultorioId: horario?.consultorioId ? String(horario.consultorioId) : '',
    diaSemana: String(horario?.diaSemana ?? 1),
    horaInicio: horario?.horaInicio.slice(0, 5) ?? '08:00',
    horaFin: horario?.horaFin.slice(0, 5) ?? '12:00',
    duracionTurnoMin: String(horario?.duracionTurnoMin ?? 20),
    vigenteDesde: horario?.vigenteDesde ?? '',
    vigenteHasta: horario?.vigenteHasta ?? '',
    activo: horario?.activo ?? true,
  });
  const { datos: consultorios } = useDatos(() => api.get<Consultorio[]>('/consultorios'), []);
  const { enviando, error, enviar } = useEnvio();
  const set = (k: keyof typeof f) => (e: { target: { value: string } }) => setF({ ...f, [k]: e.target.value });

  const guardar = async (e: FormEvent) => {
    e.preventDefault();
    const cuerpo = {
      ...f,
      especialidadId: Number(f.especialidadId),
      consultorioId: opt(f.consultorioId),
      diaSemana: Number(f.diaSemana),
      duracionTurnoMin: Number(f.duracionTurnoMin),
      vigenteDesde: opt(f.vigenteDesde),
      vigenteHasta: opt(f.vigenteHasta),
    };
    const base = `/medicos/${medico.id}/horarios`;
    const ok = await enviar(() => (horario ? api.put(`${base}/${horario.id}`, cuerpo) : api.post(base, cuerpo)));
    if (ok) onGuardado();
  };

  return (
    <Modal titulo={horario ? 'Editar horario' : 'Nuevo horario'} onCerrar={onCerrar} ancho={620}>
      <form onSubmit={guardar} className="grilla">
        <Campo etiqueta="Día">
          <select value={f.diaSemana} onChange={set('diaSemana')}>
            {DIAS.slice(1).map((d, i) => (
              <option key={d} value={i + 1}>
                {d}
              </option>
            ))}
          </select>
        </Campo>
        <Campo etiqueta="Desde">
          <input type="time" required value={f.horaInicio} onChange={set('horaInicio')} />
        </Campo>
        <Campo etiqueta="Hasta">
          <input type="time" required value={f.horaFin} onChange={set('horaFin')} />
        </Campo>
        <Campo etiqueta="Especialidad">
          <select value={f.especialidadId} onChange={set('especialidadId')}>
            {medico.especialidades.map((e) => (
              <option key={e.id} value={e.id}>
                {e.nombre}
              </option>
            ))}
          </select>
        </Campo>
        <Campo etiqueta="Duración del turno (min)">
          <input
            type="number"
            min={5}
            max={480}
            step={5}
            required
            value={f.duracionTurnoMin}
            onChange={set('duracionTurnoMin')}
          />
        </Campo>
        <Campo etiqueta="Consultorio">
          <select value={f.consultorioId} onChange={set('consultorioId')}>
            <option value="">—</option>
            {consultorios
              ?.filter((c) => c.activo || String(c.id) === f.consultorioId)
              .map((c) => (
                <option key={c.id} value={c.id}>
                  {c.nombre}
                </option>
              ))}
          </select>
        </Campo>
        <Campo etiqueta="Vigente desde">
          <input type="date" value={f.vigenteDesde} onChange={set('vigenteDesde')} />
        </Campo>
        <Campo etiqueta="Vigente hasta">
          <input type="date" value={f.vigenteHasta} onChange={set('vigenteHasta')} />
        </Campo>
        <label className="check">
          <input type="checkbox" checked={f.activo} onChange={(e) => setF({ ...f, activo: e.target.checked })} />
          Activo
        </label>
        <div className="col-3">
          <Alerta error={error} />
        </div>
        <div className="pie col-3">
          <button type="button" className="btn" onClick={onCerrar}>
            Cancelar
          </button>
          <button className="btn primario" disabled={enviando}>
            Guardar
          </button>
        </div>
      </form>
    </Modal>
  );
}

function BloqueoForm({
  medicoId,
  onCerrar,
  onGuardado,
}: {
  medicoId: number;
  onCerrar: () => void;
  onGuardado: () => void;
}) {
  const [inicio, setInicio] = useState('');
  const [fin, setFin] = useState('');
  const [motivo, setMotivo] = useState('');
  const { enviando, error, enviar } = useEnvio();

  const guardar = async (e: FormEvent) => {
    e.preventDefault();
    if (await enviar(() => api.post(`/medicos/${medicoId}/bloqueos`, { inicio, fin, motivo: opt(motivo) })))
      onGuardado();
  };

  return (
    <Modal titulo="Bloquear agenda" onCerrar={onCerrar} ancho={480}>
      <form onSubmit={guardar}>
        <Campo etiqueta="Desde">
          <input type="datetime-local" required value={inicio} onChange={(e) => setInicio(e.target.value)} />
        </Campo>
        <Campo etiqueta="Hasta">
          <input type="datetime-local" required value={fin} onChange={(e) => setFin(e.target.value)} />
        </Campo>
        <Campo etiqueta="Motivo">
          <input value={motivo} onChange={(e) => setMotivo(e.target.value)} maxLength={200} />
        </Campo>
        <p className="tenue pequeno">Los turnos ya dados en ese período no se cancelan automáticamente.</p>
        <Alerta error={error} />
        <div className="pie">
          <button type="button" className="btn" onClick={onCerrar}>
            Cancelar
          </button>
          <button className="btn primario" disabled={enviando}>
            Bloquear
          </button>
        </div>
      </form>
    </Modal>
  );
}
