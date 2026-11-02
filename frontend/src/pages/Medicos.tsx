import { useState, type FormEvent } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../api';
import { useAuth } from '../auth';
import { Alerta, Campo, Modal, useDatos, useEnvio } from '../components/comunes';
import type { Especialidad, Medico } from '../types';

export default function Medicos() {
  const { tiene } = useAuth();
  const [editando, setEditando] = useState<Medico | 'nuevo'>();
  const { datos, error, recargar } = useDatos(() => api.get<Medico[]>('/medicos'), []);

  return (
    <section>
      <div className="titulo-pagina">
        <h1>Médicos</h1>
        {tiene('ADMIN') && (
          <button className="btn primario" onClick={() => setEditando('nuevo')}>
            + Nuevo médico
          </button>
        )}
      </div>
      <Alerta error={error} />
      <div className="tarjeta">
        <table>
          <thead>
            <tr>
              <th>Médico</th>
              <th>Matrícula</th>
              <th>Especialidades</th>
              <th>Contacto</th>
              <th />
            </tr>
          </thead>
          <tbody>
            {datos?.map((m) => (
              <tr key={m.id} className={m.activo ? '' : 'inactivo'}>
                <td>
                  <span className="punto" style={{ background: m.color }} />
                  {m.apellidos}, {m.nombres} {!m.activo && <span className="badge">Inactivo</span>}
                </td>
                <td>{m.matricula}</td>
                <td>{m.especialidades.map((e) => e.nombre).join(', ')}</td>
                <td>{[m.telefono, m.email].filter(Boolean).join(' · ')}</td>
                <td>
                  <div className="acciones">
                    <Link className="btn chico" to={`/medicos/${m.id}`}>
                      Horarios
                    </Link>
                    {tiene('ADMIN') && (
                      <button className="btn chico" onClick={() => setEditando(m)}>
                        Editar
                      </button>
                    )}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {editando && (
        <MedicoForm
          medico={editando === 'nuevo' ? undefined : editando}
          onCerrar={() => setEditando(undefined)}
          onGuardado={() => {
            setEditando(undefined);
            recargar();
          }}
        />
      )}
    </section>
  );
}

function MedicoForm({
  medico,
  onCerrar,
  onGuardado,
}: {
  medico?: Medico;
  onCerrar: () => void;
  onGuardado: () => void;
}) {
  const [f, setF] = useState({
    nroDocumento: medico?.nroDocumento ?? '',
    nombres: medico?.nombres ?? '',
    apellidos: medico?.apellidos ?? '',
    matricula: medico?.matricula ?? '',
    telefono: medico?.telefono ?? '',
    email: medico?.email ?? '',
    color: medico?.color ?? '#2563eb',
    activo: medico?.activo ?? true,
  });
  const [especialidadIds, setEspecialidadIds] = useState<number[]>(medico?.especialidades.map((e) => e.id) ?? []);
  const { datos: especialidades } = useDatos(() => api.get<Especialidad[]>('/especialidades'), []);
  const { enviando, error, enviar } = useEnvio();
  const set = (k: keyof typeof f) => (e: { target: { value: string } }) => setF({ ...f, [k]: e.target.value });

  const alternar = (id: number) =>
    setEspecialidadIds(especialidadIds.includes(id) ? especialidadIds.filter((x) => x !== id) : [...especialidadIds, id]);

  const guardar = async (e: FormEvent) => {
    e.preventDefault();
    const cuerpo = { ...f, email: f.email || undefined, especialidadIds };
    const ok = await enviar(() => (medico ? api.put(`/medicos/${medico.id}`, cuerpo) : api.post('/medicos', cuerpo)));
    if (ok) onGuardado();
  };

  return (
    <Modal titulo={medico ? 'Editar médico' : 'Nuevo médico'} onCerrar={onCerrar} ancho={680}>
      <form onSubmit={guardar} className="grilla">
        <Campo etiqueta="Nombres *">
          <input required value={f.nombres} onChange={set('nombres')} />
        </Campo>
        <Campo etiqueta="Apellidos *">
          <input required value={f.apellidos} onChange={set('apellidos')} />
        </Campo>
        <Campo etiqueta="Nro. documento *">
          <input required value={f.nroDocumento} onChange={set('nroDocumento')} />
        </Campo>
        <Campo etiqueta="Matrícula *">
          <input required value={f.matricula} onChange={set('matricula')} />
        </Campo>
        <Campo etiqueta="Teléfono">
          <input value={f.telefono} onChange={set('telefono')} />
        </Campo>
        <Campo etiqueta="Email">
          <input type="email" value={f.email} onChange={set('email')} />
        </Campo>
        <Campo etiqueta="Color en agenda">
          <input type="color" value={f.color} onChange={set('color')} />
        </Campo>
        {medico && (
          <label className="check">
            <input type="checkbox" checked={f.activo} onChange={(e) => setF({ ...f, activo: e.target.checked })} />
            Activo
          </label>
        )}
        <fieldset className="col-3">
          <legend>Especialidades *</legend>
          <div className="checks">
            {especialidades
              ?.filter((e) => e.activo || especialidadIds.includes(e.id))
              .map((e) => (
                <label key={e.id} className="check">
                  <input type="checkbox" checked={especialidadIds.includes(e.id)} onChange={() => alternar(e.id)} />
                  {e.nombre}
                </label>
              ))}
          </div>
        </fieldset>
        <div className="col-3">
          <Alerta error={error} />
        </div>
        <div className="pie col-3">
          <button type="button" className="btn" onClick={onCerrar}>
            Cancelar
          </button>
          <button className="btn primario" disabled={enviando || especialidadIds.length === 0}>
            Guardar
          </button>
        </div>
      </form>
    </Modal>
  );
}
