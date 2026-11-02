# Agenda Médica

Sistema de turnos para consultorio/clínica: pacientes, médicos con sus horarios de atención, agenda diaria, turnos e historia clínica.

| Capa      | Tecnología                                                   |
|-----------|--------------------------------------------------------------|
| Backend   | Java 21 · Spring Boot 3.5 · Spring Data JPA · Flyway · JWT   |
| Frontend  | React 18 · TypeScript · Vite, servido por nginx              |
| Base      | PostgreSQL 17 (sólo en red interna de Docker)                |

## Puesta en marcha

```bash
cp .env.example .env
# completar DB_PASSWORD, JWT_SECRET (openssl rand -base64 48) y ADMIN_PASSWORD
docker compose up -d --build
```

Abrir `http://localhost:8080` (o el `WEB_PORT` definido) e ingresar con `ADMIN_USERNAME` / `ADMIN_PASSWORD`.
El administrador se crea sólo la primera vez, cuando la tabla de usuarios está vacía.

El nombre que aparece en el menú, el login y el pie de página se define con `INSTITUCION` en `.env`
(requiere `docker compose up -d --build frontend` para aplicarse).

### Datos de demostración

Para probar con datos ficticios (6 médicos con horarios, 60 pacientes, ~440 turnos entre dos semanas atrás y dos
adelante, historia clínica de los atendidos), sobre una base sin médicos ni pacientes:

```bash
docker compose exec -T db psql -U agenda -d agenda_medica -v ON_ERROR_STOP=1 < scripts/datos-demo.sql
```

Crea los usuarios `recepcion`, `agomez`, `rvillalba`, `cmartinez`, `jbenitez`, `portiz` y `dacosta`
(contraseña `Demo2026!`). Para volver a una base vacía: `docker compose down -v && docker compose up -d`.

### Red

```
navegador ──► frontend (nginx :80, publicado en WEB_PORT)
                 │  /api/*
                 ▼            red "interna" (internal: true)
              backend :8080 ───────► db :5432
```

- `db` y `backend` no publican puertos ni tienen salida a internet.
- Las subredes son explícitas (`SUBNET_INTERNA`, `SUBNET_PUBLICA`) para no depender de los pools por defecto de Docker.

Para consultar la base: `docker compose exec db psql -U agenda -d agenda_medica`.

## Primeros pasos en la aplicación

1. **Catálogos**: revisar especialidades, aseguradoras y consultorios (vienen precargados).
2. **Médicos**: alta con sus especialidades, luego **Horarios** → cargar los bloques semanales
   (p. ej. martes 08:00–12:00, turnos de 20 min, Consultorio 1).
3. **Usuarios**: crear usuarios de recepción y de médicos (vinculados a su ficha de médico).
4. **Pacientes**: alta de pacientes.
5. **Agenda**: elegir médico y día → los huecos libres salen de los horarios; clic en *Agendar*.

## Roles

| Acción                                    | ADMIN | RECEPCION | MEDICO          |
|-------------------------------------------|:-----:|:---------:|:---------------:|
| Catálogos, médicos, horarios, usuarios    |  ✔    |           |                 |
| Pacientes (alta/edición)                  |  ✔    |    ✔      | sólo lectura    |
| Dar, reprogramar, cancelar turnos         |  ✔    |    ✔      |                 |
| Bloqueos de agenda                        |  ✔    |    ✔      |                 |
| Ver agenda / turnos                       |  ✔    |    ✔      | sólo la propia  |
| Historia clínica (consultas)              |  ✔    |           | ✔ (edita las propias) |

## Modelo de datos

Esquema en [backend/src/main/resources/db/migration](backend/src/main/resources/db/migration) (Flyway).

- `pacientes`: documento único por tipo, datos de contacto, seguro, grupo sanguíneo, alergias.
- `medicos` ↔ `especialidades` (`medico_especialidades`).
- `horarios_atencion`: plantilla semanal por médico y especialidad, con duración de turno y vigencia.
- `bloqueos_agenda`: vacaciones/ausencias.
- `turnos`: estado (`PENDIENTE → CONFIRMADO → EN_ATENCION → ATENDIDO`, o `CANCELADO` / `AUSENTE`), tipo y motivo.
  Restricciones `EXCLUDE` (btree_gist) impiden **a nivel base de datos** turnos superpuestos para el mismo médico o paciente.
- `consultas`: registro de la atención (motivo, anamnesis, examen, diagnóstico, indicaciones, signos vitales).
- `usuarios`: rol `ADMIN` / `RECEPCION` / `MEDICO`; contraseñas con BCrypt.

## API

Todas las rutas bajo `/api`, autenticadas con `Authorization: Bearer <token>` salvo `POST /api/auth/login`.

| Recurso | Rutas principales |
|---|---|
| Autenticación | `POST /auth/login`, `GET /auth/yo`, `PUT /auth/clave` |
| Pacientes | `GET /pacientes?q=`, `GET/POST/PUT /pacientes/{id}` |
| Médicos | `GET /medicos?especialidadId=`, `POST/PUT /medicos/{id}` |
| Horarios | `GET/POST /medicos/{id}/horarios`, `PUT/DELETE /medicos/{id}/horarios/{hid}` |
| Bloqueos | `GET/POST /medicos/{id}/bloqueos`, `DELETE /medicos/{id}/bloqueos/{bid}` |
| Agenda | `GET /agenda/dia?medicoId=&fecha=&especialidadId=` |
| Turnos | `GET /turnos?desde=&hasta=&medicoId=&estado=`, `POST /turnos`, `PUT /turnos/{id}/reprogramar`, `PATCH /turnos/{id}/estado` |
| Consultas | `GET /consultas?pacienteId=`, `POST /consultas/turno/{turnoId}`, `PUT /consultas/{id}` |
| Catálogos | `/especialidades`, `/aseguradoras`, `/consultorios` |
| Tablero | `GET /tablero` |

Los errores siguen RFC 7807 (`detail` con el mensaje y `campos` en errores de validación).

## Desarrollo

```bash
# backend (requiere un PostgreSQL accesible; ver variables DB_* en application.yml)
cd backend && mvn spring-boot:run

# frontend (redirige /api a localhost:8080)
cd frontend && npm install && npm run dev
```

Tests del backend: `cd backend && mvn test`.

## Sistema anterior

El sistema original (Servlets/JSP + jQuery, NetBeans) y su respaldo de base de datos están en [legacy/](legacy/) sólo como referencia.
Sus datos **no** se migran automáticamente al nuevo esquema.
