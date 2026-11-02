import { useState, type FormEvent } from 'react';
import { useAuth } from '../auth';
import Escudo from '../components/Escudo';
import { Alerta, useEnvio } from '../components/comunes';
import { INSTITUCION, SISTEMA } from '../config';

export default function Login() {
  const { ingresar } = useAuth();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const { enviando, error, enviar } = useEnvio();

  const enviarForm = (e: FormEvent) => {
    e.preventDefault();
    enviar(() => ingresar(username, password));
  };

  return (
    <div className="login">
      <div className="login-marca">
        <Escudo tamano={72} />
        <h1>{INSTITUCION}</h1>
        <p>{SISTEMA}</p>
      </div>
      <div className="login-panel">
        <form onSubmit={enviarForm}>
          <h2>Acceso al sistema</h2>
          <p className="tenue">Ingrese sus credenciales institucionales.</p>
          <label className="campo">
            <span>Usuario</span>
            <input autoFocus autoComplete="username" value={username} onChange={(e) => setUsername(e.target.value)} />
          </label>
          <label className="campo">
            <span>Contraseña</span>
            <input
              type="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </label>
          <Alerta error={error} />
          <button className="btn primario ancho" disabled={enviando || !username || !password}>
            {enviando ? 'Ingresando…' : 'Ingresar'}
          </button>
          <p className="tenue pequeno aviso-legal">
            Uso exclusivo del personal autorizado. La información clínica es confidencial.
          </p>
        </form>
      </div>
    </div>
  );
}
