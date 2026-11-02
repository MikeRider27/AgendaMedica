import { useState, type FormEvent } from 'react';
import { api } from '../api';
import { Alerta, Campo, Modal, useDatos, useEnvio } from '../components/comunes';

/** Definición de un catálogo simple: ruta del API y campos editables extra. */
interface Def {
  titulo: string;
  ruta: string;
  extra?: { clave: string; etiqueta: string; tipo: 'text' | 'number'; defecto: string | number }[];
}

const CATALOGOS: Def[] = [
  {
    titulo: 'Especialidades',
    ruta: '/especialidades',
    extra: [{ clave: 'duracionTurnoMin', etiqueta: 'Duración sugerida (min)', tipo: 'number', defecto: 20 }],
  },
  { titulo: 'Aseguradoras', ruta: '/aseguradoras' },
  {
    titulo: 'Consultorios',
    ruta: '/consultorios',
    extra: [{ clave: 'ubicacion', etiqueta: 'Ubicación', tipo: 'text', defecto: '' }],
  },
];

type Item = { id: number; nombre: string; activo: boolean } & Record<string, unknown>;

export default function Catalogos() {
  return (
    <section>
      <div className="titulo-pagina">
        <h1>Catálogos</h1>
      </div>
      <div className="columnas">
        {CATALOGOS.map((d) => (
          <Catalogo key={d.ruta} def={d} />
        ))}
      </div>
    </section>
  );
}

function Catalogo({ def }: { def: Def }) {
  const [editando, setEditando] = useState<Item | 'nuevo'>();
  const { datos, error, recargar } = useDatos(() => api.get<Item[]>(def.ruta), [def.ruta]);

  return (
    <div className="tarjeta">
      <div className="titulo-pagina">
        <h2>{def.titulo}</h2>
        <button className="btn chico primario" onClick={() => setEditando('nuevo')}>
          + Agregar
        </button>
      </div>
      <Alerta error={error} />
      <ul className="lista">
        {datos?.map((i) => (
          <li key={i.id} className={i.activo ? '' : 'inactivo'}>
            <span>
              {i.nombre}
              {def.extra?.map((x) => (i[x.clave] ? <span key={x.clave} className="tenue"> · {String(i[x.clave])}{x.tipo === 'number' ? ' min' : ''}</span> : null))}
              {!i.activo && <span className="badge">Inactivo</span>}
            </span>
            <button className="btn-link" onClick={() => setEditando(i)}>
              Editar
            </button>
          </li>
        ))}
      </ul>
      {editando && (
        <ItemForm
          def={def}
          item={editando === 'nuevo' ? undefined : editando}
          onCerrar={() => setEditando(undefined)}
          onGuardado={() => {
            setEditando(undefined);
            recargar();
          }}
        />
      )}
    </div>
  );
}

function ItemForm({
  def,
  item,
  onCerrar,
  onGuardado,
}: {
  def: Def;
  item?: Item;
  onCerrar: () => void;
  onGuardado: () => void;
}) {
  const [f, setF] = useState<Record<string, string | number | boolean>>(() => ({
    nombre: item?.nombre ?? '',
    activo: item?.activo ?? true,
    ...Object.fromEntries((def.extra ?? []).map((x) => [x.clave, (item?.[x.clave] as string | number) ?? x.defecto])),
  }));
  const { enviando, error, enviar } = useEnvio();

  const guardar = async (e: FormEvent) => {
    e.preventDefault();
    const ok = await enviar(() => (item ? api.put(`${def.ruta}/${item.id}`, f) : api.post(def.ruta, f)));
    if (ok) onGuardado();
  };

  return (
    <Modal titulo={`${item ? 'Editar' : 'Agregar'} — ${def.titulo}`} onCerrar={onCerrar} ancho={440}>
      <form onSubmit={guardar}>
        <Campo etiqueta="Nombre">
          <input autoFocus required value={String(f.nombre)} onChange={(e) => setF({ ...f, nombre: e.target.value })} />
        </Campo>
        {def.extra?.map((x) => (
          <Campo key={x.clave} etiqueta={x.etiqueta}>
            <input
              type={x.tipo}
              value={String(f[x.clave] ?? '')}
              onChange={(e) => setF({ ...f, [x.clave]: x.tipo === 'number' ? Number(e.target.value) : e.target.value })}
            />
          </Campo>
        ))}
        <label className="check">
          <input type="checkbox" checked={!!f.activo} onChange={(e) => setF({ ...f, activo: e.target.checked })} />
          Activo
        </label>
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
