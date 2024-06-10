package torneoapp;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TorneoApp extends JFrame {

    private List<Equipo> listaEquipos, listaEquiposSeleccionados;
    private List<Partido> listaPartidos;
    private Partido partidofinal;
    private int duracionPartido;
   
    
    

    public TorneoApp() {
        listaEquipos = new ArrayList<>();

        // Crear equipos y agregarlos a la lista
        crearEquipo("R. CENTRAL", "escRCentral.png", "escRCentralP.png", "escRCentralBN.png", Color.YELLOW, Color.BLUE);
        crearEquipo("NEWELL´S", "escNewells.png", "escNewellsP.png", "escNewellsBN.png", Color.RED, Color.BLACK);
        crearEquipo("RIVER P.", "escRPlate.png", "escRPlateP.png", "escRPlateBN.png", Color.RED, Color.WHITE);
        crearEquipo("BOCA JR.", "escBoca.png", "escBocaP.png", "escBocaBN.png", Color.YELLOW, Color.BLUE);
        crearEquipo("SAN LORENZO", "escSanLorenzo.png", "escSanLorenzoP.png", "escSanLorenzoBN.png", Color.BLUE, Color.RED);
        crearEquipo("HURACAN", "escHuracan.png", "escHuracanP.png", "escHuracanBN.png", Color.WHITE, Color.RED);
        crearEquipo("INDEPENDIENTE", "escIndependiente.png", "escIndependienteP.png", "escIndependienteBN.png", Color.RED, Color.RED);
        crearEquipo("RACING", "escRacing.png", "escRacingP.png", "escRacingBN.png", Color.WHITE, Color.blue);
        crearEquipo("ESTUDIANTES", "escEstudiantes.png", "escEstudiantesP.png", "escEstudiantesBN.png", Color.WHITE, Color.blue);
        crearEquipo("GIMNASIA", "escGimnasia.png", "escGimnasiaP.png", "escGimnasiaBN.png", Color.WHITE, Color.blue);
        crearEquipo("COLON", "escColon.png", "escColonP.png", "escColonBN.png", Color.WHITE, Color.blue);
        crearEquipo("UNION", "escUnion.png", "escUnionP.png", "escUnionBN.png", Color.WHITE, Color.blue);
        crearEquipo("VELES", "escVeles.png", "escVelesP.png", "escVelesBN.png", Color.WHITE, Color.blue);

    }

    public void iniciarTorneo(List<Equipo> equiposSeleccionados, int duracionPartido) {
        this.listaEquiposSeleccionados = equiposSeleccionados;
        this.duracionPartido = duracionPartido;

        //CUARTOS DE FINAL
        Equipo equipo1 = equiposSeleccionados.get(0);
        Equipo equipo2 = equiposSeleccionados.get(1);
        Equipo equipo3 = equiposSeleccionados.get(2);
        Equipo equipo4 = equiposSeleccionados.get(3);
        Equipo equipo5 = equiposSeleccionados.get(4);
        Equipo equipo6 = equiposSeleccionados.get(5);
        Equipo equipo7 = equiposSeleccionados.get(6);
        Equipo equipo8 = equiposSeleccionados.get(7);

        Partido partidoCuartos1 = new Partido("Cuartos De Final (1)", equipo1, equipo2, 0, 0, 1);
        Partido partidoCuartos2 = new Partido("Cuartos De Final (2)", equipo3, equipo4, 0, 0, 2);
        Partido partidoCuartos3 = new Partido("Cuartos De Final (3)", equipo5, equipo6, 0, 0, 3);
        Partido partidoCuartos4 = new Partido("Cuartos De Final (4)", equipo7, equipo8, 0, 0, 4);
        
        

        listaPartidos = new ArrayList<>();
        listaPartidos.add(partidoCuartos1);
        listaPartidos.add(partidoCuartos2);
        listaPartidos.add(partidoCuartos3);
        listaPartidos.add(partidoCuartos4);

        comernzarTorneo(listaPartidos, duracionPartido);
        System.out.println("COMIENZA TORNEO");

    }

    
    
    
    
    private void crearEquipo(String nombre, String escudoPath, String iconoPath, String iconoPerdioPath, Color color1, Color color2) {

        System.out.println("Ruta de la imagen encontrada: " + escudoPath);

        ImageIcon escudoImagen = new ImageIcon(getClass().getResource("img/equipos/" + escudoPath));
        ImageIcon iconoImagen = new ImageIcon(getClass().getResource("img/equipos/" + iconoPath));
        ImageIcon iconoPerdioImagen = new ImageIcon(getClass().getResource("img/equipos/" + iconoPerdioPath));
        

        Image escudo = escudoImagen.getImage();
        Image icono = iconoImagen.getImage();
        Image iconoPerdio = iconoPerdioImagen.getImage();
        

        Equipo equipo = new Equipo(nombre, escudo, icono, iconoPerdio, color1, color2);
        listaEquipos.add(equipo);
    }

    public List<Equipo> getListaEquipos() {
        return listaEquipos;
    }

    private void comernzarTorneo(List<Partido> listaPartidos, int duracionPartido) {
        SwingUtilities.invokeLater(() -> {
            Juego juego = new Juego(listaPartidos, duracionPartido);
            juego.setVisible(true);
        });
    }

    
    
    
    
    
    
    public static void main(String[] args) {
        TorneoApp torneoApp = new TorneoApp();

        SwingUtilities.invokeLater(() -> {
            Presentacion presentacion = new Presentacion(torneoApp, torneoApp.getListaEquipos());
            presentacion.setVisible(true);
        });
    }

}
