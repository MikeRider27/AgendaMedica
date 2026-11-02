import { useState } from 'react';
import { api } from '../api';
import { useAuth } from '../auth';
import type { EstadoTurno, Turno } from '../types';
import { ESTADOS, SIGUIENTES } from '../util';
import ConsultaModal from './ConsultaModal';
import { Alerta, Modal, useEnvio } from './comunes';

const ESTADOS_MEDICO: EstadoTurno[] = ['CONFIRMADO', 'EN_ATENCION', 'ATENDIDO', 'AUSENTE'];

/** Botonera de cambio de estado + registro de consulta para un turno. */
export default function AccionesTurno({ turno, onCambio }: { turno: Turno; onCambio: () => void }) {
  const { tiene } = useAuth();
  const [cancelando, setCancelando] = useState(false);
  const [motivo, setMotivo] = useState('');
  const [atendiendo, setAtendiendo] = useState(false);
  const { enviando, error, enviar } = useEnvio();

  const esMedico = tiene('MEDICO');
  const puedeAtender = tiene('ADMIN', 'MEDICO') && ['PENDIENTE', 'CONFIRMADO', 'EN_ATENCION'].includes(turno.estado);
  const opciones = SIGUIENTES[turno.estado]
    .filter((e) => e !== 'ATENDIDO') // se marca al registrar la consulta
    .filter((e) => !esMedico || ESTADOS_MEDICO.includes(e));

  const cambiar = (estado: EstadoTurno, motivoCancelacion?: string) =>
    enviar(() => api.patch(`/turnos/${turno.id}/estado`, { estado, motivo: motivoCancelacion })).then(
      (ok) => ok && onCambio(),
    );

  return (
    <div className="acciones">
      {puedeAtender && (
        <button className="btn primario chico" onClick={() => setAtendiendo(true)}>
          Atender
        </button>
      )}
      {opciones.map((e) => (
        <button
          key={e}
          className={`btn chico${e === 'CANCELADO' ? ' peligro' : ''}`}
          disabled={enviando}
          onClick={() => (e === 'CANCELADO' ? setCancelando(true) : cambiar(e))}
        >
          {ESTADOS[e]}
        </button>
      ))}
      {error && !cancelando && <span className="error-linea">{error}</span>}

      {cancelando && (
        <Modal titulo="Cancelar turno" onCerrar={() => setCancelando(false)} ancho={420}>
          <p>
            {turno.paciente} — {turno.inicio.replace('T', ' ').slice(0, 16)}
          </p>
          <label className="campo">
            <span>Motivo de la cancelación</span>
            <input autoFocus value={motivo} onChange={(e) => setMotivo(e.target.value)} maxLength={250} />
          </label>
          <Alerta error={error} />
          <div className="pie">
            <button className="btn" onClick={() => setCancelando(false)}>
              Volver
            </button>
            <button
              className="btn peligro"
              disabled={enviando || !motivo.trim()}
              onClick={() => cambiar('CANCELADO', motivo).then(() => setCancelando(false))}
            >
              Cancelar turno
            </button>
          </div>
        </Modal>
      )}

      {atendiendo && (
        <ConsultaModal
          turno={turno}
          onCerrar={() => setAtendiendo(false)}
          onGuardado={() => {
            setAtendiendo(false);
            onCambio();
          }}
        />
      )}
    </div>
  );
}
