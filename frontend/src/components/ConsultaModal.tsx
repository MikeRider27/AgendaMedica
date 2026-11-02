import { useState, type FormEvent } from 'react';
import { api } from '../api';
import type { Consulta, Turno } from '../types';
import { opt } from '../util';
import { Alerta, Campo, Modal, useEnvio } from './comunes';

type Form = Record<
  | 'motivoConsulta'
  | 'anamnesis'
  | 'examenFisico'
  | 'diagnostico'
  | 'indicaciones'
  | 'pesoKg'
  | 'tallaCm'
  | 'presionArterial'
  | 'temperaturaC'
  | 'proximoControl',
  string
>;

const VACIO: Form = {
  motivoConsulta: '',
  anamnesis: '',
  examenFisico: '',
  diagnostico: '',
  indicaciones: '',
  pesoKg: '',
  tallaCm: '',
  presionArterial: '',
  temperaturaC: '',
  proximoControl: '',
};

/** Registro de la atención de un turno (o edición de una consulta existente). */
export default function ConsultaModal({
  turno,
  consulta,
  onCerrar,
  onGuardado,
}: {
  turno?: Turno;
  consulta?: Consulta;
  onCerrar: () => void;
  onGuardado: () => void;
}) {
  const [f, setF] = useState<Form>(() =>
    consulta
      ? (Object.fromEntries(
          Object.keys(VACIO).map((k) => [k, String(consulta[k as keyof Consulta] ?? '')]),
        ) as Form)
      : { ...VACIO, motivoConsulta: turno?.motivo ?? '' },
  );
  const { enviando, error, enviar } = useEnvio();
  const set = (k: keyof Form) => (e: { target: { value: string } }) => setF({ ...f, [k]: e.target.value });

  const guardar = async (e: FormEvent) => {
    e.preventDefault();
    const cuerpo = {
      ...f,
      pesoKg: opt(f.pesoKg),
      tallaCm: opt(f.tallaCm),
      temperaturaC: opt(f.temperaturaC),
      proximoControl: opt(f.proximoControl),
    };
    const ok = await enviar(() =>
      consulta ? api.put(`/consultas/${consulta.id}`, cuerpo) : api.post(`/consultas/turno/${turno!.id}`, cuerpo),
    );
    if (ok) onGuardado();
  };

  const titulo = consulta ? `Consulta de ${consulta.paciente}` : `Atención: ${turno?.paciente}`;

  return (
    <Modal titulo={titulo} onCerrar={onCerrar} ancho={760}>
      <form onSubmit={guardar} className="grilla">
        <Campo etiqueta="Motivo de consulta *" ancho={3}>
          <input required value={f.motivoConsulta} onChange={set('motivoConsulta')} />
        </Campo>
        <Campo etiqueta="Peso (kg)">
          <input type="number" step="0.1" min="0" value={f.pesoKg} onChange={set('pesoKg')} />
        </Campo>
        <Campo etiqueta="Talla (cm)">
          <input type="number" step="0.1" min="0" value={f.tallaCm} onChange={set('tallaCm')} />
        </Campo>
        <Campo etiqueta="Presión arterial">
          <input placeholder="120/80" value={f.presionArterial} onChange={set('presionArterial')} maxLength={10} />
        </Campo>
        <Campo etiqueta="Temperatura (°C)">
          <input type="number" step="0.1" value={f.temperaturaC} onChange={set('temperaturaC')} />
        </Campo>
        <Campo etiqueta="Anamnesis" ancho={3}>
          <textarea rows={3} value={f.anamnesis} onChange={set('anamnesis')} />
        </Campo>
        <Campo etiqueta="Examen físico" ancho={3}>
          <textarea rows={3} value={f.examenFisico} onChange={set('examenFisico')} />
        </Campo>
        <Campo etiqueta="Diagnóstico" ancho={3}>
          <textarea rows={2} value={f.diagnostico} onChange={set('diagnostico')} />
        </Campo>
        <Campo etiqueta="Indicaciones / tratamiento" ancho={3}>
          <textarea rows={3} value={f.indicaciones} onChange={set('indicaciones')} />
        </Campo>
        <Campo etiqueta="Próximo control">
          <input type="date" value={f.proximoControl} onChange={set('proximoControl')} />
        </Campo>
        <div className="col-3">
          <Alerta error={error} />
        </div>
        <div className="pie col-3">
          <button type="button" className="btn" onClick={onCerrar}>
            Cancelar
          </button>
          <button className="btn primario" disabled={enviando}>
            {consulta ? 'Guardar cambios' : 'Registrar y marcar atendido'}
          </button>
        </div>
      </form>
    </Modal>
  );
}
