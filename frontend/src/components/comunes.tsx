import { useCallback, useEffect, useState, type ReactNode } from 'react';
import { mensajeError } from '../api';
import type { EstadoTurno } from '../types';
import { ESTADOS } from '../util';

/** Carga datos asíncronos y permite recargarlos. */
export function useDatos<T>(cargar: () => Promise<T>, deps: unknown[]) {
  const [datos, setDatos] = useState<T | undefined>();
  const [error, setError] = useState<string>();
  const [cargando, setCargando] = useState(true);

  const fn = useCallback(cargar, deps);

  const recargar = useCallback(() => {
    setCargando(true);
    fn()
      .then((d) => {
        setDatos(d);
        setError(undefined);
      })
      .catch((e) => setError(mensajeError(e)))
      .finally(() => setCargando(false));
  }, [fn]);

  useEffect(recargar, [recargar]);

  return { datos, error, cargando, recargar };
}

export function Modal({
  titulo,
  onCerrar,
  children,
  ancho,
}: {
  titulo: string;
  onCerrar: () => void;
  children: ReactNode;
  ancho?: number;
}) {
  useEffect(() => {
    const esc = (e: KeyboardEvent) => e.key === 'Escape' && onCerrar();
    window.addEventListener('keydown', esc);
    return () => window.removeEventListener('keydown', esc);
  }, [onCerrar]);

  return (
    <div className="modal-fondo" onMouseDown={(e) => e.target === e.currentTarget && onCerrar()}>
      <div className="modal" style={ancho ? { maxWidth: ancho } : undefined} role="dialog" aria-label={titulo}>
        <div className="modal-cabecera">
          <h2>{titulo}</h2>
          <button className="btn-link" onClick={onCerrar} aria-label="Cerrar">
            ✕
          </button>
        </div>
        {children}
      </div>
    </div>
  );
}

export function Alerta({ error }: { error?: string }) {
  return error ? <div className="alerta">{error}</div> : null;
}

export function EstadoBadge({ estado }: { estado: EstadoTurno }) {
  return <span className={`badge estado-${estado}`}>{ESTADOS[estado]}</span>;
}

export function Campo({ etiqueta, children, ancho }: { etiqueta: string; children: ReactNode; ancho?: 2 | 3 }) {
  return (
    <label className={`campo${ancho ? ` col-${ancho}` : ''}`}>
      <span>{etiqueta}</span>
      {children}
    </label>
  );
}

/** Envuelve un submit asíncrono: maneja estado de envío y mensaje de error. */
export function useEnvio() {
  const [enviando, setEnviando] = useState(false);
  const [error, setError] = useState<string>();

  const enviar = useCallback(async (accion: () => Promise<unknown>) => {
    setEnviando(true);
    setError(undefined);
    try {
      await accion();
      return true;
    } catch (e) {
      setError(mensajeError(e));
      return false;
    } finally {
      setEnviando(false);
    }
  }, []);

  return { enviando, error, setError, enviar };
}
