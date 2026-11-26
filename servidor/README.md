# Módulo `servidor` — SESCAM

Dominio, reglas de negocio, persistencia en memoria y contratos del sistema integral
de gestión de radiodiagnóstico e imagen médica.

## Arranque

Requiere **Java 21** y **Maven**. Desde la raíz del repositorio:

```bash
mvn clean verify
mvn -pl servidor exec:java -Dexec.mainClass=es.sescam.servidor.ServidorMain
```

Si `exec-maven-plugin` no está configurado en tu entorno, ejecuta la clase
`es.sescam.servidor.ServidorMain` directamente desde el IDE. El `main` no abre
puertos ni espera peticiones: imprime una demostración por consola y termina.

## Qué hace el `main`

`ServidorMain` es el **único punto de composición** del módulo. En su constructor
crea, en este orden:

1. `AuditoriaStub`, `ControlAccesoBasico` y `ServicioAutenticacionSSOMemoria`.
2. Integraciones simuladas: `CatalogoPruebasStub`, `ServicioHCEStub`,
   `ServicioAdmisionStub`, `ServicioImagenesStub` y
   `ServicioAutorizacionClinicaStub`.
3. Repositorios `*RepositoryMemoria` (sin base de datos, sin datos reales).
4. Servicios `*ServiceImpl` inyectados por constructor.
5. Controladores `*ControllerImpl`, que son la puerta de entrada usada por la demo.
6. Usuarios ficticios con roles `ADMINISTRADOR_SISTEMA`, `MEDICO`,
   `ADMINISTRATIVO` y `PACIENTE`, y sus tokens SSO simulados.

La demostración recorre los casos de uso implementados:

| Etapa | Caso de uso | Controlador usado |
| --- | --- | --- |
| SSO y permisos | It. 0 / CU-18 | `ServicioAutenticacionSSO`, `ControlAcceso` |
| Alta de usuarios, roles y permisos | CU-18 | `GestionUsuariosController` |
| Alta, listado y búsqueda de pacientes | CU-12 | `PacienteController` |
| Alta y listado de médicos | CU-01 | `DoctorController` |
| Catálogo, protocolos, horarios, políticas y prioridades | CU-17 | `ParametrosMaestrosController` |
| Prescripción de pruebas y envío a HCE | CU-06 | `SolicitudPruebaController` |
| Creación, asignación, reprogramación y cancelación de citas | CU-07 | `CitaController` |
| Creación, firma, consulta e imágenes de informes | CU-09 | `InformeClinicoController` |

Al final se imprime el número de eventos de auditoría generados. Cada etapa está
aislada: si una falla, se muestra el mensaje y continúa la siguiente.

> Todos los datos son ficticios y anonimizados (DNI `0000000xA`, dominios
> `example.invalid`). No hay credenciales ni datos clínicos reales.

## Estructura de paquetes

```
es.sescam.servidor
├── admision/       Integración simulada con admisión (CU-12)
├── auditoria/      Auditoría de accesos a datos clínicos (RNF-SEG-02)
├── autorizacion/   Relación clínica simulada (CU-09)
├── catalogo/       Catálogo de pruebas simulado (CU-06)
├── controller/     Puerta de entrada; delega en servicios
├── error/          AccesoDenegado, EntidadNoEncontrada, ValidacionIncorrecta
├── hce/            Historia clínica electrónica simulada
├── imagenes/       Servicio de imágenes simulado (RIS/PACS)
├── model/          Entidades y enums del dominio (sin lógica)
├── repository/     Interfaces y implementaciones en memoria
├── seguridad/      Usuario, Rol, Permiso, ControlAcceso y SSO simulado
└── service/        Reglas de negocio y orquestación
```

## Reglas que cumple

- Las interfaces son `Repository`, `Service`, `Controller`, `View` y `Api`; las
  implementaciones se llaman `*Memoria`, `*Impl`, `*Stub` o `*Rest`.
- El controlador no accede a repositorios: solo delega en el servicio.
- El dominio no depende de frameworks ni de la capa de presentación.
- Toda operación comprueba permisos por rol y audita el acceso a datos clínicos.
- Las implementaciones concretas se eligen solo en `ServidorMain`.

## Estado y limitaciones

- No hay API HTTP: el borde REST queda pendiente de la decisión arquitectónica
  D-002/D-003 de `DECISIONS.md`. El módulo `cliente` usa hoy clientes REST
  simulados, sin red.
- Los estados de cita se gestionan como cadenas (`PENDIENTE`, `CONFIRMADA`,
  `CANCELADA`) para no alterar el modelo existente.
- La persistencia es volátil: al terminar el proceso se pierden los datos.

## Pruebas

```bash
mvn -pl servidor test
```

Las pruebas usan dobles de las interfaces y datos ficticios. No dependen de red,
base de datos ni de la demostración del `main`.
