# Reunión 4 — Planificación de CDU y asignación de tareas

**Fecha:** 8 de noviembre de 2026 (según la propia transcripción)  
**Asistentes:** Álvaro, Raúl, Marcelino, Abraham, Víctor y Juan.  
**Objetivo:** cerrar la planificación, estimar esfuerzos, repartir los casos de uso y preparar la entrega del 13 de noviembre.
**Duración aproximada:** 2 horas

## 1. Entregables y fechas

### Entrega del 13 de noviembre

Es una entrega **documental**, no de código. Debe incluir:

- La planificación del proyecto y las decisiones tomadas.
- El tipo de plan y las agrupaciones por prioridad (alta, media, baja).
- El precio por hora utilizado, tomado de los ejercicios de teoría.
- Los miembros del equipo y el uso de agentes de IA.
- El **plan de gestión de configuración**.
- El **plan de gestión de calidad**, con las características de calidad de ISO/IEC 25010 que se consideren críticas y su justificación.

### Entrega del 15 de diciembre

Debe estar completo todo lo del GitHub: código, wiki, issues, tablero, etc.

### Calendario

- La planificación debe coincidir con la actividad real del repositorio. Aunque el calendario inicial empezaba el 26 de octubre, se acuerda empezar a contar desde el **lunes 9 de noviembre**.
- Se descarta el calendario inicial de 30 días (hasta el 18 de diciembre) porque no encaja con la entrega del 15.
- Se acuerda terminar la planificación **el 4 de diciembre** (se barajó el 5), dejando margen antes de los exámenes, que empiezan el **17 de diciembre**.
- Se trabajará de **lunes a viernes**. Los fines de semana y festivos se consideran colchón de contingencia, no parte del plan. Si se trabaja alguno, se dejará constancia como decisión extraordinaria.
- Hay que tener en cuenta el festivo del **8 de diciembre** y posibles puentes.

## 2. Iteraciones y estimación

| Iteración | Contenido | Duración | Esfuerzo |
|---|---|---|---|
| **Iteración 0** | Puesta en marcha: arquitectura base, módulos Maven, interfaces, reglas para los agentes y esqueleto del proyecto. Parte ya está hecha. | Fase de inicio | Coste asumido de 1.000 € |
| **Iteración 1** | Casos de uso **prioritarios** (línea base) más la integración de la arquitectura base. | 2 semanas | **480 h** |
| **Iteración 2** | Casos de uso de prioridad **alta**. | 1 semana | **240 h** |
| **Iteración 3** | Casos de uso de prioridad **media y baja** agrupados. | 1 semana | **240 h** |

- Se manejan **7 casos de uso prioritarios, 6 altos, 4 medios y 4 bajos** (21 en total; podrían ser 24 si se desglosan algunas acciones).
- El total planificado es de **960 h**.
- Las 480 h de la iteración 1 equivalen a **2 semanas × 40 h/semana × 6 personas**.
- Las 240 h de las iteraciones 2 y 3 equivalen a **1 semana × 40 h/semana × 6 personas**.
- La arquitectura base **no es un caso de uso**: es trabajo transversal necesario antes de implementar los casos de uso. Se incluye en la iteración 0 y se integra en la iteración 1.

## 3. Reparto de trabajo y tablas

Cada caso de uso debe tener, como mínimo:

- Descripción.
- Actor principal.
- Prioridad.
- **Esfuerzo en horas**.
- Duración.
- Nivel de dificultad.

Se detecta que en GitHub faltaba el campo de esfuerzo y que no estaba asignado el esfuerzo a los casos de uso. Se crean tareas para corregirlo.

### Tablas acordadas

1. **Una tabla por iteración** (iteración 1, 2 y 3) con:
   - Casos de uso de esa iteración.
   - Fases: requisitos, análisis, diseño, implementación y pruebas.
   - Horas dedicadas por cada persona.
2. **Una tabla general** con las fases del proyecto, el esfuerzo y el coste total.
3. **Una tabla por prioridad o dificultad**, para reflejar que un caso de uso prioritario no tiene por qué ser más difícil, aunque sí deba hacerse antes.

### Asignación

- Cada caso de uso se asigna a **3 personas**, no a las 6, para que el reparto sea realista.
- Se forman **dos grupos de tres personas**. En la conversación se nombran Álvaro, Juan y Víctor por un lado, y Raúl, Abraham y Marcelino por otro; conviene confirmarlo en GitHub Projects.
- La aplicación será **cliente-servidor** y el trabajo se repartirá también 3 y 3 entre cliente y servidor.
- Cada persona trabaja 40 h/semana; el total de horas se reparte entre los miembros del grupo asignado.
- Todos los casos de uso deben quedar asignados en GitHub antes de cerrar la planificación.

## 4. GitHub y control de versiones

- Los casos de uso se gestionan como **issues** y se organizan en el **tablero de GitHub Projects**.
- Se crearán issues específicos para:
  - Asignar el esfuerzo a cada caso de uso.
  - Agrupar los casos de uso por iteración.
  - Corregir las horas y la planificación.
- Se usan ramas y **pull requests** con revisión antes de hacer merge.
- **No se deben borrar las ramas**.
- Hay que revisar bien las pull requests antes de aceptarlas; el error de las “12 semanas” pasó porque se aceptó sin leer con detalle.
- Se resolverán los conflictos antes de integrar.
- Se comentará en cada issue lo que se ha hecho, para dejar trazabilidad.
- Se propone cerrar un **release cada viernes** después de integrar los pull requests. Se recuerda la recomendación de **no hacer despliegues en viernes**.
- Hay que actualizar la wiki: planificación, equipos y roles, actas y resúmenes de las transcripciones.

## 5. Versiones

- Se acuerda que la primera versión buena será la **1.0**.
- A partir de ahí se irán añadiendo versiones menores: **1.1, 1.2**, etc.
- Para la correción de errores o elementos de menor imortancia: **1.0.1, 1.0.2**, etc.
- Si hay un cambio grande o se añaden muchas funcionalidades, se pasará a **2.0**.
- Cada componente puede tener su propia versión, pero también debe existir una versión global de la aplicación.
- El detalle completo del versionado se deja para la **siguiente reunión**.

## 6. IA y costes

- El enunciado pide una comparativa entre el trabajo humano y el uso de agentes de IA, cuantificando cuánto reduce el tiempo de implementación y cómo afecta a los costes.
- Se acuerda hacer primero la planificación **con IA**, que es lo que se va a usar, y después la versión **sin IA**.
- El código no es el objetivo principal de la entrega: lo importante es el GitHub, la planificación, el control de configuración y la documentación.
- Los agentes permiten reducir mucho el tiempo de implementación, pero el equipo debe revisar y validar el trabajo.

## 7. Calidad y configuración

- El plan de gestión de calidad se hará en la próxima reunión, junto con el control de configuración.
- Se usará la norma **ISO/IEC 25010** para seleccionar y justificar las características de calidad críticas.
- El plan de configuración debe recoger el uso de ramas, pull requests, releases, issues y control de versiones.

## 8. Acuerdos

1. Terminar la planificación el **4 de diciembre**, con margen antes de los exámenes.
2. Contar el calendario desde el **9 de noviembre** y trabajar de lunes a viernes.
3. Usar tres iteraciones: prioritarios (2 semanas), altos (1 semana) y medios/bajos (1 semana).
4. Asignar 480 h a prioritarios, 240 h a altos y 240 h a medios/bajos.
5. Asignar cada caso de uso a 3 personas y repartir el trabajo en dos grupos de tres.
6. Completar cada caso de uso con descripción, actor, prioridad, esfuerzo, duración y dificultad.
7. Corregir el error de las “12 semanas” y revisar las pull requests antes de aceptarlas.
8. No borrar ramas y dejar constancia del trabajo en los issues.
9. Hacer la comparativa con IA y sin IA.
10. Elaborar el plan de calidad y el plan de configuración en la siguiente reunión.

## 9. Riesgos y observaciones

- Los exámenes empiezan el 17 de diciembre y la entrega es el 15; conviene terminar antes.
- El festivo del 8 de diciembre y los posibles puentes reducen los días disponibles.
- Las clases y los laboratorios condicionan las reuniones.
- Hay que revisar las pull requests con atención para no arrastrar errores.
- Faltan datos de esfuerzo en GitHub y hay que completarlos antes de dar la planificación por cerrada.
- Se detectan actas antiguas sin subir; se decide no subir grabaciones que no aportan y subir la de esta reunión.

## 11. Próxima reunión

Se acuerda reunirse el **lunes 9 de noviembre a las 12:00** (se barajó también la 13:00) para terminar el documento Word, cerrar el versionado y avanzar con la calidad y la configuración.

---

*Resumen elaborado a partir de una transcripción automática. Puede contener errores de reconocimiento de voz o de nombres.*
