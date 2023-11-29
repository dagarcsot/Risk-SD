import java.io.*;
import java.net.Socket;

public class ManejadorCliente implements Runnable{

    private final Socket cliente;
    private final Jugador jugador;

    public ManejadorCliente(Socket s, Jugador j){
        this.cliente=s;
        this.jugador=j; //referencia al jugador asociado
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.cliente.getOutputStream()))){

            //Procesar mensajes de los clientes...
            String entrada;
            while ((entrada = br.readLine()) != null ){
                System.out.println(jugador.getNombre() + ": "+entrada);
            }

            //implementar la logica del juego aqui





        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
