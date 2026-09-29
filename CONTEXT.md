# CONTEXT.md · Contexto del software (Spec-Driven Development)

> Propósito: dar a cualquier agente de IA (y a cualquier persona que entre en el
> repositorio) el contexto mínimo y suficiente para implementar trabajo en este
> sistema sin inventar el dominio. **El equipo humano diseña los contratos; los
> agentes implementan el contrato.**

---

## 1. Qué es este sistema

El **Sistema Integral de Gestión de Radiodiagnóstico e Imagen Médica (SESCAM)**
administra las pruebas diagnósticas de un servicio hospitalario: catálogo de
pruebas, pacientes, solicitudes, citación, realización, informes, imagen médica
y explotación estadística.

No es un producto de propósito general: el vocabulario, las reglas y las
restricciones son las del entorno sanitario español. Cuando una regla aquí sea
ambigua, **preguntar antes de codificar**.

---

## 2. Glosario del dominio (uso obligatorio de estos términos)

| Término | Significado en este sistema |
| --- | --- |
| **Prueba** | Estudio diagnóstico realizado al paciente (Rx, US, mamografía, TAC, RM…). |
| **Tipo de prueba** | Elemento del catálogo que define una prueba: modalidad, equipo/sala, duración, preparación, contraste, consentimiento, seguridad, prioridad y seguimiento. |
| **Modalidad** | Familia tecnológica de la prueba: `RX`, `US`, `MAMO`, `TAC`, `RM`. |
| **Solicitud** | Petición de una prueba hecha por un médico, con indicación clínica y prioridad. |
| **Cita** | Asignación de una fecha y hora para la realización de una prueba. |
| **Agenda** | Conjunto de huecos de un médico o de un equipo, por día. |
| **Hueco** | Franja de tiempo disponible en una agenda, con un tipo de prueba asignado. |
| **Lista de espera** | Solicitudes sin cita asignada, ordenadas por prioridad y fecha de solicitud. |
| **Realización** | Registro de que la prueba se ha ejecutado, con incidencias. |
| **Informe** | Documento clínico redactado y firmado por el médico tras la realización. |
| **Serie de imágenes** | Conjunto de imágenes DICOM de una prueba, almacenado en el PACS. |
| **Seguimiento periódico** | Prescripción de repetir una prueba cada cierto intervalo. |
| **Admisión** | Alta, modificación o baja de un paciente en el sistema. |
| **Auditoría** | Registro inalterable de accesos a datos clínicos. |
| **HCE** | Historia Clínica Electrónica del hospital (sistema central, no nuestro). |

**Prohibido** inventar sinónimos: si un término del dominio no está en esta
tabla, primero se añade aquí (con issue) y después se usa en el código.

---

## 3. Actores y sus permisos

| Actor | Puede | No puede |
| --- | --- | --- |
| **Paciente** | Ver y gestionar sus citas, ver su historial, comunicar incidencias, responder encuestas | Ver datos de otros pacientes, crear solicitudes ni informes |
| **Médico** | Prescribir pruebas, gestionar sus solicitudes y agenda, redactar y firmar informes, registrar realización e incidencias, ver sus estadísticas | Modificar datos maestros de pacientes, cambiar parámetros del sistema |
| **Administración** | Alta/baja/modificación de pacientes, gestionar citas y agendas, listas de espera y prioridades, informes operativos | Acceder al contenido clínico de los informes sin motivo justificado |
| **Administrador** | Configurar parámetros, roles y permisos, auditar accesos, integrarse con sistemas centrales | Realizar acciones asistenciales (no prescribe ni informa) |

Principio de mínimo privilegio: **todo acceso se autoriza por rol y queda
auditado**.

---

## 4. Reglas de negocio invariantes

Estas reglas no se negocian. Si una implementación las contradice, es un
defecto.

1. **Una cita necesita una solicitud en firme.** No se puede citar una prueba
   que no tenga solicitud aprobada.
2. **La prioridad clínica va primero.** El orden de la lista de espera lo
   determina la prioridad clínica; el desempate es la fecha de solicitud.
3. **Un paciente no puede tener dos citas solapadas** para el mismo instante,
   sea cual sea la modalidad.
4. **La duración de una cita la fija el tipo de prueba** del catálogo. No se
   pueden solapar citas que excedan la duración reservada.
5. **Un informe solo se firma sobre una realización registrada.** No se puede
   firmar un informe de una prueba no realizada.
6. **Un informe firmado es inmutable.** Cualquier corrección genera un nuevo
   informe que referencia al anterior (`informeAnteriorId`).
7. **El acceso a datos clínicos es auditable.** Toda lectura de un informe o de
   una imagen genera un asiento de auditoría con actor, instante y propósito.
8. **La baja de un paciente es lógica**, no física: se conservan el historial
   clínico y la auditoría (obligación legal de conservación de datos de salud).
9. **El consentimiento informado es un requisito bloqueante** cuando el tipo de
   prueba lo exige. Sin consentimiento registrado no se puede realizar.
10. **La trazabilidad es completa**: toda acción clínica identifica al paciente,
    al profesional y al instante.

---

## 5. Restricciones técnicas

### 5.1 Stack (fijado por el enunciado)

- **Java 17** con **Eclipse** y **Visual Paradigm**.
- **Maven** en proyectos multimódulo: un componente por iteración.
- **Git** con estrategia **Git Flow**.
- Arquitectura **cliente-servidor** con clientes de escritorio (Windows, Linux,
  macOS), móviles (Android, iOS, iPadOS) y web.
- Patrón **Modelo-Vista-Controlador** en las capas de presentación.
- Principios **SOLID** y **programación orientada a interfaces**, nunca a
  clases concretas.

### 5.2 Integraciones (progresivas, no bloqueantes)

| Estándar | Uso | Prioridad |
| --- | --- | --- |
| **SSO** corporativo | Inicio de sesión único | Alta |
| **HCE** | Historia clínica del paciente | Alta |
| **DICOM** | Imágenes médicas y visores | Alta |
| **HL7** / **FHIR** | Interoperabilidad de datos clínicos | Media |
| RIS/PACS | Gestión de estudios e imágenes | Media |

> **Regla de los agentes:** si la integración real no está disponible en el
> entorno, se implementa **contra el contrato** (interfaz) y se proporciona un
> doble de prueba (*test double*). Nunca se acapara la lógica de negocio de la
> integración dentro de un agente externo.

### 5.3 Capa de persistencia

- Acceso a datos **exclusivamente a través de interfaces de repositorio**.
- Sin framework ORM en el núcleo: el núcleo define los contratos, la
  implementación decide la tecnología.
- **Trazabilidad y auditoría no son opcionales en ninguna tabla.**

---

## 6. Convenciones de código

```
es.escam.radiodiagnostico.<componente>.<capa>
```

| Capa | Contenido | Regla |
| --- | --- | --- |
| `dominio` | Entidades, tipos de valor, invariantes | Sin dependencias fuera del `nucleo` |
| `aplicacion` | Casos de uso, orquestación | Depende de `dominio` mediante interfaces |
| `infraestructura` | Persistencia, integraciones, UI | Depende de `dominio` y `aplicacion` |
| `nucleo` | Contratos transversales | No depende de ningún dominio |

- Nombres en español para el dominio, en inglés para los identificadores
  técnicos (`id`, `nombre`, `fecha`). Consistente y revisable.
- Toda clase pública lleva Javadoc que exprese el **contrato**, no la
  implementación: precondiciones, poscondiciones e invariantes.
- Nada de estado global mutable.
- Excepciones de dominio tipadas; los errores técnicos se traducen a
  excepciones de dominio en la frontera.

---

## 7. Cómo trabajar un caso de uso (flujo obligatorio)

1. **Antes de escribir código**, localizar el CDU en `PLAN.md` §5 y su
   descripción completa en la wiki.
2. Crear la rama desde `develop`:
   `git checkout -b feature/<componente> develop`
3. Si el CDU aún no está descrito, **detenerse**: primero se escribe la
   descripción en la wiki y se abre el issue. No se implementa sobre
   suposiciones.
4. Diseñar primero las **interfaces** del contrato; después la implementación.
5. Escribir el test antes que la implementación siempre que sea posible.
6. Refactoring en commit aparte, con la suite en verde antes y después.
7. Abrir PR a `develop` con la revisión cruzada solicitada.

---

## 8. Preguntas abiertas (bloquean implementación)

Se mantienen aquí hasta que la reunión las resuelva. Quien implemente no debe
decidir por su cuenta.

| # | Pregunta | Estado | Impacto |
| --- | --- | --- | --- |
| P1 | ¿El paciente se autentica con DNI + SMS, o solo a través del SSO corporativo? | Abierta | `CDU-03`, iteración 2 |
| P2 | ¿El soporte de DICOM viewer es web embebido o aplicación de escritorio? | Abierta | `CDU-15`, iteración 5 |
| P3 | ¿Las escalas de prioridad son las del SESCAM (urgente, programada…) o un catálogo propio? | Abierta | `CDU-09`, iteración 4 |
| P4 | ¿Cuál es el periodo legal de conservación de los datos clínicos que se aplica? | Abierta | `CDU-18`, iteración 6 |
| P5 | ¿El PACS es el existente del hospital o se simula en el laboratorio? | Abierta | `CDU-14`, `CDU-15` |

---

## 9. Glosario de salida rápida para agentes

Cuando se implemente un CDU, el resultado esperado es:

- Interfaces (`interface`, no clase) para todo contrato entre componentes.
- Casos de uso en la capa de aplicación, sin lógica de UI ni de base de datos.
- Tests JUnit 5 para cada criterio de aceptación del CDU.
- Errores de dominio tipados y traducibles a mensaje de usuario.
- Nada de secretos, ni claves, ni datos clínicos reales en el repositorio.
