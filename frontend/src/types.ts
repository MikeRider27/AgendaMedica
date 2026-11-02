export type Rol = 'ADMIN' | 'RECEPCION' | 'MEDICO';

export type EstadoTurno = 'PENDIENTE' | 'CONFIRMADO' | 'EN_ATENCION' | 'ATENDIDO' | 'CANCELADO' | 'AUSENTE';

export type TipoTurno = 'PRIMERA_VEZ' | 'CONSULTA' | 'CONTROL' | 'PROCEDIMIENTO' | 'URGENCIA';

export interface Usuario {
  id: number;
  username: string;
  nombreCompleto: string;
  email?: string;
  rol: Rol;
  medicoId?: number;
  medico?: string;
  activo: boolean;
  ultimoAcceso?: string;
}

export interface Especialidad {
  id: number;
  nombre: string;
  duracionTurnoMin: number;
  activo: boolean;
}

export interface Aseguradora {
  id: number;
  nombre: string;
  activo: boolean;
}

export interface Consultorio {
  id: number;
  nombre: string;
  ubicacion?: string;
  activo: boolean;
}

export interface Paciente {
  id: number;
  tipoDocumento: 'CI' | 'PASAPORTE' | 'RUC' | 'OTRO';
  nroDocumento: string;
  nombres: string;
  apellidos: string;
  fechaNacimiento: string;
  edad: number;
  sexo: 'M' | 'F' | 'X';
  telefono?: string;
  email?: string;
  direccion?: string;
  ciudad?: string;
  aseguradoraId?: number;
  aseguradora?: string;
  nroAfiliado?: string;
  grupoSanguineo?: string;
  alergias?: string;
  observaciones?: string;
  activo: boolean;
}

export interface Medico {
  id: number;
  nroDocumento: string;
  nombres: string;
  apellidos: string;
  nombreCompleto: string;
  matricula: string;
  telefono?: string;
  email?: string;
  color: string;
  activo: boolean;
  especialidades: { id: number; nombre: string }[];
}

export interface Horario {
  id: number;
  especialidadId: number;
  especialidad: string;
  consultorioId?: number;
  consultorio?: string;
  diaSemana: number;
  horaInicio: string;
  horaFin: string;
  duracionTurnoMin: number;
  vigenteDesde: string;
  vigenteHasta?: string;
  activo: boolean;
}

export interface Bloqueo {
  id: number;
  inicio: string;
  fin: string;
  motivo?: string;
}

export interface Turno {
  id: number;
  inicio: string;
  fin: string;
  estado: EstadoTurno;
  tipo: TipoTurno;
  pacienteId: number;
  paciente: string;
  pacienteDocumento: string;
  pacienteTelefono?: string;
  medicoId: number;
  medico: string;
  medicoColor: string;
  especialidadId: number;
  especialidad: string;
  consultorioId?: number;
  consultorio?: string;
  motivo?: string;
  observaciones?: string;
  motivoCancelacion?: string;
}

export interface Hueco {
  inicio: string;
  fin: string;
  especialidadId: number;
  especialidad: string;
  consultorioId?: number;
  consultorio?: string;
  disponible: boolean;
  turnoId?: number;
}

export interface AgendaDia {
  fecha: string;
  medicoId: number;
  medico: string;
  huecos: Hueco[];
  turnos: Turno[];
  bloqueos: Bloqueo[];
}

export interface Consulta {
  id: number;
  turnoId?: number;
  pacienteId: number;
  paciente: string;
  medicoId: number;
  medico: string;
  especialidad: string;
  fecha: string;
  motivoConsulta: string;
  anamnesis?: string;
  examenFisico?: string;
  diagnostico?: string;
  indicaciones?: string;
  pesoKg?: number;
  tallaCm?: number;
  presionArterial?: string;
  temperaturaC?: number;
  proximoControl?: string;
}

export interface Pagina<T> {
  content: T[];
  page: { size: number; number: number; totalElements: number; totalPages: number };
}
