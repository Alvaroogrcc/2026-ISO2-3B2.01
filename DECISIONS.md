## D-001 · REUNION 1 TOMA DE DECISIONES

Fecha: 11/10 · Acta 1

### 1. Herramientas e Inteligencia Artificial
* **Herramientas de desarrollo y diseño:**
  * **Código:** Visual Studio Code.
  * **Casos de uso:** Visual Paradigm.
* **Inteligencia Artificial:**
  * Uso principal de **OpenCode** y **GitHub Copilot**.

### 2. Planificación de Reuniones
* **Fecha y Hora:** Reuniones semanales todos los **miércoles a las 10:00**.

### 3. Definición de Roles del Equipo
Roles asignados a los miembros del equipo en la primera reunion:

| Miembro | Rol |
| :--- | :--- |
| **Marcelino Izquierdo Molina** | Analista de requisitos |
| **Víctor Bravo Rojo** | Arquitecto o diseñador de interfaces |
| **Álvaro García Cámara** | Coordinador |
| **Raúl Arroyo Cañadas** | Responsable de calidad y pruebas |
| **Juan Zopeque Ginés** | Responsable de IA |
| **Abraham Escalona Seco de Herrera** | Responsable de gestión de configuración |

## D-002 · ASIGNACIÓN DE ESFUERZO Y CASOS DE USO

Fecha: 08/11/2026

### 1. Distribución del esfuerzo por prioridad

Se ha establecido el esfuerzo total para cada nivel de prioridad, con un máximo de 40 horas por semana de trabajo:

- **Prioritarios:** 480 horas 
- **Altos:** 240 horas 
- **Medios y bajos:** 240 horas 

El esfuerzo total del proyecto es de **960 horas** .

### 2. Criterios de asignación de horas

Las horas de cada caso de uso se han estimado según su complejidad técnica:

- **Prioritarios (7 casos de uso):** CU-09 (96h), CU-06 (84h), CU-13 (84h), CU-01 (72h), CU-12 (60h), CU-18 (48h), CU-17 (36h)
- **Altos (6 casos de uso):** CU-15 (54h), CU-10 (48h), CU-07 (42h), CU-02 (36h), CU-05 (30h), CU-19 (30h)
- **Medios y bajos (8 casos de uso):** CU-20 (39h), CU-16 (36h), CU-21 (33h), CU-14 (30h), CU-08 (27h), CU-04 (27h), CU-03 (24h), CU-11 (24h)

### 3. División en equipos

Para equilibrar la carga de trabajo, los casos de uso se han dividido en dos grupos de **480 horas** cada uno:

**Equipo 1 – Raúl, Abraham, Marcelino (480h):**
- Prioritarios: CU-09 (96h), CU-06 (84h), CU-12 (60h) → 240h
- Altos: CU-15 (54h), CU-02 (36h), CU-05 (30h) → 120h
- Medios/bajos: CU-16 (36h), CU-21 (33h), CU-08 (27h), CU-03 (24h) → 120h

**Equipo 2 – Juan, Álvaro, Víctor (480h):**
- Prioritarios: CU-13 (84h), CU-01 (72h), CU-18 (48h), CU-17 (36h) → 240h
- Altos: CU-10 (48h), CU-07 (42h), CU-19 (30h) → 120h
- Medios/bajos: CU-20 (39h), CU-14 (30h), CU-04 (27h), CU-11 (24h) → 120h

### 4. Asignación por persona

Cada caso de uso tiene asignadas **3 personas** del mismo equipo. La carga de trabajo se distribuye equitativamente dentro de cada equipo, de modo que cada miembro tiene aproximadamente **160 horas** asignadas.

Todos los títulos de los issues en GitHub han sido actualizados con el formato: `CU-XX – Nombre (módulo vX.Y.Z) [Xh]` para reflejar el esfuerzo estimado.

## D-003 · ITERACIÓN 0: MÓDULOS, INTERFACES Y COMUNICACIÓN CLIENTE-SERVIDOR

Fecha: 20/11/2026

### 1. Organización de módulos Maven

El enunciado establece **1 CDU : 1 iteración** y **un módulo (componente Maven) por iteración**. Dada la arquitectura **cliente-servidor** (aplicaciones de escritorio, móvil y web), se mantienen dos módulos Maven físicos en el repositorio:

- `servidor`: dominio, repositorios, servicios, controladores y contratos transversales (auditoría, seguridad/SSO).
- `cliente`: controladores de presentación, vistas y cliente de comunicación con el servidor.

**Justificación:** la separación física refleja el despliegue real y el mínimo acoplamiento. El incremento de cada iteración se materializa como evolución versionada y trazable de estos módulos (paquetes/contratos asociados al CDU), siguiendo SemVer y Git Flow. Si más adelante se requiere empaquetado independiente por incremento, se evaluará la extracción a submódulos sin romper esta separación.

### 2. Interfaces base y contratos transversales

Se aplica **programación orientada a interfaces** en las tres capas:
- **Servidor**: `*Repository`, `*Service` y `*Controller` definidos como interfaces, con implementaciones mínimas (`*RepositoryMemoria`, `*ServiceImpl`, `*ControllerImpl`) que delegan/almacenan lo necesario para compilar y probar (sin lógica de negocio de CU). Métodos base definidos según lo conocido (p. ej. `findById`, `save`, `findAll`).
- **Cliente**: `ApiClient` y `*View` definidos como interfaces (para permitir implementaciones por plataforma: escritorio, móvil, web), con implementaciones mínimas (`ApiClientRest`, `*ViewImpl`); `*Controller` de cliente como interfaz con implementación mínima que depende de `ApiClient` y vistas por interfaz.

**Contratos transversales (uso común a varios CU):** `auditoria.Auditoria` con `AuditoriaStub`, y `seguridad.ServicioAutenticacionSSO`, `seguridad.Usuario` y `seguridad.Rol` (modelo de usuario y roles, RNF-SEG-01).

Las **clases de datos** existentes (`Cita`, `Paciente`, `Doctor`, `Persona` y DTOs de cliente) se tratan como modelos sin lógica y no se modifican en esta iteración.

**Gobierno de interfaces:** añadir métodos nuevos es compatible con la evolución por CU (p. ej. `reprogramarCita(...)` en un CU futuro), pero cambiar o eliminar firmas rompe a otros equipos; cualquier cambio de interfaz debe ser revisado por el responsable de diseño de interfaces (Víctor).

### 3. Comunicación cliente-servidor

- El **cliente** no accede directamente a clases del servidor: se comunica a través de la interfaz `ApiClient` (abstracción del canal) intercambiando **DTOs** (`*DTO`).
- La implementación prevista es una **API remota (p. ej. REST/HTTP)** expuesta por el módulo `servidor`; `ApiClientRest` encapsula el punto de acceso (p. ej. `baseUrl`) y en iteraciones posteriores incorporará las llamadas reales a la red. Las vistas se mantienen como interfaces para desacoplar la UI concreta de cada plataforma.
- El **servidor** expone la funcionalidad mediante sus interfaces de controlador/servicio sobre el modelo de dominio; la adaptación modelo↔DTO se realizará en la frontera cuando se concrete la API. Se pospone un módulo común de contratos/DTOs hasta definir el contrato de API/serialización para evitar acoplamiento prematuro.
