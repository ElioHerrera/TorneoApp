package torneoapp;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Partido {
    
    

    private String nombre;
    private Equipo equipo1;
    private Equipo equipo2;
    private int golesEq1;
    private int golesEq2;
    private int numeroPartido;
    private boolean disputado, creado;
    private Resultado resultado;
    private final Image imagenGanador;
    private final Image imagenGanadorPorSorteo;
    private Image imagenAMostrar;

    // Constructor
    public Partido(String nombre, Equipo equipo1, Equipo equipo2, int golesEq1, int golesEq2, int numeroPartido) {
        this.nombre = nombre;
        this.numeroPartido = numeroPartido;
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.golesEq1 = golesEq1;
        this.golesEq2 = golesEq2;
        this.disputado = false;
        this.resultado = null;
        // Asignar las imágenes para todos los partidos
        this.imagenGanador = obtenerImagenGanador();  // Método que retorna la imagen correspondiente
        this.imagenGanadorPorSorteo = obtenerImagenGanadorPorSorteo();  // Método que retorna la imagen correspondiente
        this.imagenAMostrar = null;
        
    }

    // GETTER
    public String getNombre() {
        return nombre;
    }   
    public Equipo getEquipo1() {
        return equipo1;
    }
    public Equipo getEquipo2() {
        return equipo2;
    }
    public int getGolesEq1() {
        return golesEq1;
    }
    public int getGolesEq2() {
        return golesEq2;
    }
    public boolean getDisputado() {
        return disputado;
    }
    public int getNumeroPartido() {
        return numeroPartido;
    }
    public Resultado getResultado() {
        return resultado;
    }
    public Image getImagenGanador() {
        return imagenGanador;
    }
    public Image getImagenGanadorPorSorteo() {
        return imagenGanadorPorSorteo;
    }
    public Image getImagenAMostrar() {
        return imagenAMostrar;
    }

    

    
    
    
    //SETTER
    public void setGolesEq1(int golesEq1) {
        this.golesEq1 = golesEq1;
    }
    public void setGolesEq2(int golesEq2) {
        this.golesEq2 = golesEq2;
    }
    public void setDisputado(boolean disputado) {
        this.disputado = disputado;
    }
    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }
    public void setImagenAMostrar(Image imagenAMostrar) {
        this.imagenAMostrar = imagenAMostrar;
    }

    //OTROS METODOS
    private Image obtenerImagenGanador() {

        ImageIcon imagenGana = new ImageIcon(getClass().getResource("img/efectos/ganador.png"));
        Image imagenGanador = imagenGana.getImage();
        return imagenGanador;
    }
    private Image obtenerImagenGanadorPorSorteo() {
        ImageIcon imagenPierde = new ImageIcon(getClass().getResource("img/efectos/ganadorPorSorteo.png"));
        Image imagenGanador = imagenPierde.getImage();
        return imagenGanador;
    }
    
    public void incrementarGolesEq1() {
        golesEq1++;
    }

    public void incrementarGolesEq2() {
         golesEq2++;
    }
     public void incrementarGolesEq1X10() {
        golesEq1 = golesEq1 +10;
    }

    public void incrementarGolesEq2X10() {
        golesEq2 = golesEq2 +10;
    }
    
    
    

}
