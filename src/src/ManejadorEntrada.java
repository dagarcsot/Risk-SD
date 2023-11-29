import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ManejadorEntrada implements Runnable{

    private final Socket socket;
    private final Jugador jugador;

    public ManejadorEntrada(Socket s, Jugador j){
        this.socket=s;
        this.jugador=j;
    }

    @Override
    public void run() {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
             Scanner entrada = new Scanner(System.in)){

            //Manejar la entrada del usuario
            while (true){
                System.out.println("Es el turno de "+this.jugador.getNombre());
                System.out.println("¿Que quiere hacer?");
                String accion = entrada.nextLine();

                //Enviamos el comando al servidor
                pw.println(accion);

            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
