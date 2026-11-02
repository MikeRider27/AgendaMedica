INSERT INTO especialidades (nombre, duracion_turno_min) VALUES
    ('Clínica Médica', 20),
    ('Pediatría', 20),
    ('Ginecología', 30),
    ('Cardiología', 30),
    ('Odontología', 30),
    ('Oftalmología', 20),
    ('Otorrinolaringología', 20),
    ('Traumatología', 20),
    ('Dermatología', 20);

INSERT INTO aseguradoras (nombre) VALUES
    ('Particular'),
    ('IPS');

INSERT INTO consultorios (nombre, ubicacion) VALUES
    ('Consultorio 1', 'Planta baja'),
    ('Consultorio 2', 'Planta baja'),
    ('Consultorio 3', 'Primer piso');

-- El usuario administrador inicial lo crea la aplicación al arrancar
-- (variables ADMIN_USERNAME / ADMIN_PASSWORD) para no versionar contraseñas.
