const CLAVE_TOKEN = 'agenda.token';

export class ApiError extends Error {
  constructor(
    public status: number,
    message: string,
    public campos?: Record<string, string>,
  ) {
    super(message);
  }
}

export const sesion = {
  token: () => localStorage.getItem(CLAVE_TOKEN),
  guardar: (token: string) => localStorage.setItem(CLAVE_TOKEN, token),
  cerrar: () => localStorage.removeItem(CLAVE_TOKEN),
};

/** Se dispara cuando el servidor rechaza el token (vencido o inválido). */
export const EVENTO_SESION_VENCIDA = 'agenda:sesion-vencida';

async function pedir<T>(metodo: string, ruta: string, cuerpo?: unknown): Promise<T> {
  const headers: Record<string, string> = { Accept: 'application/json' };
  const token = sesion.token();
  if (token) headers.Authorization = `Bearer ${token}`;
  if (cuerpo !== undefined) headers['Content-Type'] = 'application/json';

  const r = await fetch(`/api${ruta}`, {
    method: metodo,
    headers,
    body: cuerpo === undefined ? undefined : JSON.stringify(cuerpo),
  });

  if (r.status === 401 && token) {
    sesion.cerrar();
    window.dispatchEvent(new Event(EVENTO_SESION_VENCIDA));
  }
  if (!r.ok) {
    let mensaje = `Error ${r.status}`;
    let campos: Record<string, string> | undefined;
    try {
      const p = await r.json();
      mensaje = p.detail || p.title || mensaje;
      campos = p.campos;
    } catch {
      /* respuesta sin cuerpo JSON */
    }
    if (r.status === 403) mensaje = 'No tiene permisos para esta operación';
    throw new ApiError(r.status, mensaje, campos);
  }
  if (r.status === 204) return undefined as T;
  return r.json() as Promise<T>;
}

export const api = {
  get: <T>(ruta: string) => pedir<T>('GET', ruta),
  post: <T>(ruta: string, cuerpo?: unknown) => pedir<T>('POST', ruta, cuerpo ?? {}),
  put: <T>(ruta: string, cuerpo?: unknown) => pedir<T>('PUT', ruta, cuerpo ?? {}),
  patch: <T>(ruta: string, cuerpo?: unknown) => pedir<T>('PATCH', ruta, cuerpo ?? {}),
  del: (ruta: string) => pedir<void>('DELETE', ruta),
};

/** Mensaje legible para mostrar en pantalla, incluyendo errores por campo. */
export function mensajeError(e: unknown): string {
  if (e instanceof ApiError) {
    if (e.campos && Object.keys(e.campos).length) {
      return `${e.message}: ${Object.entries(e.campos)
        .map(([c, m]) => `${c} ${m}`)
        .join('; ')}`;
    }
    return e.message;
  }
  return e instanceof Error ? e.message : String(e);
}

export function qs(params: Record<string, string | number | undefined | null>): string {
  const p = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== null && v !== '') p.set(k, String(v));
  });
  const s = p.toString();
  return s ? `?${s}` : '';
}
