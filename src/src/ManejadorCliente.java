import java.io.*;
import java.net.Socket;
import java.util.List;

public class ManejadorCliente implements Runnable{

    private final Socket cliente;
    private final Jugador jugador;

    private final int numConexion;
    private static Mapa mapa;


    public ManejadorCliente(Socket s, Jugador j, int numConexion){
        this.cliente=s;
        this.jugador=j; //referencia al jugador asociado
        this.numConexion=numConexion; //orden en el que va el juego
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.cliente.getOutputStream()));
             ){


            pw.println("Hola, "+this.jugador.getNombre()+". Eres la conexíón numero: "+this.numConexion);

            //implementar la logica del juego aqui
            if(mapa.quedanPaisesSinOcupar()){ //mientras que queden paises sin ocupar, los jugadores iran ocupandolos, poniendo una tropa en cada pais
                int num = mapa.getPaisesLibres().size();
                for(int i = 0;i<num;i++){
                    mapa.getPaisesLibres().get(i).toString(); //se muestran los paises que no tienen dueño para qu el jugador coloque una tropa en uno de ellos
                }
            } else if (!mapa.mapaConquistado()) { //mientras que todo el mapa sea conquistado por un unico jugador la partida no acaba.

            } else{
                System.out.println("Ha ganado el jugador: " + mapa.getPaises().get(0).getPropietario().getNombre());
            }


        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                this.cliente.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void actualizarPartida(Mapa m) {
        mapa = m;
    }
}
