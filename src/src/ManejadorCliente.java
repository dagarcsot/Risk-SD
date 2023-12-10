import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ManejadorCliente implements Runnable{

    private final Socket cliente;
    private final Jugador jugador;
    private static Mapa mapa;


    public ManejadorCliente(Socket s, Jugador j, Mapa mapa){
        this.cliente=s;
        this.jugador=j; //referencia al jugador asociado
        ManejadorCliente.mapa = mapa; //pasamos el mapa con el que va a jugar
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.cliente.getOutputStream()));
             ObjectInputStream ois = new ObjectInputStream(this.cliente.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(this.cliente.getOutputStream())){

            Scanner entrada = new Scanner(System.in);
            pw.println("Hola, "+this.jugador.getNombre());

           if (!mapa.mapaConquistado()) {
                //mientras que el mapa sea conquistado por un unico jugador la partida no acaba.
                oos.writeObject(mapa);// envio el mapa al jugador
                mapa = (Mapa) ois.readObject();// leemos el mapa modificado
            } else{
                System.out.println("Ha ganado el jugador: " + mapa.getPaises().get(0).getPropietario().getNombre());
            }

        } catch (IOException | ClassNotFoundException e){
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
