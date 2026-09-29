# PLAN.md · Planificación del proyecto (PUD)

> Sistema Integral de Gestión de Radiodiagnóstico e Imagen Médica (SESCAM)
> 2026-ISO2-3B2.01 · Ingeniería del Software II · PUD iterativo e incremental
> dirigido por casos de uso y centrado en la arquitectura.

**Estado:** Iteración 0 (fase de Inicio) — borrador para revisión del equipo.
**Última actualización:** 2026-10-19

---

## 1. Parámetros de coste fijados por el enunciado

| Parámetro | Valor |
| --- | --- |
| Coste Iteración 0 (Inicio) | 1 000 € |
| Coste Iteración *N* (Transición) | 2 000 € |
| Horas laborables por semana de consultant | 40 h |
| **Tarifa por hora efectiva** | **50 €/h** |
| Relación 1 RF : 1 CDU | obligatorio |
| Relación 1 CDU : 1 Iteración | obligatorio |
| Relación 1 Iteración : 1 componente (módulo Maven) | obligatorio |

**Derivación de la tarifa.** Es la única tarifa que hace coherentes las dos
suposiciones de coste: `2000 € / 40 h = 50 €/h`. Con esa tarifa, la Iteración 0
cuesta 1 000 € = **20 h** de consultant, y cada iteración posterior cuesta
2 000 € = **40 h** = **1 semana** de consultant. Toda la planificación
económica de este documento usa esa única cifra, de modo que coste, agenda y
horas son consistentes entre sí y con la Teoría 1.

---

## 2. Equipo y modelo de responsabilidades

| Miembro | Rol en el proyecto | Ámbito principal |
| --- | --- | --- |
| Álvaro García | Arquitecto y responsable de Configuración | Núcleo, integración, Git Flow |
| Abraham Escalona | Analista de dominio | Catálogo de tipos de prueba |
| Víctor Bravo | Analista de dominio | Pacientes y admisión |
| Marcelino Izquierdo | Analista de dominio | Agendas y citas |
| Raúl Arroyo | Analista de dominio | Solicitudes, prioridades e incidencias |
| Juan Zopeque | Analista de dominio | Informes e imagen médica (RIS/PACS) |

- **Responsable de componente:** diseña los contratos/interfaces del componente
  que le toca y aprueba su integración. Ver §7.
- **Revisor cruzado:** cada componente lo revisa el responsable del componente
  adyacente. La revisión cruzada es obligatoria y se evidencia en el PR.
- **Profesor de prácticas:** *contributor* del repositorio, con acceso de solo
  lectura al seguimiento del avance.

---

## 3. Modelo de casos de uso

Un **Caso de Uso (CDU)** es la unidad atómica de trabajo. Cada CDU:

- se corresponde con exactamente un Requisito Funcional (RF);
- se implementa en exactamente una iteración;
- produce exactamente un componente (módulo Maven) versionado.

**Patrón CDU** (plantilla común, reutilizada por todos los casos de uso):

`ID CDU · Nombre · Actor principal · Actores secundarios · Precondición ·
Disparador · Escenario principal (extensiones) · Postcondición ·
Contratos/interfaces involucrados · RADIT · Criterios de aceptación ·
Estimación (h sin agente / h con agente)`.

---

## 4. Iteraciones, hitos y costes

Fechas de la agenda: cada iteración ocupa una semana laboral completa
(lunes a viernes). Inicio del proyecto: **lunes 2026-10-19**.

| It. | Fases | Componente (módulo Maven) | CDU implementados | Build | Fechas | Horas | Coste |
| --- | --- | --- | --- | --- | --- | --- | --- |
| **0** | Inicio | `radiodiagnostico-nucleo` (esqueleto) | — (no produce CDU) | 0.1.0 | 19/10 – 23/10 | 20 | 1 000 € |
| **1** | Transición | `radiodiagnostico-catalogo` | CDU-01, CDU-02 | 0.2.0 | 26/10 – 30/10 | 40 | 2 000 € |
| **2** | Transición | `radiodiagnostico-pacientes` | CDU-03, CDU-04, CDU-05 | 0.3.0 | 02/11 – 06/11 | 40 | 2 000 € |
| **3** | Transición | `radiodiagnostico-agenda` | CDU-06, CDU-07, CDU-08 | 0.4.0 | 09/11 – 13/11 | 40 | 2 000 € |
| **4** | Transición | `radiodiagnostico-solicitudes` | CDU-09, CDU-10, CDU-11, CDU-12 | 0.5.0 | 16/11 – 20/11 | 40 | 2 000 € |
| **5** | Transición | `radiodiagnostico-informes` | CDU-13, CDU-14, CDU-15 | 0.6.0 | 23/11 – 27/11 | 40 | 2 000 € |
| **6** | Transición | `radiodiagnostico-gestion` | CDU-16, CDU-17, CDU-18, CDU-19 | 0.7.0 | 30/11 – 04/12 | 40 | 2 000 € |
| **7** | Transición | `radiodiagnostico-integracion` | CDU-20 (transversal) | 0.8.0 | 07/12 – 11/12 | 40 | 2 000 € |

> La columna **Build** es la versión del *agregador* Maven al cerrar la
> iteración, que es la que se etiqueta y se publica. El detalle de versiones por
> componente está en §8.1.

### 4.1 Coste total

| Concepto | Horas | Coste |
| --- | --- | --- |
| Iteración 0 (Inicio) | 20 | 1 000 € |
| Iteraciones 1 – 7 (Transición) | 280 | 14 000 € |
| **TOTAL** | **300 h** | **15 000 €** |

**Desglose del coste de una iteración de Transición (2000 € = 40 h):**
integración de componentes, empaquetado, despliegue, pruebas de integración y
manual de usuario, tal y como fija el enunciado.

### 4.2 Reparto de carga

300 h entre 6 miembros = **50 h por miembro** de media. El reparto se
equilibra asignando a cada iteración un responsable principal (40 h de
esfuerzo de diseño + integración de su componente, mayoritariamente en la
semana de su iteración) y dos revisores cruzados (5 h cada uno).

---

## 5. Catálogo de casos de uso

| CDU | Nombre | Actor principal | Iter. | Componente |
| --- | --- | --- | --- | --- |
| CDU-01 | Consultar el catálogo de tipos de prueba | Todos | 1 | `catalogo` |
| CDU-02 | Dar de alta y editar un tipo de prueba | Administrador | 1 | `catalogo` |
| CDU-03 | Dar de alta a un paciente | Administración | 2 | `pacientes` |
| CDU-04 | Modificar y dar de baja a un paciente | Administración | 2 | `pacientes` |
| CDU-05 | Consultar el historial de pruebas del paciente | Paciente, Médico | 2 | `pacientes` |
| CDU-06 | Pedir cita para una prueba | Paciente, Médico | 3 | `agenda` |
| CDU-07 | Gestionar la agenda de un médico | Administración, Médico | 3 | `agenda` |
| CDU-08 | Enviar notificaciones y recordatorios de cita | Paciente (sistema) | 3 | `agenda` |
| CDU-09 | Solicitar/prescribir una prueba con indicación y prioridad | Médico | 4 | `solicitudes` |
| CDU-10 | Prescribir seguimiento periódico | Médico | 4 | `solicitudes` |
| CDU-11 | Gestionar la lista de espera, prioridades y huecos | Administración | 4 | `solicitudes` |
| CDU-12 | Registrar realización e incidencias de una prueba | Médico, Técnico | 4 | `solicitudes` |
| CDU-13 | Redactar y firmar un informe de prueba | Médico | 5 | `informes` |
| CDU-14 | Consultar informes e imágenes previas | Médico, Paciente | 5 | `informes` |
| CDU-15 | Visualizar imágenes DICOM | Médico, Paciente | 5 | `informes` |
| CDU-16 | Configurar parámetros del sistema | Administrador | 6 | `gestion` |
| CDU-17 | Gestionar roles y permisos | Administrador | 6 | `gestion` |
| CDU-18 | Auditar accesos a datos clínicos | Administrador | 6 | `gestion` |
| CDU-19 | Obtener estadísticas e informes operativos | Administrador, Médico | 6 | `gestion` |
| CDU-20 | Integrar todos los componentes y desplegar | Transversal | 7 | `integracion` |

**Trazabilidad 1 RF : 1 CDU.** Cada CDU de la tabla anterior tiene exactamente un
RF asociado con el mismo índice. La matriz RF ↔ CDU se mantiene en la wiki y se
revisa en cada reunión.

---

## 6. Hitos del proyecto

| Hito | Fecha | Descripción | Criterio de logro |
| --- | --- | --- | --- |
| **H0 · Inicio completado** | 2026-10-23 | Casos de uso descritos, arquitectura y planes aprobados | `PLAN.md`, `DECISIONS.md` y `CONTEXT.md` en `main` vía PR |
| **H1 · Entrega Teoría 1** | 2026-11-13 | Documento de planificación, GC y calidad entregado en Moodle | PDF subido a Moodle antes de las 23:59 |
| **H2 · Entrega Práctica 1** | 2026-12-15 | Wiki, commits, issues e Insights al día | Wiki navegable y release `v1.0.0` publicada |
| **H3 · Release candidate** | 2026-12-11 | Todos los componentes integrados y probados | `mvn verify` en verde sobre `develop` |
| **H4 · Release v1.0.0** | 2026-12-11 | Tag `v1.0.0` sobre `main` | Tag creado desde `main` vía PR de `release/v1.0.0` a `main` |

---

## 7. Gestión de configuración (resumen)

- **Quién aprueba los requisitos:** responsable del caso de uso (analista de
  dominio) + VoBo del arquitecto. Sin VoBo arquitectónico no se abre el issue.
- **Quién se responsabiliza de cada componente:** la tabla de §2. Es el
  responsable quien diseña las interfaces del componente y firma su
  integración.
- **Quién aprueba los componentes:** el responsable del componente, tras la
  revisión cruzada del responsable del componente adyacente.
- **Plan de versiones:** ver §8.
- **Ramas:** Git Flow estricto según el README (`main`, `develop`, `feature/*`,
  `release/*`, `hotfix/*`).

**Reglas operativas:**

1. Nada entra en `main` sin pull request aprobado.
2. `main` con protección de rama: PR obligatorio, sin *force push* ni borrado.
3. El trabajo empieza en `feature/<componente>` naciendo de `develop`.
4. Las correcciones urgentes van en `hotfix/<version>` naciendo de `main`.
5. Cada iteración trabaja **exclusivamente** los CDU asignados en §5.
6. Todo commit referencia su issue: `Refs #<n>` en el pie del mensaje.
7. Formato de mensaje de commit: `<tipo>(<ámbito>): <descripción>` con
   `feat`, `fix`, `docs`, `test`, `refactor`, `chore`.

---

## 8. Plan de versiones (Semantic Versioning)

`MAJOR.MINOR.PATCH` con las siguientes reglas:

- **MAJOR**: se incrementa al publicar una release que introduce un cambio
  incompatible en los contratos públicos entre componentes.
- **MINOR**: se incrementa en cada iteración completada, al integrar un
  componente nuevo.
- **PATCH**: se incrementa por corrección de defecto sin cambio de contrato;
  se aplica desde `hotfix/*`.

> `1.0.0` (iteración 7) todos los componentes pasan a `1.0.0`; hasta entonces
> ninguno declara un contrato estable de producción, por lo que las versiones
> `0.x` no generan promesas de estabilidad.

### 8.1 Versiones por componente

| Componente | Se crea en | Versión al crearse | Versión en `v1.0.0` |
| --- | --- | --- | --- |
| `2026-iso2-3b2-01` (agregador) | Inicio | 0.1.0 | 1.0.0 |
| `radiodiagnostico-nucleo` | Iteración 0 | 0.1.0 | 1.0.0 |
| `radiodiagnostico-catalogo` | Iteración 1 | 0.2.0 | 1.0.0 |
| `radiodiagnostico-pacientes` | Iteración 2 | 0.3.0 | 1.0.0 |
| `radiodiagnostico-agenda` | Iteración 3 | 0.4.0 | 1.0.0 |
| `radiodiagnostico-solicitudes` | Iteración 4 | 0.5.0 | 1.0.0 |
| `radiodiagnostico-informes` | Iteración 5 | 0.6.0 | 1.0.0 |
| `radiodiagnostico-gestion` | Iteración 6 | 0.7.0 | 1.0.0 |
| `radiodiagnostico-integracion` | Iteración 7 | 0.8.0 | 1.0.0 |

### 8.2 Versiones por construcción (builds)

| Build | Fecha | Ramas de integración | Descripción |
| --- | --- | --- | --- |
| `0.1.0` | 2026-10-23 | `develop` | Esqueleto multimódulo y documentación de Inicio |
| `0.2.0` | 2026-10-30 | `develop` | Catálogo de tipos de prueba |
| `0.3.0` | 2026-11-06 | `develop` | Pacientes y admisión |
| `0.4.0` | 2026-11-13 | `develop` | Agendas y citas |
| `0.5.0` | 2026-11-20 | `develop` | Solicitudes y prioridades |
| `0.6.0` | 2026-11-27 | `develop` | Informes e imagen médica |
| `0.7.0` | 2026-12-04 | `develop` | Gestión, roles, auditoría y estadísticas |
| `1.0.0` | 2026-12-11 | `release/v1.0.0` → `main` | Release candidate: integración total y manual de usuario |

> **Única release del proyecto.** Al ser un proyecto académico con una sola
> entrega de producción, la rama `release/v1.0.0` se abre en la Iteración 0 y
> permanece congelada: solo recibe correcciones de `hotfix/*` y el hito H4. A
> partir de ese momento `develop` queda libre para la siguiente iteración
> mientras `release/v1.0.0` se estabiliza. El tag `v1.0.0` se corta en el
> cierre de la Iteración 7, no antes.

---

## 9. Gestión de la calidad (resumen)

### 9.1 Características de calidad relevantes (ISO/IEC 25010)

| Característica | Sub-característica prioritaria | Por qué en este sistema |
| --- | --- | --- |
| **Fiabilidad** | Maturidad, tolerancia a fallos, recuperabilidad | Datos clínicos: no se puede perder ni duplicar una cita |
| **Seguridad** | Confidencialidad, integridad, trazabilidad, no repudio | Datos de salud; auditoría obligatoria de accesos |
| **Usabilidad** | Conformidad, accesibilidad, interfaz | Cuatro tipos de usuario con perfiles muy distintos |
| **Mantenibilidad** | Modularidad, reusabilidad, analizabilidad, modificabilidad, testabilidad | 7 componentes evolutivos con contratos estables |
| **Compatibilidad** | Interoperabilidad | DICOM, HL7, FHIR, SSO corporativo, HCE |
| **Portabilidad** | Adaptabilidad | Escritorio, móvil y web |
| **Rendimiento** | Idoneidad (time behaviour) | Visores de imagen y listas de espera |

> La sub-característica de rendimiento se aborda con pruebas de carga en la
> iteración de integración; el resto se verifica de forma continua.

### 9.2 Quality by design

- Cada contrato lleva sus precondiciones, poscondiciones e invariantes
  documentadas en la wiki.
- Los criterios de aceptación del §3 son tests: cada CDU nace con su test.
- La integración en `develop` está condicionada a `mvn verify` en verde.
- Revisión cruzada obligatoria en cada PR (Definition of Done en §10).
- Cobertura mínima por componente: **70 % de líneas**, medida con JaCoCo en
  la fase `verify`. Es la barrera de entrada de la integración.

### 9.3 Papel del refactoring

Se practica refactoring de forma explícita y trazable:

- al final de cada iteración, en el PR de cierre, con su propio commit
  `refactor(<componente>): <motivo>`;
- nunca mezclado con funcionalidad nueva en el mismo PR;
- solo se admite si la suite de pruebas queda en verde antes y después, lo que
  demuestra que el comportamiento no cambia;
- se prioriza la eliminación de duplicación (`Duplicated Code`) y el
  acoplamiento entre componentes, por ser los dos riesgos de arquitectura más
  graves del diseño.

---

## 10. Definition of Done

Una iteración está terminada cuando **todo** lo siguiente es cierto:

1. Todos los CDU asignados están implementados y sus tests pasan.
2. `mvn verify` en verde en la rama de la iteración, con cobertura ≥ 70 %.
3. Revisión cruzada aprobada y registrada en el PR.
4. `PLAN.md` actualizado con el coste real consumido.
5. Wiki actualizada: acta de reunión, análisis del CDU e imputación de horas.
6. Issue de cierre creado y referenciado en el commit final.
7. PR a `develop` fusionado; `main` actualizado mediante PR desde `develop`.

---

## 11. Riesgos

| # | Riesgo | Prob. | Impacto | Mitigación |
| --- | --- | --- | --- | --- |
| R1 | Los requisitos del dominio clínico se expanden a mitad del proyecto | Alta | Alto | Requisito congelado por iteración; cambios solo con issue y reestimación |
| R2 | Sobrecarga de un miembro del equipo | Media | Medio | Reparto rotativo de revisiones cruzadas |
| R3 | Acoplamiento excesivo entre componentes | Media | Alto | Regla de dependencia y revisión arquitectónica en cada PR (ADR-001) |
| R4 | Integración con RIS/PACS (DICOM) no disponible en el laboratorio | Alta | Medio | Simulador/mock del PACS en la iteración 5; la conexión real queda como demo |
| R5 | Conflicto de versiones entre componentes | Baja | Medio | Versión de agregador única; `mvn -U verify` obligatorio antes de integrar |
| R6 | Atraso en la entrega de Teoría 1 (13/11) | Media | Alto | H1 coincide con el cierre de la Iteración 3; el borrador se para en la Iteración 2 |

---

## 12. Seguimiento

- **Tablero Kanban** en un proyecto de GitHub: `Por hacer` → `En curso` →
  `En revisión` → `Hecho`.
- Un issue por decisión o compromiso de reunión, con: objetivos, RADIT
  asociados, contratos involucrados, criterios de aceptación y estimación de
  horas **con y sin agente**.
- **Insights** de GitHub como fuente de evidencia del ritmo real de avance.
- Wiki como guía de lectura: actas y análisis, `.md`, ficheros de Visual
  Paradigm e imputación de horas, más la sección de autoevaluación.
