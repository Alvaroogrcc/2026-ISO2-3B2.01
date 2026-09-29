# DECISIONS.md · Decisiones arquitecturales (ADR)

Registro de decisiones arquitecturales del Sistema Integral de Gestión de
Radiodiagnóstico e Imagen Médica (SESCAM) — 2026-ISO2-3B2.01.

Este documento es un **registro de decisiones** en formato ADR
(*Architecture Decision Record*). Cada decisión se numera, se fecha y **no se
reescribe**: si cambia, se marca como `Sustituida` y se añade una ADR nueva que
la referencia.

**Estado del documento:** Iteración 0 (fase de Inicio).
**Última actualización:** 2026-10-19

---

## Formato y ciclo de vida

| Campo | Contenido |
| --- | --- |
| **ID** | `ADR-NNN`, correlativo y nunca reutilizado |
| **Título** | Enunciado breve de la decisión |
| **Estado** | `Propuesta` · `Aceptada` · `Sustituida por ADR-XXX` · `Rechazada` |
| **Contexto** | El problema y las fuerzas en juego |
| **Decisión** | Qué se decide, en una frase afirmativa |
| **Justificación** | Por qué esta opción y no las alternativas |
| **Alternativas consideradas** | Opciones descartadas y su motivo |
| **Consecuencias** | Efectos positivos, negativos y neutros |
| **RADIT** | Disciplinas de la ISO2 afectadas |
| **Contexto de software** | Sección de `CONTEXT.md` relacionada |

---

## ADR-001 · Módulo `nucleo` compartido y regla de dependencia entre componentes

- **Estado:** Aceptada
- **Fecha:** 2026-10-19
- **RADIT:** Arquitectura, Diseño
- **Contexto de software:** `CONTEXT.md` §5.3 y §6

**Contexto.** El enunciado obliga a dividir el sistema en módulos con máxima
coherencia y mínimo acoplamiento, y a programar orientado a interfaces. Pero sin
un punto común, cada componente acaba redefiniendo los conceptos transversales
(identificadores, errores, tipos de fecha/hora) y el `mvn verify` falla al
integrar.

**Decisión.** Se crea un módulo `radiodiagnostico-nucleo` que no depende de
ningún dominio concreto, y se establece la siguiente **regla de dependencia**:
los componentes (`catalogo`, `pacientes`, `agenda`, `solicitudes`, `informes`,
`gestion`, `integracion`) **dependen del núcleo**, nunca al revés, y **nunca
dependen entre sí directamente**; la comunicación entre componentes se hace
exclusivamente a través de **interfaces** publicadas en el núcleo o mediante
mensajería.

**Justificación.** El núcleo es el único módulo cuyo ciclo de cambios puede
justificarse de forma independiente del resto: es barato de versionar, evita la
duplicación de conceptos y hace imposible la formación de ciclos entre
componentes, que es la causa habitual de los problemas de integración en
proyectos multimódulo.

**Alternativas consideradas.**

| Alternativa | Motivo del descarte |
| --- | --- |
| Un único módulo con todo el código | Violaría la coherencia mínima; el enunciado exige un componente por iteración |
| Cada componente repite sus tipos comunes | Duplicación de dominio; refactoring posterior caro |
| Dependencias directas entre componentes (`agenda` → `solicitudes`) | Acoplamiento directo: un cambio de contrato de un componente rompe la compilación de todos los dependientes |

**Consecuencias.**

- Positivas: mínimo acoplamiento, contratos estables, SOLID respetado.
- Negativas: los conceptos transversales deben descubrirse pronto o
  Surgirán duplicados en el núcleo. Se mitiga con revisión arquitectónica.
- Neutras: el núcleo crece lentamente; hay que vigilar que no absorva lógica de
  dominio (sería una señal de que el módulo es demasiado ancho).

---

## ADR-002 · Un componente (módulo Maven) por iteración, sin módulos por capa

- **Estado:** Aceptada
- **Fecha:** 2026-10-19
- **RADIT:** Arquitectura
- **Contexto de software:** `PLAN.md` §1 y §4

**Contexto.** El enunciado fija 1 CDU : 1 Iteración y 1 iteración = 1 componente
Maven. Queda por decidir si «componente» significa un módulo por *caso de uso*,
un módulo por *subsistema* o un módulo por *capa técnica* (`dominio`,
`aplicación`, `infraestructura`).

**Decisión.** El componente = **subsistema funcional** (catálogo, pacientes,
agenda, solicitudes, informes, gestión, integración), y **dentro** de él se
aplican las capas `dominio` / `aplicacion` / `infraestructura` como paquetes, no
como módulos Maven adicionales.

**Justificación.** Un módulo por capa multiplicaría por cuatro el número de
módulos y dejaría la funcionalidad troceada entre varios artefactos, lo que
contradice «máxima coherencia». Un módulo por CDU produciría decenas de
artefactos diminutos. El subsistema funcional es el único corte que da
módulos coherentes y en número manejable.

**Alternativas consideradas.**

| Alternativa | Motivo del descarte |
| --- | --- |
| Un módulo Maven por capa técnica | Duplicaría el número de módulos y fragmentaría la funcionalidad |
| Un módulo Maven por CDU | Módulos diminutos, sobrecarga de versionado y compilaciones muy lentas |

**Consecuencias.**

- Positivas: un componente = un artefacto publicable y versionable de forma
  independiente; se puede estancar la iteración sin bloquear al resto.
- Negativas: los paquetes por capa contienen clases de varias iteraciones
  distintas; se compensa con la convención de nombres de `CONTEXT.md` §6.
- Neutras: el `pom.xml` padre crece en cada iteración; es el punto donde se
  ve la estructura del sistema.

---

## ADR-003 · Tarifa única de 50 €/h y coste de 300 h / 15 000 €

- **Estado:** Aceptada
- **Fecha:** 2026-10-19
- **RADIT:** Requisitos, Análisis
- **Contexto de software:** `PLAN.md` §1

**Contexto.** El enunciado da 1 000 € para la Iteración 0 y 2 000 € para cada
Iteración *N*, y fija 40 h laborables por semana de consultant, pero no da la
tarifa horaria.

**Decisión.** Se adopta una **tarifa única de 50 €/h** para todo el proyecto. En
consecuencia, la Iteración 0 cuesta 20 h y cada iteración posterior cuesta 40 h
(1 semana). Con 7 iteraciones, el total es **300 h y 15 000 €**.

**Justificación.** `2000 / 40 = 50` es la única tarifa compatible con las dos
suposiciones de coste a la vez; usar tarifas distintas por iteración
introduciría un incoherente artificial en la planificación económica que
debería justificarse, y el enunciado no da margen para justificarlo.

**Alternativas consideradas.**

| Alternativa | Motivo del descarte |
| --- | --- |
| 25 €/h en la Iteración 0 y 50 €/h en el resto | Tarifa distinta entre iteraciones sin justificación en el enunciado |
| Expresar el coste solo en horas | El enunciado exige el coste final en euros, no solo en horas |

**Consecuencias.**

- Positivas: coste, agenda y horas son coherentes entre sí; la planificación
  económica es auditable.
- Negativas: si el profesor fija otra tarifa, el total en euros cambia y hay
  que reestirar. Las **horas no cambian**, por eso se planifica en horas y se
  convierte a euros al final.
- Neutras: el reparto de 300 h entre 6 miembros es de 50 h de media, lo que
  se ajusta a la carga real de un TFG de ingenuity.

---

## ADR-004 · Ramas Git Flow estrictas y `main` protegida

- **Estado:** Aceptada
- **Fecha:** 2026-10-19
- **RADIT:** Diseño, Implementación
- **Contexto de software:** `README.md` § Gestión de configuración: Git Flow

**Contexto.** El enunciado fija Git Flow y exige que nada entre en `main` sin
pull request aprobado.

**Decisión.** `main` y `develop` son ramas de larga vida; `feature/*` nace
siempre de `develop`, `release/*` nace de `develop`, y `hotfix/*` nace de
`main`. `main` se protege en GitHub con: PR obligatorio, al menos una
aprobación, con prohibición de borrar la rama y de hacer *force push*.

**Justificación.** Sin protección de rama, la regla "nada entra en `main` sin
PR" es una recomendación que se incumple en cuanto hay prisa. La protección se
configura en el servidor, no en la costumbre.

**Alternativas consideradas.**

| Alternativa | Motivo del descarte |
| --- | --- |
| Trunk-based development | Contradice el Git Flow exigido por el enunciado |
| Ramas por persona | El commit unitario no permite revisión cruzada ni trazabilidad a un CDU |

**Consecuencias.**

- Positivas: trazabilidad completa desde el CDU hasta la release; el historial
  es legible y auditable.
- Negativas: más trabajo de integración; los conflictos deben resolverse en
  `develop` y no en `main`.
- Neutras: se requiere disciplina del equipo para no hacer `merge` directos.

---

## ADR-005 · Contrato primero, implementación después (los agentes implementan)

- **Estado:** Aceptada
- **Fecha:** 2026-10-19
- **RADIT:** Análisis, Diseño, Implementación
- **Contexto de software:** `CONTEXT.md` §7 y `README.md` §Metodología

**Contexto.** El proyecto usa agentes de IA como fuerza de ejecución. Si el
agente decide el modelo de dominio, el resultado es inconsistente y no
revisable.

**Decisión.** El flujo obligatorio es: **análisis humano → contrato (interfaz)
públicado en la wiki → test → implementación por el agente**. Un agente que
encuentre un CDU no descrito en la wiki **se detiene y pregunta**; no inventa
vocabulario ni reglas de negocio.

**Justificación.** El enunciado lo dice literalmente: «el ingeniero diseña las
interfaces; la IA implementa los contratos». Además, el vocabulario del dominio
sanitario no es adivinable, y un término inventado contamina el modelo de forma
irreversible.

**Alternativas consideradas.**

| Alternativa | Motivo del descarte |
| --- | --- |
| Dejar que el agente proponga el modelo y luego revisarlo | La revisión llega tarde: el código ya está escrito sobre supuestos |
| Escribir el modelo en el propio repositorio sin publicarlo en la wiki | El enunciado exige la wiki como guía de lectura de la documentación |

**Consecuencias.**

- Positivas: coherencia, revisabilidad y responsabilidades separadas.
- Negativas: más pasos y más latencia; se compensa con plantillas (patrón
  CDU en `PLAN.md` §3).
- Neutras: se mantiene un registro de **preguntas abiertas** en `CONTEXT.md`
  §8 que bloquea la implementación de los CDU afectados.

---

## ADR-006 · Sin framework ORM en el núcleo: acceso por interfaces de repositorio

- **Estado:** Aceptada
- **Fecha:** 2026-10-19
- **RADIT:** Arquitectura, Diseño
- **Contexto de software:** `CONTEXT.md` §5.3

**Contexto.** La infraestructura de persistencia no está fijada por el
enunciado, y las integraciones con HCE, RIS/PACS y SSO pueden no estar
disponibles en el laboratorio.

**Decisión.** El núcleo y la capa de dominio definen **interfaces de
repositorio**; la tecnología concreta (JPA, JDBC, HL7/FHIR, doble de prueba)
se decide en la capa de infraestructura y puede sustituirse sin tocar el
dominio.

**Justificación.** Con las integraciones reales probablemente no disponibles en
el entorno (riesgo R4 de `PLAN.md` §11), un acoplamiento a una tecnología
concreta dejaría el proyecto sin poder probarse. Además, un dominio de datos
clínicos necesita que la trazabilidad y la auditoría estén en los contratos, no
en la tabla.

**Alternativas consideradas.**

| Alternativa | Motivo del descarte |
| --- | --- |
| JPA directo en la capa de dominio | Acopla el dominio a la persistencia; impide usar dobles de prueba |
| Elegir la base de datos en Inicio | Sin conocer el entorno real del hospital es una decisión prematura |

**Consecuencias.**

- Positivas: dominio testeable sin base de datos; sustituciones de tecnología
  baratas.
- Negativas: más interfaces que mantener; se mitiga con generadores o clases
  base sólo si el volumen lo justifica.
- Neutras: la elección definitiva de base de datos se difiere a la iteración de
  integración y se registrará aquí como ADR nueva.

---

## ADR-007 · Integraciones externas mediante contrato y doble de prueba

- **Estado:** Aceptada
- **Fecha:** 2026-10-19
- **RADIT:** Diseño, Implementación, Pruebas
- **Contexto de software:** `CONTEXT.md` §5.2

**Contexto.** SSO corporativo, HCE, RIS/PACS, DICOM, HL7 y FHIR son
integraciones externas que probablemente no están disponibles en el laboratorio
(riesgo R4 de `PLAN.md` §11).

**Decisión.** Cada integración se define por una **interfaz** (contrato) y se
implementa **contra el contrato** con un doble de prueba. La lógica de negocio
de la integración **nunca** vive dentro de la integración: vive en el dominio.

**Justificación.** Es la forma de que las iteraciones 5 y 6 (informes e
imagen, gestión e integración) se puedan desarrollar y demostrar de extremo a
extremo sin depender de un hospital real, y de que los tests sean
deterministas.

**Alternativas consideradas.**

| Alternativa | Motivo del descarte |
| --- | --- |
| Integrar directamente contra un PACS real | No disponible en el laboratorio; bloquea la iteración |
| Omitir las integraciones y simular con `System.out` | El contrato es lo que se evalúa; la lógica debe quedar en el dominio |

**Consecuencias.**

- Positivas: el sistema es demostrable y testeable; sustituir el doble por la
  integración real es un cambio de implementación, no de diseño.
- Negativas: el doble puede no reproducir fielmente el comportamiento real;
  se documenta la divergencia en la wiki.
- Neutras: quedan pendientes P1–P5 de `CONTEXT.md` §8.

---

## ADR-008 · Refactoring en commit propio, con la suite en verde antes y después

- **Estado:** Aceptada
- **Fecha:** 2026-10-19
- **RADIT:** Diseño, Implementación, Pruebas
- **Contexto de software:** `PLAN.md` §9.3 y §10

**Contexto.** El enunciado exige un Plan de Gestión de la Calidad que incluya
el papel del refactoring, y las MÉTRICAS de calidad de `PLAN.md` §9.1.

**Decisión.** Todo refactoring se entrega en un **commit propio**, de tipo
`refactor(<componente>): <motivo>`, nunca mezclado con funcionalidad nueva. La
suite de pruebas debe estar en verde **antes y después**; el PR que lo contiene
lo demuestra, y es lo que prueba que el comportamiento no ha cambiado.

**Justificación.** Un refactoring mezclado con funcionalidad es imposible de
revisar y destruye la evidencia de calidad. La condición de «verde antes y
después» convierte el refactoring en un argumento verificable de la calidad del
código, que es exactamente lo que pide el plan de calidad.

**Alternativas consideradas.**

| Alternativa | Motivo del descarte |
| --- | --- |
| Refactoring libre dentro del commit de funcionalidad | Sin evidencia de que el comportamiento se conserva |
| Refactoring solo al final del proyecto | Acumula deuda de diseño; imposible de refactorizar sin red de seguridad |

**Consecuencias.**

- Positivas: historial legible; el refactoring queda evaluable.
- Negativas: PRs algo más largos; se acepta como coste de la trazabilidad.
- Neutras: se prioriza eliminar `Duplicated Code` y el acoplamiento entre
  componentes.

---

## Plantilla de ADR nueva

```markdown
## ADR-0NN · Título de la decisión

- **Estado:** Propuesta
- **Fecha:** AAAA-MM-DD
- **RADIT:** Requisitos, Análisis, Diseño, Implementación, Pruebas
- **Contexto de software:** `CONTEXT.md` §X

**Contexto.** ...

**Decisión.** ...

**Justificación.** ...

**Alternativas consideradas.**

| Alternativa | Motivo del descarte |
| --- | --- |
| ... | ... |

**Consecuencias.**

- Positivas: ...
- Negativas: ...
- Neutras: ...
```
