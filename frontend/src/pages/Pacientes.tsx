import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api, qs } from '../api';
import { useAuth } from '../auth';
import PacienteForm from '../components/PacienteForm';
import { Alerta, useDatos } from '../components/comunes';
import type { Pagina, Paciente } from '../types';
import { fecha } from '../util';

export default function Pacientes() {
  const { tiene } = useAuth();
  const navegar = useNavigate();
  const [texto, setTexto] = useState('');
  const [q, setQ] = useState('');
  const [pagina, setPagina] = useState(0);
  const [creando, setCreando] = useState(false);

  // Espera a que el usuario deje de escribir antes de buscar.
  useEffect(() => {
    const t = setTimeout(() => {
      setQ(texto);
      setPagina(0);
    }, 300);
    return () => clearTimeout(t);
  }, [texto]);

  const { datos, error } = useDatos(
    () => api.get<Pagina<Paciente>>(`/pacientes${qs({ q, page: pagina, size: 20 })}`),
    [q, pagina],
  );

  return (
    <section>
      <div className="titulo-pagina">
        <h1>Pacientes</h1>
        {tiene('ADMIN', 'RECEPCION') && (
          <button className="btn primario" onClick={() => setCreando(true)}>
            + Nuevo paciente
          </button>
        )}
      </div>
      <div className="tarjeta">
        <input
          className="busqueda"
          placeholder="Buscar por documento, nombre o apellido…"
          value={texto}
          onChange={(e) => setTexto(e.target.value)}
        />
        <Alerta error={error} />
        {datos && (
          <>
            <table>
              <thead>
                <tr>
                  <th>Documento</th>
                  <th>Apellidos y nombres</th>
                  <th>Nacimiento</th>
                  <th>Teléfono</th>
                  <th>Seguro</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {datos.content.map((p) => (
                  <tr key={p.id} className={p.activo ? '' : 'inactivo'}>
                    <td>
                      {p.tipoDocumento} {p.nroDocumento}
                    </td>
                    <td>
                      <Link to={`/pacientes/${p.id}`}>
                        {p.apellidos}, {p.nombres}
                      </Link>
                    </td>
                    <td>
                      {fecha(p.fechaNacimiento)} <span className="tenue">({p.edad})</span>
                    </td>
                    <td>{p.telefono}</td>
                    <td>{p.aseguradora}</td>
                    <td>{!p.activo && <span className="badge">Inactivo</span>}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            {datos.content.length === 0 && <p className="tenue">No se encontraron pacientes.</p>}
            {datos.page.totalPages > 1 && (
              <div className="paginador">
                <button className="btn" disabled={pagina === 0} onClick={() => setPagina(pagina - 1)}>
                  Anterior
                </button>
                <span>
                  Página {pagina + 1} de {datos.page.totalPages} ({datos.page.totalElements} pacientes)
                </span>
                <button
                  className="btn"
                  disabled={pagina + 1 >= datos.page.totalPages}
                  onClick={() => setPagina(pagina + 1)}
                >
                  Siguiente
                </button>
              </div>
            )}
          </>
        )}
      </div>
      {creando && <PacienteForm onCerrar={() => setCreando(false)} onGuardado={(p) => navegar(`/pacientes/${p.id}`)} />}
    </section>
  );
}
