import { Link } from 'react-router-dom';
import { api } from '../api';
import { useAuth } from '../auth';
import AccionesTurno from '../components/AccionesTurno';
import { Alerta, EstadoBadge, useDatos } from '../components/comunes';
import type { EstadoTurno, Turno } from '../types';
import { ESTADOS, hora } from '../util';

interface Tablero {
  fecha: string;
  total: number;
  porEstado: Record<EstadoTurno, number>;
  proximos: Turno[];
}

export default function Inicio() {
  const { usuario } = useAuth();
  const { datos, error, recargar } = useDatos(() => api.get<Tablero>('/tablero'), []);

  return (
    <section>
      <div className="titulo-pagina">
        <h1>Panel del día</h1>
        <span className="tenue">Bienvenido/a, {usuario?.nombreCompleto}</span>
      </div>
      <Alerta error={error} />
      {datos && (
        <>
          <div className="indicadores">
            <div className="indicador">
              <span className="valor">{datos.total}</span>
              <span>Turnos hoy</span>
            </div>
            {(['PENDIENTE', 'CONFIRMADO', 'ATENDIDO', 'CANCELADO', 'AUSENTE'] as EstadoTurno[]).map((e) => (
              <div key={e} className={`indicador borde-${e}`}>
                <span className="valor">{datos.porEstado[e] ?? 0}</span>
                <span>{ESTADOS[e]}</span>
              </div>
            ))}
          </div>

          <div className="tarjeta">
            <div className="titulo-pagina">
              <h2>Próximos turnos de hoy</h2>
              <Link to="/agenda">Ir a la agenda →</Link>
            </div>
            {datos.proximos.length === 0 ? (
              <p className="tenue">No quedan turnos pendientes para hoy.</p>
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Hora</th>
                    <th>Paciente</th>
                    <th>Médico</th>
                    <th>Especialidad</th>
                    <th>Estado</th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {datos.proximos.map((t) => (
                    <tr key={t.id}>
                      <td>{hora(t.inicio)}</td>
                      <td>
                        <Link to={`/pacientes/${t.pacienteId}`}>{t.paciente}</Link>
                      </td>
                      <td>
                        <span className="punto" style={{ background: t.medicoColor }} />
                        {t.medico}
                      </td>
                      <td>{t.especialidad}</td>
                      <td>
                        <EstadoBadge estado={t.estado} />
                      </td>
                      <td>
                        <AccionesTurno turno={t} onCambio={recargar} />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </>
      )}
    </section>
  );
}
