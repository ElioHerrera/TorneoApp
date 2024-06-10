package torneoapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.border.LineBorder;

public class Presentacion extends JFrame implements ActionListener {

    private List<Equipo> listaEquipos;
    private List<JCheckBox> checkBoxes;
    private JButton comenzarTorneoBoton;
    private JLabel vacio;
    private JComboBox duracionPartidoComboBox;
    private String tiempoSeleccionadoStr = "1 minuto";
    private JComboBox<String> comboBoxTiempo;
    private TorneoApp torneoApp;

    public Presentacion(TorneoApp torneoApp, List<Equipo> listaEquipos) {

        this.torneoApp = torneoApp;
        this.listaEquipos = listaEquipos;

        // CONFIGURACIÓN DE LA VENTANA
        setTitle("Torneo Argentino Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 1000);
        setIconImage(new ImageIcon(getClass().getResource("img/equipos/escLPF.png")).getImage());
        //setResizable(false);
        setLocationRelativeTo(null);

        // PANEL PRINCIPAL 
        FondoPanel panelPrincipal = new FondoPanel("img/fondoVentanaPrincipal.jpg");
        panelPrincipal.setLayout(new GridBagLayout());

        JLabel vacio = new JLabel("");
        vacio.setOpaque(false);

        // Configuración de GridBagConstraints para el JLabel
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0; // Primera columna
        labelConstraints.gridy = 0; // Primera fila
        labelConstraints.weighty = 0.85; // Ocupa el 85% del alto vertical
        labelConstraints.fill = GridBagConstraints.VERTICAL; // Llenar verticalmente
        labelConstraints.anchor = GridBagConstraints.NORTH; // Anclar a la izquierda

        panelPrincipal.add(vacio, labelConstraints);// Añade el JLabel al panel con las restricciones dadas

        checkBoxes = new ArrayList<>();
        // CHECKBOXES
        JPanel contenedorCkeckBoxes = new JPanel();
        contenedorCkeckBoxes.setOpaque(false);

        // Configuración de GridBagConstraints para el contenedor de checkboxes
        GridBagConstraints checkboxesConstraints = new GridBagConstraints();
        checkboxesConstraints.gridx = 0; // Primera columna
        checkboxesConstraints.gridy = 1; // Segunda fila
        checkboxesConstraints.weighty = 0.65; // Ocupa el 80% del alto vertical
        checkboxesConstraints.fill = GridBagConstraints.VERTICAL; // Llenar verticalmente

        // Añade el contenedor de checkboxes al panel con las restricciones dadas
        panelPrincipal.add(contenedorCkeckBoxes, checkboxesConstraints);
        contenedorCkeckBoxes.setLayout(new GridLayout(0, 2, 5, 5));

        for (Equipo equipo : listaEquipos) {
            JPanel checkBoxPanel = new JPanel();
            checkBoxPanel.setOpaque(false);
            checkBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            JCheckBox checkBox = new JCheckBox();

            //checkBox.setSelected(true);

            JLabel iconoEquipoLabel = new JLabel(new ImageIcon(equipo.getIcono()));
            JLabel nombreEquipoLabel = new JLabel(equipo.getNombre());
            nombreEquipoLabel.setFont(new Font("Karmatic Arcade", Font.PLAIN, 16));
            checkBoxPanel.add(checkBox);
            checkBoxPanel.add(iconoEquipoLabel);
            checkBoxPanel.add(nombreEquipoLabel);
            checkBoxes.add(checkBox);
            contenedorCkeckBoxes.add(checkBoxPanel);
        }

        // JCheckBox con 5 opciones
        JPanel opcionesPanel = new JPanel();
        opcionesPanel.setOpaque(false);

        // Configuración de GridBagConstraints para el panel de opciones
        GridBagConstraints opcionesConstraints = new GridBagConstraints();
        opcionesConstraints.gridx = 0; // Primera columna
        opcionesConstraints.gridy = 2; // Tercera fila
        opcionesConstraints.weighty = 0.10; // Ocupa el 10% del alto vertical
        opcionesConstraints.fill = GridBagConstraints.VERTICAL; // Llenar verticalmente

        // Añade el panel de opciones al panel principal con las restricciones dadas
        panelPrincipal.add(opcionesPanel, opcionesConstraints);

        JLabel labelTiempo = new JLabel("Seleccionar tiempo de partidos:");
        opcionesPanel.add(labelTiempo);

        // JComboBox con las opciones de tiempo
        String[] opcionesTiempo = {"15 segundos", "30 segundos", "1 minuto", "3 minutos", "9 minutos"};
        JComboBox<String> comboBoxTiempo = new JComboBox<>(opcionesTiempo);
        comboBoxTiempo.setSelectedItem("1 minuto");
        
        // Añade el JComboBox al panel de opciones
        opcionesPanel.add(comboBoxTiempo);

        comboBoxTiempo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tiempoSeleccionadoStr = (String) comboBoxTiempo.getSelectedItem();

            }
        });

        // BUTTON
        Color colorBoton = new Color(255, 216, 123);
        Color colorBordes = new Color(237, 171, 0);
        Color colorTexto = new Color(0, 71, 123);

        comenzarTorneoBoton = new JButton("Comenzar Torneo");
        comenzarTorneoBoton.setBackground(colorBoton);
        comenzarTorneoBoton.setFont(new Font("Doctor Glitch", Font.PLAIN, 22));
        comenzarTorneoBoton.setForeground(colorTexto);
        comenzarTorneoBoton.setBorder(new LineBorder(colorBordes, 4));
        comenzarTorneoBoton.addActionListener(this);
        add(comenzarTorneoBoton, BorderLayout.SOUTH);

        // SCROLLPANE
        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        add(scrollPane, BorderLayout.CENTER);
        add(comenzarTorneoBoton, BorderLayout.SOUTH);
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == comenzarTorneoBoton) {
            
            List<Equipo> equiposSeleccionados = obtenerEquiposSeleccionados(); // Obtener los equipos seleccionados
            int duracionPartido = obtenerTiempoSeleccionado();

            if (equiposSeleccionados.size() == 8) {
                
                Collections.shuffle(equiposSeleccionados); // Barajar la lista de equipos seleccionados
                System.out.println("Segundos por Partido: " + duracionPartido);
                dispose(); //Cerrar ventana
                torneoApp.iniciarTorneo(equiposSeleccionados, duracionPartido); // Pasar la información de vuelta a TorneoApp
            
            } else {
                
                JOptionPane.showMessageDialog(this, "Selecciona exactamente 8 equipos para comenzar el torneo.");
            }
        }
    }

    
    private List<Equipo> obtenerEquiposSeleccionados() {
        List<Equipo> equiposSeleccionados = new ArrayList<>();
        for (int i = 0; i < checkBoxes.size(); i++) {
            JCheckBox checkBox = checkBoxes.get(i);
            if (checkBox.isSelected()) {
                String nombreEquipo = listaEquipos.get(i).getNombre();
                equiposSeleccionados.add(listaEquipos.get(i));
                System.out.println("Equipo agregado al torneo: " + listaEquipos.get(i).getNombre());
            }
        }

        return equiposSeleccionados;
    }

    private int obtenerTiempoSeleccionado() {

        return switch (tiempoSeleccionadoStr) {
            case "15 segundos" ->
                15;
            case "30 segundos" ->
                30;
            case "1 minuto" ->
                60;
            case "3 minutos" ->
                180;
            case "9 minutos" ->
                540;
            
            default ->
                0;
        };
    }
}
