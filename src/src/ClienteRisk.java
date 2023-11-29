import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClienteRisk {
    private static final int puerto = 6666;
    private static Scanner entrada;

    public static void main(String[] args) {
        try{
            Socket socket = new Socket("localhost",puerto);
            System.out.println("Conectado al servidor\n");

            //Obtener nombre del jugador
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println(br.readLine());
            entrada = new Scanner(System.in);
            String s = entrada.nextLine().trim();
            pw.println(s);

            //Asignarle al usuario un hilo


            //Manejar la salida del servidor


        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
