import java.io.*;
import java.net.Socket;
import java.util.List;

public class ManejadorCliente implements Runnable{

    private final Socket cliente;
    private final Jugador jugador;
    private static Mapa mapa;


    public ManejadorCliente(Socket s, Jugador j){
        this.cliente=s;
        this.jugador=j; //referencia al jugador asociado
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.cliente.getOutputStream()));
             ){

            //Procesar mensajes de los clientes...
            String entrada;
            while ((entrada = br.readLine()) != null ){
                System.out.println(jugador.getNombre() + ": "+entrada); //esto para que?
            }

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
        }

    }

    public void actualizarPartida(Mapa m) {
        mapa = m;
    }
}
