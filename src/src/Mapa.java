import java.util.List;

public class Mapa {
    private List<Jugador> jugadores;
    private List<Continente> contintentes;

    Mapa(List<Jugador> jugadores,List<Continente> continentes){
        this.jugadores = jugadores;
        this.contintentes = continentes;
    }
}
