import { useState, type FormEvent } from 'react';
import { api } from '../api';
import { Alerta, Campo, Modal, useDatos, useEnvio } from '../components/comunes';
import type { Medico, Rol, Usuario } from '../types';
import { fecha, hora } from '../util';

const ROLES: Record<Rol, string> = { ADMIN: 'Administrador', RECEPCION: 'Recepción', MEDICO: 'Médico' };

export default function Usuarios() {
  const [editando, setEditando] = useState<Usuario | 'nuevo'>();
  const { datos, error, recargar } = useDatos(() => api.get<Usuario[]>('/usuarios'), []);

  return (
    <section>
      <div className="titulo-pagina">
        <h1>Usuarios</h1>
        <button className="btn primario" onClick={() => setEditando('nuevo')}>
          + Nuevo usuario
        </button>
      </div>
      <Alerta error={error} />
      <div className="tarjeta">
        <table>
          <thead>
            <tr>
              <th>Usuario</th>
              <th>Nombre</th>
              <th>Rol</th>
              <th>Médico vinculado</th>
              <th>Último acceso</th>
              <th />
            </tr>
          </thead>
          <tbody>
            {datos?.map((u) => (
              <tr key={u.id} className={u.activo ? '' : 'inactivo'}>
                <td>{u.username}</td>
                <td>{u.nombreCompleto}</td>
                <td>{ROLES[u.rol]}</td>
                <td>{u.medico ?? '—'}</td>
                <td>{u.ultimoAcceso ? `${fecha(u.ultimoAcceso)} ${hora(u.ultimoAcceso)}` : '—'}</td>
                <td>
                  {!u.activo && <span className="badge">Inactivo</span>}{' '}
                  <button className="btn chico" onClick={() => setEditando(u)}>
                    Editar
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {editando && (
        <UsuarioForm
          usuario={editando === 'nuevo' ? undefined : editando}
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

function UsuarioForm({
  usuario,
  onCerrar,
  onGuardado,
}: {
  usuario?: Usuario;
  onCerrar: () => void;
  onGuardado: () => void;
}) {
  const [f, setF] = useState({
    username: usuario?.username ?? '',
    password: '',
    nombreCompleto: usuario?.nombreCompleto ?? '',
    email: usuario?.email ?? '',
    rol: usuario?.rol ?? ('RECEPCION' as Rol),
    medicoId: usuario?.medicoId ? String(usuario.medicoId) : '',
    activo: usuario?.activo ?? true,
  });
  const { datos: medicos } = useDatos(() => api.get<Medico[]>('/medicos?soloActivos=true'), []);
  const { enviando, error, enviar } = useEnvio();
  const set = (k: keyof typeof f) => (e: { target: { value: string } }) => setF({ ...f, [k]: e.target.value });

  const guardar = async (e: FormEvent) => {
    e.preventDefault();
    const cuerpo = {
      ...f,
      password: f.password || undefined,
      email: f.email || undefined,
      medicoId: f.rol === 'MEDICO' && f.medicoId ? Number(f.medicoId) : undefined,
    };
    const ok = await enviar(() =>
      usuario ? api.put(`/usuarios/${usuario.id}`, cuerpo) : api.post('/usuarios', cuerpo),
    );
    if (ok) onGuardado();
  };

  return (
    <Modal titulo={usuario ? 'Editar usuario' : 'Nuevo usuario'} onCerrar={onCerrar} ancho={520}>
      <form onSubmit={guardar}>
        <Campo etiqueta="Usuario">
          <input required value={f.username} onChange={set('username')} autoComplete="off" />
        </Campo>
        <Campo etiqueta={usuario ? 'Nueva contraseña (vacío = no cambiar)' : 'Contraseña (mín. 8)'}>
          <input
            type="password"
            minLength={8}
            required={!usuario}
            value={f.password}
            onChange={set('password')}
            autoComplete="new-password"
          />
        </Campo>
        <Campo etiqueta="Nombre completo">
          <input required value={f.nombreCompleto} onChange={set('nombreCompleto')} />
        </Campo>
        <Campo etiqueta="Email">
          <input type="email" value={f.email} onChange={set('email')} />
        </Campo>
        <Campo etiqueta="Rol">
          <select value={f.rol} onChange={set('rol')}>
            {Object.entries(ROLES).map(([v, t]) => (
              <option key={v} value={v}>
                {t}
              </option>
            ))}
          </select>
        </Campo>
        {f.rol === 'MEDICO' && (
          <Campo etiqueta="Médico vinculado">
            <select required value={f.medicoId} onChange={set('medicoId')}>
              <option value="">Seleccione…</option>
              {medicos?.map((m) => (
                <option key={m.id} value={m.id}>
                  {m.apellidos}, {m.nombres}
                </option>
              ))}
            </select>
          </Campo>
        )}
        {usuario && (
          <label className="check">
            <input type="checkbox" checked={f.activo} onChange={(e) => setF({ ...f, activo: e.target.checked })} />
            Activo
          </label>
        )}
        <Alerta error={error} />
        <div className="pie">
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
