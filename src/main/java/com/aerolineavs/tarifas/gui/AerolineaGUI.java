package com.aerolineavs.tarifas.gui;

import com.aerolineavs.tarifas.ClaseVuelo;
import com.aerolineavs.tarifas.ClientePotencial;
import com.aerolineavs.tarifas.IPricingService;
import com.aerolineavs.tarifas.PricingServiceImpl;
import com.aerolineavs.tarifas.RegionDestino;
import com.aerolineavs.tarifas.ResultadoTarifa;
import com.aerolineavs.tarifas.TipoViajero;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Objects;

/**
 * Interfaz gráfica de usuario (GUI Swing) para el cálculo de tarifas aéreas.
 * Totalmente desacoplada de la lógica de negocio: se comunica exclusivamente
 * a través de la abstracción {@link IPricingService} (Inversión de Dependencias).
 */
public class AerolineaGUI extends JFrame {

    private final IPricingService pricingService;

    // Componentes de entrada de datos
    private JSpinner spinnerEdad;
    private JSpinner spinnerVuelos;
    private JComboBox<TipoViajero> comboTipoViajero;
    private JComboBox<ClaseVuelo> comboClase;
    private JComboBox<RegionDestino> comboDestino;
    private JTextField txtIngresos;
    private JCheckBox chkConNinos;
    private JCheckBox chkViveConPadres;

    // Componentes de resultado
    private JLabel lblTarifaResultado;
    private JLabel lblDescuentoResultado;
    private JTextArea txtSuposiciones;

    /**
     * Construye la GUI inyectando el servicio de precios (DIP).
     *
     * @param pricingService implementación del contrato {@link IPricingService}
     */
    public AerolineaGUI(IPricingService pricingService) {
        this.pricingService = Objects.requireNonNull(pricingService, "El servicio de precios no puede ser nulo.");
        initComponents();
    }

    /**
     * Constructor por defecto para inicio autónomo.
     */
    public AerolineaGUI() {
        this(new PricingServiceImpl());
    }

    private void initComponents() {
        setTitle("Aerolínea - Recomendador de Tarifas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(560, 680));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Encabezado
        JLabel lblHeader = new JLabel("Calculadora de Tarifas Aéreas", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // Panel de Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new CompoundBorder(
                new TitledBorder(BorderFactory.createEtchedBorder(), " Datos del Pasajero "),
                new EmptyBorder(10, 12, 10, 12)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Edad
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Edad (años):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        spinnerEdad = new JSpinner(new SpinnerNumberModel(22, 0, 120, 1));
        formPanel.add(spinnerEdad, gbc);

        // Vuelos anuales
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Frecuencia de vuelos/año:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        spinnerVuelos = new JSpinner(new SpinnerNumberModel(6, 0, 365, 1));
        formPanel.add(spinnerVuelos, gbc);

        // Tipo de viajero
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Tipo de viajero:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        comboTipoViajero = new JComboBox<>(TipoViajero.values());
        formPanel.add(comboTipoViajero, gbc);

        // Clase preferida
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Clase preferida:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        comboClase = new JComboBox<>(ClaseVuelo.values());
        formPanel.add(comboClase, gbc);

        // Destino preferido
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Destino preferido:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        comboDestino = new JComboBox<>(RegionDestino.values());
        formPanel.add(comboDestino, gbc);

        // Ingresos anuales
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Ingresos anuales (€):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        txtIngresos = new JTextField("25000.0");
        formPanel.add(txtIngresos, gbc);

        // Viaja con niños
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        chkConNinos = new JCheckBox("¿Viaja habitualmente con niños menores de 12 años?");
        formPanel.add(chkConNinos, gbc);

        // Vive con padres
        row++;
        gbc.gridy = row;
        chkViveConPadres = new JCheckBox("¿Vive con sus padres? (aplica a jóvenes trabajadores)");
        formPanel.add(chkViveConPadres, gbc);

        // Botón Calcular
        row++;
        gbc.gridy = row; gbc.insets = new Insets(12, 6, 6, 6);
        JButton btnCalcular = new JButton("Calcular Tarifa Óptima");
        btnCalcular.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCalcular.setBackground(Color.WHITE);
        btnCalcular.setForeground(Color.BLACK);
        btnCalcular.setOpaque(true);
        btnCalcular.setFocusPainted(false);
        btnCalcular.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        btnCalcular.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCalcular.addActionListener(e -> calcularTarifa());
        formPanel.add(btnCalcular, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel de Resultados
        JPanel resultPanel = new JPanel(new GridBagLayout());
        resultPanel.setBorder(new CompoundBorder(
                new TitledBorder(BorderFactory.createEtchedBorder(), " Tarifa Recomendada "),
                new EmptyBorder(10, 12, 10, 12)
        ));

        GridBagConstraints gbcRes = new GridBagConstraints();
        gbcRes.insets = new Insets(4, 6, 4, 6);
        gbcRes.fill = GridBagConstraints.HORIZONTAL;

        gbcRes.gridx = 0; gbcRes.gridy = 0; gbcRes.weightx = 0.3;
        resultPanel.add(new JLabel("Tarifa:"), gbcRes);
        gbcRes.gridx = 1; gbcRes.weightx = 0.7;
        lblTarifaResultado = new JLabel("-");
        lblTarifaResultado.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTarifaResultado.setForeground(new Color(16, 92, 160));
        resultPanel.add(lblTarifaResultado, gbcRes);

        gbcRes.gridx = 0; gbcRes.gridy = 1; gbcRes.weightx = 0.3;
        resultPanel.add(new JLabel("Descuento obtenido:"), gbcRes);
        gbcRes.gridx = 1; gbcRes.weightx = 0.7;
        lblDescuentoResultado = new JLabel("-");
        lblDescuentoResultado.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblDescuentoResultado.setForeground(new Color(34, 139, 34));
        resultPanel.add(lblDescuentoResultado, gbcRes);

        gbcRes.gridx = 0; gbcRes.gridy = 2; gbcRes.gridwidth = 2;
        txtSuposiciones = new JTextArea(3, 30);
        txtSuposiciones.setLineWrap(true);
        txtSuposiciones.setWrapStyleWord(true);
        txtSuposiciones.setEditable(false);
        txtSuposiciones.setBackground(new Color(245, 245, 245));
        txtSuposiciones.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        JScrollPane scrollSuposiciones = new JScrollPane(txtSuposiciones);
        resultPanel.add(scrollSuposiciones, gbcRes);

        mainPanel.add(resultPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
    }

    private void calcularTarifa() {
        try {
            int edad = (Integer) spinnerEdad.getValue();
            int vuelos = (Integer) spinnerVuelos.getValue();
            TipoViajero tipo = (TipoViajero) comboTipoViajero.getSelectedItem();
            ClaseVuelo clase = (ClaseVuelo) comboClase.getSelectedItem();
            RegionDestino destino = (RegionDestino) comboDestino.getSelectedItem();

            String ingresosStr = txtIngresos.getText().trim();
            if (ingresosStr.isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar un valor para los ingresos anuales.");
            }
            double ingresos = Double.parseDouble(ingresosStr);
            if (ingresos < 0) {
                throw new IllegalArgumentException("Los ingresos anuales no pueden ser negativos.");
            }

            boolean conNinos = chkConNinos.isSelected();
            boolean viveConPadres = chkViveConPadres.isSelected();

            // Construir el modelo del cliente
            ClientePotencial cliente = new ClientePotencial(
                    edad, vuelos, tipo, clase, destino, ingresos, conNinos, viveConPadres
            );

            // Comunicación EXCLUSIVA con la lógica mediante la interfaz IPricingService
            ResultadoTarifa resultado = pricingService.evaluar(cliente);

            // Actualizar la vista
            lblTarifaResultado.setText(resultado.tarifa().getNombre());
            lblDescuentoResultado.setText(resultado.tarifa().getDescuentoPorcentaje() + "%");
            txtSuposiciones.setText(resultado.suposiciones());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, ingrese un número válido en los ingresos anuales.",
                    "Formato inválido", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Datos incorrectos", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            IPricingService service = new PricingServiceImpl();
            AerolineaGUI gui = new AerolineaGUI(service);
            gui.setVisible(true);
        });
    }
}
