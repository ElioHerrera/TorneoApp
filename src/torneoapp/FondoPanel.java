package torneoapp;

import javax.swing.*;
import java.awt.*;

public class FondoPanel extends JPanel {

    private Image imagenFondo;

    public FondoPanel(String rutaImagen) {
        this.imagenFondo = new ImageIcon(getClass().getResource("img/fondoVentanaPrincipal.jpg")).getImage();
        setOpaque(false); // Hacer que el panel sea transparente
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
    }
}