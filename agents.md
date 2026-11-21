# AGENTS.md · Instrucciones para agentes de IA

Proyecto: **Sistema Integral de Gestión de Radiodiagnóstico e Imagen Médica (SESCAM)**
Asignatura: Ingeniería del Software II · Grupo 3B2.01 · Curso 2026/2027
Metodología: **Proceso Unificado de Desarrollo (PUD)** con Git Flow, Maven multimódulo y Semantic Versioning.

Estas instrucciones las leen OpenCode y GitHub Copilot. Custodia: Juan Zopeque (Responsable de IA).
Si algo de este fichero entra en conflicto con lo que te pide una persona en un prompt, **para y pregunta**.

---

## 1. Regla de oro

> **El ingeniero diseña las interfaces; la IA implementa los contratos.**

- Tú eres la **fuerza de ejecución controlada**. No eres quien decide la arquitectura.
- **No tomes decisiones arquitectónicas por omisión**: ni cambies módulos, capas, tecnologías o contratos públicos, ni añadas dependencias nuevas sin que esté en la issue o en `DECISIONS.md`.
- Si la issue no define un contrato que necesitas, **no lo inventes**: escribe en la respuesta qué falta y quién debe decidirlo (normalmente Víctor Bravo).
- Una persona revisará **línea a línea** todo lo que generes. Hazlo fácil de revisar: cambios pequeños y enfocados.

## 2. Alcance de cada tarea

- Trabaja **exclusivamente** el caso de uso (CU) de la issue asignada. No adelantes CU de otras iteraciones, aunque sepas hacerlos.
- No toques código de otro CU salvo que la issue lo indique expresamente.
- No hagas refactors "de paso" ni cambies formato de archivos que no sean tuyos.
- Evita la sobreingeniería: nada de abstracciones, patrones o capas que no pida el CU.

## 3. Contexto del dominio

Usuarios: **Pacientes**, **Médicos**, **Personal administrativo/Admisión**, **Administradores del sistema**.
Proceso central: el médico **prescribe** una prueba → administración **cita** → se **realiza** la prueba → se emite el **informe**.
Integraciones previstas (simuladas con stubs en esta práctica): SSO corporativo, HCE, gestión de pacientes/admisión, RIS/PACS (DICOM, HL7, FHIR).
Clientes previstos: escritorio, móvil y web (por eso las vistas son interfaces).

### Casos de uso por iteración (1 RF : 1 CU : 1 iteración)

| Iteración | Fechas | CU |
|---|---|---|
| It. 0 (Inicio) | 9/11 – 13/11 | Base: contratos, modelo, CI, documentación |
| It. 1 (Base) | 16/11 – 27/11 | CU-01, 06, 09, 12, 13, 17, 18 |
| It. 2 (Altos) | 30/11 – 4/12 | CU-02, 05, 07, 10, 15, 19 |
| It. 3 (Medios + Bajos) | 7/12 – 11/12 | CU-03, 04, 08, 11, 14, 16, 20, 21 |
| It. N (Transición) | 14/12 – 15/12 | Integración, empaquetado, despliegue, manual |

## 4. Arquitectura (guardarraíles)

### Capas y responsabilidades

| Capa | Dónde | Qué hace | Qué NO hace |
|---|---|---|---|
| `model` | servidor | Datos del negocio (`Cita`, `Paciente`, `TipoPrueba`...) | Lógica, persistencia |
| `repository` | servidor | Guardar y recuperar datos | Reglas de negocio |
| `service` | servidor | **Reglas de negocio** y orquestación | Acceso a red, vistas |
| `controller` (servidor) | servidor | Puerta de entrada: recibe, delega en el servicio, responde | Lógica de negocio |
| `view` | cliente | Mostrar datos y recoger entradas | Decidir, validar reglas de negocio |
| `controller` (cliente) | cliente | Conecta la vista con el servidor mediante la API | Acceso directo a clases del servidor |
| `*Api` / `ApiClient` | cliente | Habla con el servidor (el resto del cliente no sabe cómo) | Lógica de presentación |
| `*DTO` | cliente | Copia simplificada de los datos para viajar por red | Lógica |

### Reglas de dependencia (se comprueban con ArchUnit)

1. **Programa contra interfaces, no contra clases.** `Service`, `Repository`, `Controller`, `View` y `Api` son interfaces; las clases que las implementan se llaman `*Impl` (o `*Memoria`, `*Rest`...).
2. La vista **nunca** llama a un repositorio ni a un servicio. El controller **no** accede a repositorios.
3. El **cliente nunca importa clases del servidor** (solo habla por la API con DTOs).
4. **Sin dependencias cíclicas** entre paquetes ni entre módulos.
5. La elección de la implementación concreta se hace **en un único sitio** (clase de arranque), nunca dentro de los servicios.
6. Inyecta las dependencias por **constructor** (`Objects.requireNonNull`). Sin estáticos ni singletons ocultos.
7. El **dominio (model, service, repository) no depende de ningún framework**. Si se usa un framework web, solo vive en el borde (controladores y arranque).
8. Persistencia: repositorios **en memoria** (`*RepositoryMemoria`). No añadas base de datos.

### Gobierno de interfaces

- **Añadir** métodos a una interfaz es compatible y se permite si lo exige el CU.
- **Cambiar o eliminar** firmas, renombrar o quitar valores de un `enum`, o cambiar campos de un DTO **rompe a otros equipos**. No lo hagas sin aprobación explícita de **Víctor Bravo** (responsable de interfaces).
- Cada interfaz lleva **Javadoc de contrato**: qué hace, precondiciones, excepciones que lanza, roles autorizados y tiempo máximo esperado cuando aplique.

### Contratos transversales (It. 0)

- `Auditoria` (stub `AuditoriaStub`): registra accesos a datos clínicos. **Todo servicio que lea o escriba datos clínicos la invoca** (RNF-SEG-02).
- `ServicioAutenticacionSSO`, `Usuario`, `Rol`: SSO simulado hasta el CU-20 (RNF-SEG-03).
- `ControlAcceso`: toda operación de servicio comprueba permisos por rol antes de actuar (RNF-SEG-01).
- Errores comunes: `EntidadNoEncontrada`, `ValidacionIncorrecta`, `AccesoDenegado`. No inventes excepciones propias por CU para estos casos.

### Modelo base (compartido por varios CU)

`Persona` (abstracta) → `Paciente`, `Doctor` · `Cita` · `TipoPrueba` · `SolicitudPrueba` · enums `Modalidad` (`RX`, `ECOGRAFIA`, `MAMOGRAFIA`, `TAC`, `RM`), `Prioridad` (`URGENTE`, `PREFERENTE`, `ORDINARIA`) y `EstadoCita` (`PENDIENTE`, `CONFIRMADA`, `CAMBIO_SOLICITADO`, `CANCELADA`, `REALIZADA`, `NO_PRESENTADO`).
Son clases de datos **sin lógica**. Las transiciones válidas de estado y las validaciones van en el servicio.

### Decisiones pendientes de ratificar (no las asumas)

Consulta `DECISIONS.md` antes de dar por hecho cualquiera de estas. Si no está registrada, pregunta:

- Estructura de módulos Maven: un módulo por iteración (propuesta) frente a `cliente`/`servidor`.
- Tecnología de la API: REST/JSON (propuesta: Spring Boot en el borde del servidor y `HttpClient` en el cliente).
- `ApiClient` único o una interfaz por recurso (propuesta: por recurso).
- Contenido exacto de `SolicitudPrueba` y de los estados de `EstadoCita`.

## 5. Calidad (quality by design)

Características críticas (ISO/IEC 25010): **seguridad**, **adecuación funcional** y **eficacia de desempeño**.

| RNF | Qué implica para tu código |
|---|---|
| RNF-SEG-01 | Toda operación valida permisos por rol |
| RNF-SEG-02 | Todo acceso a datos clínicos se audita (usuario, fecha, acción) |
| RNF-SEG-03 | Autenticación SSO y sesiones con caducidad |
| RNF-SEG-04 | Sin secretos en el repositorio; sin dependencias con vulnerabilidades altas o críticas |
| RNF-FUN-01 | Cada CU tiene criterios de aceptación y pruebas asociadas |
| RNF-FUN-02 | Intercambios HL7/FHIR/DICOM conformes a los esquemas (pruebas de contrato con stubs) |
| RNF-FUN-03 | Validar atributos del catálogo (preparación, contraste, consentimiento) al prescribir y citar |
| RNF-EF-01 | Consultas y operaciones de agenda y listas de espera con p95 < 2 s |
| RNF-EF-02 | El visor de imágenes no bloquea la interfaz |

### Umbrales (quality gates)

- Cobertura **≥ 80 %** en CU prioritarios y **≥ 60 %** en el resto.
- Complejidad ciclomática **≤ 10** por método.
- Duplicación **< 3 %**.
- Linter y formateador **sin avisos**.

### Definition of Done de cada issue

1. Se cumplen los criterios de aceptación del CU y tienen pruebas automáticas.
2. Linter y formateador pasan sin avisos.
3. Control de acceso por rol aplicado y accesos a datos clínicos auditados.
4. Sin secretos ni dependencias vulnerables.
5. Quality gates en verde.
6. Documentación del módulo y `CHANGELOG.md` actualizados.
7. PR con la doble aprobación (propietario del módulo y Raúl Arroyo; Víctor Bravo también en CU prioritarios que afectan a la arquitectura). **Nadie aprueba su propio PR.**

### Pruebas

- Java 21, **JUnit 5**. Pruebas unitarias por componente y pruebas de aceptación por CU.
- Usa **dobles** (implementaciones falsas de las interfaces) para aislar capas. No dependas de red ni de datos reales.
- **Nunca debilites una prueba existente** para que pase (borrar asserts, relajar umbrales, `@Disabled`) ni cambies un contrato para que encaje con tu código.
- Datos de prueba **anonimizados y ficticios**. Jamás datos clínicos reales.

### Refactorización asistida

- Solo sobre focos que detecte el análisis estático (complejidad, duplicación, *code smells*).
- Con la suite **en verde antes y después**.
- **Sin cambiar interfaces públicas.**
- En commits separados de los de funcionalidad (`chore:`).

## 6. Configuración y Git Flow

| Rama | Origen | Destino | Notas |
|---|---|---|---|
| `main` | — | — | Producción. **Nunca** se hace commit directo |
| `develop` | `main` | — | Integración. También protegida: todo entra por PR |
| `feature/CU-NN-nombre` | `develop` | `develop` | Una por CU/issue |
| `release/vX.Y.0` | `develop` | `main` y `develop` | Al cierre de cada iteración; tag anotado |
| `hotfix/vX.Y.Z` | `main` | `main` y `develop` | Solo urgencias |

- **Commits:** *Conventional Commits* (`feat:`, `fix:`, `docs:`, `chore:`, `test:`, `refactor:`) que **referencian el número de issue**. Ejemplo: `feat(citas): reprogramar cita (#13)`.
- **Pull request:** a `develop`, con `Closes #NN` en la descripción y la etiqueta **`ai-assisted`** si has generado código.
- **CI en verde obligatorio** antes de fusionar. Si falla, corrige; no desactives comprobaciones.
- **Versionado (SemVer):** cada iteración sube la MINOR (v0.1.0, v0.2.0, v0.3.0); la **v1.0.0 es la entrega final** con interfaces congeladas. PATCH = corrección sin tocar interfaces; MINOR = añadir compatible; **MAJOR = romper algo que otros usan** (lo deciden Víctor y Abraham). Cada módulo Maven lleva su propia versión.
- **Hitos:** v0.1.0 (27/11), v0.2.0 (4/12), v0.3.0 y v1.0.0-rc.1 (11/12), v1.0.0 (15/12).
- Cada tag anotado en `main` publica `MODULES.md` con las versiones de cada artefacto y el `CHANGELOG.md` (*Keep a Changelog*).

## 7. Cómo trabajar una issue

1. Lee la issue: objetivos, disciplinas RADIT, **contratos/interfaces involucrados**, criterios de aceptación y horas.
2. Comprueba que las interfaces que necesitas existen y están documentadas. Si no, **detente y pregunta**.
3. Crea o confirma la rama `feature/CU-NN-nombre` desde `develop`.
4. Implementa **de dentro hacia fuera** en el servidor (repository → service → controller) y luego el cliente (Api → controller → view).
5. Escribe las pruebas junto al código; ejecuta `mvn clean verify` y deja el build en verde.
6. Haz commits pequeños con mensaje convencional y número de issue.
7. Resume en la respuesta: qué has hecho, qué contratos has usado, **qué has asumido** y qué queda pendiente.

## 8. Qué NO debes hacer

- Hacer commit o push a `main` o `develop`, ni forzar historia (`force push`).
- Fusionar ni aprobar PR.
- Cambiar, borrar o renombrar interfaces públicas, enums o DTO sin aprobación.
- Añadir dependencias, frameworks, base de datos o módulos nuevos por tu cuenta.
- Poner secretos, credenciales o datos clínicos reales en el código, las pruebas o los logs.
- Saltarte el control de acceso o la auditoría "para simplificar".
- Dejar `TODO` sin issue asociada ni código muerto.
- Escribir en `DECISIONS.md` decisiones que no haya tomado una persona.

## 9. Convenciones de código

- Java 21, codificación UTF-8.
- Nombres de clases, métodos y variables **en español**, como el código existente (`Cita`, `obtenerPorId`, `registrarAcceso`).
- Javadoc y mensajes de commit **en español**.
- Paquetes: `es.sescam.servidor.*` y `es.sescam.cliente.*`.
- Una clase pública por archivo. Sin `import` con comodines.
- Validación de entrada en el servicio, con las excepciones comunes. Los mensajes de error no revelan datos clínicos.

## 10. Quién decide qué

| Tema | Responsable |
|---|---|
| Requisitos y casos de uso | Marcelino Izquierdo (visto bueno de Álvaro García) |
| Arquitectura e interfaces | Víctor Bravo |
| Calidad y pruebas | Raúl Arroyo |
| Gestión de configuración, ramas y versiones | Abraham Escalona |
| Agentes de IA y este fichero | Juan Zopeque |
| Coordinación y documentación | Álvaro García |

## 11. Documentos de referencia

- `DECISIONS.md`: decisiones arquitectónicas, suposiciones y su razonamiento.
- `PLAN.md`: planificación, iteraciones, hitos y costes.
- `CONTEXT.md`: contexto del software para el desarrollo guiado por especificación.
- `README.md` y la wiki: enunciado resumido, actas y guía de lectura.
