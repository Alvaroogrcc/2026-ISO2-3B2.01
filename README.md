# 2026-ISO2-3B2.01

**Ingeniería del Software II** · Grado en Ingeniería Informática · Curso 2026/2027
Grupo `3B2.01`

---

## Sistema Integral de Gestión de Radiodiagnóstico e Imagen Médica (SESCAM)

Sistema integral para administrar de forma eficiente las pruebas diagnósticas, las
agendas de trabajo, los recursos técnicos y las actividades del Servicio de
Radiodiagnóstico e Imagen Médica del SESCAM.

El objetivo es facilitar la interacción entre los distintos tipos de usuario,
automatizar los procesos clave (solicitud, citación, realización e informe de
pruebas) y proporcionar herramientas de análisis y seguimiento para la mejora
continua del servicio asistencial.

### Integración con el entorno hospitalario

El sistema debe poder integrarse de forma progresiva con los servicios centrales
del hospital mediante los estándares sanitarios habituales:

- Inicio de sesión único (SSO) corporativo
- Historia Clínica Electrónica (HCE)
- Gestión de pacientes y admisión
- Plataforma RIS/PACS, con interoperabilidad **DICOM**, **HL7** y **FHIR**

### Arquitectura de despliegue

Arquitectura **cliente-servidor**, dando cabida a distintas aplicaciones cliente:

- Escritorio: Windows, Linux, macOS
- Móvil: Android, iOS, iPadOS
- Web, incluyendo visores de imagen compatibles con DICOM

---

## Equipo

| Miembro | Responsabilidad |
| --- | --- |
| Álvaro García | |
| Abraham Escalona | |
| Víctor Bravo | |
| Marcelino Izquierdo | |
| Raúl Arroyo | |
| Juan Zopeque | |

> La tabla de responsabilidades se completa durante la fase de Inicio del PUD,
> en cuanto se repartan los casos de uso entre las iteraciones.

---

## Tipos de usuario

1. **Pacientes** — consulta y gestión de citas, notificaciones y recordatorios,
   historial de pruebas, comunicación de incidencias, encuestas de satisfacción e
   información sobre pruebas de seguimiento.
2. **Médicos** — solicitud/prescripción de pruebas con indicación clínica y
   prioridad, prescripción de seguimiento periódico, gestión de solicitudes y
   agendas, creación y consulta de informes e imágenes, registro de realización e
   incidencias, estadísticas propias.
3. **Personal administrativo / Admisión** — alta, modificación y baja de pacientes,
   gestión de citas, agendas de médicos, listas de espera, prioridades y huecos, e
   informes operativos de ocupación y tiempos de espera.
4. **Administradores del sistema** — configuración de parámetros, gestión de roles
   y permisos, auditoría de accesos a datos clínicos, integración con sistemas
   hospitalarios y estadísticas globales.

## Catálogo de tipos de prueba

Cada tipo de prueba se modela como un elemento del catálogo con sus atributos:
modalidad, equipo/sala requeridos, duración estimada, preparación previa del
paciente, uso de contraste y consentimiento informado, requisitos de seguridad,
prioridad y posibilidad de seguimiento periódico.

Modalidades previstas (listado no exhaustivo): radiografía convencional (Rx),
ecografía (US), mamografía y tomosíntesis, tomografía computarizada (TAC) y
resonancia magnética (RM).

---

## Metodología: Proceso Unificado de Desarrollo (PUD)

El proyecto se conduce con el **PUD**, en sus tres características:

1. **Dirigido por casos de uso**
2. **Iterativo e incremental**
3. **Centrado en la arquitectura**

Suposiciones de partida fijadas en el enunciado:

- Un Requisito Funcional se mapea en un solo Caso de Uso (**1 RF : 1 CDU**).
- Un Caso de Uso se realiza en una y solo una iteración (**1 CDU : 1 Iteración**).
- Cada iteración genera **un componente (módulo Maven)**, con su versión en
  *Semantic Versioning*.
- **Iteración 0** (fase de Inicio) con coste supuesto de **1000 €**.
- **Iteración N** (fase de Transición) con coste supuesto de **2000 €**: integración
  de todos los componentes, empaquetado, despliegue, pruebas de integración y
  manual de usuario.
- Una semana de trabajo equivale a **40 horas** laborables de consultant humano.

El desarrollo del sistema se descompone en **módulos** con máxima coherencia y
mínimo acoplamiento, aplicando los principios **SOLID**, programación orientada a
**interfaces** (no a clases) y el patrón **Modelo-Vista-Controlador**.

> **El ingeniero diseña las interfaces; la IA implementa los contratos.**

El estudiante es quien analiza el negocio, crea la arquitectura, divide el sistema
en módulos, diseña las interfaces y establece el plan. Los agentes de IA son la
fuerza de ejecución controlada, no el sustituto de esas decisiones.

### Stack

- **Java**, con **Eclipse** y **Visual Paradigm**
- **Maven** en proyectos multimódulo
- **Git** con estrategia de ramificación **Git Flow**
- Agentes de soporte al desarrollo (IA): VS Code + GitHub Copilot Chat, OpenCode,
  Antigravity, Claude Code, modelos locales vía Ollama o LM Studio

---

## Gestión de configuración: Git Flow

Ramas del repositorio y su origen:

| Rama | Origen | Uso |
| --- | --- | --- |
| `main` | — | Código en producción. **Protegida**: no se hacen commits directos, todo entra por pull request. |
| `develop` | `main` | Rama de integración, contiene lo listo para la siguiente release. |
| `feature/*` | `develop` | Desarrollo de un caso de uso. Se fusiona en `develop` y `main`. |
| `release/*` | `develop` | Preparación de una release. Se fusiona en `main` y `develop`. |
| `hotfix/*` | `main` | Corrección crítica en producción. Se fusiona en `main` y `develop`. |

Convenciones:

- Ningún commit directo en `main`.
- Toda integración en la rama principal se realiza mediante **pull request**.
- Versiones con **Semantic Versioning**.
- Plan de versiones por componente y por construcción.
- `main` con protección de rama: pull request obligatorio, sin *force push* ni
  borrado.

---

## Entregables

### Teoría 1 — entrega en Moodle, 13 de noviembre de 2026

Documento autocontenido que recoja:

- **Planificación del proyecto** según el PUD, con el coste final, la agenda, las
  fechas de liberación y el detalle razonado de todas las decisiones tomadas.
- **Plan de Gestión de Configuración**: quién aprueba los requisitos, quién se
  responsabiliza de cada componente, quién aprueba los componentes, plan de
  versiones de componentes y construcciones.
- **Plan de Gestión de la Calidad**: características de calidad relevantes
  (**ISO/IEC 25010**, *quality by design*) y cómo se van a controlar y mejorar
  en el proyecto, incluyendo el papel del *refactoring*.

### Práctica 1 — entrega conjunta el 15 de diciembre de 2026

Se evalúa lo relativo a planificación y ejecución, gestión de configuración y
gestión de calidad, evidenciados en GitHub a través de la wiki, los commits, las
tareas creadas e Insights. Esta entrega es única e incluye la parte de **testing
y mantenimiento**.

### Wiki del repositorio

La wiki es la **guía de lectura de la documentación** y debe enlazar toda la
planificación, actas de reuniones, descripciones y análisis de casos de uso,
ficheros `.md`, ficheros de Visual Paradigm y los ficheros de imputación de horas.
Incluye además una sección de **Autoevaluación y Experiencia** con la
autoevaluación y autocrítica de cada miembro.

---

## Documentación del proyecto

| Fichero | Contenido |
| --- | --- |
| `PLAN.md` | Planificación del proyecto, iteraciones, hitos y costes. |
| `CONTEXT.md` | Contexto del software para el desarrollo Spec-Driven. |
| `DECISIONS.md` | Decisiones arquitecturales, suposiciones y su razonamiento. |
| Wiki | Actas, análisis, casos de uso, informes y autoevaluación. |

## Convenciones de trabajo

- Un issue por cada decisión o compromiso de reunión, con objetivos, disciplinas
  **RADIT** asociadas, contratos/interfaces involucrados, criterios de aceptación
  (*definition of done*) y estimación en horas con y sin agente.
- Seguimiento del avance mediante un **tablero Kanban** en un proyecto de GitHub.
- Coherencia obligatoria entre planificación, compromisos de reunión, issues
  creados y secuencia real de avances.
- Todas las reuniones se documentan; el profesor de prácticas se incorpora como
  *contributor* para el seguimiento del avance.

---

## Reglas de contribución

1. Nada entra en `main` sin pull request aprobado.
2. El trabajo se desarrolla en `feature/*` naciendo de `develop`.
3. Las correcciones urgentes van en `hotfix/*` naciendo de `main`.
4. Cada iteración del PUD trabaja **exclusivamente** los casos de uso asignados.
5. Se respeta la separación de responsabilidades: el equipo humano decide y audita;
   los agentes implementan.
