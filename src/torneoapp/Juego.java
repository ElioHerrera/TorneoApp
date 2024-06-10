package torneoapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Juego extends JFrame implements KeyListener {

    private int indicePartidoActual, segundosDeseados, minutosTranscurridos, segundosTranscurridos;
    private double velocidad;
    private boolean finDePartido = false;
    private boolean finDelTorneo = false;
    private Partido partidoActual, partidoSemi1, partidoSemi2, partidoFinal;
    private Equipo equipoSemifinalista1, equipoSemifinalista2, equipoSemifinalista3, equipoSemifinalista4, equipoFinalista1, equipoFinalista2, equipoCampeon;
    private List<Partido> partidos;
    private Image iconoVacio, escudoCampeon;
    private Timer timer, timer2;
    private Thread thread;
    private JProgressBar progressBar;
    private JLabel labelReloj, labelEscudo1, labelEscudo2, nombreEquipo1Label, nombreEquipo2Label, golesEquipo1Label, golesEquipo2Label, labelEscudoCampeon, golLabelGif, golLabelGif2;
    private JLabel icono1, icono2, icono3, icono4, icono5, icono6, icono7, icono8, icono9, icono10, icono11, icono12, icono13, icono14, papelitosLabelGif, papelitosLabelGif2, confettiLabelGif;
    private JLabel ganador1, ganador2, ganador1sorteo, ganador2sorteo, labelTextoMovimiento;
    private JButton botonProximoPartido, cerrar, golEquipo1Button, golEquipo2Button, golEquipo1ButtonX10, golEquipo2ButtonX10, botonBarraProgressEq1, botonBarraProgressEq2;
    private AnimatedGifLabel animatedGifLabelEq1, animatedGifLabelEq2;

    public Juego(List<Partido> listaPartidos, int segundosPartido) {
        this.partidos = listaPartidos;
        this.segundosDeseados = segundosPartido;
        this.indicePartidoActual = 0;
        this.partidoActual = listaPartidos.get(0);
        velocidad = calcularVelocidad(segundosDeseados);
        minutosTranscurridos = 0;
        segundosTranscurridos = 0;
        // Agregar el KeyListener a la ventana
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        setTitle("Torneo Argentino Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 1000);
        setIconImage(new ImageIcon(getClass().getResource("img/equipos/escLPF.png")).getImage());
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        labelTextoMovimiento = new JLabel("COMENTA: " + (partidoActual.getEquipo1().getNombre()) + "= Gol          COMENTA: " + (partidoActual.getEquipo1().getNombre()) + "          SUSCRIBETE          TorneoApp");

        //     labelTextoMovimiento = new JLabel("<html>Escribe el nombre de tu equipo para subir la barra de gol <img src='path/to/icon.png'>  <b>SUSCRIBETE</b>      Rosa = GOL del Local      </html>");
        labelTextoMovimiento.setFont(new Font("Arial", Font.PLAIN, 36));
        labelTextoMovimiento.setForeground(Color.WHITE); // Color del texto
        labelTextoMovimiento.setBackground(Color.BLACK);

        FontMetrics fontMetrics = labelTextoMovimiento.getFontMetrics(labelTextoMovimiento.getFont());
        // Obtén el tamaño del texto

        int textoAncho = fontMetrics.stringWidth(labelTextoMovimiento.getText());
        int textoAltura = fontMetrics.getHeight();

        // Ahora, puedes usar textoAncho y textoAltura para ajustar el tamaño de tu JLabel
        labelTextoMovimiento.setBounds(getWidth(), 50, textoAncho, textoAltura);
        add(labelTextoMovimiento);

        // Crea un temporizador para animar el movimiento del texto
        Timer timerTextoMovimiento = new Timer(10, new ActionListener() {
            int x = getWidth(); // Inicia la posición x en el ancho de la ventana

            @Override
            public void actionPerformed(ActionEvent e) {
                x--; // Mueve el texto hacia la izquierda
                labelTextoMovimiento.setLocation(x, 30); // Establece la nueva posición del texto

                // Si el texto se mueve fuera de la ventana, reinicia la posición
                if (x + labelTextoMovimiento.getWidth() < 0) {
                    x = getWidth();
                }
            }
        });

        timerTextoMovimiento.start(); // Inicia el temporizador

        mostrarImagenGolesYGanador();

        labelEscudo1 = new JLabel(new ImageIcon(partidoActual.getEquipo1().getEscudo()));
        labelEscudo1.setBounds(0, 500, 500, 500);
        add(labelEscudo1);

        labelEscudo2 = new JLabel(new ImageIcon(partidoActual.getEquipo2().getEscudo()));
        labelEscudo2.setBounds(300, 500, 500, 500);
        add(labelEscudo2);

        URL golURL = getClass().getResource("img/efectos/gol.gif");
        ImageIcon golGif = new ImageIcon(golURL);

        // Crear los JLabel, pero inicialmente invisibles
        golLabelGif = new JLabel(golGif);
        golLabelGif.setBounds(150, 550, golGif.getIconWidth(), golGif.getIconHeight());
        golLabelGif.setVisible(false);
        add(golLabelGif);

        // Crear los JLabel, pero inicialmente invisibles
        golLabelGif2 = new JLabel(golGif);
        golLabelGif2.setBounds(450, 550, golGif.getIconWidth(), golGif.getIconHeight());
        golLabelGif2.setVisible(false);
        add(golLabelGif2);

        mostrarIconos();
        mostrarEquipos();
        mostrarProgrssBar();
        mostrarReloj();

        mostrarBotones();
        mostrarBarraDeGoles();
        mostrarConfetti();

        JLabel fondo = new JLabel(new ImageIcon(getClass().getResource("img/fondoVentanaPartido.jpg")));
        fondo.setBounds(0, 0, getWidth(), getHeight());
        add(fondo);

    }

    private void mostrarImagenGolesYGanador() {

        ganador1 = new JLabel(new ImageIcon(getClass().getResource("img/efectos/ganador.png")));
        ganador1.setBounds(40, 480, 400, 300);
        ganador1.setVisible(false);
        add(ganador1);

        ganador2 = new JLabel(new ImageIcon(getClass().getResource("img/efectos/ganador.png")));
        ganador2.setBounds(340, 480, 400, 300);
        ganador2.setVisible(false);
        add(ganador2);

        ganador1sorteo = new JLabel(new ImageIcon(getClass().getResource("img/efectos/ganadorPorSorteo.png")));
        ganador1sorteo.setBounds(40, 480, 400, 300);
        ganador1sorteo.setVisible(false);
        add(ganador1sorteo);

        ganador2sorteo = new JLabel(new ImageIcon(getClass().getResource("img/efectos/ganadorPorSorteo.png")));
        ganador2sorteo.setBounds(340, 480, 400, 300);
        ganador2sorteo.setVisible(false);
        add(ganador2sorteo);

    }

    private void mostrarConfetti() {

        URL papelitosURL = getClass().getResource("img/efectos/papelitos.gif");
        ImageIcon papelitosGif = new ImageIcon(papelitosURL);

        // Crear los JLabel, pero inicialmente invisibles
        papelitosLabelGif = new JLabel(papelitosGif);
        papelitosLabelGif.setBounds(30, 100, papelitosGif.getIconWidth(), papelitosGif.getIconHeight());
        papelitosLabelGif.setVisible(false);
        add(papelitosLabelGif);

        papelitosLabelGif2 = new JLabel(papelitosGif);
        papelitosLabelGif2.setBounds(30, 500, papelitosGif.getIconWidth(), papelitosGif.getIconHeight());
        papelitosLabelGif2.setVisible(false);
        add(papelitosLabelGif2);

        labelEscudoCampeon = new JLabel(new ImageIcon(partidoActual.getEquipo1().getEscudo()));
        labelEscudoCampeon.setBounds(206, 460, 400, 400);
        labelEscudoCampeon.setVisible(false);
        add(labelEscudoCampeon);

        URL confettiURL = getClass().getResource("img/efectos/confetti.gif");
        ImageIcon confettiGif = new ImageIcon(confettiURL);

        confettiLabelGif = new JLabel(confettiGif);
        confettiLabelGif.setBounds(30, 700, papelitosGif.getIconWidth(), papelitosGif.getIconHeight());
        confettiLabelGif.setVisible(false);
        add(confettiLabelGif);

    }

    private void mostrarEquipos() {
        //EQUIPOS

        nombreEquipo1Label = new JLabel(partidoActual.getEquipo1().getNombre());
        nombreEquipo1Label.setBounds(70, 280, 350, 350);
        nombreEquipo1Label.setHorizontalAlignment(JLabel.CENTER);
        nombreEquipo1Label.setFont(new Font("Doctor Glitch", Font.BOLD, 30));
        nombreEquipo1Label.setForeground(Color.WHITE);
        add(nombreEquipo1Label);

        nombreEquipo2Label = new JLabel(partidoActual.getEquipo2().getNombre());
        nombreEquipo2Label.setBounds(370, 280, 350, 350);
        nombreEquipo2Label.setHorizontalAlignment(JLabel.CENTER);
        nombreEquipo2Label.setFont(new Font("Doctor Glitch", Font.BOLD, 30));
        nombreEquipo2Label.setForeground(Color.WHITE);
        add(nombreEquipo2Label);

        golesEquipo1Label = new JLabel(String.valueOf(partidoActual.getGolesEq1()));
        golesEquipo1Label.setBounds(74, 390, 350, 350);
        golesEquipo1Label.setHorizontalAlignment(JLabel.CENTER);
        golesEquipo1Label.setFont(new Font("Doctor Glitch", Font.BOLD, 100));
        golesEquipo1Label.setForeground(Color.WHITE);
        add(golesEquipo1Label);

        golesEquipo2Label = new JLabel(String.valueOf(partidoActual.getGolesEq2()));
        golesEquipo2Label.setBounds(370, 390, 350, 350);
        golesEquipo2Label.setHorizontalAlignment(JLabel.CENTER);
        golesEquipo2Label.setFont(new Font("Doctor Glitch", Font.BOLD, 100));
        golesEquipo2Label.setForeground(Color.WHITE);
        add(golesEquipo2Label);
    }

    private void mostrarBotones() {
        // Crear y configurar el botón de reinicio
        botonProximoPartido = new JButton("NEXT");
        botonProximoPartido.setBounds(730, 150, 50, 40);
        botonProximoPartido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                mostrarProximoPartido();

                if (indicePartidoActual <= 6) {

                    if (finDelTorneo == false) {
                        reiniciarCronometro();
                    }
                    revalidate();  // Revalida la disposición de los componentes
                    repaint();
                }

            }
        });
        botonProximoPartido.setVisible(false);
        add(botonProximoPartido);

        golEquipo1Button = new JButton("X1");
        golEquipo1Button.setBounds(730, 650, 50, 40);
        add(golEquipo1Button);
        golEquipo1Button.addActionListener((ActionEvent e) -> {
            partidoActual.incrementarGolesEq1();
            golesEquipo1Label.setText(String.valueOf(partidoActual.getGolesEq1()));
            mostrarDuranteTiempo(golLabelGif, 1);
            actualizarProgressBar();
        });

        golEquipo2Button = new JButton("X1");
        golEquipo2Button.setBounds(730, 700, 50, 40);
        add(golEquipo2Button);
        golEquipo2Button.addActionListener((ActionEvent e) -> {
            partidoActual.incrementarGolesEq2();
            golesEquipo2Label.setText(String.valueOf(partidoActual.getGolesEq2()));
            mostrarDuranteTiempo(golLabelGif2, 1);
            actualizarProgressBar();
        });

        golEquipo1ButtonX10 = new JButton("X10");
        golEquipo1ButtonX10.setBounds(730, 750, 50, 40);
        add(golEquipo1ButtonX10);
        golEquipo1ButtonX10.addActionListener((ActionEvent e) -> {

            partidoActual.incrementarGolesEq1X10();
            golesEquipo1Label.setText(String.valueOf(partidoActual.getGolesEq1()));
            mostrarDuranteTiempo(golLabelGif, 1);
            actualizarProgressBar();
        });

        golEquipo2ButtonX10 = new JButton("X10");
        golEquipo2ButtonX10.setBounds(730, 800, 50, 40);
        add(golEquipo2ButtonX10);
        golEquipo2ButtonX10.addActionListener((ActionEvent e) -> {
            //partidoActual.incrementarGolesEq2();
            partidoActual.incrementarGolesEq2X10();
            golesEquipo2Label.setText(String.valueOf(partidoActual.getGolesEq2()));
            mostrarDuranteTiempo(golLabelGif2, 1);
            actualizarProgressBar();
        });

        cerrar = new JButton("CLOSE");
        cerrar.setBounds(730, 95, 50, 50);
        add(cerrar);
        cerrar.addActionListener((ActionEvent e) -> {
            dispose();  // Cerrar la ventana actual
        });

    }

    private void mostrarBarraDeGoles() {

        animatedGifLabelEq1 = new AnimatedGifLabel("img/efectos/fondoBarra.gif", "img/efectos/barra.gif");
        animatedGifLabelEq1.setBounds(110, 650, 35, 200);
        add(animatedGifLabelEq1);

        botonBarraProgressEq1 = new JButton("Aumentar Progreso");
        botonBarraProgressEq1.setBounds(730, 550, 50, 40);
        add(botonBarraProgressEq1);
        botonBarraProgressEq1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aumentarProgresoEq1();
            }
        });
        animatedGifLabelEq2 = new AnimatedGifLabel("img/efectos/fondoBarra.gif", "img/efectos/barra.gif");
        animatedGifLabelEq2.setBounds(655, 650, 35, 200);
        add(animatedGifLabelEq2);

        botonBarraProgressEq2 = new JButton("Aumentar Progreso");
        botonBarraProgressEq2.setBounds(730, 600, 50, 40);
        add(botonBarraProgressEq2);
        botonBarraProgressEq2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aumentarProgresoEq2();
            }
        });

    }

    private void aumentarProgresoEq1() {
        int valorActual = animatedGifLabelEq1.getProgress();
        if (valorActual < 90) {
            animatedGifLabelEq1.setProgress(valorActual + 11);
        } else {
            partidoActual.incrementarGolesEq1();
            golesEquipo1Label.setText(String.valueOf(partidoActual.getGolesEq1()));
            mostrarDuranteTiempo(golLabelGif, 1);
            animatedGifLabelEq1.setProgress(0);
        }
    }

    private void aumentarProgresoEq2() {
        int valorActual = animatedGifLabelEq2.getProgress();
        if (valorActual < 90) {
            animatedGifLabelEq2.setProgress(valorActual + 11);
        } else {
            partidoActual.incrementarGolesEq2();
            golesEquipo2Label.setText(String.valueOf(partidoActual.getGolesEq2()));
            mostrarDuranteTiempo(golLabelGif2, 1);

            animatedGifLabelEq2.setProgress(0);
        }
    }

    private void mostrarReloj() {

        //CONFIGURACION DEL RELOJ
        labelReloj = new JLabel();
        labelReloj.setBounds(0, 200, 800, 400);
        labelReloj.setFont(new Font("Arial", Font.BOLD, 50));
        labelReloj.setHorizontalAlignment(SwingConstants.CENTER);
        labelReloj.setForeground(Color.WHITE);
        add(labelReloj);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep((int) (1000 / velocidad));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }

                    segundosTranscurridos++;
                    if (segundosTranscurridos == 60) {
                        segundosTranscurridos = 0;
                        minutosTranscurridos++;
                    }

                    labelReloj.setText(getTiempoFormateado(minutosTranscurridos, segundosTranscurridos));

                    if (minutosTranscurridos >= 90) {
                        thread.interrupt();
                        finDePartido = true;

                        labelReloj.setText("FIN DEL PARTIDO");

                        // Muestra el botón de reinicio
                        botonProximoPartido.setVisible(true);
                        //botonProximoPartido.doClick();

                        golEquipo1Button.setEnabled(false);
                        golEquipo2Button.setEnabled(false);

                        try {
                            // Espera 2 segundos
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        botonProximoPartido.doClick();

                        break;
                    }
                }
            }
        });

        thread.start();

    }

    private void mostrarProgrssBar() {

        progressBar = new JProgressBar() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int width = getWidth();
                int height = getHeight();

                // Obtener los colores del equipo 1 y equipo 2
                Color colorIzquierdoArriba = partidoActual.getEquipo1().getColor1();
                Color colorIzquierdoAbajo = partidoActual.getEquipo1().getColor2();

                Color colorDerechoArriba = partidoActual.getEquipo2().getColor1();
                Color colorDerechoAbajo = partidoActual.getEquipo2().getColor2();

                int totalGoles = partidoActual.getGolesEq1() + partidoActual.getGolesEq2(); // Asegurar que nunca sea cero
                int targetDivisionX;

                // Controlar la opacidad del colorDerechoArriba
                if (partidoActual.getGolesEq1() == 0 && partidoActual.getGolesEq2() == 0) {
                    colorDerechoArriba = new Color(colorDerechoArriba.getRed(), colorDerechoArriba.getGreen(), colorDerechoArriba.getBlue(), 0);
                    colorDerechoAbajo = new Color(colorDerechoArriba.getRed(), colorDerechoArriba.getGreen(), colorDerechoArriba.getBlue(), 0);
                    colorIzquierdoArriba = new Color(colorDerechoArriba.getRed(), colorDerechoArriba.getGreen(), colorDerechoArriba.getBlue(), 0);
                    colorIzquierdoAbajo = new Color(colorDerechoArriba.getRed(), colorDerechoArriba.getGreen(), colorDerechoArriba.getBlue(), 0);
                }

                if (totalGoles != 0) {
                    targetDivisionX = (partidoActual.getGolesEq1() * width) / totalGoles;

                } else {
                    // Manejar el caso cuando totalGoles es igual a cero, asignar un valor predeterminado o lanzar una excepción según sea necesario.
                    // Ejemplo:
                    targetDivisionX = 0; // o asigna otro valor predeterminado
                    // o lanza una excepción
                    // throw new ArithmeticException("División por cero");
                }

                // Configurar el gráfico con colores sólidos
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(colorIzquierdoArriba);
                g2d.fillRect(0, 0, targetDivisionX, height / 2);
                g2d.setColor(colorDerechoArriba);
                g2d.fillRect(targetDivisionX, 0, width - targetDivisionX, height / 2);
                g2d.setColor(colorIzquierdoAbajo);
                g2d.fillRect(0, height / 2, targetDivisionX, height / 2);
                g2d.setColor(colorDerechoAbajo);
                g2d.fillRect(targetDivisionX, height / 2, width - targetDivisionX, height / 2);

            }
        };
        progressBar.setBounds(70, 475, 650, 25);

        progressBar.setStringPainted(false);
        add(progressBar);
        Timer timer = new Timer(50, e -> progressBar.repaint());
        timer.start();

    }

    private void mostrarIconos() {

        iconoVacio = new ImageIcon("img/efectos/iconoVacio.png").getImage();
        equipoSemifinalista1 = new Equipo(null, null, null, null, null, null);
        equipoSemifinalista2 = new Equipo(null, null, null, null, null, null);
        equipoSemifinalista3 = new Equipo(null, null, null, null, null, null);
        equipoSemifinalista4 = new Equipo(null, null, null, null, null, null);
        equipoFinalista1 = new Equipo(null, null, null, null, null, null);
        equipoFinalista2 = new Equipo(null, null, null, null, null, null);
        equipoCampeon = new Equipo(null, null, null, null, null, null);

        //fixture();
        icono1 = new JLabel(new ImageIcon(icono(partidos.get(0).getEquipo1(), partidos.get(0).getEquipo1().getIcono(), partidos.get(0).getEquipo1().getIconoPerdio())));
        icono1.setBounds(-38, -76, 400, 400);
        add(icono1);
        icono2 = new JLabel(new ImageIcon(icono(partidos.get(0).getEquipo2(), partidos.get(0).getEquipo2().getIcono(), partidos.get(0).getEquipo2().getIconoPerdio())));
        icono2.setBounds(-38, -4, 400, 400);
        add(icono2);
        icono3 = new JLabel(new ImageIcon(icono(partidos.get(1).getEquipo1(), partidos.get(1).getEquipo1().getIcono(), partidos.get(1).getEquipo1().getIconoPerdio())));
        icono3.setBounds(-38, 68, 400, 400);
        add(icono3);
        icono4 = new JLabel(new ImageIcon(icono(partidos.get(1).getEquipo2(), partidos.get(1).getEquipo2().getIcono(), partidos.get(1).getEquipo2().getIconoPerdio())));
        icono4.setBounds(-38, 142, 400, 400);
        add(icono4);
        icono5 = new JLabel(new ImageIcon(icono(partidos.get(2).getEquipo1(), partidos.get(2).getEquipo1().getIcono(), partidos.get(2).getEquipo1().getIconoPerdio())));
        icono5.setBounds(426, -76, 400, 400);
        add(icono5);
        icono6 = new JLabel(new ImageIcon(icono(partidos.get(2).getEquipo2(), partidos.get(2).getEquipo2().getIcono(), partidos.get(2).getEquipo2().getIconoPerdio())));
        icono6.setBounds(426, -4, 400, 400);
        add(icono6);
        icono7 = new JLabel(new ImageIcon(icono(partidos.get(3).getEquipo1(), partidos.get(3).getEquipo1().getIcono(), partidos.get(3).getEquipo1().getIconoPerdio())));
        icono7.setBounds(426, 68, 400, 400);
        add(icono7);
        icono8 = new JLabel(new ImageIcon(icono(partidos.get(3).getEquipo2(), partidos.get(3).getEquipo2().getIcono(), partidos.get(3).getEquipo2().getIconoPerdio())));
        icono8.setBounds(426, 142, 400, 400);
        add(icono8);
        icono9 = new JLabel(new ImageIcon(icono(equipoSemifinalista1, equipoSemifinalista1.getIcono(), equipoSemifinalista1.getIconoPerdio())));
        icono9.setBounds(44, -40, 400, 400);
        add(icono9);
        icono10 = new JLabel(new ImageIcon(icono(equipoSemifinalista2, equipoSemifinalista2.getIcono(), equipoSemifinalista2.getIconoPerdio())));
        icono10.setBounds(44, 105, 400, 400);
        add(icono10);

        icono11 = new JLabel(new ImageIcon(icono(equipoSemifinalista3, equipoSemifinalista3.getIcono(), equipoSemifinalista3.getIconoPerdio())));
        icono11.setBounds(344, -40, 400, 400);
        add(icono11);

        icono12 = new JLabel(new ImageIcon(icono(equipoSemifinalista4, equipoSemifinalista4.getIcono(), equipoSemifinalista4.getIconoPerdio())));
        icono12.setBounds(344, 105, 400, 400);
        add(icono12);

        icono13 = new JLabel(new ImageIcon(icono(equipoFinalista1, equipoFinalista1.getIcono(), equipoFinalista1.getIconoPerdio())));
        icono13.setBounds(130, 33, 400, 400);
        add(icono13);

        icono14 = new JLabel(new ImageIcon(icono(equipoFinalista1, equipoFinalista1.getIcono(), equipoFinalista1.getIconoPerdio())));
        icono14.setBounds(258, 33, 400, 400);
        add(icono14);

    }

    private void mostrarProximoPartido() {

        System.out.println("METODO DETERMINAR GANADOR");
        determinarGanador();
        esperar(3);
        if (indicePartidoActual < partidos.size() - 1) {

            //agregarPartidoLista(indicePartidoActual);
            indicePartidoActual++;
            System.out.println(indicePartidoActual);
            partidoActual = partidos.get(indicePartidoActual);
            actualizarLabels();
            animatedGifLabelEq1.setProgress(0);
            animatedGifLabelEq2.setProgress(0);

            revalidate();  // Revalida la disposición de los componentes
            repaint();
            //botonProximoPartido.setEnabled(false);  // Deshabilitar el botón después de hacer clic
        } else {
            //JOptionPane.showMessageDialog(this, "¡Torneo Finalizado!");
            labelReloj.setText("CAMPEON " + equipoCampeon.getNombre());
            finDelTorneo = true;
            mostrarCampeon();

        }
    }

    private void actualizarLabels() {

        labelEscudo1.setIcon(new ImageIcon(partidoActual.getEquipo1().getEscudo()));
        labelEscudo2.setIcon(new ImageIcon(partidoActual.getEquipo2().getEscudo()));

        nombreEquipo1Label.setText(partidoActual.getEquipo1().getNombre());
        nombreEquipo2Label.setText(partidoActual.getEquipo2().getNombre());

        golesEquipo1Label.setText(String.valueOf(partidoActual.getGolesEq1()));
        golesEquipo2Label.setText(String.valueOf(partidoActual.getGolesEq1()));
        golEquipo1Button.setEnabled(true);
        golEquipo2Button.setEnabled(true);

        icono1.setIcon(new ImageIcon(icono(partidos.get(0).getEquipo1(), partidos.get(0).getEquipo1().getIcono(), partidos.get(0).getEquipo1().getIconoPerdio())));
        icono2.setIcon(new ImageIcon(icono(partidos.get(0).getEquipo2(), partidos.get(0).getEquipo2().getIcono(), partidos.get(0).getEquipo2().getIconoPerdio())));
        icono3.setIcon(new ImageIcon(icono(partidos.get(1).getEquipo1(), partidos.get(1).getEquipo1().getIcono(), partidos.get(1).getEquipo1().getIconoPerdio())));
        icono4.setIcon(new ImageIcon(icono(partidos.get(1).getEquipo2(), partidos.get(1).getEquipo2().getIcono(), partidos.get(1).getEquipo2().getIconoPerdio())));
        icono5.setIcon(new ImageIcon(icono(partidos.get(2).getEquipo1(), partidos.get(2).getEquipo1().getIcono(), partidos.get(2).getEquipo1().getIconoPerdio())));
        icono6.setIcon(new ImageIcon(icono(partidos.get(2).getEquipo2(), partidos.get(2).getEquipo2().getIcono(), partidos.get(2).getEquipo2().getIconoPerdio())));
        icono7.setIcon(new ImageIcon(icono(partidos.get(3).getEquipo1(), partidos.get(3).getEquipo1().getIcono(), partidos.get(3).getEquipo1().getIconoPerdio())));
        icono8.setIcon(new ImageIcon(icono(partidos.get(3).getEquipo2(), partidos.get(3).getEquipo2().getIcono(), partidos.get(3).getEquipo2().getIconoPerdio())));
        icono9.setIcon(new ImageIcon(icono(equipoSemifinalista1, equipoSemifinalista1.getIcono(), equipoSemifinalista1.getIconoPerdio())));
        icono10.setIcon(new ImageIcon(icono(equipoSemifinalista2, equipoSemifinalista2.getIcono(), equipoSemifinalista2.getIconoPerdio())));
        icono11.setIcon(new ImageIcon(icono(equipoSemifinalista3, equipoSemifinalista3.getIcono(), equipoSemifinalista3.getIconoPerdio())));
        icono12.setIcon(new ImageIcon(icono(equipoSemifinalista4, equipoSemifinalista4.getIcono(), equipoSemifinalista4.getIconoPerdio())));
        icono13.setIcon(new ImageIcon(icono(equipoFinalista1, equipoFinalista1.getIcono(), equipoFinalista1.getIconoPerdio())));
        icono14.setIcon(new ImageIcon(icono(equipoFinalista2, equipoFinalista2.getIcono(), equipoFinalista2.getIconoPerdio())));

        repaint();
    }

    private void determinarGanador() {

        if (partidoActual.getGolesEq1() > partidoActual.getGolesEq2()) {

            partidoActual.setResultado(Resultado.GANA_EQUIPO_1);
            partidoActual.getEquipo2().setEnCompetencia(false);
            System.out.print(partidoActual.getNombre() + ": - ");
            System.out.println("continua " + partidoActual.getEquipo1().getNombre());
            System.out.println("METODO AGREGAR EQUIPO");
            agregarEquipo(partidoActual.getEquipo1());

            //partidoActual.setImagenAMostrar(partidoActual.getImagenGanador()); // Configurar la imagen a mostrar
            //System.out.println("MOSTRAR IMAGEN GANADOR EN POSICION 1");
            mostrarDuranteTiempo(ganador1, 2);

        } else if (partidoActual.getGolesEq1() < partidoActual.getGolesEq2()) {
            partidoActual.setResultado(Resultado.GANA_EQUIPO_2);
            partidoActual.getEquipo1().setEnCompetencia(false);
            System.out.print(partidoActual.getNombre() + ": - ");
            System.out.println("continua " + partidoActual.getEquipo2().getNombre());
            System.out.println("METODO AGREGAR EQUIPO");
            agregarEquipo(partidoActual.getEquipo2());

            mostrarDuranteTiempo(ganador2, 2);

            //partidoActual.setImagenAMostrar(partidoActual.getImagenGanadorPorSorteo()); // Configurar la imagen a mostrar
            //System.out.println("MOSTRAR IMAGEN GANADOR EN POSICION 2");
        } else {
            Random random = new Random();
            int ganadorAleatorio = random.nextInt(2); // Genera 0 o 1

            if (ganadorAleatorio == 0) {
                partidoActual.setResultado(Resultado.GANA_EQUIPO_1);
                partidoActual.getEquipo2().setEnCompetencia(false);
                System.out.print(partidoActual.getNombre() + ": - ");
                System.out.println("continua " + partidoActual.getEquipo1().getNombre() + " (Por sorteo)");
                System.out.println("METODO AGREGAR EQUIPO");
                agregarEquipo(partidoActual.getEquipo1());

                //partidoActual.setImagenAMostrar(partidoActual.getImagenGanador()); // Configurar la imagen a mostrar
                //System.out.println("MOSTRAR IMAGEN GANADORPORSORTEO EN POSICION 1 SORTEO");
                mostrarDuranteTiempo(ganador1sorteo, 2);

            } else {
                partidoActual.setResultado(Resultado.GANA_EQUIPO_2);
                partidoActual.getEquipo1().setEnCompetencia(false);
                System.out.print(partidoActual.getNombre() + ": - ");
                System.out.print("continua " + partidoActual.getEquipo2().getNombre() + " (Por sorteo) ");
                System.out.println("METODO AGREGAR EQUIPO");
                agregarEquipo(partidoActual.getEquipo2());

                //partidoActual.setImagenAMostrar(partidoActual.getImagenGanadorPorSorteo()); // Configurar la imagen a mostrar
                //System.out.println("MOSTRAR IMAGEN GANADORPORSORTEO EN POSICION 2 SORTEO");
                mostrarDuranteTiempo(ganador2sorteo, 2);

            }
        }

    }

    private void agregarEquipo(Equipo equipo) {

        if (indicePartidoActual == 0) {
            System.out.println("METODO SETEAR DATO");
            setearDatos(equipoSemifinalista1, equipo);
            System.out.println("METODO AGREGAR PARTIDO");
            agregarPartidoLista();
            System.out.println("semifinalista 1 :" + equipoSemifinalista1.getNombre());

        } else if (indicePartidoActual == 1) {
            System.out.println("METODO SETEAR DATO");
            setearDatos(equipoSemifinalista2, equipo);

            agregarPartidoLista();
            System.out.println("semifinalista 2 :" + equipoSemifinalista2.getNombre());
        } else if (indicePartidoActual == 2) {
            System.out.println("METODO SETEAR DATO");
            setearDatos(equipoSemifinalista3, equipo);
            System.out.println("METODO AGREGAR PARTIDO");
            agregarPartidoLista();
            System.out.println("semifinalista 3 :" + equipoSemifinalista3.getNombre());
        } else if (indicePartidoActual == 3) {
            System.out.println("METODO SETEAR DATO");
            setearDatos(equipoSemifinalista4, equipo);
            System.out.println("METODO AGREGAR PARTIDO");
            agregarPartidoLista();
            System.out.println("semifinalista 4 :" + equipoSemifinalista4.getNombre());
        } else if (indicePartidoActual == 4) {
            System.out.println("METODO SETEAR DATO");
            setearDatos(equipoFinalista1, equipo);
            System.out.println("METODO AGREGAR PARTIDO");
            agregarPartidoLista();
            System.out.println("finalista 1 :" + equipoFinalista1.getNombre());
        } else if (indicePartidoActual == 5) {
            System.out.println("METODO SETEAR DATO");
            setearDatos(equipoFinalista2, equipo);
            System.out.println("METODO AGREGAR PARTIDO");
            agregarPartidoLista();
            System.out.println("finalista 2 :" + equipoFinalista2.getNombre());
        } else if (indicePartidoActual == 6) {
            System.out.println("METODO SETEAR DATO");
            setearDatos(equipoCampeon, equipo);
            System.out.println("METODO AGREGAR PARTIDO");
            agregarPartidoLista();
            System.out.println("Campeon :" + equipoCampeon.getNombre());

            actualizarLabels();

            //System.out.println("***************     FIN DEL TORNEO                    *********** ");
        }

    }

    private void mostrarCampeon() {

        System.out.println("CAMPEOON " + equipoCampeon.getNombre());

        ImageIcon escudo = new ImageIcon(equipoCampeon.getEscudo());
        Image escudoEscalada = escudo.getImage().getScaledInstance(380, 380, Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(escudoEscalada);
        labelEscudoCampeon.setIcon(iconoEscalado);
        labelEscudoCampeon.setVisible(true);

        //VISUALIZAR ARCHIVOS GIF
        confettiLabelGif.setVisible(true);
        papelitosLabelGif.setVisible(true);
        papelitosLabelGif2.setVisible(true);

        //OCULTAR DEMAS LABELS
        nombreEquipo1Label.setVisible(false);
        nombreEquipo2Label.setVisible(false);
        labelEscudo1.setVisible(false);
        labelEscudo2.setVisible(false);
        progressBar.setVisible(false);
        golesEquipo1Label.setVisible(false);
        golesEquipo2Label.setVisible(false);
        golEquipo1Button.setVisible(false);
        golEquipo2Button.setVisible(false);
        animatedGifLabelEq1.setVisible(false);
        animatedGifLabelEq2.setVisible(false);
        botonBarraProgressEq1.setVisible(false);
        botonBarraProgressEq2.setVisible(false);
        botonProximoPartido.setVisible(false);
        golEquipo1ButtonX10.setVisible(false);
        golEquipo2ButtonX10.setVisible(false);

        esperar(60);
    }

    private void agregarPartidoLista() {

        if (equipoSemifinalista1.getNombre() != null && equipoSemifinalista2.getNombre() != null && indicePartidoActual == 1) {

            partidoSemi1 = new Partido("Partido Semifinal 1", equipoSemifinalista1, equipoSemifinalista2, 0, 0, 5);         //VER

            partidos.add(partidoSemi1);  // Agrega el partido a la lista
            System.out.println("CREADO " + partidoSemi1.getNombre() + ": " + partidoSemi1.getEquipo1().getNombre() + " - " + partidoSemi1.getEquipo2().getNombre());

        } else if (equipoSemifinalista3.getNombre() != null && equipoSemifinalista4.getNombre() != null && indicePartidoActual == 3) {
            partidoSemi2 = new Partido("Partido Semifinal 2", equipoSemifinalista3, equipoSemifinalista4, 0, 0, 6);

            partidos.add(partidoSemi2);  // Agrega el partido a la lista
            System.out.println("CREADO " + partidoSemi1.getNombre() + ": " + partidoSemi1.getEquipo1().getNombre() + " - " + partidoSemi1.getEquipo2().getNombre());
        } else if (equipoFinalista1.getNombre() != null && equipoFinalista2.getNombre() != null && indicePartidoActual == 5) {
            partidoFinal = new Partido("Partido Final", equipoFinalista1, equipoFinalista2, 0, 0, 7);

            partidos.add(partidoFinal);// Agrega el partido a la lista
            System.out.println("CREADO " + partidoFinal.getNombre() + ": " + partidoFinal.getEquipo1().getNombre() + " - " + partidoFinal.getEquipo2().getNombre());
        }

    }

    private void esperar(int segundos) {

        int seg = segundos * 1000;
        try {

            Thread.sleep(seg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void setearDatos(Equipo equipoReceptor, Equipo equipo) {

        equipoReceptor.setNombre(equipo.getNombre());
        equipoReceptor.setNombre(equipo.getNombre());
        equipoReceptor.setEscudo(equipo.getEscudo());
        equipoReceptor.setIcono(equipo.getIcono());
        equipoReceptor.setIconoPerdio(equipo.getIconoPerdio());
        equipoReceptor.setColor1(equipo.getColor1());
        equipoReceptor.setColor2(equipo.getColor2());
        equipoReceptor.setGoles(equipo.getGoles());
        equipoReceptor.setEnCompetencia(equipo.getEnCompetencia());

    }

    private void actualizarProgressBar() {
        int totalGoles = partidoActual.getGolesEq1() + partidoActual.getGolesEq2();
        int porcentajeEquipo1 = (partidoActual.getGolesEq1() * 100) / totalGoles;
        progressBar.setValue(porcentajeEquipo1);
        progressBar.setString(porcentajeEquipo1 + "%");
    }

    private void reiniciarCronometro() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                minutosTranscurridos = 0;
                segundosTranscurridos = 0;
                labelReloj.setText(getTiempoFormateado(minutosTranscurridos, segundosTranscurridos));

                // Oculta el botón de reinicio nuevamente
                botonProximoPartido.setVisible(false);
                finDePartido = false;

                // Iniciar el nuevo hilo del cronómetro
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        while (true) {
                            try {
                                Thread.sleep((int) (1000 / velocidad));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }

                            segundosTranscurridos++;
                            if (segundosTranscurridos == 60) {
                                segundosTranscurridos = 0;
                                minutosTranscurridos++;
                            }

                            labelReloj.setText(getTiempoFormateado(minutosTranscurridos, segundosTranscurridos));

                            if (minutosTranscurridos >= 90) {
                                thread.interrupt();
                                finDePartido = true;

                                // Muestra el botón de reinicio
                                labelReloj.setText("FIN DEL PARTIDO");

                                golEquipo1Button.setEnabled(false);
                                golEquipo2Button.setEnabled(false);

                                botonProximoPartido.setVisible(true);

                                /*
                                try {
                                    // Espera 2 segundos
                                    Thread.sleep(2000);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                 */
                                botonProximoPartido.doClick();

                                break;
                            }
                        }
                    }
                });

                thread.start();
            }
        });
    }

    private void mostrarDuranteTiempo(JLabel label, int seg) {
        label.setVisible(true);

        Timer timer = new Timer((seg * 1000), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Oculta el JLabel después del tiempo especificado
                label.setVisible(false);
            }
        });

        // Inicia el Timer
        timer.setRepeats(false); // Para que solo se ejecute una vez
        timer.start();
    }

    private Image icono(Equipo equipo, Image icono, Image iconoPerdedor) {

        if (equipo.getIcono() != null) {

            if (equipo.getEnCompetencia()) {
                return icono;
            } else {
                return iconoPerdedor;

            }

        }
        return iconoVacio;
    }

    private double calcularVelocidad(int segundosDeseados) {
        return switch (segundosDeseados) {
            case 15 ->
                540.0;
            case 30 ->
                180.0;
            case 60 ->
                90.0;
            case 180 ->
                30.0;
            case 540 ->
                10.0;
            default ->
                90.0;
        };
    }

    private String getTiempoFormateado(int minutos, int segundos) {
        int tiempoTotal = minutos * 60 + segundos;
        int minutosFormateados;
        int segundosFormateados;

        if (tiempoTotal < 45 * 60) {
            // Primer tiempo
            minutosFormateados = tiempoTotal / 60;
            segundosFormateados = tiempoTotal % 60;
            return String.format("PT %02d:%02d", minutosFormateados, segundosFormateados);
        } else {
            // Segundo tiempo
            tiempoTotal -= 45 * 60;
            minutosFormateados = tiempoTotal / 60;
            segundosFormateados = tiempoTotal % 60;
            return String.format("ST %02d:%02d", minutosFormateados, segundosFormateados);
        }
    }

    private class AnimatedGifLabel extends JLabel {

        private final ImageIcon progressGif;
        private final ImageIcon backgroundGif;
        private int progress;

        public AnimatedGifLabel(String progressGifFileName, String backgroundGifFileName) {
            progressGif = new ImageIcon(getClass().getResource(progressGifFileName));
            backgroundGif = new ImageIcon(getClass().getResource(backgroundGifFileName));
            setIcon(progressGif);
            //setBounds(50, 100, 30, 200);

            // Añadir borde alrededor del componente
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); // Ajusta el grosor y color del borde
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int newProgress) {
            progress = newProgress;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = 30;
            int height = (int) (getHeight() * (progress / 100.0));

            // Dibujar el fondo del progreso
            g.drawImage(progressGif.getImage(), 0, 0, width, 200, null);

            // Dibujar la porción del GIF de progreso correspondiente al progreso
            //g.drawImage(backgroundGif.getImage(), 0, 0, width, height, 0, 0, width, height, null);
            g.drawImage(backgroundGif.getImage(), 0, getHeight() - height, width, getHeight(), 0, progressGif.getIconHeight() - height, width, progressGif.getIconHeight(), null);
            // Dibujar la porción del GIF de progreso correspondiente al progreso (invertir)

        }
    }

    @Override
    public void keyPressed(KeyEvent e) {  //IMPLEMENTAR MÉTODO DE KEYLISTENER
        char tecla = e.getKeyChar();

        // Asignar acciones según la tecla presionada
        switch (tecla) {
            case 'C':
                botonProximoPartido.doClick();
                break;
            case '1':
                botonBarraProgressEq1.doClick();
                break;
            case '3':
                botonBarraProgressEq2.doClick();
                break;
            case '4':
                golEquipo1Button.doClick();
                break;
            case '6':
                golEquipo2Button.doClick();
                break;
            case '7':
                golEquipo1ButtonX10.doClick();
                break;
            case '9':
                golEquipo2ButtonX10.doClick();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Puedes implementar acciones adicionales si es necesario
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Puedes implementar acciones adicionales si es necesario
    }

}
