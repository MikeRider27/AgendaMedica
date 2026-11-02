import { useState } from 'react';
import { useParams } from 'react-router-dom';
import { api, qs } from '../api';
import { useAuth } from '../auth';
import ConsultaModal from '../components/ConsultaModal';
import PacienteForm from '../components/PacienteForm';
import { Alerta, EstadoBadge, useDatos } from '../components/comunes';
import type { Consulta, Paciente, Turno } from '../types';
import { fecha, hora } from '../util';

const SEXO = { M: 'Masculino', F: 'Femenino', X: 'Otro' };

export default function PacienteDetalle() {
  const { id } = useParams();
  const { tiene, usuario } = useAuth();
  const verHistoria = tiene('ADMIN', 'MEDICO');
  const [editando, setEditando] = useState(false);
  const [consultaEditada, setConsultaEditada] = useState<Consulta>();

  const { datos: p, error, recargar } = useDatos(() => api.get<Paciente>(`/pacientes/${id}`), [id]);
  const { datos: turnos } = useDatos(() => api.get<Turno[]>(`/turnos/paciente/${id}`), [id]);
  const { datos: consultas, recargar: recargarConsultas } = useDatos(
    () => (verHistoria ? api.get<Consulta[]>(`/consultas${qs({ pacienteId: id })}`) : Promise.resolve([])),
    [id, verHistoria],
  );

  if (error) return <Alerta error={error} />;
  if (!p) return null;

  return (
    <section>
      <div className="titulo-pagina">
        <h1>
          {p.apellidos}, {p.nombres} {!p.activo && <span className="badge">Inactivo</span>}
        </h1>
        {tiene('ADMIN', 'RECEPCION') && (
          <button className="btn" onClick={() => setEditando(true)}>
            Editar
          </button>
        )}
      </div>

      <div className="tarjeta ficha">
        <dl>
          <dt>Documento</dt>
          <dd>
            {p.tipoDocumento} {p.nroDocumento}
          </dd>
          <dt>Nacimiento</dt>
          <dd>
            {fecha(p.fechaNacimiento)} ({p.edad} años)
          </dd>
          <dt>Sexo</dt>
          <dd>{SEXO[p.sexo]}</dd>
          <dt>Teléfono</dt>
          <dd>{p.telefono || '—'}</dd>
          <dt>Email</dt>
          <dd>{p.email || '—'}</dd>
          <dt>Dirección</dt>
          <dd>{[p.direccion, p.ciudad].filter(Boolean).join(', ') || '—'}</dd>
          <dt>Seguro</dt>
          <dd>
            {p.aseguradora || 'Sin seguro'}
            {p.nroAfiliado ? ` · Nº ${p.nroAfiliado}` : ''}
          </dd>
          <dt>Grupo sanguíneo</dt>
          <dd>{p.grupoSanguineo || '—'}</dd>
        </dl>
        {p.alergias && <div className="aviso">Alergias: {p.alergias}</div>}
        {p.observaciones && <p className="tenue">{p.observaciones}</p>}
      </div>

      {verHistoria && (
        <div className="tarjeta">
          <h2>Historia clínica</h2>
          {consultas?.length === 0 && <p className="tenue">Sin consultas registradas.</p>}
          {consultas?.map((c) => (
            <article key={c.id} className="consulta">
              <header>
                <strong>{fecha(c.fecha)}</strong> · {c.especialidad} · {c.medico}
                {(tiene('ADMIN') || c.medicoId === usuario?.medicoId) && (
                  <button className="btn-link" onClick={() => setConsultaEditada(c)}>
                    Editar
                  </button>
                )}
              </header>
              <p>
                <b>Motivo:</b> {c.motivoConsulta}
              </p>
              {c.anamnesis && (
                <p>
                  <b>Anamnesis:</b> {c.anamnesis}
                </p>
              )}
              {c.examenFisico && (
                <p>
                  <b>Examen físico:</b> {c.examenFisico}
                </p>
              )}
              {c.diagnostico && (
                <p>
                  <b>Diagnóstico:</b> {c.diagnostico}
                </p>
              )}
              {c.indicaciones && (
                <p>
                  <b>Indicaciones:</b> {c.indicaciones}
                </p>
              )}
              <p className="tenue pequeno">
                {[
                  c.pesoKg && `Peso ${c.pesoKg} kg`,
                  c.tallaCm && `Talla ${c.tallaCm} cm`,
                  c.presionArterial && `PA ${c.presionArterial}`,
                  c.temperaturaC && `T° ${c.temperaturaC} °C`,
                  c.proximoControl && `Próximo control ${fecha(c.proximoControl)}`,
                ]
                  .filter(Boolean)
                  .join(' · ')}
              </p>
            </article>
          ))}
        </div>
      )}

      <div className="tarjeta">
        <h2>Turnos</h2>
        {turnos?.length === 0 && <p className="tenue">Sin turnos.</p>}
        {turnos && turnos.length > 0 && (
          <table>
            <thead>
              <tr>
                <th>Fecha</th>
                <th>Hora</th>
                <th>Médico</th>
                <th>Especialidad</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              {turnos.map((t) => (
                <tr key={t.id}>
                  <td>{fecha(t.inicio)}</td>
                  <td>{hora(t.inicio)}</td>
                  <td>{t.medico}</td>
                  <td>{t.especialidad}</td>
                  <td>
                    <EstadoBadge estado={t.estado} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {editando && (
        <PacienteForm
          paciente={p}
          onCerrar={() => setEditando(false)}
          onGuardado={() => {
            setEditando(false);
            recargar();
          }}
        />
      )}
      {consultaEditada && (
        <ConsultaModal
          consulta={consultaEditada}
          onCerrar={() => setConsultaEditada(undefined)}
          onGuardado={() => {
            setConsultaEditada(undefined);
            recargarConsultas();
          }}
        />
      )}
    </section>
  );
}
