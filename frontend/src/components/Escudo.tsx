/** Emblema institucional: escudo con cruz médica. */
export default function Escudo({ tamano = 40 }: { tamano?: number }) {
  return (
    <svg width={tamano} height={tamano} viewBox="0 0 48 48" aria-hidden="true">
      <path d="M24 3 6 9v13c0 11.5 7.7 19.6 18 23 10.3-3.4 18-11.5 18-23V9L24 3Z" fill="#fff" />
      <path d="M24 6.2 9 11.2V22c0 9.8 6.4 16.8 15 19.9 8.6-3.1 15-10.1 15-19.9V11.2L24 6.2Z" fill="#1d4f8c" />
      <path d="M20.5 14h7v6.5H34v7h-6.5V34h-7v-6.5H14v-7h6.5V14Z" fill="#fff" />
    </svg>
  );
}
