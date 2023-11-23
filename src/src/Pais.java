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

    public String getNombre(){
        return this.nombre;
    }

    public void agnadirFronteras(List<Pais> l){
        this.paisesFrontera=l;
    }

    public List<Pais> getPaisesFrontera(){
        return this.paisesFrontera;
    }

    public void asignarPropietario(Jugador j){
        this.propietario=j;
    }

    public Jugador getPropietario(){
        return this.propietario;
    }

    @Override
    public String toString() {
        return "Pais: " + this.nombre + "\nPropietario: " + ((this.propietario != null) ? this.propietario.getNombre() : "No tiene propietario") + "\n";
    }
}
