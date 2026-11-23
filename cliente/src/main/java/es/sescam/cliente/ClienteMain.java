package es.sescam.cliente;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import es.sescam.cliente.controller.CitaController;
import es.sescam.cliente.controller.CitaControllerImpl;
import es.sescam.cliente.controller.DoctorController;
import es.sescam.cliente.controller.DoctorControllerImpl;
import es.sescam.cliente.controller.InformeClinicoController;
import es.sescam.cliente.controller.InformeClinicoControllerImpl;
import es.sescam.cliente.controller.PacienteController;
import es.sescam.cliente.controller.PacienteControllerImpl;
import es.sescam.cliente.controller.ParametrosMaestrosController;
import es.sescam.cliente.controller.ParametrosMaestrosControllerImpl;
import es.sescam.cliente.controller.SolicitudPruebaController;
import es.sescam.cliente.controller.SolicitudPruebaControllerImpl;
import es.sescam.cliente.model.CitaDTO;
import es.sescam.cliente.model.DoctorDTO;
import es.sescam.cliente.model.HorarioAtencionDTO;
import es.sescam.cliente.model.ImagenMedicaDTO;
import es.sescam.cliente.model.InformeClinicoDTO;
import es.sescam.cliente.model.PacienteDTO;
import es.sescam.cliente.model.ParametroPrioridadDTO;
import es.sescam.cliente.model.PoliticaCitacionDTO;
import es.sescam.cliente.model.PrioridadSolicitud;
import es.sescam.cliente.model.ProtocoloDTO;
import es.sescam.cliente.model.SolicitudPruebaDTO;
import es.sescam.cliente.model.TipoPruebaDTO;
import es.sescam.cliente.service.CitaApiClientRest;
import es.sescam.cliente.service.DoctorApiClientRest;
import es.sescam.cliente.service.InformeClinicoApiClientRest;
import es.sescam.cliente.service.PacienteApiClientRest;
import es.sescam.cliente.service.ParametrosMaestrosApiClientRest;
import es.sescam.cliente.service.SolicitudPruebaApiClientRest;
import es.sescam.cliente.service.auth.RolSolicitante;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.view.CitaView;
import es.sescam.cliente.view.DoctorView;
import es.sescam.cliente.view.InformeClinicoView;
import es.sescam.cliente.view.PacienteView;
import es.sescam.cliente.view.ParametrosMaestrosView;
import es.sescam.cliente.view.SolicitudPruebaView;

/**
 * Punto de entrada de la aplicación de escritorio del SESCAM.
 * <p>
 * Presenta una ventana Swing con un menú principal y una pestaña por cada
 * funcionalidad implementada en el cliente. Cada pestaña cablea, en este único
 * sitio, un {@code *ApiClientRest}, una vista de texto y su controlador de
 * presentación. La vista solo muestra resultados; las reglas de negocio siguen
 * residiendo en el servidor y la validación de las peticiones en los contratos
 * de la API.
 * </p>
 * <p>
 * Los clientes REST de esta iteración son simulados (no realizan llamadas de
 * red), por lo que la aplicación puede ejecutarse sin un servidor HTTP.
 * </p>
 */
public final class ClienteMain {

    private static final String DNI_DEMO = "00000000T";

    private final String baseUrl;
    private final JTextField campoDni = new JTextField(DNI_DEMO, 12);
    private final JComboBox<RolSolicitante> comboRol =
            new JComboBox<>(RolSolicitante.values());
    private final JTabbedPane pestañas = new JTabbedPane();

    /**
     * Abre la aplicación de escritorio.
     *
     * @param args si se indica, el primer argumento es la URL base de la API
     */
    public static void main(String[] args) {
        String url = args.length > 0 ? args[0] : "http://localhost:8080";
        SwingUtilities.invokeLater(() -> new ClienteMain(url).mostrar());
    }

    private ClienteMain(String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
    }

    private void mostrar() {
        JFrame ventana = new JFrame("SESCAM - Gestión de radiodiagnóstico");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        crearPestañas();
        ventana.setJMenuBar(crearMenu(ventana));
        ventana.add(crearPanelIdentidad(), BorderLayout.NORTH);
        ventana.add(pestañas, BorderLayout.CENTER);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    private JMenuBar crearMenu(JFrame ventana) {
        JMenuBar barra = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(evento -> ventana.dispose());
        menuArchivo.add(salir);
        barra.add(menuArchivo);

        JMenu menuFuncionalidades = new JMenu("Funcionalidades");
        for (int indice = 0; indice < pestañas.getTabCount(); indice++) {
            final int seleccion = indice;
            JMenuItem item = new JMenuItem(pestañas.getTitleAt(indice));
            item.addActionListener(evento -> pestañas.setSelectedIndex(seleccion));
            menuFuncionalidades.add(item);
        }
        barra.add(menuFuncionalidades);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem acercaDe = new JMenuItem("Acerca de");
        acercaDe.addActionListener(evento -> JOptionPane.showMessageDialog(ventana,
                "SESCAM - Cliente de demostración\n"
                        + "URL base de la API: " + baseUrl + "\n"
                        + "Los clientes REST son simulados en esta iteración.",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(acercaDe);
        barra.add(menuAyuda);
        return barra;
    }

    private JPanel crearPanelIdentidad() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Solicitante"));
        panel.add(new JLabel("DNI:"));
        panel.add(campoDni);
        panel.add(new JLabel("Rol:"));
        panel.add(comboRol);
        return panel;
    }

    private Supplier<Solicitante> solicitanteActual() {
        return () -> new Solicitante(campoDni.getText().trim(),
                (RolSolicitante) comboRol.getSelectedItem());
    }

    private void crearPestañas() {
        pestañas.addTab("Pacientes", crearPanelPacientes());
        pestañas.addTab("Médicos", crearPanelDoctores());
        pestañas.addTab("Citas", crearPanelCitas());
        pestañas.addTab("Solicitudes de prueba", crearPanelSolicitudes());
        pestañas.addTab("Informes clínicos", crearPanelInformes());
        pestañas.addTab("Parámetros maestros", crearPanelParametros());
    }

    private JPanel crearPanelPacientes() {
        VistaTexto vista = new VistaTexto();
        PacienteController controlador = new PacienteControllerImpl(
                new PacienteApiClientRest(baseUrl), vista);

        return crearPanel("Gestión de pacientes (CU-12)",
                new String[] {"ID paciente", "DNI"}, vista,
                new Accion("Listar", (s, p) -> controlador.listarPacientes(s)),
                new Accion("Consultar por ID",
                        (s, p) -> controlador.consultarPaciente(s, p.numero(0))),
                new Accion("Buscar por DNI",
                        (s, p) -> controlador.buscarPorDni(s, p.texto(1))),
                new Accion("Alta de ejemplo",
                        (s, p) -> controlador.crearPaciente(s, pacienteEjemplo())),
                new Accion("Dar de baja",
                        (s, p) -> controlador.darDeBajaPaciente(s, p.numero(0))));
    }

    private JPanel crearPanelDoctores() {
        VistaTexto vista = new VistaTexto();
        DoctorController controlador = new DoctorControllerImpl(
                new DoctorApiClientRest(baseUrl), vista);

        return crearPanel("Consulta de médicos (CU-01)",
                new String[] {"ID médico"}, vista,
                new Accion("Listar", (s, p) -> controlador.listarDoctores(s)),
                new Accion("Consultar por ID",
                        (s, p) -> controlador.consultarDoctor(s, p.numero(0))));
    }

    private JPanel crearPanelCitas() {
        VistaTexto vista = new VistaTexto();
        CitaController controlador = new CitaControllerImpl(
                new CitaApiClientRest(baseUrl), vista);

        return crearPanel("Gestión de citas (CU-07)",
                new String[] {"ID cita", "ID paciente", "Fecha (yyyy-MM-dd)", "Hora (HH:mm)"}, vista,
                new Accion("Mis citas",
                        (s, p) -> controlador.consultarMisCitas(s, p.numero(1))),
                new Accion("Detalle",
                        (s, p) -> controlador.consultarDetalleCita(s, p.numero(0))),
                new Accion("Asignar",
                        (s, p) -> controlador.asignarCita(s, p.numero(0), p.fecha(2), p.hora(3))),
                new Accion("Reprogramar",
                        (s, p) -> controlador.reprogramarCita(s, p.numero(0), p.fecha(2), p.hora(3))),
                new Accion("Cancelar",
                        (s, p) -> controlador.cancelarCita(s, p.numero(0))));
    }

    private JPanel crearPanelSolicitudes() {
        VistaTexto vista = new VistaTexto();
        SolicitudPruebaController controlador = new SolicitudPruebaControllerImpl(
                new SolicitudPruebaApiClientRest(baseUrl), vista);

        return crearPanel("Prescripción de pruebas (CU-06)",
                new String[] {"ID paciente", "ID médico", "ID tipo de prueba"}, vista,
                new Accion("Cargar catálogo", (s, p) -> controlador.cargarTiposPrueba(s)),
                new Accion("Listar pendientes", (s, p) -> controlador.listarPendientes(s)),
                new Accion("Prescribir de ejemplo",
                        (s, p) -> controlador.prescribir(s, solicitudEjemplo(p, s))));
    }

    private JPanel crearPanelInformes() {
        VistaTexto vista = new VistaTexto();
        InformeClinicoController controlador = new InformeClinicoControllerImpl(
                new InformeClinicoApiClientRest(baseUrl), vista);

        return crearPanel("Informes clínicos e imágenes (CU-09)",
                new String[] {"ID informe", "ID paciente", "ID solicitud", "ID médico"}, vista,
                new Accion("Listar por paciente",
                        (s, p) -> controlador.listarPorPaciente(s, p.numero(1))),
                new Accion("Consultar por ID",
                        (s, p) -> controlador.consultar(s, p.numero(0))),
                new Accion("Crear de ejemplo",
                        (s, p) -> controlador.crear(s, informeEjemplo(p))),
                new Accion("Firmar", (s, p) -> controlador.firmar(s, p.numero(0))),
                new Accion("Cargar imágenes",
                        (s, p) -> controlador.cargarImagenes(s, p.numero(2))));
    }

    private JPanel crearPanelParametros() {
        VistaTexto vista = new VistaTexto();
        ParametrosMaestrosController controlador = new ParametrosMaestrosControllerImpl(
                new ParametrosMaestrosApiClientRest(baseUrl), vista);

        return crearPanel("Parámetros maestros (CU-17)",
                new String[] {"ID tipo de prueba", "ID protocolo", "ID horario", "ID política"}, vista,
                new Accion("Listar tipos de prueba", (s, p) -> controlador.listarTiposPrueba(s)),
                new Accion("Listar protocolos", (s, p) -> controlador.listarProtocolos(s)),
                new Accion("Listar horarios", (s, p) -> controlador.listarHorarios(s)),
                new Accion("Listar políticas", (s, p) -> controlador.listarPoliticas(s)),
                new Accion("Listar prioridades",
                        (s, p) -> controlador.listarParametrosPrioridad(s)),
                new Accion("Niveles de prioridad", (s, p) -> controlador.listarNivelesPrioridad(s)));
    }

    private JPanel crearPanel(String descripcion, String[] etiquetas, VistaTexto vista,
                              Accion... acciones) {
        return new PanelFuncionalidad(descripcion, etiquetas, vista, solicitanteActual(),
                Arrays.asList(acciones));
    }

    private static PacienteDTO pacienteEjemplo() {
        PacienteDTO paciente = new PacienteDTO();
        paciente.setNombre("Paciente");
        paciente.setApellidos("Anonimo Demo");
        paciente.setDni("00000001A");
        paciente.setFechaNacimiento(LocalDate.of(1980, 1, 15));
        paciente.setTelefono("600000001");
        paciente.setEmail("paciente.demo@example.invalid");
        paciente.setDireccion("Calle Ficticia, 1");
        paciente.setNumeroHistoriaClinica("HC-DEMO-0001");
        return paciente;
    }

    private static SolicitudPruebaDTO solicitudEjemplo(PanelFuncionalidad panel,
                                                       Solicitante solicitante) {
        TipoPruebaDTO tipo = new TipoPruebaDTO();
        tipo.setId(panel.numero(2));

        SolicitudPruebaDTO solicitud = new SolicitudPruebaDTO();
        solicitud.setPacienteId(panel.numero(0));
        solicitud.setDoctorId(panel.numero(1));
        solicitud.setTipoPrueba(tipo);
        solicitud.setPrioridad(PrioridadSolicitud.ORDINARIA);
        solicitud.setIndicacionClinica("Control de demostración");
        solicitud.setJustificacion("Datos ficticios de demostración");
        solicitud.setConsentimientoInformadoAceptado(false);
        solicitud.setUsuarioValidador(solicitante.getDni());
        return solicitud;
    }

    private static InformeClinicoDTO informeEjemplo(PanelFuncionalidad panel) {
        InformeClinicoDTO informe = new InformeClinicoDTO();
        informe.setSolicitudId(panel.numero(2));
        informe.setPacienteId(panel.numero(1));
        informe.setDoctorId(panel.numero(3));
        informe.setContenido("Exploración ficticia de demostración");
        informe.setConclusion("Sin hallazgos ficticios");
        return informe;
    }

    /**
     * Acción disponible en una pestaña del menú principal.
     */
    private record Accion(String etiqueta,
                          Operacion operacion) {
    }

    /**
     * Operación que recibe el solicitante activo y el panel que la ejecuta.
     */
    @FunctionalInterface
    private interface Operacion {
        void ejecutar(Solicitante solicitante, PanelFuncionalidad panel);
    }

    /**
     * Panel genérico con campos de entrada, botones de operación y área de
     * resultados. No contiene reglas de negocio: solo recoge entradas y delega.
     */
    private static final class PanelFuncionalidad extends JPanel {

        private static final long serialVersionUID = 1L;

        private final List<JTextField> campos = new ArrayList<>();
        private final JTextArea area = new JTextArea(12, 56);
        private final Supplier<Solicitante> solicitante;
        private final List<Accion> acciones;

        private PanelFuncionalidad(String descripcion, String[] etiquetas, VistaTexto vista,
                                   Supplier<Solicitante> solicitante, List<Accion> acciones) {
            Objects.requireNonNull(vista, "vista").establecerArea(area);
            this.solicitante = Objects.requireNonNull(solicitante, "solicitante");
            this.acciones = List.copyOf(acciones);

            setLayout(new BorderLayout(8, 8));
            add(crearPanelEntrada(descripcion, etiquetas), BorderLayout.NORTH);

            area.setEditable(false);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            JScrollPane desplazamiento = new JScrollPane(area);
            desplazamiento.setBorder(BorderFactory.createTitledBorder("Resultados"));
            add(desplazamiento, BorderLayout.CENTER);
            add(crearPanelBotones(), BorderLayout.SOUTH);
        }

        private JPanel crearPanelEntrada(String descripcion, String[] etiquetas) {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createTitledBorder(descripcion));
            GridBagConstraints restricciones = new GridBagConstraints();
            restricciones.gridx = 0;
            restricciones.gridy = 0;
            restricciones.gridwidth = 2;
            restricciones.anchor = GridBagConstraints.LINE_START;
            restricciones.insets = new Insets(4, 4, 8, 4);
            panel.add(new JLabel("Introduzca los identificadores y pulse una operación:"),
                    restricciones);

            int fila = 1;
            for (String etiqueta : etiquetas) {
                JTextField campo = new JTextField(12);
                campos.add(campo);
                panel.add(new JLabel(etiqueta + ":"), restricciones(fila, 0, GridBagConstraints.LINE_END));
                panel.add(campo, restricciones(fila, 1, GridBagConstraints.LINE_START));
                fila++;
            }
            return panel;
        }

        private GridBagConstraints restricciones(int fila, int columna, int anclaje) {
            GridBagConstraints restricciones = new GridBagConstraints();
            restricciones.gridx = columna;
            restricciones.gridy = fila;
            restricciones.anchor = anclaje;
            restricciones.insets = new Insets(2, 4, 2, 4);
            return restricciones;
        }

        private JPanel crearPanelBotones() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
            for (Accion accion : acciones) {
                JButton boton = new JButton(accion.etiqueta());
                boton.addActionListener(evento -> ejecutar(accion));
                panel.add(boton);
            }
            return panel;
        }

        private void ejecutar(Accion accion) {
            try {
                accion.operacion().ejecutar(solicitante.get(), this);
            } catch (RuntimeException excepcion) {
                area.append("Error: " + excepcion.getMessage() + "\n");
            }
        }

        private String texto(int indice) {
            return campos.get(indice).getText().trim();
        }

        private Long numero(int indice) {
            String valor = texto(indice);
            if (valor.isEmpty()) {
                throw new IllegalArgumentException("Introduzca un valor en el campo " + (indice + 1));
            }
            try {
                return Long.valueOf(valor);
            } catch (NumberFormatException excepcion) {
                throw new IllegalArgumentException(
                        "El campo " + (indice + 1) + " debe ser un número entero");
            }
        }

        private LocalDate fecha(int indice) {
            try {
                return LocalDate.parse(texto(indice));
            } catch (DateTimeParseException excepcion) {
                throw new IllegalArgumentException("La fecha debe tener formato yyyy-MM-dd");
            }
        }

        private LocalTime hora(int indice) {
            try {
                return LocalTime.parse(texto(indice));
            } catch (DateTimeParseException excepcion) {
                throw new IllegalArgumentException("La hora debe tener formato HH:mm");
            }
        }
    }

    /**
     * Vista de texto compartida por los controladores de una pestaña.
     * <p>
     * Implementa todos los contratos de vista del cliente para poder mostrar
     * cualquier resultado sin añadir nuevas vistas ni lógica de presentación al
     * resto del código.
     * </p>
     */
    private static final class VistaTexto implements PacienteView, CitaView, DoctorView,
            InformeClinicoView, SolicitudPruebaView, ParametrosMaestrosView {

        private JTextArea area;

        private void establecerArea(JTextArea area) {
            this.area = Objects.requireNonNull(area, "area");
        }

        @Override
        public void mostrarPaciente(PacienteDTO paciente) {
            escribir(paciente);
        }

        @Override
        public void mostrarPacientes(List<PacienteDTO> pacientes) {
            mostrarLista(pacientes);
        }

        @Override
        public void mostrarCita(CitaDTO cita) {
            escribir(cita);
        }

        @Override
        public void mostrarCitas(List<CitaDTO> citas) {
            mostrarLista(citas);
        }

        @Override
        public void mostrarDoctor(DoctorDTO doctor) {
            escribir(doctor);
        }

        @Override
        public void mostrarDoctores(List<DoctorDTO> doctores) {
            mostrarLista(doctores);
        }

        @Override
        public void mostrarInformes(List<InformeClinicoDTO> informes) {
            mostrarLista(informes);
        }

        @Override
        public void mostrarInforme(InformeClinicoDTO informe) {
            escribir(informe);
        }

        @Override
        public void mostrarImagenes(List<ImagenMedicaDTO> imagenes) {
            mostrarLista(imagenes);
        }

        @Override
        public void mostrarTiposPrueba(List<TipoPruebaDTO> tiposPrueba) {
            mostrarLista(tiposPrueba);
        }

        @Override
        public void mostrarSolicitudesPendientes(List<SolicitudPruebaDTO> solicitudes) {
            mostrarLista(solicitudes);
        }

        @Override
        public void mostrarProtocolos(List<ProtocoloDTO> protocolos) {
            mostrarLista(protocolos);
        }

        @Override
        public void mostrarHorarios(List<HorarioAtencionDTO> horarios) {
            mostrarLista(horarios);
        }

        @Override
        public void mostrarPoliticas(List<PoliticaCitacionDTO> politicas) {
            mostrarLista(politicas);
        }

        @Override
        public void mostrarParametrosPrioridad(List<ParametroPrioridadDTO> parametros) {
            mostrarLista(parametros);
        }

        @Override
        public void mostrarNivelesPrioridad(List<String> niveles) {
            mostrarLista(niveles);
        }

        @Override
        public void mostrarMensaje(String mensaje) {
            escribir(mensaje);
        }

        @Override
        public void mostrarConfirmacion(String mensaje) {
            escribir("OK: " + mensaje);
        }

        @Override
        public void mostrarError(String mensaje) {
            escribir("Error: " + mensaje);
        }

        private void mostrarLista(List<?> elementos) {
            if (elementos == null || elementos.isEmpty()) {
                escribir("No hay resultados.");
                return;
            }
            for (Object elemento : elementos) {
                escribir(elemento);
            }
        }

        private void escribir(Object valor) {
            String mensaje = valor == null ? "-" : formatear(valor);
            area.append(mensaje + "\n");
            area.setCaretPosition(area.getDocument().getLength());
        }

        private String formatear(Object valor) {
            if (valor instanceof PacienteDTO paciente) {
                return "Paciente #" + paciente.getId() + ": " + paciente.getNombre() + " "
                        + paciente.getApellidos() + " (" + paciente.getDni() + ")";
            }
            if (valor instanceof CitaDTO cita) {
                return "Cita #" + cita.getId() + ": " + cita.getFecha() + " " + cita.getHora()
                        + " [" + cita.getEstado() + "] paciente=" + cita.getPacienteId();
            }
            if (valor instanceof DoctorDTO doctor) {
                return "Médico #" + doctor.getId() + ": " + doctor.getNombre() + " "
                        + doctor.getApellidos() + " (" + doctor.getEspecialidad() + ")";
            }
            if (valor instanceof InformeClinicoDTO informe) {
                return "Informe #" + informe.getId() + ": " + informe.getEstado()
                        + " solicitud=" + informe.getSolicitudId() + " - " + informe.getConclusion();
            }
            if (valor instanceof ImagenMedicaDTO imagen) {
                return "Imagen #" + imagen.getId() + ": estudio=" + imagen.getEstudioId()
                        + " serie=" + imagen.getSerie() + " (" + imagen.getFormato() + ")";
            }
            if (valor instanceof SolicitudPruebaDTO solicitud) {
                return "Solicitud #" + solicitud.getId() + ": " + solicitud.getEstado()
                        + " paciente=" + solicitud.getPacienteId()
                        + " prioridad=" + solicitud.getPrioridad();
            }
            if (valor instanceof TipoPruebaDTO tipo) {
                return "Tipo de prueba #" + tipo.getId() + ": " + tipo.getNombre()
                        + " (" + tipo.getModalidad() + ")";
            }
            if (valor instanceof ProtocoloDTO protocolo) {
                return "Protocolo #" + protocolo.getId() + ": " + protocolo.getNombre();
            }
            if (valor instanceof HorarioAtencionDTO horario) {
                return "Horario #" + horario.getId() + ": " + horario.getDiaSemana()
                        + " " + horario.getHoraInicio() + "-" + horario.getHoraFin();
            }
            if (valor instanceof PoliticaCitacionDTO politica) {
                return "Política #" + politica.getId() + ": " + politica.getNombre();
            }
            if (valor instanceof ParametroPrioridadDTO parametro) {
                return "Prioridad #" + parametro.getId() + ": " + parametro.getPrioridad()
                        + " (" + parametro.getDiasMaximosEspera() + " días)";
            }
            return String.valueOf(valor);
        }
    }
}
