import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ServidorRisk {
    private static final int puerto = 6666;
    private static  List<Jugador> jugadores = new ArrayList<>(); //no puede ser final, la clase jugador tiene una lista con los paises ocupados que varian
    private static final int numCores = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService pool = Executors.newFixedThreadPool(numCores);

    private static Mapa mapa = new Mapa();

    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(puerto);
            System.out.println("Esperando conexión de jugadores...\n");

            while (true) {
                try {
                    Socket clienteRisk = ss.accept();

                    System.out.println("Cliente conectado: " + clienteRisk); //que hace esto?

                    //Crear nuevo jugador añadirlo a la lista

                    BufferedReader br = new BufferedReader(new InputStreamReader(clienteRisk.getInputStream()));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(clienteRisk.getOutputStream()));
                    ObjectInputStream ois = new ObjectInputStream(clienteRisk.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(clienteRisk.getOutputStream());


                    pw.println("Por favor, introduce tu nombre: ");
                    String nom = br.readLine();
                    Jugador jugador = new Jugador(nom.trim(), 0);
                    jugadores.add(jugador);

                    //Crear nuevo manejador de cliente y asignarle el jugador
                    ManejadorCliente manejadorCliente = new ManejadorCliente(clienteRisk, jugador);

                    //Ejecutar el manejador e risk en el pool de hilos
                    pool.execute(manejadorCliente);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                pool.shutdown();
                if (ss != null) {
                    ss.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void actualizarPartida(Mapa m, List<Jugador> j) {
        jugadores = j;
        mapa = m;

    }
}