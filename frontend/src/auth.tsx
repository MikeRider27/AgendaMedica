import { createContext, useCallback, useContext, useEffect, useState, type ReactNode } from 'react';
import { api, EVENTO_SESION_VENCIDA, sesion } from './api';
import type { Rol, Usuario } from './types';

interface Auth {
  usuario: Usuario | null;
  cargando: boolean;
  ingresar: (username: string, password: string) => Promise<void>;
  salir: () => void;
  tiene: (...roles: Rol[]) => boolean;
}

const AuthContext = createContext<Auth | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [usuario, setUsuario] = useState<Usuario | null>(null);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    if (!sesion.token()) {
      setCargando(false);
      return;
    }
    api
      .get<Usuario>('/auth/yo')
      .then(setUsuario)
      .catch(() => sesion.cerrar())
      .finally(() => setCargando(false));
  }, []);

  useEffect(() => {
    const vencida = () => setUsuario(null);
    window.addEventListener(EVENTO_SESION_VENCIDA, vencida);
    return () => window.removeEventListener(EVENTO_SESION_VENCIDA, vencida);
  }, []);

  const ingresar = useCallback(async (username: string, password: string) => {
    const r = await api.post<{ token: string; usuario: Usuario }>('/auth/login', { username, password });
    sesion.guardar(r.token);
    setUsuario(r.usuario);
  }, []);

  const salir = useCallback(() => {
    sesion.cerrar();
    setUsuario(null);
  }, []);

  const tiene = useCallback((...roles: Rol[]) => !!usuario && roles.includes(usuario.rol), [usuario]);

  return (
    <AuthContext.Provider value={{ usuario, cargando, ingresar, salir, tiene }}>{children}</AuthContext.Provider>
  );
}

export function useAuth(): Auth {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth fuera de AuthProvider');
  return ctx;
}
