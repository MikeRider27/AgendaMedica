import { useState, type FormEvent } from 'react';
import { api } from '../api';
import type { Hueco, Paciente, TipoTurno } from '../types';
import { fecha, hora, TIPOS } from '../util';
import BuscadorPaciente from './BuscadorPaciente';
import { Alerta, Campo, Modal, useEnvio } from './comunes';

export default function NuevoTurnoModal({
  medicoId,
  medico,
  hueco,
  onCerrar,
  onCreado,
}: {
  medicoId: number;
  medico: string;
  hueco: Hueco;
  onCerrar: () => void;
  onCreado: () => void;
}) {
  const [paciente, setPaciente] = useState<Paciente>();
  const [tipo, setTipo] = useState<TipoTurno>('CONSULTA');
  const [motivo, setMotivo] = useState('');
  const [observaciones, setObservaciones] = useState('');
  const { enviando, error, enviar } = useEnvio();

  const guardar = async (e: FormEvent) => {
    e.preventDefault();
    if (!paciente) return;
    const ok = await enviar(() =>
      api.post('/turnos', {
        pacienteId: paciente.id,
        medicoId,
        especialidadId: hueco.especialidadId,
        inicio: hueco.inicio,
        tipo,
        motivo: motivo || undefined,
        observaciones: observaciones || undefined,
      }),
    );
    if (ok) onCreado();
  };

  return (
    <Modal titulo="Nuevo turno" onCerrar={onCerrar} ancho={560}>
      <div className="resumen-turno">
        <div>
          <strong>
            {fecha(hueco.inicio)} {hora(hueco.inicio)}–{hora(hueco.fin)}
          </strong>
        </div>
        <div className="tenue">
          {medico} · {hueco.especialidad}
          {hueco.consultorio ? ` · ${hueco.consultorio}` : ''}
        </div>
      </div>
      <form onSubmit={guardar}>
        <Campo etiqueta="Paciente">
          <BuscadorPaciente seleccionado={paciente} onSeleccionar={setPaciente} />
        </Campo>
        {paciente?.alergias && <div className="aviso">Alergias: {paciente.alergias}</div>}
        <Campo etiqueta="Tipo">
          <select value={tipo} onChange={(e) => setTipo(e.target.value as TipoTurno)}>
            {Object.entries(TIPOS).map(([v, t]) => (
              <option key={v} value={v}>
                {t}
              </option>
            ))}
          </select>
        </Campo>
        <Campo etiqueta="Motivo">
          <input value={motivo} onChange={(e) => setMotivo(e.target.value)} maxLength={250} />
        </Campo>
        <Campo etiqueta="Observaciones">
          <textarea rows={2} value={observaciones} onChange={(e) => setObservaciones(e.target.value)} />
        </Campo>
        <Alerta error={error} />
        <div className="pie">
          <button type="button" className="btn" onClick={onCerrar}>
            Cancelar
          </button>
          <button className="btn primario" disabled={enviando || !paciente}>
            Agendar
          </button>
        </div>
      </form>
    </Modal>
  );
}
