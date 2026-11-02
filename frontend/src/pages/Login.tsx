import { useState, type FormEvent } from 'react';
import { useAuth } from '../auth';
import { Alerta, useEnvio } from '../components/comunes';

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
      <form onSubmit={enviarForm} className="tarjeta">
        <h1>Agenda Médica</h1>
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
      </form>
    </div>
  );
}
