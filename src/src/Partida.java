
import java.util.*;

public class Partida {

    private static List<Jugador> jugadores;
    private static Mapa mapa;
    private static Dado dado;

    Scanner read = new Scanner(System.in);

    public Partida(){
        mapa = new Mapa();
        dado = new Dado();

        System.out.println("Escribe el numero de jugadores (3-6)");
        int numJ = read.nextInt();
        int numTropas = 0; //numero de tropas por jugador

        while(numTropas == 0) {
            switch (numJ) {
                case 3:
                    numTropas = 35;
                    break;
                case 4:
                    numTropas = 30;
                    break;
                case 5:
                    numTropas = 25;
                    break;
                case 6:
                    numTropas = 20;
                    break;
                default:
                    System.out.println("Número de jugadores incorrecto (3-6).");
                    System.out.println("Escribe el numero de jugadores (3-6)");
                    numJ = read.nextInt();

            }
        }
       jugadores = new ArrayList<>();
        for(int i = 0; i < numJ;i++){ //creacion de los jugadores
            System.out.println("Escribe el nombre del jugador");
            String nombre = read.nextLine();
            jugadores.add(new Jugador(nombre,numTropas));
        }

        jugadores = elegirOrden(numJ,jugadores);






    }

    public  static List<Jugador> elegirOrden(int numJ,List<Jugador> listAUX){ //elegir orden
        return null;
    }

    public  void comenzarPartida(){

    }

    public  void turnoJugador(Jugador jugador){

    }

    public  void finalizarPartida(){ //para salir del juego
        System.exit(0);
    }
}
