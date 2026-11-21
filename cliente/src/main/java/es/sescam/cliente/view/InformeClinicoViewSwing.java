package es.sescam.cliente.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import es.sescam.cliente.controller.InformeClinicoController;
import es.sescam.cliente.controller.InformeClinicoControllerImpl;
import es.sescam.cliente.model.ImagenMedicaDTO;
import es.sescam.cliente.model.InformeClinicoDTO;
import es.sescam.cliente.service.InformeClinicoApiClient;
import es.sescam.cliente.service.InformeClinicoApiClientRest;
import es.sescam.cliente.service.auth.RolSolicitante;
import es.sescam.cliente.service.auth.Solicitante;

/**
 * Vista de escritorio (Swing) para la gestión de informes clínicos e imágenes
 * médicas (CU-09).
 * <p>
 * Permite crear, modificar, firmar y consultar informes asociados a una
 * solicitud de prueba, listar los informes de un paciente y cargar las imágenes
 * DICOM de un estudio. La vista solo recoge los datos del formulario, invoca al
 * {@link InformeClinicoController} y presenta los resultados; no accede a
 * servicios ni repositorios del servidor.
 * </p>
 * <p>
 * La carga del estudio se ejecuta en un {@link SwingWorker} para no bloquear el
 * hilo de eventos (RNF-EF-02): la barra de progreso refleja el avance y el botón
 * Cancelar cancela la tarea. Como esta iteración no incorpora ninguna biblioteca
 * DICOM, el panel de vista previa dibuja una imagen de marcador con los
 * metadatos de la imagen seleccionada; la decodificación DICOM real se integrará
 * con el RIS/PACS.
 * </p>
 */
public class InformeClinicoViewSwing extends JFrame implements InformeClinicoView {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNAS_INFORMES = {
        "ID", "Fecha", "Paciente", "Médico", "Estado", "Conclusión"
    };
    private static final String[] COLUMNAS_IMAGENES = {
        "ID", "Serie", "Formato", "Descripción", "Adquisición"
    };
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());

    private final InformeClinicoController controlador;
    private final Solicitante solicitante;

    private final JTextField campoPacienteId = new JTextField(8);
    private final JTextField campoSolicitudId = new JTextField(8);
    private final JLabel etiquetaInformeActual = new JLabel("-");
    private final JTextArea areaContenido = new JTextArea(5, 40);
    private final JTextArea areaConclusion = new JTextArea(3, 40);

    private final DefaultTableModel modeloInformes = new DefaultTableModel(COLUMNAS_INFORMES, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tablaInformes = new JTable(modeloInformes);
    private final List<InformeClinicoDTO> informesMostrados = new ArrayList<>();

    private final JTextField campoEstudioSolicitudId = new JTextField(8);
    private final JButton botonCargarEstudio = new JButton("Cargar estudio");
    private final JButton botonCancelar = new JButton("Cancelar");
    private final JProgressBar barraProgreso = new JProgressBar(0, 100);
    private final DefaultTableModel modeloImagenes = new DefaultTableModel(COLUMNAS_IMAGENES, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tablaImagenes = new JTable(modeloImagenes);
    private final List<ImagenMedicaDTO> imagenesMostradas = new ArrayList<>();
    private final JLabel etiquetaVistaPrevia = new JLabel();
    private final JTextArea areaMetadatosImagen = new JTextArea(6, 30);

    private final JTextArea areaMensajes = new JTextArea(5, 80);

    private Long idInformeActual;
    private SwingWorker<List<ImagenMedicaDTO>, Void> trabajadorCarga;

    /**
     * Crea la ventana de gestión de informes clínicos.
     *
     * @param controlador controlador de presentación de informes clínicos
     * @param solicitante usuario autenticado que realiza las operaciones
     */
    public InformeClinicoViewSwing(InformeClinicoController controlador, Solicitante solicitante) {
        super("Informes clínicos e imágenes médicas - SESCAM");
        this.controlador = Objects.requireNonNull(controlador, "controlador");
        this.solicitante = Objects.requireNonNull(solicitante, "solicitante");
        configurarInterfaz();
    }

    /**
     * Carga los informes del paciente indicado en el formulario.
     *
     * @param pacienteId identificador del paciente
     */
    public void cargarInformesDePaciente(Long pacienteId) {
        controlador.listarPorPaciente(solicitante, pacienteId);
    }

    @Override
    public void mostrarInformes(List<InformeClinicoDTO> informes) {
        enHiloDeEventos(() -> {
            modeloInformes.setRowCount(0);
            informesMostrados.clear();
            if (informes != null) {
                for (InformeClinicoDTO informe : informes) {
                    if (informe == null) {
                        continue;
                    }
                    informesMostrados.add(informe);
                    modeloInformes.addRow(new Object[] {
                        informe.getId(),
                        formatearFecha(informe.getFecha()),
                        informe.getPacienteId(),
                        informe.getDoctorId(),
                        informe.getEstado(),
                        texto(informe.getConclusion())
                    });
                }
            }
            if (!informesMostrados.isEmpty()) {
                tablaInformes.setRowSelectionInterval(0, 0);
            }
            areaMensajes.append("Informes del paciente: " + modeloInformes.getRowCount() + ".\n");
        });
    }

    @Override
    public void mostrarInforme(InformeClinicoDTO informe) {
        enHiloDeEventos(() -> {
            if (informe == null) {
                areaMensajes.append("No se ha encontrado el informe solicitado.\n");
                return;
            }
            idInformeActual = informe.getId();
            etiquetaInformeActual.setText(texto(informe.getEstado())
                    + (informe.getId() == null ? "" : " (ID " + informe.getId() + ")"));
            if (informe.getSolicitudId() != null) {
                campoSolicitudId.setText(informe.getSolicitudId().toString());
            }
            areaContenido.setText(texto(informe.getContenido()));
            areaConclusion.setText(texto(informe.getConclusion()));
            areaMensajes.append("Informe cargado en el formulario.\n");
        });
    }

    @Override
    public void mostrarImagenes(List<ImagenMedicaDTO> imagenes) {
        enHiloDeEventos(() -> {
            modeloImagenes.setRowCount(0);
            imagenesMostradas.clear();
            if (imagenes != null) {
                for (ImagenMedicaDTO imagen : imagenes) {
                    if (imagen == null) {
                        continue;
                    }
                    imagenesMostradas.add(imagen);
                    modeloImagenes.addRow(new Object[] {
                        imagen.getId(),
                        imagen.getSerie(),
                        imagen.getFormato(),
                        imagen.getDescripcion(),
                        formatearFecha(imagen.getFechaAdquisicion())
                    });
                }
            }
            if (!imagenesMostradas.isEmpty()) {
                tablaImagenes.setRowSelectionInterval(0, 0);
                mostrarVistaPrevia(imagenesMostradas.get(0));
            } else {
                mostrarVistaPrevia(null);
            }
            areaMensajes.append("Imágenes del estudio: " + modeloImagenes.getRowCount() + ".\n");
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
            JOptionPane.showMessageDialog(this, contenido, "Confirmación",
                    JOptionPane.INFORMATION_MESSAGE);
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
            InformeClinicoApiClient api = new InformeClinicoApiClientRest("http://localhost");
            VistaDiferida vistaDiferida = new VistaDiferida();
            InformeClinicoController controlador = new InformeClinicoControllerImpl(api,
                    vistaDiferida);
            Solicitante medico = new Solicitante("00000000T", RolSolicitante.DOCTOR);
            InformeClinicoViewSwing ventana = new InformeClinicoViewSwing(controlador, medico);
            vistaDiferida.establecerDestino(ventana);
            ventana.campoSolicitudId.setText("1");
            ventana.campoEstudioSolicitudId.setText("1");
            ventana.setVisible(true);
        });
    }

    private void configurarInterfaz() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        areaContenido.setLineWrap(true);
        areaContenido.setWrapStyleWord(true);
        areaConclusion.setLineWrap(true);
        areaConclusion.setWrapStyleWord(true);
        tablaInformes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaImagenes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaInformes.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting()) {
                cargarInformeSeleccionado();
            }
        });
        tablaImagenes.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting()) {
                cargarImagenSeleccionada();
            }
        });
        botonCancelar.setEnabled(false);
        barraProgreso.setStringPainted(true);
        areaMetadatosImagen.setEditable(false);
        areaMetadatosImagen.setLineWrap(true);
        areaMetadatosImagen.setWrapStyleWord(true);
        etiquetaVistaPrevia.setHorizontalAlignment(JLabel.CENTER);
        etiquetaVistaPrevia.setVerticalAlignment(JLabel.CENTER);
        etiquetaVistaPrevia.setPreferredSize(new Dimension(320, 240));
        etiquetaVistaPrevia.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mostrarVistaPrevia(null);

        add(crearPanelFormulario(), BorderLayout.NORTH);
        JSplitPane division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                crearPanelInformes(), crearPanelImagenes());
        division.setResizeWeight(0.4);
        add(division, BorderLayout.CENTER);
        add(crearPanelMensajes(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Informe clínico"));
        int fila = 0;
        agregarCampo(panel, "ID solicitud:", campoSolicitudId, fila++);
        agregarCampo(panel, "Informe actual:", etiquetaInformeActual, fila++);
        agregarCampo(panel, "Contenido:", new JScrollPane(areaContenido), fila++);
        agregarCampo(panel, "Conclusión:", new JScrollPane(areaConclusion), fila++);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        JButton botonCrear = new JButton("Crear");
        botonCrear.addActionListener(evento -> ejecutarCrear());
        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.addActionListener(evento -> ejecutarGuardar());
        JButton botonFirmar = new JButton("Firmar");
        botonFirmar.addActionListener(evento -> ejecutarFirmar());
        JButton botonConsultar = new JButton("Consultar");
        botonConsultar.addActionListener(evento -> ejecutarConsultar());
        botones.add(botonCrear);
        botones.add(botonGuardar);
        botones.add(botonFirmar);
        botones.add(botonConsultar);

        GridBagConstraints restriccionesBotones = new GridBagConstraints();
        restriccionesBotones.gridx = 0;
        restriccionesBotones.gridy = fila;
        restriccionesBotones.gridwidth = 2;
        restriccionesBotones.fill = GridBagConstraints.HORIZONTAL;
        restriccionesBotones.insets = new Insets(8, 4, 4, 4);
        panel.add(botones, restriccionesBotones);
        return panel;
    }

    private JPanel crearPanelInformes() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Informes del paciente"));

        JPanel cabecera = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        cabecera.add(new JLabel("ID paciente:"));
        cabecera.add(campoPacienteId);
        JButton botonListar = new JButton("Listar informes");
        botonListar.addActionListener(evento -> ejecutarListado());
        cabecera.add(botonListar);
        panel.add(cabecera, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaInformes), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelImagenes() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Imágenes médicas (DICOM)"));

        JPanel cabecera = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        cabecera.add(new JLabel("ID solicitud:"));
        cabecera.add(campoEstudioSolicitudId);
        botonCargarEstudio.addActionListener(evento -> cargarEstudio());
        botonCancelar.addActionListener(evento -> cancelarCarga());
        cabecera.add(botonCargarEstudio);
        cabecera.add(botonCancelar);
        cabecera.add(barraProgreso);
        panel.add(cabecera, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaImagenes), BorderLayout.CENTER);

        JPanel vistaPrevia = new JPanel(new BorderLayout(6, 6));
        vistaPrevia.setBorder(BorderFactory.createTitledBorder("Vista previa"));
        vistaPrevia.add(etiquetaVistaPrevia, BorderLayout.WEST);
        vistaPrevia.add(new JScrollPane(areaMetadatosImagen), BorderLayout.CENTER);
        panel.add(vistaPrevia, BorderLayout.SOUTH);
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

    private void ejecutarCrear() {
        try {
            controlador.crear(solicitante, leerInforme());
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void ejecutarGuardar() {
        try {
            Long id = exigirInformeSeleccionado();
            controlador.guardar(solicitante, id, leerInforme());
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void ejecutarFirmar() {
        try {
            controlador.firmar(solicitante, exigirInformeSeleccionado());
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void ejecutarConsultar() {
        try {
            controlador.consultar(solicitante, exigirInformeSeleccionado());
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void ejecutarListado() {
        try {
            controlador.listarPorPaciente(solicitante, leerIdentificador(campoPacienteId.getText(),
                    "Introduzca un ID de paciente válido"));
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private InformeClinicoDTO leerInforme() {
        InformeClinicoDTO informe = new InformeClinicoDTO();
        informe.setId(idInformeActual);
        informe.setSolicitudId(leerIdentificador(campoSolicitudId.getText(),
                "Introduzca un ID de solicitud válido"));
        String contenido = areaContenido.getText().trim();
        if (contenido.isEmpty()) {
            throw new IllegalArgumentException("El contenido del informe es obligatorio");
        }
        informe.setContenido(contenido);
        informe.setConclusion(areaConclusion.getText().trim());
        return informe;
    }

    private Long exigirInformeSeleccionado() {
        if (idInformeActual == null) {
            throw new IllegalArgumentException("Seleccione o cree un informe clínico");
        }
        return idInformeActual;
    }

    private void cargarInformeSeleccionado() {
        int fila = tablaInformes.getSelectedRow();
        if (fila >= 0 && fila < informesMostrados.size()) {
            mostrarInforme(informesMostrados.get(fila));
        }
    }

    private void cargarImagenSeleccionada() {
        int fila = tablaImagenes.getSelectedRow();
        if (fila >= 0 && fila < imagenesMostradas.size()) {
            mostrarVistaPrevia(imagenesMostradas.get(fila));
        }
    }

    private void cargarEstudio() {
        try {
            Long solicitudId = leerIdentificador(campoEstudioSolicitudId.getText(),
                    "Introduzca un ID de solicitud válido");
            if (trabajadorCarga != null && !trabajadorCarga.isDone()) {
                mostrarError("Ya hay una carga de estudio en curso");
                return;
            }
            iniciarCarga(solicitudId);
        } catch (IllegalArgumentException excepcion) {
            mostrarError(excepcion.getMessage());
        }
    }

    private void iniciarCarga(Long solicitudId) {
        botonCargarEstudio.setEnabled(false);
        botonCancelar.setEnabled(true);
        barraProgreso.setValue(0);
        barraProgreso.setString("Cargando estudio... 0%");
        trabajadorCarga = new SwingWorker<>() {
            @Override
            protected List<ImagenMedicaDTO> doInBackground() {
                setProgress(0);
                List<ImagenMedicaDTO> imagenes = controlador.cargarImagenes(solicitante, solicitudId);
                setProgress(100);
                return imagenes;
            }

            @Override
            protected void done() {
                botonCargarEstudio.setEnabled(true);
                botonCancelar.setEnabled(false);
                if (isCancelled()) {
                    barraProgreso.setValue(0);
                    barraProgreso.setString("Carga cancelada");
                    areaMensajes.append("Carga del estudio cancelada.\n");
                    return;
                }
                try {
                    get();
                    barraProgreso.setValue(100);
                    barraProgreso.setString("Estudio cargado");
                } catch (CancellationException excepcion) {
                    barraProgreso.setValue(0);
                    barraProgreso.setString("Carga cancelada");
                    areaMensajes.append("Carga del estudio cancelada.\n");
                } catch (InterruptedException excepcion) {
                    Thread.currentThread().interrupt();
                    mostrarError("La carga del estudio fue interrumpida");
                } catch (ExecutionException excepcion) {
                    mostrarError("No se pudo cargar el estudio");
                }
            }
        };
        trabajadorCarga.addPropertyChangeListener(evento -> {
            if ("progress".equals(evento.getPropertyName())) {
                int progreso = (Integer) evento.getNewValue();
                barraProgreso.setValue(progreso);
                barraProgreso.setString("Cargando estudio... " + progreso + "%");
            }
        });
        trabajadorCarga.execute();
    }

    private void cancelarCarga() {
        if (trabajadorCarga != null && !trabajadorCarga.isDone()) {
            trabajadorCarga.cancel(true);
        }
    }

    private void mostrarVistaPrevia(ImagenMedicaDTO imagen) {
        BufferedImage marcador = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB);
        Graphics2D grafico = marcador.createGraphics();
        try {
            grafico.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            grafico.setColor(new Color(18, 18, 24));
            grafico.fillRect(0, 0, 320, 240);
            grafico.setColor(new Color(55, 55, 70));
            for (int x = 0; x < 320; x += 20) {
                grafico.drawLine(x, 0, x, 240);
            }
            for (int y = 0; y < 240; y += 20) {
                grafico.drawLine(0, y, 320, y);
            }
            grafico.setColor(new Color(0, 200, 160));
            grafico.drawLine(160, 0, 160, 240);
            grafico.drawLine(0, 120, 320, 120);
            grafico.drawOval(110, 70, 100, 100);
            grafico.setColor(Color.WHITE);
            grafico.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
            grafico.drawString("VISTA PREVIA DICOM (marcador)", 18, 28);
            grafico.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            if (imagen == null) {
                grafico.drawString("Sin imagen seleccionada", 18, 55);
            } else {
                grafico.drawString("Serie: " + texto(imagen.getSerie()), 18, 55);
                grafico.drawString("Formato: " + texto(imagen.getFormato()), 18, 75);
                grafico.drawString(texto(imagen.getDescripcion()), 18, 95);
                grafico.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
                grafico.drawString("URL: " + recortar(texto(imagen.getUrl())), 18, 200);
                grafico.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            }
            grafico.setColor(new Color(190, 190, 190));
            grafico.drawString("Decodificación DICOM pendiente de RIS/PACS", 18, 222);
        } finally {
            grafico.dispose();
        }
        etiquetaVistaPrevia.setIcon(new ImageIcon(marcador));
        areaMetadatosImagen.setText(describirImagen(imagen));
        areaMetadatosImagen.setCaretPosition(0);
    }

    private String describirImagen(ImagenMedicaDTO imagen) {
        if (imagen == null) {
            return "Seleccione una imagen del estudio para ver sus metadatos.";
        }
        return "Identificador: " + texto(imagen.getId())
                + "\nEstudio: " + texto(imagen.getEstudioId())
                + "\nSerie: " + texto(imagen.getSerie())
                + "\nFormato: " + texto(imagen.getFormato())
                + "\nDescripción: " + texto(imagen.getDescripcion())
                + "\nAdquisición: " + formatearFecha(imagen.getFechaAdquisicion())
                + "\nURL: " + texto(imagen.getUrl());
    }

    private Long leerIdentificador(String valor, String mensaje) {
        try {
            return Long.valueOf(texto(valor).trim());
        } catch (NumberFormatException excepcion) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    private String formatearFecha(Instant fecha) {
        return fecha == null ? "" : FORMATO_FECHA.format(fecha);
    }

    private String recortar(String valor) {
        int maximo = 46;
        return valor.length() <= maximo ? valor : valor.substring(0, maximo - 3) + "...";
    }

    private String texto(Object valor) {
        return valor == null ? "" : valor.toString();
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
    private static final class VistaDiferida implements InformeClinicoView {

        private InformeClinicoView destino;

        private void establecerDestino(InformeClinicoView destino) {
            this.destino = destino;
        }

        @Override
        public void mostrarInformes(List<InformeClinicoDTO> informes) {
            if (destino != null) {
                destino.mostrarInformes(informes);
            }
        }

        @Override
        public void mostrarInforme(InformeClinicoDTO informe) {
            if (destino != null) {
                destino.mostrarInforme(informe);
            }
        }

        @Override
        public void mostrarImagenes(List<ImagenMedicaDTO> imagenes) {
            if (destino != null) {
                destino.mostrarImagenes(imagenes);
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
