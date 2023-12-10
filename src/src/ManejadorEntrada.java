import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class ManejadorEntrada implements Callable<Jugador> {

    private final Socket socket;


    public ManejadorEntrada(Socket s) {
        this.socket = s;
    }

    @Override
    public Jugador call() {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
             BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))) {

            pw.println("Por favor, introduce tu nombre: ");
            String nombre = br.readLine();
            System.out.println("Nombre del jugador: " + nombre);
            return new Jugador(nombre,0);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
