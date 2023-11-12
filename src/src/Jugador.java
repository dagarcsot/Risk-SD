import java.util.ArrayList;
import java.util.List;

public class Jugador {

    private String nombre;
    private int numTropas; //numero de tropas que tiene el jugador en el mapa
    private int tropasDisponibles; //al empezar un nuevo turno, numero de tropas que posee el jugador para asignar a sus territorios
    private List<Pais> paisesOcupados;

    Jugador(String nombre,int numTropas){   //si hacemos representacion grafica, habria que añadir color
        this.nombre = nombre;
        this.paisesOcupados = new ArrayList<>();
        this.numTropas = numTropas;
    }

    public String getNombre() {
        return this.nombre;
    }
}
