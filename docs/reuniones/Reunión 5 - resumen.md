# Reunión 5 — Finalizar planificación y primer entregable

**Fecha:** 09/11/2026  
**Duración aproximada:** 1 hora
**Asistentes:** Álvaro García Cámara, Raúl Arroyo Cañadas, Marcelino Izquierdo Molina, Abraham Escalona Seco de Herrera, Víctor Bravo Rojo y Juan Zopeque Ginés.  
**Objetivo:** terminar las tablas de planificación, ajustar el presupuesto, preparar la comparativa con y sin IA y avanzar el documento de la primera entrega.

## 1. Estado de la planificación

- Se retoma la reunión del día anterior. El acta de ayer ya está subida.
- Las tablas de las iteraciones 1, 2 y 3 están hechas, pero desactualizadas y todavía no están completas en el repositorio.
- Falta la **tabla general** de la planificación, que debe incluir las iteraciones, las fases, las horas y los costes.
- Se acuerda pasar las tablas a la **wiki en Markdown**, en lugar de mantenerlas solo en Excel. La wiki queda como fuente principal porque es más cómoda de consultar y mantiene la documentación junta.
- Hay que añadir a la wiki los **grupos de trabajo** y una breve descripción de cada uno. Actualmente solo están en el apartado de decisiones, no en la wiki.
- Los grupos son:
  - **Grupo 1:** Raúl, Abraham y Marcelino.
  - **Grupo 2:** Álvaro, Juan y Víctor.
- Los grupos se crearon para equilibrar la carga de trabajo. Todavía no se concreta un reparto fijo de cliente/servidor.
- En la wiki aparecen los requisitos, pero los casos de uso están en los **issues** de GitHub. Hay que tenerlo en cuenta para no duplicar información.
- También faltan los **requisitos no funcionales**, que hay que añadir.

## 2. Tablas, iteraciones y presupuesto

Se revisan las tablas de planificación y se acuerda esta estructura:

| Fase / iteración | Duración | Esfuerzo | Coste |
|---|---|---|---|
| **Iteración 0 — Inicio** | Ya realizada | 40 h | 1.000 € |
| **Iteración 1 — Elaboración** (casos de uso prioritarios) | 2 semanas / 10 días | 480 h | 12.000 € |
| **Iteración 2 — Construcción** (casos de uso altos) | 1 semana / 5 días | 240 h | 6.000 € |
| **Iteración 3 — Construcción** (casos de uso medios y bajos) | 1 semana / 5 días | 240 h | 6.000 € |
| **Iteración N — Transición / cierre** | 2 días | 80 h | 2.000 € |

- Se fija un precio de **25 €/h**, siguiendo el ejercicio de clase.
- La iteración 0 ya está hecha y se mantiene con un coste asumido de **1.000 €**.
- La iteración 1 son **480 h** (2 semanas × 40 h/semana × 6 personas) y **12.000 €**.
- Las iteraciones 2 y 3 son de **240 h** cada una (1 semana × 40 h/semana × 6 personas) y **6.000 €** cada una.
- Se añade la **iteración N** porque el enunciado pide una última iteración en la fase de transición con tareas de cierre: integración, pruebas de integración, despliegue, manual de usuario, etc.
- La iteración N se estima en **2 días, 80 h y 2.000 €**.
- Se acuerda que la tabla general incluya las fases de **inicio, elaboración, construcción y transición**. Para que sea compatible con Markdown, se simplifica la estructura y se agrupan las iteraciones 2 y 3 dentro de la construcción, evitando celdas combinadas.
- Se ajusta la fecha de fin: la tabla se hace para terminar el **domingo 6 de diciembre**, dejando margen hasta el **9 de diciembre** y hasta la entrega del **15 de diciembre**. La idea es terminar el trabajo real hacia el **4 de diciembre** y usar los últimos días para el cierre y el despliegue simulado.
- Se mantienen las tres tablas de iteración, con las fases de requisitos, análisis, diseño, implementación y pruebas, más la tabla general.

## 3. Comparativa con IA y sin IA

- El enunciado pide comparar el trabajo humano con el uso de agentes de IA y cuantificar cuánto reduce el tiempo de implementación y cómo afecta a los costes.
- Se acuerda duplicar la planificación para crear una versión **sin agentes de IA**.
- El criterio no es duplicar todas las horas: se duplican únicamente la **implementación y las pruebas**, porque los requisitos, el análisis y el diseño se consideran trabajo humano según el enunciado.
- Si se duplicaran todas las fases, el proyecto se iría a enero y la planificación dejaría de ser realista.
- La tabla “sin IA” se hará aparte y no se modificará la tabla principal, que es la que se usará para el proyecto.

## 4. Plan de calidad y requisitos no funcionales

- Álvaro y Marcelino se encargan del **plan de gestión de calidad**.
- Hay que decidir si se usa la norma **ISO/IEC 25010 de 2011 o la de 2023**. Se comenta que lo lógico sería usar la más actual, pero antes hay que comprobar qué exige el enunciado y qué características se van a poder justificar.
- El plan debe identificar y justificar las **características de calidad** que se consideran críticas para el producto.
- Se añadirán **tres características de calidad** a la nueva planificación, partiendo del trabajo que ya hicieron Juan, Álvaro y Marcelino.
- Hay que redactar también los **requisitos no funcionales**, que todavía no están hechos.

## 5. Documento de la primera entrega

- El documento está en **Teams**, en el apartado de archivos, dentro de “Trabajos de teoría uno”.
- Reparto de trabajo:
  - **Víctor:** apartado 2 (planificación) y apartado 3 (presupuesto). Debe traer el presupuesto que ya se hizo.
  - **Álvaro y Marcelino:** apartado C, centrado en el plan de calidad.
  - **Abraham:** revisar la coherencia el documento entregabl que empezó ayer y apoyar en C1/C2.
  - **Raúl:** transcripciones y resúmenes.
- Los apartados **B1, B2 y B3** ya están hechos; solo hay que cambiar algunos detalles.
- El apartado 4 se hará en clase o en el laboratorio.
- Antes de cerrar el documento hay que revisar que las tablas, las horas, los costes y las fechas cuadren entre sí.

## 6. Repositorio, actas y transcripciones

- Se acuerda crear una carpeta **`docs/actas`** dentro del repositorio para guardar las actas y las transcripciones.
- Las actas y transcripciones se subirán desde una rama creada a partir de `develop` (por ejemplo, `docs/subir-actas`) y después se hará merge a `main`.
- El acta de la reunión de ayer ya está subida; falta subir la de esta reunión.
- La transcripción automática de Teams requiere una licencia premium, así que no se puede usar. Se subirán los textos y los resúmenes.
- Raúl se encarga de las transcripciones y de sus resúmenes.

## 7. Diagramas de casos de uso

- Hay que hacer los **diagramas de casos de uso en Visual Paradigm**.
- Se duda si hay que hacer un diagrama por cada caso de uso o uno general; se acuerda preguntarlo en clase o en el laboratorio.
- Los diagramas se irán haciendo según se avance con los casos de uso.

## 8. Acuerdos

1. Actualizar las tablas de planificación en la wiki en Markdown: tres tablas de iteración más la tabla general.
2. Añadir a la wiki los grupos de trabajo y una breve descripción de cada uno.
3. Incluir la iteración N de transición con 2 días, 80 h y 2.000 €.
4. Fijar el precio de **25 €/h** y los costes de 1.000 €, 12.000 €, 6.000 €, 6.000 € y 2.000 € para las iteraciones 0, 1, 2, 3 y N.
5. Ajustar el cierre al 6 de diciembre, con margen hasta el 9 y hasta la entrega del 15.
6. Hacer la comparativa sin IA duplicando solo implementación y pruebas.
7. Elaborar el plan de calidad con ISO/IEC 25010 y añadir los requisitos no funcionales.
8. Repartir el documento: Víctor planificación y presupuesto; Álvaro y Marcelino calidad; Abraham revisión; Raúl transcripciones.
9. Crear `docs/actas` y subir actas y transcripciones mediante una rama y una pull request.
10. Hacer los diagramas de casos de uso en Visual Paradigm y preguntar el formato en clase.

## 9. Riesgos y observaciones

- Las fechas son ajustadas y coinciden con exámenes, festivos y posibles puentes.
- Duplicar todas las fases para la versión sin IA haría que el proyecto se fuera a enero; hay que duplicar solo implementación y pruebas.
- Las tablas pueden quedar incoherentes si no se revisan después de cada cambio.
- El documento de calidad puede incluir funcionalidades que no se van a implementar; hay que revisarlo.
- Hay que evitar mantener dos versiones distintas de las tablas (Excel y wiki) para no trabajar con datos desactualizados.
- La transcripción automática de Teams no está disponible sin premium; se usarán textos y resúmenes.

## 10. Próxima reunión

No se fija una reunión formal nueva. Se continuará en clase o en el laboratorio, se terminará el documento y se revisará la pull request por WhatsApp.

---

*Resumen elaborado a partir de una transcripción automática. Puede contener errores de reconocimiento de voz o de nombres.*
