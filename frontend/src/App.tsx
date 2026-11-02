import { Navigate, NavLink, Route, Routes } from 'react-router-dom';
import { useAuth } from './auth';
import Escudo from './components/Escudo';
import { INSTITUCION, SISTEMA } from './config';
import type { Rol } from './types';
import { diaSemanaLargo, hoyISO } from './util';
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

const MENU: { seccion: string; items: { a: string; texto: string; roles?: Rol[] }[] }[] = [
  {
    seccion: 'Atención',
    items: [
      { a: '/', texto: 'Inicio' },
      { a: '/agenda', texto: 'Agenda' },
      { a: '/turnos', texto: 'Turnos' },
      { a: '/pacientes', texto: 'Pacientes' },
    ],
  },
  {
    seccion: 'Administración',
    items: [
      { a: '/medicos', texto: 'Médicos y horarios', roles: ['ADMIN', 'RECEPCION'] },
      { a: '/catalogos', texto: 'Catálogos', roles: ['ADMIN'] },
      { a: '/usuarios', texto: 'Usuarios', roles: ['ADMIN'] },
    ],
  },
];

const NOMBRE_ROL: Record<Rol, string> = { ADMIN: 'Administrador', RECEPCION: 'Recepción', MEDICO: 'Médico' };

export default function App() {
  const { usuario, cargando, salir, tiene } = useAuth();

  if (cargando) return <div className="centrado">Cargando…</div>;
  if (!usuario) return <Login />;

  return (
    <div className="app">
      <aside className="lateral">
        <div className="identidad">
          <Escudo />
          <div>
            <div className="institucion">{INSTITUCION}</div>
            <div className="sistema">Agenda Médica</div>
          </div>
        </div>
        <nav>
          {MENU.map((s) => {
            const visibles = s.items.filter((m) => !m.roles || tiene(...m.roles));
            if (visibles.length === 0) return null;
            return (
              <div key={s.seccion} className="seccion">
                <div className="seccion-titulo">{s.seccion}</div>
                {visibles.map((m) => (
                  <NavLink key={m.a} to={m.a} end={m.a === '/'}>
                    {m.texto}
                  </NavLink>
                ))}
              </div>
            );
          })}
        </nav>
        <div className="sesion">
          <div className="sesion-nombre">{usuario.nombreCompleto}</div>
          <div className="sesion-rol">{NOMBRE_ROL[usuario.rol]}</div>
          <div className="sesion-acciones">
            <NavLink to="/clave">Cambiar contraseña</NavLink>
            <button className="btn-link" onClick={salir}>
              Cerrar sesión
            </button>
          </div>
        </div>
      </aside>

      <div className="cuerpo">
        <header className="encabezado">
          <span>{SISTEMA}</span>
          <span className="fecha-hoy">{diaSemanaLargo(hoyISO())}</span>
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
        <footer className="pie-pagina">
          © {new Date().getFullYear()} {INSTITUCION} · {SISTEMA}
        </footer>
      </div>
    </div>
  );
}
