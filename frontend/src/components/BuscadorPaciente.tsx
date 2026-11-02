import { useEffect, useState } from 'react';
import { api, qs } from '../api';
import type { Pagina, Paciente } from '../types';

/** Campo de búsqueda de pacientes por documento o nombre con lista de resultados. */
export default function BuscadorPaciente({
  seleccionado,
  onSeleccionar,
}: {
  seleccionado?: Paciente;
  onSeleccionar: (p?: Paciente) => void;
}) {
  const [q, setQ] = useState('');
  const [resultados, setResultados] = useState<Paciente[]>([]);

  useEffect(() => {
    if (q.trim().length < 2) {
      setResultados([]);
      return;
    }
    const t = setTimeout(() => {
      api
        .get<Pagina<Paciente>>(`/pacientes${qs({ q, size: 8 })}`)
        .then((r) => setResultados(r.content.filter((p) => p.activo)))
        .catch(() => setResultados([]));
    }, 250);
    return () => clearTimeout(t);
  }, [q]);

  if (seleccionado) {
    return (
      <div className="seleccion">
        <div>
          <strong>
            {seleccionado.apellidos}, {seleccionado.nombres}
          </strong>
          <div className="tenue">
            {seleccionado.tipoDocumento} {seleccionado.nroDocumento} · {seleccionado.edad} años
            {seleccionado.aseguradora ? ` · ${seleccionado.aseguradora}` : ''}
          </div>
        </div>
        <button type="button" className="btn-link" onClick={() => onSeleccionar(undefined)}>
          Cambiar
        </button>
      </div>
    );
  }

  return (
    <div className="buscador">
      <input
        autoFocus
        placeholder="Buscar por documento, nombre o apellido…"
        value={q}
        onChange={(e) => setQ(e.target.value)}
      />
      {resultados.length > 0 && (
        <ul className="sugerencias">
          {resultados.map((p) => (
            <li key={p.id}>
              <button type="button" onClick={() => onSeleccionar(p)}>
                <strong>
                  {p.apellidos}, {p.nombres}
                </strong>{' '}
                <span className="tenue">
                  {p.nroDocumento} · {p.edad} años
                </span>
              </button>
            </li>
          ))}
        </ul>
      )}
      {q.trim().length >= 2 && resultados.length === 0 && (
        <div className="tenue pequeno">Sin resultados. Registre al paciente en la sección Pacientes.</div>
      )}
    </div>
  );
}
