import { useState, type FormEvent } from 'react';
import { api } from '../api';
import type { Aseguradora, Paciente } from '../types';
import { opt } from '../util';
import { Alerta, Campo, Modal, useDatos, useEnvio } from './comunes';

const GRUPOS = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];

type Form = {
  tipoDocumento: Paciente['tipoDocumento'];
  nroDocumento: string;
  nombres: string;
  apellidos: string;
  fechaNacimiento: string;
  sexo: Paciente['sexo'] | '';
  telefono: string;
  email: string;
  direccion: string;
  ciudad: string;
  aseguradoraId: string;
  nroAfiliado: string;
  grupoSanguineo: string;
  alergias: string;
  observaciones: string;
  activo: boolean;
};

function desde(p?: Paciente): Form {
  return {
    tipoDocumento: p?.tipoDocumento ?? 'CI',
    nroDocumento: p?.nroDocumento ?? '',
    nombres: p?.nombres ?? '',
    apellidos: p?.apellidos ?? '',
    fechaNacimiento: p?.fechaNacimiento ?? '',
    sexo: p?.sexo ?? '',
    telefono: p?.telefono ?? '',
    email: p?.email ?? '',
    direccion: p?.direccion ?? '',
    ciudad: p?.ciudad ?? '',
    aseguradoraId: p?.aseguradoraId ? String(p.aseguradoraId) : '',
    nroAfiliado: p?.nroAfiliado ?? '',
    grupoSanguineo: p?.grupoSanguineo ?? '',
    alergias: p?.alergias ?? '',
    observaciones: p?.observaciones ?? '',
    activo: p?.activo ?? true,
  };
}

export default function PacienteForm({
  paciente,
  onCerrar,
  onGuardado,
}: {
  paciente?: Paciente;
  onCerrar: () => void;
  onGuardado: (p: Paciente) => void;
}) {
  const [f, setF] = useState<Form>(() => desde(paciente));
  const { datos: aseguradoras } = useDatos(() => api.get<Aseguradora[]>('/aseguradoras'), []);
  const { enviando, error, enviar } = useEnvio();
  const set = (k: keyof Form) => (e: { target: { value: string } }) => setF({ ...f, [k]: e.target.value });

  const guardar = async (e: FormEvent) => {
    e.preventDefault();
    const cuerpo = {
      ...f,
      aseguradoraId: opt(f.aseguradoraId),
      email: opt(f.email),
      grupoSanguineo: opt(f.grupoSanguineo),
    };
    let guardado: Paciente | undefined;
    const ok = await enviar(async () => {
      guardado = paciente
        ? await api.put<Paciente>(`/pacientes/${paciente.id}`, cuerpo)
        : await api.post<Paciente>('/pacientes', cuerpo);
    });
    if (ok && guardado) onGuardado(guardado);
  };

  return (
    <Modal titulo={paciente ? 'Editar paciente' : 'Nuevo paciente'} onCerrar={onCerrar} ancho={760}>
      <form onSubmit={guardar} className="grilla">
        <Campo etiqueta="Tipo doc.">
          <select value={f.tipoDocumento} onChange={set('tipoDocumento')}>
            <option value="CI">CI</option>
            <option value="PASAPORTE">Pasaporte</option>
            <option value="RUC">RUC</option>
            <option value="OTRO">Otro</option>
          </select>
        </Campo>
        <Campo etiqueta="Nro. documento *">
          <input required value={f.nroDocumento} onChange={set('nroDocumento')} maxLength={20} />
        </Campo>
        <Campo etiqueta="Fecha de nacimiento *">
          <input required type="date" value={f.fechaNacimiento} onChange={set('fechaNacimiento')} />
        </Campo>
        <Campo etiqueta="Nombres *">
          <input required value={f.nombres} onChange={set('nombres')} maxLength={100} />
        </Campo>
        <Campo etiqueta="Apellidos *">
          <input required value={f.apellidos} onChange={set('apellidos')} maxLength={100} />
        </Campo>
        <Campo etiqueta="Sexo *">
          <select required value={f.sexo} onChange={set('sexo')}>
            <option value="">—</option>
            <option value="F">Femenino</option>
            <option value="M">Masculino</option>
            <option value="X">Otro / No especifica</option>
          </select>
        </Campo>
        <Campo etiqueta="Teléfono">
          <input value={f.telefono} onChange={set('telefono')} maxLength={30} />
        </Campo>
        <Campo etiqueta="Email">
          <input type="email" value={f.email} onChange={set('email')} maxLength={150} />
        </Campo>
        <Campo etiqueta="Ciudad">
          <input value={f.ciudad} onChange={set('ciudad')} maxLength={100} />
        </Campo>
        <Campo etiqueta="Dirección" ancho={3}>
          <input value={f.direccion} onChange={set('direccion')} maxLength={200} />
        </Campo>
        <Campo etiqueta="Seguro / aseguradora">
          <select value={f.aseguradoraId} onChange={set('aseguradoraId')}>
            <option value="">Sin seguro</option>
            {aseguradoras
              ?.filter((a) => a.activo || String(a.id) === f.aseguradoraId)
              .map((a) => (
                <option key={a.id} value={a.id}>
                  {a.nombre}
                </option>
              ))}
          </select>
        </Campo>
        <Campo etiqueta="Nro. afiliado">
          <input value={f.nroAfiliado} onChange={set('nroAfiliado')} maxLength={50} />
        </Campo>
        <Campo etiqueta="Grupo sanguíneo">
          <select value={f.grupoSanguineo} onChange={set('grupoSanguineo')}>
            <option value="">—</option>
            {GRUPOS.map((g) => (
              <option key={g}>{g}</option>
            ))}
          </select>
        </Campo>
        <Campo etiqueta="Alergias" ancho={3}>
          <textarea rows={2} value={f.alergias} onChange={set('alergias')} />
        </Campo>
        <Campo etiqueta="Observaciones" ancho={3}>
          <textarea rows={2} value={f.observaciones} onChange={set('observaciones')} />
        </Campo>
        {paciente && (
          <label className="check col-3">
            <input type="checkbox" checked={f.activo} onChange={(e) => setF({ ...f, activo: e.target.checked })} />
            Activo
          </label>
        )}
        <div className="col-3">
          <Alerta error={error} />
        </div>
        <div className="pie col-3">
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
