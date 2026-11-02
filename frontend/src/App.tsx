import { Navigate, NavLink, Route, Routes } from 'react-router-dom';
import { useAuth } from './auth';
import type { Rol } from './types';
import Login from './pages/Login';
import Inicio from './pages/Inicio';
import Agenda from './pages/Agenda';
import Turnos from './pages/Turnos';
import Pacientes from './pages/Pacientes';
import PacienteDetalle from './pages/PacienteDetalle';
import Medicos from './pages/Medicos';
import MedicoAgenda from './pages/MedicoAgenda';
import Catalogos from './pages/Catalogos';
import Usuarios from './pages/Usuarios';
import CambiarClave from './pages/CambiarClave';

const MENU: { a: string; texto: string; roles?: Rol[] }[] = [
  { a: '/', texto: 'Inicio' },
  { a: '/agenda', texto: 'Agenda' },
  { a: '/turnos', texto: 'Turnos' },
  { a: '/pacientes', texto: 'Pacientes' },
  { a: '/medicos', texto: 'Médicos', roles: ['ADMIN', 'RECEPCION'] },
  { a: '/catalogos', texto: 'Catálogos', roles: ['ADMIN'] },
  { a: '/usuarios', texto: 'Usuarios', roles: ['ADMIN'] },
];

const NOMBRE_ROL: Record<Rol, string> = { ADMIN: 'Administrador', RECEPCION: 'Recepción', MEDICO: 'Médico' };

export default function App() {
  const { usuario, cargando, salir, tiene } = useAuth();

  if (cargando) return <div className="centrado">Cargando…</div>;
  if (!usuario) return <Login />;

  return (
    <div className="app">
      <header className="barra">
        <span className="marca">Agenda Médica</span>
        <nav>
          {MENU.filter((m) => !m.roles || tiene(...m.roles)).map((m) => (
            <NavLink key={m.a} to={m.a} end={m.a === '/'}>
              {m.texto}
            </NavLink>
          ))}
        </nav>
        <div className="usuario">
          <NavLink to="/clave" title="Cambiar contraseña">
            {usuario.nombreCompleto} · {NOMBRE_ROL[usuario.rol]}
          </NavLink>
          <button className="btn-link" onClick={salir}>
            Salir
          </button>
        </div>
      </header>
      <main>
        <Routes>
          <Route path="/" element={<Inicio />} />
          <Route path="/agenda" element={<Agenda />} />
          <Route path="/turnos" element={<Turnos />} />
          <Route path="/pacientes" element={<Pacientes />} />
          <Route path="/pacientes/:id" element={<PacienteDetalle />} />
          {tiene('ADMIN', 'RECEPCION') && <Route path="/medicos" element={<Medicos />} />}
          {tiene('ADMIN', 'RECEPCION') && <Route path="/medicos/:id" element={<MedicoAgenda />} />}
          {tiene('ADMIN') && <Route path="/catalogos" element={<Catalogos />} />}
          {tiene('ADMIN') && <Route path="/usuarios" element={<Usuarios />} />}
          <Route path="/clave" element={<CambiarClave />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
    </div>
  );
}
