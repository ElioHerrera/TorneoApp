package torneoapp;

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Equipo {

    private String nombre;
    private Image escudo;
    private Image icono;
    private Image iconoPerdio;
   
    private Color color1;
    private Color color2;
    private int goles;
    private boolean enCompetencia;

    // Constructor
    public Equipo(String nombre, Image escudo, Image icono, Image iconoPerdio, Color color1, Color color2) {
        this.nombre = nombre;
        this.escudo = escudo;
        this.icono = icono;
        this.iconoPerdio = iconoPerdio;
        this.color1 = color1;
        this.color2 = color2;
        this.goles = 0;
        this.enCompetencia = true;
        
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Image getEscudo() {
        return escudo;

    }

    public Image getIcono() {
        return icono;

    }

    public Image getIconoPerdio() {
        return iconoPerdio;

    }

    public Color getColor1() {
        return color1;
    }

    

    public Color getColor2() {
        return color2;
    }

    public void setColor1(Color color1){
    
    this.color1 = color1;
    }
     public void setColor2(Color color2){
    
    this.color2 = color2;
    }
     
     public void setEscudo(Image escudo) {
        this.escudo = escudo;

    }

    public void setIconoPerdio(Image iconoPerdio) {
       this.iconoPerdio = iconoPerdio;

    }

    public void setIcono(Image icono) {
       this.icono = icono;

    }
     
     
     
     
     
    

    public int getGoles() {
        return goles;
    }

    public void setGoles(int goles) {
        this.goles = goles;
    }
    
    public boolean getEnCompetencia(){
    return enCompetencia;
    }
    
    public void setEnCompetencia(boolean enCompetencia){
    this.enCompetencia = enCompetencia;
    }
   

}
