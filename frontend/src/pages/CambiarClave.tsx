import { useState, type FormEvent } from 'react';
import { api } from '../api';
import { Alerta, Campo, useEnvio } from '../components/comunes';

export default function CambiarClave() {
  const [actual, setActual] = useState('');
  const [nueva, setNueva] = useState('');
  const [repetir, setRepetir] = useState('');
  const [listo, setListo] = useState(false);
  const { enviando, error, setError, enviar } = useEnvio();

  const guardar = async (e: FormEvent) => {
    e.preventDefault();
    setListo(false);
    if (nueva !== repetir) {
      setError('Las contraseñas nuevas no coinciden');
      return;
    }
    if (await enviar(() => api.put('/auth/clave', { actual, nueva }))) {
      setListo(true);
      setActual('');
      setNueva('');
      setRepetir('');
    }
  };

  return (
    <section className="tarjeta angosta">
      <h1>Cambiar contraseña</h1>
      <form onSubmit={guardar}>
        <Campo etiqueta="Contraseña actual">
          <input type="password" value={actual} onChange={(e) => setActual(e.target.value)} required />
        </Campo>
        <Campo etiqueta="Nueva contraseña (mín. 8)">
          <input type="password" minLength={8} value={nueva} onChange={(e) => setNueva(e.target.value)} required />
        </Campo>
        <Campo etiqueta="Repetir nueva contraseña">
          <input type="password" value={repetir} onChange={(e) => setRepetir(e.target.value)} required />
        </Campo>
        <Alerta error={error} />
        {listo && <div className="ok">Contraseña actualizada.</div>}
        <button className="btn primario" disabled={enviando}>
          Guardar
        </button>
      </form>
    </section>
  );
}
