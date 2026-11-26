# Reunión 3 — Continuación de casos de uso y requisitos funcionales

**Fecha:** 19/10/2026   
**Duración aproximada:** 22 minutos  
**Asistentes:** Álvaro García Cámara, Raúl Arroyo Cañadas, Marcelino Izquierdo Molina, Abraham Escalona Seco de Herrera, Víctor Bravo Rojo y Juan Zopeque Ginés.

## Objetivo

Revisar los requisitos funcionales ya redactados, asignarles prioridad y empezar a convertir los prioritarios en casos de uso para la planificación.

## 1. Criterio de prioridad

Se acuerda una escala de prioridad:

- **Prioritaria / muy alta:** funcionalidad básica e imprescindible para la primera versión y para construir el resto del sistema. Define la línea base.
- **Alta:** funcionalidad importante, pero no imprescindible para el arranque.
- **Media:** funcionalidad útil que puede llegar en iteraciones posteriores.
- **Baja:** funcionalidad secundaria o de mejora.

Las funcionalidades prioritarias serán las que se implementen primero y las que formen la **línea base** del producto.

## 2. Priorización de requisitos

| Requisito funcional (según la transcripción) | Prioridad acordada | Observaciones |
|---|---|---|
| Paciente: consultar y gestionar sus citas | **Prioritaria** | Es la funcionalidad básica de la aplicación. |
| Paciente: consultar su historial | **Media** | Se debate entre media y alta; se deja media. |
| Paciente: reportar incidencias | **Baja** | Puede añadirse más adelante. |
| Paciente: consultar información y recomendaciones sobre pruebas de seguimiento y revisiones | **Alta** | La consulta de información se considera básica; las recomendaciones, menos. |
| Paciente: recibir notificaciones y recordatorios | **Alta, no prioritaria** | Es útil, pero el paciente puede recordar la cita por otros medios. |
| Médico: solicitar pruebas diagnósticas con indicación clínica y justificación de prioridad | **Prioritaria** | Se considera parte del núcleo del sistema. |
| Médico: programar pruebas de seguimiento periódicas y generar automáticamente la necesidad de futura cita | **Alta / pendiente** | Se debate si es prioritaria; la automatización se ve como un extra. Se marca con asterisco para preguntar. |
| Médico: consultar estado de tramitación de solicitudes y disponibilidad de agenda | **Media** | Se marca con asterisco; hay dudas sobre su prioridad real. |
| Médico: crear, editar y consultar informes clínicos y acceder a imágenes médicas asociadas | **Prioritaria** | Se considera fundamental para el trabajo médico. |
| Médico: registrar la realización efectiva de la prueba y documentar incidencias | **Alta** | Importante, pero no imprescindible para el arranque. |
| Médico: consultar estadísticas sobre solicitudes, tiempos de espera, uso de recursos y adherencia | **Media** | Se debate entre media y baja; se deja media. |
| Personal administrativo: gestionar el ciclo de vida de los pacientes | **Prioritaria** | Alta y baja de pacientes; se considera esencial. |
| Personal administrativo: gestionar citas (asignar, reprogramar, cancelar) | **Prioritaria** | Es una de las funciones principales del sistema. |
| Personal administrativo: configurar y gestionar las agendas de los médicos | **Media / alta** | Se debate; se deja en media-alta y se pregunta al profesor. |
| Personal administrativo: gestionar listas de espera, prioridades y asignación de huecos | **Alta** | Se considera importante para la organización del hospital. |
| Personal administrativo: generar informes operativos de ocupación, tiempos de espera y cancelaciones | **Baja** | No es funcionalidad básica. |
| Administrador: configurar parámetros del sistema (tipos de prueba, protocolos, políticas de citación) | **Prioritaria** | Necesario para que el sistema funcione correctamente. |
| Administrador: gestionar usuarios, roles y permisos de acceso | **Prioritaria** | Seguridad y control de acceso. |
| Administrador: registrar pistas de auditoría de actividades y accesos a datos clínicos | **Alta** | Importante, pero se deja como alta, no prioritaria. |
| Sistema: integrarse con otros sistemas hospitalarios | **Media** | Primero debe funcionar la aplicación de forma independiente. |
| Sistema: generar estadísticas | **Baja** | Se puede añadir en fases posteriores. |

> Las prioridades marcadas con asterisco o con debate quedaron pendientes de confirmar con la profesora.

## 3. Casos de uso

- Se mantiene la idea de **un requisito funcional = un caso de uso = una iteración**, aunque más adelante se podrán agrupar casos de uso por prioridad.
- Se usarán los casos de uso prioritarios para la **iteración 0 / línea base**.
- Los casos de uso se irán creando a partir de la plantilla existente.
- Se propone usar **GitHub Issues** para cada caso de uso y etiquetas o flags para clasificarlos por prioridad (prioritaria, alta, media, baja). Así se podrán filtrar y ordenar.
- También se reflejarán en la wiki y en el tablero del proyecto.
- Se discute si conviene implementar primero por prioridad o por grupos de actores. Se concluye que la decisión final se tomará al hacer la planificación, valorando también qué agrupación requiere menos horas y qué aporta antes valor al cliente.
- Se propone que la iteración 0 incluya lo mínimo para que el sistema funcione; después se irán añadiendo los casos de uso de prioridad alta, media y baja.

## 4. Dudas y revisión con la profesora

Se acuerda llevar a la profesora las siguientes dudas:

- Si la priorización realizada es correcta.
- Cómo se deben agrupar los casos de uso en las iteraciones.
- Si la iteración 0 debe contener todos los casos de uso prioritarios o solo una parte.
- Cómo tratar los casos de uso que dependen de otros.
- Si el criterio de prioridad es suficiente para agrupar o hay que buscar relaciones funcionales entre casos de uso.

## 5. Acuerdos

1. Usar la escala prioritaria / alta / media / baja.
2. Considerar prioritarias las funcionalidades básicas que forman la línea base.
3. Crear un caso de uso por requisito funcional.
4. Reflejar los casos de uso en GitHub Issues y en la wiki.
5. Marcar con asterisco las prioridades dudosas y consultarlas con la profesora.
6. Dejar la planificación y la asignación de iteraciones para la siguiente reunión.
---

*Resumen elaborado a partir de una transcripción automática. Puede contener errores de reconocimiento de voz o de nombres.*
