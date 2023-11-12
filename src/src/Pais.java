import java.util.List;

public class Pais {
    private String nombre;
    private int nomTropas;
    private Jugador propietario;
    private Continente contintente;
    private List<Pais> paisesFrontera;

    Pais(String nombre,Continente continente){
        this.nombre = nombre;
        this.propietario = null;
        this.nomTropas = 0;
        this.contintente = continente;
    }

    @Override
    public String toString() {
        return "Pais: " + this.nombre + "\nPropietario: " + ((this.propietario != null) ? this.propietario.getNombre() : "No tiene propietario") + "\nContinente: " + this.contintente.getNombre();
    }


}
