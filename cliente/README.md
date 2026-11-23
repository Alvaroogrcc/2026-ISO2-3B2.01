# Módulo `cliente` — SESCAM

Aplicación de escritorio (Swing) del sistema integral de gestión de
radiodiagnóstico e imagen médica.

## Arranque

Requiere **Java 21** y **Maven**. Desde la raíz del repositorio:

```bash
mvn clean verify
mvn -pl cliente exec:java -Dexec.mainClass=es.sescam.cliente.ClienteMain
```

También puedes ejecutar `es.sescam.cliente.ClienteMain` directamente desde el IDE.
Por defecto usa la URL base `http://localhost:8080`; se puede cambiar con el primer
argumento:

```bash
mvn -pl cliente exec:java -Dexec.mainClass=es.sescam.cliente.ClienteMain \
    -Dexec.args="http://localhost:9090"
```

## Menú principal

La ventana tiene un `JMenuBar` con **Archivo**, **Funcionalidades** y **Ayuda**.
El menú *Funcionalidades* abre cada una de las pestañas:

| Pestaña | Caso de uso | Controlador |
| --- | --- | --- |
| Pacientes | CU-12 | `PacienteController` |
| Médicos | CU-01 | `DoctorController` |
| Citas | CU-07 | `CitaController` |
| Solicitudes de prueba | CU-06 | `SolicitudPruebaController` |
| Informes clínicos | CU-09 | `InformeClinicoController` |
| Parámetros maestros | CU-17 | `ParametrosMaestrosController` |

En la parte superior se indica el **solicitante** (DNI y rol: `PACIENTE`, `DOCTOR`
o `ADMINISTRADOR`). El rol cambia las operaciones permitidas por los contratos de
la API. Cada pestaña tiene campos para los identificadores, botones para las
operaciones del caso de uso y un área de resultados. Algunas operaciones incluyen
datos de ejemplo ficticios para poder probarlas sin rellenar formularios largos.

## Cómo está cableado

`ClienteMain` es el único sitio donde se crean las implementaciones concretas:

1. Un `*ApiClientRest` por recurso con la URL base indicada.
2. Una vista de texto (`VistaTexto`) que implementa los contratos `*View` y escribe
   los resultados en el área de la pestaña.
3. Un `*ControllerImpl` que recibe el cliente de API y la vista por constructor.
4. Un `PanelFuncionalidad` genérico que recoge entradas y delega en el controlador.

La vista **no** llama a servicios ni repositorios del servidor: solo muestra datos
y recoge entradas. El cliente **no** importa clases del servidor: intercambia DTOs
a través de los contratos `ApiClient`.

## Cliente REST simulado

Los `ApiClientRest` actuales **no hacen llamadas de red**: validan el solicitante y
los parámetros y devuelven errores o datos de ejemplo coherentes. Por eso la GUI
puede arrancar sin servidor HTTP. Cuando se concrete el borde REST del servidor,
estos clientes se sustituirán por la implementación real sin tocar controladores
ni vistas.

## Estructura de paquetes

```
es.sescam.cliente
├── controller/   Puerta de entrada de la vista; delega en la API
├── model/        DTOs que viajan por la API (sin lógica)
├── service/      Contratos *ApiClient y sus implementaciones *ApiClientRest
│   ├── auth/     Solicitante y RolSolicitante
│   └── error/    ApiError y ApiException
└── view/         Contratos *View e implementaciones (Swing y de referencia)
```

Además de la GUI de `ClienteMain`, existen vistas Swing específicas
(`PacienteViewSwing`, `SolicitudPruebaViewSwing`, `InformeClinicoViewSwing`) con su
propio `main` de demostración. No se modificarán desde este arranque central.

## Pruebas

```bash
mvn -pl cliente test
```

Las pruebas usan dobles de `ApiClient` y `View`; no abren ventanas ni usan red.
