import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ServidorRisk {
    private static final int puerto = 6666;
    private static final List<Jugador> jugadores = new ArrayList<>();
    private static final int numCores = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService pool = Executors.newFixedThreadPool(numCores);

    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(puerto);
            System.out.println("Esperando conexión de jugadores...\n");

            while (true){
                try{
                    Socket clienteRisk = ss.accept();

                    System.out.println("Cliente conectado: "+clienteRisk);

                    //Crear nuevo jugador añadirlo a la lista

                    BufferedReader br = new BufferedReader(new InputStreamReader(clienteRisk.getInputStream()));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(clienteRisk.getOutputStream()));
                    pw.println("Por favor, introduce tu nombre: ");
                    String nom = br.readLine();
                    Jugador jugador = new Jugador(nom.trim(),0);
                    jugadores.add(jugador);

                    //Crear nuevo manejador de cliente y asignarle el jugador
                    ManejadorCliente manejadorCliente = new ManejadorCliente(clienteRisk,jugador);

                    //Ejecutar el manejador e risk en el pool de hilos
                    pool.execute(manejadorCliente);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                pool.shutdown();
                if(ss != null){
                    ss.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
