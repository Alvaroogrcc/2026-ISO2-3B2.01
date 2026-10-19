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

## D-002 · Estructura Maven multimódulo
Fecha: 19/10 · It. 0 · Acta: 19/10 · Issue: #12
Contexto: hay dos aplicaciones, servidor y
cliente, en un mismo repositorio.
Decisión: pom padre radiodiagnostico con dos
módulos: servidor y cliente.
Alternativas: dos repositorios; un solo módulo.
Motivo: un mvn verify construye todo y las
versiones se fijan una vez en el padre.
Consecuencias: cada módulo con su versión; al
cambiar la del padre, actualizar <parent>.
