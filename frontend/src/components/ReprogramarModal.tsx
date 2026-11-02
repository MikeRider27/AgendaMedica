import { useState } from 'react';
import { api, qs } from '../api';
import type { AgendaDia, Turno } from '../types';
import { diaSemanaLargo, hora, sumarDias } from '../util';
import { Alerta, Modal, useDatos, useEnvio } from './comunes';

/** Elige un nuevo horario libre del mismo médico y especialidad. */
export default function ReprogramarModal({
  turno,
  onCerrar,
  onHecho,
}: {
  turno: Turno;
  onCerrar: () => void;
  onHecho: () => void;
}) {
  const [dia, setDia] = useState(turno.inicio.slice(0, 10));
  const { datos, error: errorCarga } = useDatos(
    () =>
      api.get<AgendaDia>(
        `/agenda/dia${qs({ medicoId: turno.medicoId, fecha: dia, especialidadId: turno.especialidadId })}`,
      ),
    [dia],
  );
  const { enviando, error, enviar } = useEnvio();
  const libres = datos?.huecos.filter((h) => h.disponible) ?? [];

  const elegir = async (inicio: string) => {
    if (await enviar(() => api.put(`/turnos/${turno.id}/reprogramar`, { inicio }))) onHecho();
  };

  return (
    <Modal titulo={`Reprogramar turno de ${turno.paciente}`} onCerrar={onCerrar} ancho={560}>
      <p className="tenue">
        {turno.medico} · {turno.especialidad}
      </p>
      <div className="navegador-fecha">
        <button className="btn" onClick={() => setDia(sumarDias(dia, -1))}>
          ←
        </button>
        <input type="date" value={dia} onChange={(e) => e.target.value && setDia(e.target.value)} />
        <button className="btn" onClick={() => setDia(sumarDias(dia, 1))}>
          →
        </button>
        <span className="tenue">{diaSemanaLargo(dia)}</span>
      </div>
      <Alerta error={errorCarga ?? error} />
      {datos && libres.length === 0 && <p className="tenue">No hay horarios libres este día.</p>}
      <div className="huecos-libres">
        {libres.map((h) => (
          <button key={h.inicio} className="btn" disabled={enviando} onClick={() => elegir(h.inicio)}>
            {hora(h.inicio)}
          </button>
        ))}
      </div>
    </Modal>
  );
}
