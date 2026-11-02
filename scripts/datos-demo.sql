-- =====================================================================
-- Datos FICTICIOS de demostración. NO usar en producción.
--
--   docker compose exec -T db psql -U agenda -d agenda_medica -v ON_ERROR_STOP=1 < scripts/datos-demo.sql
--
-- Las fechas son relativas al día en que se ejecuta: turnos desde dos semanas
-- atrás (atendidos, ausentes, cancelados) hasta dos semanas adelante.
-- Usuarios creados (contraseña "Demo2026!"): recepcion, y un usuario por médico
-- (ver tabla al final).
-- =====================================================================
BEGIN;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM medicos) OR EXISTS (SELECT 1 FROM pacientes) THEN
        RAISE EXCEPTION 'La base ya tiene médicos o pacientes: los datos demo sólo se cargan sobre una base vacía';
    END IF;
END $$;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ---------------------------------------------------------------------
-- Médicos y especialidades
-- ---------------------------------------------------------------------
INSERT INTO medicos (nro_documento, nombres, apellidos, matricula, telefono, email, color) VALUES
    ('2345678', 'Ana María',  'Gómez Duarte',    'MP-1021', '0981 410 221', 'agomez@demo.local',    '#1d4f8c'),
    ('3456789', 'Roberto',    'Villalba Insfrán','MP-1188', '0982 330 118', 'rvillalba@demo.local', '#23663a'),
    ('1987654', 'Claudia',    'Martínez Ayala',  'MP-0874', '0971 220 874', 'cmartinez@demo.local', '#8f1d6b'),
    ('4123987', 'Jorge Luis', 'Benítez Sosa',    'MP-1342', '0985 118 342', 'jbenitez@demo.local',  '#b06a10'),
    ('2876543', 'Patricia',   'Ortiz Cáceres',   'MP-0956', '0991 776 956', 'portiz@demo.local',    '#0f7a7a'),
    ('3765432', 'Diego',      'Acosta Riveros',  'MP-1417', '0976 404 417', 'dacosta@demo.local',   '#5a3e9c');

INSERT INTO medico_especialidades (medico_id, especialidad_id)
SELECT m.id, e.id
FROM (VALUES
        ('MP-1021', 'Clínica Médica'), ('MP-1021', 'Cardiología'),
        ('MP-1188', 'Pediatría'),
        ('MP-0874', 'Ginecología'),
        ('MP-1342', 'Traumatología'),
        ('MP-0956', 'Dermatología'), ('MP-0956', 'Clínica Médica'),
        ('MP-1417', 'Oftalmología')
     ) AS x (matricula, especialidad)
JOIN medicos m ON m.matricula = x.matricula
JOIN especialidades e ON e.nombre = x.especialidad;

-- Horarios semanales (dia_semana ISO: 1 = lunes)
INSERT INTO horarios_atencion (medico_id, especialidad_id, consultorio_id, dia_semana,
                               hora_inicio, hora_fin, duracion_turno_min, vigente_desde)
SELECT m.id, e.id, c.id, x.dia, x.desde::time, x.hasta::time, x.dur, CURRENT_DATE - 30
FROM (VALUES
        ('MP-1021', 'Clínica Médica', 'Consultorio 1', 1, '07:30', '11:30', 20),
        ('MP-1021', 'Clínica Médica', 'Consultorio 1', 3, '07:30', '11:30', 20),
        ('MP-1021', 'Cardiología',    'Consultorio 1', 5, '08:00', '12:00', 30),
        ('MP-1188', 'Pediatría',      'Consultorio 2', 1, '14:00', '18:00', 20),
        ('MP-1188', 'Pediatría',      'Consultorio 2', 2, '14:00', '18:00', 20),
        ('MP-1188', 'Pediatría',      'Consultorio 2', 4, '14:00', '18:00', 20),
        ('MP-0874', 'Ginecología',    'Consultorio 3', 2, '08:00', '12:00', 30),
        ('MP-0874', 'Ginecología',    'Consultorio 3', 4, '08:00', '12:00', 30),
        ('MP-1342', 'Traumatología',  'Consultorio 2', 1, '08:00', '12:00', 20),
        ('MP-1342', 'Traumatología',  'Consultorio 2', 3, '14:00', '18:00', 20),
        ('MP-1342', 'Traumatología',  'Consultorio 1', 6, '08:00', '11:00', 20),
        ('MP-0956', 'Dermatología',   'Consultorio 3', 3, '08:00', '12:00', 20),
        ('MP-0956', 'Clínica Médica', 'Consultorio 3', 5, '14:00', '18:00', 20),
        ('MP-1417', 'Oftalmología',   'Consultorio 1', 2, '14:00', '18:00', 20),
        ('MP-1417', 'Oftalmología',   'Consultorio 1', 4, '14:00', '18:00', 20)
     ) AS x (matricula, especialidad, consultorio, dia, desde, hasta, dur)
JOIN medicos m ON m.matricula = x.matricula
JOIN especialidades e ON e.nombre = x.especialidad
JOIN consultorios c ON c.nombre = x.consultorio;

-- Congreso del traumatólogo la semana próxima (sin turnos en ese período)
INSERT INTO bloqueos_agenda (medico_id, inicio, fin, motivo)
SELECT id, (CURRENT_DATE + 8)::timestamp, (CURRENT_DATE + 11)::timestamp, 'Congreso de Traumatología'
FROM medicos WHERE matricula = 'MP-1342';

-- ---------------------------------------------------------------------
-- Pacientes (60, combinando listas de nombres ficticios)
-- ---------------------------------------------------------------------
WITH nombres_f AS (SELECT ARRAY['María José','Lucía','Ana Belén','Sofía','Camila','Valeria','Rosa','Gabriela',
                                'Natalia','Fernanda','Liz','Mirta','Carmen','Andrea','Florencia'] AS v),
     nombres_m AS (SELECT ARRAY['Juan Carlos','Carlos','José Luis','Miguel','Pedro','Luis','Diego','Andrés',
                                'Fernando','Ramón','Hugo','Óscar','Matías','Rodrigo','Sebastián'] AS v),
     apellidos AS (SELECT ARRAY['Pérez','González','Rodríguez','López','Martínez','Benítez','Giménez','Ramírez',
                                'Fernández','Cabrera','Duarte','Rojas','Báez','Vera','Ayala','Núñez','Franco',
                                'Ruiz Díaz','Ortiz','Cáceres','Insfrán','Ojeda','Galeano','Amarilla'] AS v),
     ciudades AS (SELECT ARRAY['Asunción','San Lorenzo','Luque','Fernando de la Mora','Lambaré',
                               'Capiatá','Mariano Roque Alonso','Limpio','Ñemby','Villa Elisa'] AS v)
INSERT INTO pacientes (tipo_documento, nro_documento, nombres, apellidos, fecha_nacimiento, sexo, telefono,
                       email, direccion, ciudad, aseguradora_id, nro_afiliado, grupo_sanguineo, alergias)
SELECT 'CI',
       (1500000 + i * 73541)::text,
       CASE WHEN i % 2 = 0 THEN f.v[1 + i % 15] ELSE m.v[1 + i % 15] END,
       a.v[1 + (i * 7) % 24] || ' ' || a.v[1 + (i * 11 + 5) % 24],
       CURRENT_DATE - ((2 + (i * 397) % 80) * 365 + (i * 53) % 365),
       CASE WHEN i % 2 = 0 THEN 'F' ELSE 'M' END,
       '09' || (71 + i % 25) || ' ' || lpad(((i * 4567) % 1000000)::text, 6, '0'),
       CASE WHEN i % 3 = 0 THEN 'paciente' || i || '@correo.demo' END,
       'Calle ' || (100 + i * 13) || ' c/ Avda. ' || a.v[1 + i % 24],
       c.v[1 + i % 10],
       CASE WHEN i % 3 = 0 THEN NULL
            ELSE (SELECT id FROM aseguradoras WHERE nombre = CASE WHEN i % 3 = 1 THEN 'IPS' ELSE 'Particular' END) END,
       CASE WHEN i % 3 = 1 THEN 'AF-' || (200000 + i * 91) END,
       (ARRAY['O+','O+','A+','B+','O-','A-','AB+'])[1 + i % 7],
       CASE WHEN i % 9 = 0 THEN 'Penicilina' WHEN i % 13 = 0 THEN 'AINEs' END
FROM generate_series(1, 60) AS i, nombres_f f, nombres_m m, apellidos a, ciudades c;

-- ---------------------------------------------------------------------
-- Turnos: se ocupa ~65 % de los huecos entre -14 y +14 días
-- ---------------------------------------------------------------------
WITH pac AS (SELECT id, row_number() OVER (ORDER BY id) - 1 AS k, count(*) OVER () AS total FROM pacientes),
     huecos AS (
        SELECT h.medico_id, h.especialidad_id, h.consultorio_id, s AS inicio,
               s + make_interval(mins => h.duracion_turno_min) AS fin,
               row_number() OVER (ORDER BY s, h.medico_id) AS n
        FROM horarios_atencion h
        JOIN generate_series(CURRENT_DATE - 14, CURRENT_DATE + 14, interval '1 day') AS d
             ON extract(isodow FROM d) = h.dia_semana
        CROSS JOIN LATERAL generate_series(d::date + h.hora_inicio,
                                           d::date + h.hora_fin - make_interval(mins => h.duracion_turno_min),
                                           make_interval(mins => h.duracion_turno_min)) AS s
        WHERE NOT EXISTS (SELECT 1 FROM bloqueos_agenda b
                          WHERE b.medico_id = h.medico_id AND b.inicio < s + make_interval(mins => h.duracion_turno_min)
                            AND b.fin > s)
     )
INSERT INTO turnos (paciente_id, medico_id, especialidad_id, consultorio_id, inicio, fin, estado, tipo, motivo,
                    motivo_cancelacion, creado_por, creado_en)
SELECT p.id, x.medico_id, x.especialidad_id, x.consultorio_id, x.inicio, x.fin,
       CASE
           WHEN x.inicio < localtimestamp THEN
               CASE WHEN x.n % 10 = 0 THEN 'AUSENTE' WHEN x.n % 10 = 3 THEN 'CANCELADO' ELSE 'ATENDIDO' END
           WHEN x.n % 12 = 0 THEN 'CANCELADO'
           WHEN x.inicio < CURRENT_DATE + 2 AND x.n % 3 <> 0 THEN 'CONFIRMADO'
           ELSE 'PENDIENTE'
       END,
       (ARRAY['CONSULTA','CONSULTA','CONTROL','PRIMERA_VEZ','CONSULTA','CONTROL','PROCEDIMIENTO'])[1 + x.n % 7],
       (ARRAY['Control de rutina','Dolor de cabeza persistente','Seguimiento de tratamiento','Fiebre y malestar',
              'Revisión de estudios','Dolor lumbar','Chequeo anual','Tos de varios días','Renovación de receta',
              'Consulta por resultados de laboratorio'])[1 + x.n % 10],
       CASE WHEN x.n % 10 = 3 OR (x.inicio >= localtimestamp AND x.n % 12 = 0)
            THEN (ARRAY['El paciente no puede asistir','Reprogramará por teléfono','Viaje imprevisto'])[1 + x.n % 3] END,
       (SELECT id FROM usuarios WHERE rol = 'ADMIN' ORDER BY id LIMIT 1),
       x.inicio - interval '5 days'
FROM huecos x
JOIN pac p ON p.k = (x.n * 17) % p.total
WHERE (x.n * 37) % 100 < 65
ON CONFLICT DO NOTHING;  -- descarta los que chocarían con otro turno del mismo paciente

-- ---------------------------------------------------------------------
-- Consultas (historia clínica) para los turnos atendidos
-- ---------------------------------------------------------------------
INSERT INTO consultas (turno_id, paciente_id, medico_id, especialidad_id, fecha, motivo_consulta, anamnesis,
                       examen_fisico, diagnostico, indicaciones, peso_kg, talla_cm, presion_arterial,
                       temperatura_c, proximo_control)
SELECT t.id, t.paciente_id, t.medico_id, t.especialidad_id, t.inicio + interval '5 minutes', t.motivo,
       (ARRAY['Refiere síntomas de 3 días de evolución, sin antecedentes relevantes.',
              'Paciente en seguimiento, refiere mejoría parcial con el tratamiento indicado.',
              'Consulta por control periódico. Sin quejas actuales.',
              'Refiere dolor de intensidad moderada que empeora con el esfuerzo.'])[1 + t.id % 4],
       (ARRAY['Lúcido, orientado, hidratado. Sin particularidades.',
              'Buen estado general. Auscultación normal.',
              'Dolor a la palpación localizada, sin signos de alarma.'])[1 + t.id % 3],
       (ARRAY['Cefalea tensional','Infección respiratoria alta','Hipertensión arterial controlada',
              'Lumbalgia mecánica','Control de salud sin hallazgos','Dermatitis de contacto',
              'Gastritis','Rinitis alérgica'])[1 + t.id % 8],
       (ARRAY['Paracetamol 500 mg cada 8 h por 3 días. Reposo relativo.',
              'Continuar con la medicación habitual. Dieta hiposódica.',
              'Hidratación abundante. Control si persiste la fiebre.',
              'Analgésicos según necesidad. Fisioterapia 10 sesiones.'])[1 + t.id % 4],
       55 + (t.id * 7) % 40, 150 + (t.id * 3) % 40,
       (110 + (t.id % 5) * 5) || '/' || (70 + (t.id % 4) * 5),
       36.2 + (t.id % 8) / 10.0,
       CASE WHEN t.id % 3 = 0 THEN t.inicio::date + 30 END
FROM turnos t
WHERE t.estado = 'ATENDIDO';

-- ---------------------------------------------------------------------
-- Usuarios demo (contraseña: Demo2026!)
-- ---------------------------------------------------------------------
INSERT INTO usuarios (username, password_hash, nombre_completo, email, rol)
VALUES ('recepcion', crypt('Demo2026!', gen_salt('bf', 10)), 'Laura Recepción', 'recepcion@demo.local', 'RECEPCION');

INSERT INTO usuarios (username, password_hash, nombre_completo, email, rol, medico_id)
SELECT split_part(email, '@', 1), crypt('Demo2026!', gen_salt('bf', 10)),
       nombres || ' ' || apellidos, email, 'MEDICO', id
FROM medicos;

COMMIT;

SELECT u.username, u.rol, coalesce(m.nombres || ' ' || m.apellidos, '') AS medico
FROM usuarios u LEFT JOIN medicos m ON m.id = u.medico_id
ORDER BY u.rol, u.username;

SELECT estado, count(*) FROM turnos GROUP BY estado ORDER BY estado;
