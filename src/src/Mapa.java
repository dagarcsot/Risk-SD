import java.util.ArrayList;
import java.util.List;

public class Mapa {
    private List<Continente> contintentes;
    private GrafoPaises<Pais> gp = new GrafoPaises<Pais>();
    Mapa(List<Jugador> jugadores){
        this.contintentes = new ArrayList<>();
        this.contintentes.add(new Continente("America del Norte"));
        this.contintentes.add(new Continente("America del Sur"));
        this.contintentes.add(new Continente("Oceania"));
        this.contintentes.add(new Continente("Africa"));
        this.contintentes.add(new Continente("Europa"));
        this.contintentes.add(new Continente("Asia"));


    }


}
