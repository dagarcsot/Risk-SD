import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ManejadorCliente implements Runnable{

    private final Socket cliente;
    private final Jugador jugador;
    private final Mapa mapa;


    public ManejadorCliente(Socket s, Jugador j, Mapa mapa){
        this.cliente=s;
        this.jugador=j; //referencia al jugador asociado
        this.mapa = mapa; //pasamos el mapa con el que va a jugar
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.cliente.getOutputStream()))){

            Scanner entrada = new Scanner(System.in);
            pw.println("Hola, "+this.jugador.getNombre());

            if(mapa.quedanPaisesSinOcupar()){
                //mientras que queden paises sin ocupar, los jugadores iran ocupandolos, poniendo una tropa en cada pais
                int num = mapa.getPaisesLibres().size();

                pw.println("Que pais quieres ocupar: "); //Mandamos cliente
                pw.println(num); //Cliente

                for(int i = 0;i<num;i++){
                    pw.println(i+". "+mapa.getPaisesLibres().get(i).toString());
                    //mandas al cliente los paises que no tienen dueño para que el jugador coloque una tropa en uno de ellos
                }
                int opc = Integer.parseInt(br.readLine());
                //Asignamos el pais al propietario y lo añadimos a su lista de paises
                String nom = mapa.getPaisesLibres().get(opc).getNombre();
                this.jugador.addPais(mapa.getPaisPorNombre(nom));
                this.jugador.getPais(nom).setNumTropas(1);
                mapa.getPaisesLibres().get(opc).setPropietario(jugador);

                //Yo creo que no hace falta mas
                //Revisa el orden del codigo porque es importante porque una vez le asignameos propietario al pais
                //ya no vamos a encontrarlo en la lista de paises libres
                //Lo que no se hacer es lo get las tropas

            } else if (!mapa.mapaConquistado()) {
                //mientras que el mapa sea conquistado por un unico jugador la partida no acaba.




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
}
