import java.util.List;

public class Pais {
    private String nombre;
    private int nomTropas; //num tropas que tiene ese país
    private Jugador propietario; //jugador que tiene el país
    private List<Pais> paisesFrontera;

    Pais(String nombre){
        this.nombre = nombre;
        this.propietario = null;
        this.nomTropas = 0;
    }

    @Override
    public String toString() {
        return "Pais: " + this.nombre + "\nPropietario: " + ((this.propietario != null) ? this.propietario.getNombre() : "No tiene propietario") + "\n";
    }


}
