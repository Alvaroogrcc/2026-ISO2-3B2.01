package es.sescam.cliente.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import es.sescam.cliente.controller.PacienteController;
import es.sescam.cliente.controller.PacienteControllerImpl;
import es.sescam.cliente.model.PacienteDTO;
import es.sescam.cliente.service.PacienteApiClient;
import es.sescam.cliente.service.PacienteApiClientRest;
import es.sescam.cliente.service.auth.RolSolicitante;
import es.sescam.cliente.service.auth.Solicitante;

/**
 * Vista de escritorio (Swing) para la gestión de pacientes (CU-12).
 * <p>
 * Permite dar de alta, modificar, dar de baja, buscar por DNI y listar pacientes.
 * La vista solo recoge los datos del formulario, invoca al
 * {@link PacienteController} y presenta los resultados; no accede a servicios ni
 * repositorios del servidor.
 * </p>
 * <p>
 * El formulario utiliza fechas en formato {@code yyyy-MM-dd}. Las operaciones de
 * modificación y baja actúan sobre el paciente seleccionado en la tabla o cargado
 * mediante una búsqueda.
 * </p>
 */
public class PacienteViewSwing extends JFrame implements PacienteView {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNAS = {
        "ID", "Nombre", "Apellidos", "DNI", "Fecha nacimiento", "Teléfono",
        "Email", "Dirección", "Nº historia clínica", "Fecha baja", "Activo"
    };
    private static final int COLUMNA_ID = 0;
    private static final int COLUMNA_NOMBRE = 1;
    private static final int COLUMNA_APELLIDOS = 2;
    private static final int COLUMNA_DNI = 3;
    private static final int COLUMNA_FECHA_NACIMIENTO = 4;
    private static final int COLUMNA_TELEFONO = 5;
    private static final int COLUMNA_EMAIL = 6;
    private static final int COLUMNA_DIRECCION = 7;
    private static final int COLUMNA_HISTORIA_CLINICA = 8;

    private final PacienteController controlador;
    private final Solicitante solicitante;

    private final JTextField campoNombre = new JTextField(20);
    private final JTextField campoApellidos = new JTextField(20);
    private final JTextField campoDni = new JTextField(12);
    private final JTextField campoFechaNacimiento = new JTextField(10);
    private final JTextField campoTelefono = new JTextField(12);
    private final JTextField campoEmail = new JTextField(20);
    private final JTextField campoDireccion = new JTextField(24);
    private final JTextField campoHistoriaClinica = new JTextField(12);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);
    private final JTextArea areaMensajes = new JTextArea(5, 60);

    private Long idSeleccionado;

    /**
     * Crea la ventana de gestión de pacientes.
     *
     * @param controlador controlador de presentación de pacientes
     * @param solicitante usuario autenticado que realiza las operaciones
     */
    public PacienteViewSwing(PacienteController controlador, Solicitante solicitante) {
        super("Gestión de pacientes - SESCAM");
        this.controlador = Objects.requireNonNull(controlador, "controlador");
        this.solicitante = Objects.requireNonNull(solicitante, "solicitante");
        configurarInterfaz();
    }

    @Override
    public void mostrarPaciente(PacienteDTO paciente) {
        enHiloDeEventos(() -> {
            if (paciente == null) {
                limpiarFormulario();
                return;
            }
            idSeleccionado = paciente.getId();
            rellenarFormulario(paciente);
            if (paciente.getId() != null) {
                seleccionarFila(paciente.getId());
            }
            areaMensajes.append("Paciente cargado: " + nombreCompleto(paciente) + "\n");
        });
    }

    @Override
    public void mostrarPacientes(List<PacienteDTO> pacientes) {
        enHiloDeEventos(() -> {
            modeloTabla.setRowCount(0);
            if (pacientes == null) {
                return;
            }
            for (PacienteDTO paciente : pacientes) {
                if (paciente == null) {
                    continue;
                }
                modeloTabla.addRow(new Object[] {
                    paciente.getId(),
                    paciente.getNombre(),
                    paciente.getApellidos(),
                    paciente.getDni(),
                    formatearFecha(paciente.getFechaNacimiento()),
                    paciente.getTelefono(),
                    paciente.getEmail(),
                    paciente.getDireccion(),
                    paciente.getNumeroHistoriaClinica(),
                    formatearFecha(paciente.getFechaBaja()),
                    paciente.isActivo() ? "Sí" : "No"
                });
            }
            areaMensajes.append("Se han cargado " + pacientes.size() + " pacientes.\n");
        });
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        enHiloDeEventos(() -> areaMensajes.append(texto(mensaje) + "\n"));
    }

    @Override
    public void mostrarConfirmacion(String mensaje) {
        enHiloDeEventos(() -> {
            String contenido = texto(mensaje);
            areaMensajes.append(contenido + "\n");
            JOptionPane.showMessageDialog(this, contenido, "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    @Override
    public void mostrarError(String mensaje) {
        enHiloDeEventos(() -> {
            String contenido = texto(mensaje);
            areaMensajes.append("Error: " + contenido + "\n");
            JOptionPane.showMessageDialog(this, contenido, "Error", JOptionPane.ERROR_MESSAGE);
        });
    }

    /**
     * Arranca la vista con un cliente de API local y un solicitante administrativo
     * de ejemplo.
     *
     * @param args argumentos de línea de comandos (no se utilizan)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PacienteApiClient api = new PacienteApiClientRest("http://localhost");
            VistaDiferida vistaDiferida = new VistaDiferida();
            PacienteController controlador = new PacienteControllerImpl(api, vistaDiferida);
            Solicitante solicitante = new Solicitante("00000000T", RolSolicitante.ADMINISTRADOR);
            PacienteViewSwing ventana = new PacienteViewSwing(controlador, solicitante);
            vistaDiferida.establecerDestino(ventana);
            ventana.setVisible(true);
        });
    }

    private void configurarInterfaz() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelMensajes(), BorderLayout.SOUTH);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting()) {
                cargarPacienteSeleccionado();
            }
        });
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del paciente"));
        int fila = 0;
        agregarCampo(panel, "Nombre:", campoNombre, fila++);
        agregarCampo(panel, "Apellidos:", campoApellidos, fila++);
        agregarCampo(panel, "DNI:", campoDni, fila++);
        agregarCampo(panel, "Fecha de nacimiento (yyyy-MM-dd):", campoFechaNacimiento, fila++);
        agregarCampo(panel, "Teléfono:", campoTelefono, fila++);
        agregarCampo(panel, "Email:", campoEmail, fila++);
        agregarCampo(panel, "Dirección:", campoDireccion, fila++);
        agregarCampo(panel, "Nº historia clínica:", campoHistoriaClinica, fila++);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        JButton botonAlta = new JButton("Alta");
        JButton botonModificar = new JButton("Modificar");
        JButton botonBaja = new JButton("Baja");
        JButton botonBuscar = new JButton("Buscar por DNI");
        JButton botonListar = new JButton("Listar");
        botonAlta.addActionListener(evento -> ejecutarAlta());
        botonModificar.addActionListener(evento -> ejecutarModificacion());
        botonBaja.addActionListener(evento -> ejecutarBaja());
        botonBuscar.addActionListener(evento -> ejecutarBusqueda());
        botonListar.addActionListener(evento -> ejecutarListado());
        botones.add(botonAlta);
        botones.add(botonModificar);
        botones.add(botonBaja);
        botones.add(botonBuscar);
        botones.add(botonListar);

        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.gridx = 0;
        restricciones.gridy = fila;
        restricciones.gridwidth = 2;
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.insets = new Insets(8, 4, 4, 4);
        panel.add(botones, restricciones);
        return panel;
    }

    private void agregarCampo(JPanel panel, String etiqueta, JTextField campo, int fila) {
        GridBagConstraints restriccionesEtiqueta = new GridBagConstraints();
        restriccionesEtiqueta.gridx = 0;
        restriccionesEtiqueta.gridy = fila;
        restriccionesEtiqueta.anchor = GridBagConstraints.LINE_END;
        restriccionesEtiqueta.insets = new Insets(2, 4, 2, 4);
        panel.add(new JLabel(etiqueta), restriccionesEtiqueta);

        GridBagConstraints restriccionesCampo = new GridBagConstraints();
        restriccionesCampo.gridx = 1;
        restriccionesCampo.gridy = fila;
        restriccionesCampo.weightx = 1.0;
        restriccionesCampo.fill = GridBagConstraints.HORIZONTAL;
        restriccionesCampo.insets = new Insets(2, 4, 2, 4);
        panel.add(campo, restriccionesCampo);
    }

    private JScrollPane crearPanelTabla() {
        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.setBorder(BorderFactory.createTitledBorder("Pacientes"));
        return desplazamiento;
    }

    private JScrollPane crearPanelMensajes() {
        areaMensajes.setEditable(false);
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);
        JScrollPane desplazamiento = new JScrollPane(areaMensajes);
        desplazamiento.setBorder(BorderFactory.createTitledBorder("Mensajes"));
        return desplazamiento;
    }

    private void ejecutarAlta() {
        try {
            PacienteDTO datos = leerFormulario();
            datos.setId(null);
            datos.setFechaBaja(null);
            controlador.crearPaciente(solicitante, datos);
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void ejecutarModificacion() {
        if (idSeleccionado == null) {
            mostrarError("Seleccione un paciente de la tabla o búsquelo por DNI antes de modificar");
            return;
        }
        try {
            PacienteDTO datos = leerFormulario();
            datos.setId(idSeleccionado);
            controlador.actualizarPaciente(solicitante, idSeleccionado, datos);
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void ejecutarBaja() {
        if (idSeleccionado == null) {
            mostrarError("Seleccione un paciente de la tabla o búsquelo por DNI antes de dar de baja");
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea dar de baja al paciente seleccionado?", "Confirmar baja",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            controlador.darDeBajaPaciente(solicitante, idSeleccionado);
        }
    }

    private void ejecutarBusqueda() {
        String dni = campoDni.getText().trim();
        if (dni.isEmpty()) {
            mostrarError("Introduzca un DNI para realizar la búsqueda");
            return;
        }
        controlador.buscarPorDni(solicitante, dni);
    }

    private void ejecutarListado() {
        controlador.listarPacientes(solicitante);
    }

    private PacienteDTO leerFormulario() {
        PacienteDTO datos = new PacienteDTO();
        datos.setId(idSeleccionado);
        datos.setNombre(campoNombre.getText().trim());
        datos.setApellidos(campoApellidos.getText().trim());
        datos.setDni(campoDni.getText().trim());
        datos.setFechaNacimiento(leerFecha(campoFechaNacimiento.getText()));
        datos.setTelefono(campoTelefono.getText().trim());
        datos.setEmail(campoEmail.getText().trim());
        datos.setDireccion(campoDireccion.getText().trim());
        datos.setNumeroHistoriaClinica(campoHistoriaClinica.getText().trim());
        return datos;
    }

    private LocalDate leerFecha(String valor) {
        String texto = texto(valor).trim();
        if (texto.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(texto);
        } catch (DateTimeParseException excepcion) {
            throw new IllegalArgumentException("La fecha de nacimiento debe tener formato yyyy-MM-dd", excepcion);
        }
    }

    private void cargarPacienteSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        Object identificador = modeloTabla.getValueAt(fila, COLUMNA_ID);
        idSeleccionado = identificador instanceof Number numero ? numero.longValue() : null;
        campoNombre.setText(valorTabla(fila, COLUMNA_NOMBRE));
        campoApellidos.setText(valorTabla(fila, COLUMNA_APELLIDOS));
        campoDni.setText(valorTabla(fila, COLUMNA_DNI));
        campoFechaNacimiento.setText(valorTabla(fila, COLUMNA_FECHA_NACIMIENTO));
        campoTelefono.setText(valorTabla(fila, COLUMNA_TELEFONO));
        campoEmail.setText(valorTabla(fila, COLUMNA_EMAIL));
        campoDireccion.setText(valorTabla(fila, COLUMNA_DIRECCION));
        campoHistoriaClinica.setText(valorTabla(fila, COLUMNA_HISTORIA_CLINICA));
    }

    private void rellenarFormulario(PacienteDTO paciente) {
        campoNombre.setText(texto(paciente.getNombre()));
        campoApellidos.setText(texto(paciente.getApellidos()));
        campoDni.setText(texto(paciente.getDni()));
        campoFechaNacimiento.setText(formatearFecha(paciente.getFechaNacimiento()));
        campoTelefono.setText(texto(paciente.getTelefono()));
        campoEmail.setText(texto(paciente.getEmail()));
        campoDireccion.setText(texto(paciente.getDireccion()));
        campoHistoriaClinica.setText(texto(paciente.getNumeroHistoriaClinica()));
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        campoNombre.setText("");
        campoApellidos.setText("");
        campoDni.setText("");
        campoFechaNacimiento.setText("");
        campoTelefono.setText("");
        campoEmail.setText("");
        campoDireccion.setText("");
        campoHistoriaClinica.setText("");
        tabla.clearSelection();
    }

    private void seleccionarFila(Long id) {
        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            Object valor = modeloTabla.getValueAt(fila, COLUMNA_ID);
            if (valor instanceof Number numero && numero.longValue() == id.longValue()) {
                tabla.setRowSelectionInterval(fila, fila);
                return;
            }
        }
    }

    private String valorTabla(int fila, int columna) {
        Object valor = modeloTabla.getValueAt(fila, columna);
        return valor == null ? "" : valor.toString();
    }

    private String nombreCompleto(PacienteDTO paciente) {
        return (texto(paciente.getNombre()) + " " + texto(paciente.getApellidos())).trim();
    }

    private String formatearFecha(LocalDate fecha) {
        return fecha == null ? "" : fecha.toString();
    }

    private String texto(String valor) {
        return valor == null ? "" : valor;
    }

    private void enHiloDeEventos(Runnable tarea) {
        if (SwingUtilities.isEventDispatchThread()) {
            tarea.run();
        } else {
            SwingUtilities.invokeLater(tarea);
        }
    }

    /**
     * Vista intermedia que permite construir el controlador antes que la ventana,
     * evitando una dependencia circular en el arranque de la aplicación.
     */
    private static final class VistaDiferida implements PacienteView {

        private PacienteView destino;

        private void establecerDestino(PacienteView destino) {
            this.destino = destino;
        }

        @Override
        public void mostrarPaciente(PacienteDTO paciente) {
            if (destino != null) {
                destino.mostrarPaciente(paciente);
            }
        }

        @Override
        public void mostrarPacientes(List<PacienteDTO> pacientes) {
            if (destino != null) {
                destino.mostrarPacientes(pacientes);
            }
        }

        @Override
        public void mostrarMensaje(String mensaje) {
            if (destino != null) {
                destino.mostrarMensaje(mensaje);
            }
        }

        @Override
        public void mostrarConfirmacion(String mensaje) {
            if (destino != null) {
                destino.mostrarConfirmacion(mensaje);
            }
        }

        @Override
        public void mostrarError(String mensaje) {
            if (destino != null) {
                destino.mostrarError(mensaje);
            }
        }
    }
}
