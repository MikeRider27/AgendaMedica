import type { EstadoTurno, TipoTurno } from './types';

export const DIAS = ['', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'];

export const ESTADOS: Record<EstadoTurno, string> = {
  PENDIENTE: 'Pendiente',
  CONFIRMADO: 'Confirmado',
  EN_ATENCION: 'En atención',
  ATENDIDO: 'Atendido',
  CANCELADO: 'Cancelado',
  AUSENTE: 'Ausente',
};

export const TIPOS: Record<TipoTurno, string> = {
  PRIMERA_VEZ: 'Primera vez',
  CONSULTA: 'Consulta',
  CONTROL: 'Control',
  PROCEDIMIENTO: 'Procedimiento',
  URGENCIA: 'Urgencia',
};

/** Transiciones permitidas (igual que EstadoTurno en el backend). */
export const SIGUIENTES: Record<EstadoTurno, EstadoTurno[]> = {
  PENDIENTE: ['CONFIRMADO', 'EN_ATENCION', 'CANCELADO', 'AUSENTE'],
  CONFIRMADO: ['PENDIENTE', 'EN_ATENCION', 'ATENDIDO', 'CANCELADO', 'AUSENTE'],
  EN_ATENCION: ['CONFIRMADO', 'ATENDIDO'],
  ATENDIDO: [],
  CANCELADO: [],
  AUSENTE: [],
};

const pad = (n: number) => String(n).padStart(2, '0');

/** Fecha local en formato ISO (yyyy-MM-dd), sin desfase de zona horaria. */
export function hoyISO(d = new Date()): string {
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
}

export function sumarDias(fechaISO: string, dias: number): string {
  const [a, m, d] = fechaISO.split('-').map(Number);
  return hoyISO(new Date(a, m - 1, d + dias));
}

/** "2026-11-02T08:20:00" -> "08:20" */
export const hora = (fechaHora: string) => fechaHora.slice(11, 16);

/** "2026-11-02" o "2026-11-02T08:20" -> "02/11/2026" */
export function fecha(f?: string): string {
  if (!f) return '';
  const [a, m, d] = f.slice(0, 10).split('-');
  return `${d}/${m}/${a}`;
}

export function diaSemanaLargo(fechaISO: string): string {
  const [a, m, d] = fechaISO.split('-').map(Number);
  return new Date(a, m - 1, d).toLocaleDateString('es', {
    weekday: 'long',
    day: 'numeric',
    month: 'long',
    year: 'numeric',
  });
}

/** Valor vacío de formulario -> undefined (para no enviar cadenas vacías al API). */
export const opt = <T,>(v: T | '' | null | undefined): T | undefined => (v === '' || v == null ? undefined : v);
