package es.sescam.cliente.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import es.sescam.cliente.controller.SolicitudPruebaController;
import es.sescam.cliente.controller.SolicitudPruebaControllerImpl;
import es.sescam.cliente.model.PrioridadSolicitud;
import es.sescam.cliente.model.SolicitudPruebaDTO;
import es.sescam.cliente.model.TipoPruebaDTO;
import es.sescam.cliente.service.SolicitudPruebaApiClient;
import es.sescam.cliente.service.SolicitudPruebaApiClientRest;
import es.sescam.cliente.service.auth.RolSolicitante;
import es.sescam.cliente.service.auth.Solicitante;

/**
 * Vista de escritorio (Swing) para la prescripción de pruebas diagnósticas
 * (CU-06).
 * <p>
 * Permite seleccionar paciente, médico, tipo de prueba y prioridad, informar la
 * indicación clínica y la justificación, aceptar el consentimiento informado
 * cuando la prueba lo exija y consultar las solicitudes pendientes de citación.
 * La vista solo recoge los datos del formulario, invoca al
 * {@link SolicitudPruebaController} y presenta los resultados; no accede a
 * servicios ni repositorios del servidor.
 * </p>
 */
public class SolicitudPruebaViewSwing extends JFrame implements SolicitudPruebaView {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNAS = {
        "ID", "Fecha", "Paciente", "Médico", "Prueba", "Prioridad", "Estado"
    };
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());

    private final SolicitudPruebaController controlador;
    private final Solicitante solicitante;

    private final JTextField campoPacienteId = new JTextField(8);
    private final JTextField campoDoctorId = new JTextField(8);
    private final JComboBox<TipoPruebaDTO> comboTipoPrueba = new JComboBox<>();
    private final JComboBox<PrioridadSolicitud> comboPrioridad =
            new JComboBox<>(PrioridadSolicitud.values());
    private final JTextArea areaIndicacion = new JTextArea(3, 30);
    private final JTextArea areaJustificacion = new JTextArea(3, 30);
    private final JCheckBox casillaConsentimiento =
            new JCheckBox("Consentimiento informado aceptado");
    private final JLabel etiquetaPreparacion = new JLabel("-");
    private final JLabel etiquetaContraste = new JLabel("-");
    private final JLabel etiquetaConsentimiento = new JLabel("-");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);
    private final JTextArea areaMensajes = new JTextArea(5, 60);

    /**
     * Crea la ventana de prescripción de pruebas diagnósticas.
     *
     * @param controlador controlador de presentación de solicitudes de prueba
     * @param solicitante usuario autenticado que realiza las operaciones
     */
    public SolicitudPruebaViewSwing(SolicitudPruebaController controlador, Solicitante solicitante) {
        super("Prescripción de pruebas diagnósticas - SESCAM");
        this.controlador = Objects.requireNonNull(controlador, "controlador");
        this.solicitante = Objects.requireNonNull(solicitante, "solicitante");
        configurarInterfaz();
    }

    /**
     * Solicita al controlador la carga inicial del catálogo de pruebas.
     */
    public void cargarCatalogo() {
        controlador.cargarTiposPrueba(solicitante);
    }

    @Override
    public void mostrarTiposPrueba(List<TipoPruebaDTO> tiposPrueba) {
        enHiloDeEventos(() -> {
            comboTipoPrueba.removeAllItems();
            if (tiposPrueba != null) {
                for (TipoPruebaDTO tipo : tiposPrueba) {
                    if (tipo != null) {
                        comboTipoPrueba.addItem(tipo);
                    }
                }
            }
            if (comboTipoPrueba.getItemCount() > 0) {
                comboTipoPrueba.setSelectedIndex(0);
            }
            actualizarAtributosCatalogo();
            areaMensajes.append("Catálogo cargado con " + comboTipoPrueba.getItemCount()
                    + " tipos de prueba.\n");
        });
    }

    @Override
    public void mostrarSolicitudesPendientes(List<SolicitudPruebaDTO> solicitudes) {
        enHiloDeEventos(() -> {
            modeloTabla.setRowCount(0);
            if (solicitudes != null) {
                for (SolicitudPruebaDTO solicitud : solicitudes) {
                    if (solicitud == null) {
                        continue;
                    }
                    modeloTabla.addRow(new Object[] {
                        solicitud.getId(),
                        formatearFecha(solicitud.getFechaSolicitud()),
                        solicitud.getPacienteId(),
                        solicitud.getDoctorId(),
                        nombrePrueba(solicitud.getTipoPrueba()),
                        solicitud.getPrioridad(),
                        solicitud.getEstado()
                    });
                }
            }
            areaMensajes.append("Solicitudes pendientes: " + modeloTabla.getRowCount() + ".\n");
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
     * Arranca la vista con un cliente de API local y un solicitante médico de
     * ejemplo.
     *
     * @param args argumentos de línea de comandos (no se utilizan)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SolicitudPruebaApiClient api = new SolicitudPruebaApiClientRest("http://localhost");
            VistaDiferida vistaDiferida = new VistaDiferida();
            SolicitudPruebaController controlador = new SolicitudPruebaControllerImpl(api, vistaDiferida);
            Solicitante medico = new Solicitante("00000000T", RolSolicitante.DOCTOR);
            SolicitudPruebaViewSwing ventana = new SolicitudPruebaViewSwing(controlador, medico);
            vistaDiferida.establecerDestino(ventana);
            ventana.setVisible(true);
            ventana.cargarCatalogo();
        });
    }

    private void configurarInterfaz() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        comboTipoPrueba.setRenderer(new RenderizadorTipoPrueba());
        comboTipoPrueba.addActionListener(evento -> actualizarAtributosCatalogo());
        areaIndicacion.setLineWrap(true);
        areaIndicacion.setWrapStyleWord(true);
        areaJustificacion.setLineWrap(true);
        areaJustificacion.setWrapStyleWord(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelMensajes(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nueva solicitud de prueba"));
        int fila = 0;
        agregarCampo(panel, "ID paciente:", campoPacienteId, fila++);
        agregarCampo(panel, "ID médico:", campoDoctorId, fila++);
        agregarCampo(panel, "Tipo de prueba:", comboTipoPrueba, fila++);
        agregarCampo(panel, "Prioridad:", comboPrioridad, fila++);
        agregarCampo(panel, "Indicación clínica:", new JScrollPane(areaIndicacion), fila++);
        agregarCampo(panel, "Justificación:", new JScrollPane(areaJustificacion), fila++);
        agregarCampo(panel, "Preparación previa:", etiquetaPreparacion, fila++);
        agregarCampo(panel, "Uso de contraste:", etiquetaContraste, fila++);
        agregarCampo(panel, "Consentimiento exigido:", etiquetaConsentimiento, fila++);

        GridBagConstraints restriccionesConsentimiento = new GridBagConstraints();
        restriccionesConsentimiento.gridx = 1;
        restriccionesConsentimiento.gridy = fila++;
        restriccionesConsentimiento.anchor = GridBagConstraints.LINE_START;
        restriccionesConsentimiento.insets = new Insets(2, 4, 2, 4);
        panel.add(casillaConsentimiento, restriccionesConsentimiento);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        JButton botonPrescribir = new JButton("Prescribir");
        botonPrescribir.addActionListener(evento -> ejecutarPrescripcion());
        botones.add(botonPrescribir);

        GridBagConstraints restriccionesBotones = new GridBagConstraints();
        restriccionesBotones.gridx = 0;
        restriccionesBotones.gridy = fila;
        restriccionesBotones.gridwidth = 2;
        restriccionesBotones.fill = GridBagConstraints.HORIZONTAL;
        restriccionesBotones.insets = new Insets(8, 4, 4, 4);
        panel.add(botones, restriccionesBotones);
        return panel;
    }

    private void agregarCampo(JPanel panel, String etiqueta, Component componente, int fila) {
        GridBagConstraints restriccionesEtiqueta = new GridBagConstraints();
        restriccionesEtiqueta.gridx = 0;
        restriccionesEtiqueta.gridy = fila;
        restriccionesEtiqueta.anchor = GridBagConstraints.LINE_END;
        restriccionesEtiqueta.insets = new Insets(2, 4, 2, 4);
        panel.add(new JLabel(etiqueta), restriccionesEtiqueta);

        GridBagConstraints restriccionesComponente = new GridBagConstraints();
        restriccionesComponente.gridx = 1;
        restriccionesComponente.gridy = fila;
        restriccionesComponente.weightx = 1.0;
        restriccionesComponente.fill = GridBagConstraints.HORIZONTAL;
        restriccionesComponente.insets = new Insets(2, 4, 2, 4);
        panel.add(componente, restriccionesComponente);
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Solicitudes pendientes de citación"));
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        JButton botonActualizar = new JButton("Actualizar");
        botonActualizar.addActionListener(evento -> controlador.listarPendientes(solicitante));
        botones.add(botonActualizar);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JScrollPane crearPanelMensajes() {
        areaMensajes.setEditable(false);
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);
        JScrollPane desplazamiento = new JScrollPane(areaMensajes);
        desplazamiento.setBorder(BorderFactory.createTitledBorder("Mensajes"));
        return desplazamiento;
    }

    private void actualizarAtributosCatalogo() {
        TipoPruebaDTO tipo = (TipoPruebaDTO) comboTipoPrueba.getSelectedItem();
        if (tipo == null) {
            etiquetaPreparacion.setText("-");
            etiquetaContraste.setText("-");
            etiquetaConsentimiento.setText("-");
            return;
        }
        etiquetaPreparacion.setText(texto(tipo.getPreparacionPrevia()));
        etiquetaContraste.setText(Boolean.TRUE.equals(tipo.getUsoContraste()) ? "Sí" : "No");
        etiquetaConsentimiento.setText(
                Boolean.TRUE.equals(tipo.getRequiereConsentimientoInformado()) ? "Sí" : "No");
    }

    private void ejecutarPrescripcion() {
        try {
            controlador.prescribir(solicitante, leerSolicitud());
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private SolicitudPruebaDTO leerSolicitud() {
        TipoPruebaDTO tipoPrueba = (TipoPruebaDTO) comboTipoPrueba.getSelectedItem();
        if (tipoPrueba == null) {
            throw new IllegalArgumentException("Seleccione un tipo de prueba");
        }
        PrioridadSolicitud prioridad = (PrioridadSolicitud) comboPrioridad.getSelectedItem();
        if (prioridad == null) {
            throw new IllegalArgumentException("Seleccione una prioridad");
        }
        SolicitudPruebaDTO solicitud = new SolicitudPruebaDTO();
        solicitud.setPacienteId(leerIdentificador(campoPacienteId.getText(),
                "Introduzca un ID de paciente válido"));
        solicitud.setDoctorId(leerIdentificador(campoDoctorId.getText(),
                "Introduzca un ID de médico válido"));
        solicitud.setTipoPrueba(tipoPrueba);
        solicitud.setPrioridad(prioridad);
        solicitud.setIndicacionClinica(areaIndicacion.getText().trim());
        solicitud.setJustificacion(areaJustificacion.getText().trim());
        solicitud.setConsentimientoInformadoAceptado(casillaConsentimiento.isSelected());
        solicitud.setUsuarioValidador(solicitante.getDni());
        return solicitud;
    }

    private Long leerIdentificador(String valor, String mensaje) {
        try {
            return Long.valueOf(texto(valor).trim());
        } catch (NumberFormatException excepcion) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    private String nombrePrueba(TipoPruebaDTO tipoPrueba) {
        return tipoPrueba == null ? "" : texto(tipoPrueba.getNombre());
    }

    private String formatearFecha(Instant fecha) {
        return fecha == null ? "" : FORMATO_FECHA.format(fecha);
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
     * Renderizador que muestra el nombre del tipo de prueba en el desplegable.
     */
    private static final class RenderizadorTipoPrueba extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                      boolean seleccionado, boolean tieneFoco) {
            super.getListCellRendererComponent(lista, valor, indice, seleccionado, tieneFoco);
            if (valor instanceof TipoPruebaDTO tipo) {
                setText(tipo.getNombre() == null ? "" : tipo.getNombre());
            } else {
                setText("");
            }
            return this;
        }
    }

    /**
     * Vista intermedia que permite construir el controlador antes que la ventana,
     * evitando una dependencia circular en el arranque de la aplicación.
     */
    private static final class VistaDiferida implements SolicitudPruebaView {

        private SolicitudPruebaView destino;

        private void establecerDestino(SolicitudPruebaView destino) {
            this.destino = destino;
        }

        @Override
        public void mostrarTiposPrueba(List<TipoPruebaDTO> tiposPrueba) {
            if (destino != null) {
                destino.mostrarTiposPrueba(tiposPrueba);
            }
        }

        @Override
        public void mostrarSolicitudesPendientes(List<SolicitudPruebaDTO> solicitudes) {
            if (destino != null) {
                destino.mostrarSolicitudesPendientes(solicitudes);
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
