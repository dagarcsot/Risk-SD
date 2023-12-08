import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ServidorRisk {
    private static final int puerto = 6666;
    private static  List<Jugador> jugadores = new ArrayList<>();

    private static final int MIN_JUGADORES = 2;
    private static final int MAX_JUGADORES = 6;
    private static ExecutorService pool;

    private static Mapa mapa;

    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            pool = Executors.newFixedThreadPool(MAX_JUGADORES);
            int numConexiones = 0;
            ss = new ServerSocket(puerto);
            System.out.println("Esperando conexión de jugadores...\n");

            while (true) {
                try {
                    if(numConexiones < MIN_JUGADORES){
                        //Espera hasta que se alcance el número mínimo de conexiones
                        continue;
                    }
                    Socket clienteRisk = ss.accept();

                    //Crear nuevo jugador añadirlo a la lista
                    BufferedReader br = new BufferedReader(new InputStreamReader(clienteRisk.getInputStream()));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(clienteRisk.getOutputStream()));
                    ObjectInputStream ois = new ObjectInputStream(clienteRisk.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(clienteRisk.getOutputStream());

                    pw.println("Por favor, introduce tu nombre: ");
                    String nom = br.readLine();
                    Jugador jugador = new Jugador(nom.trim(), 0);
                    jugadores.add(jugador);

                    //Comprobamos que con el nuevo jugador no superamos el máximo
                    if(numConexiones >= MAX_JUGADORES){
                        //Si se alcanza el máximo la conexión es rechazada
                        System.out.println("Límite de conexionnes alcanzada. Rechazando al cliente");
                        clienteRisk.close();
                        continue;
                    } else {
                        //Incrementamos el numero de jugadores
                        numConexiones++;

                        //Ejecutar el manejador risk en el pool de hilos
                        for (int i=0; i<jugadores.size(); i++){
                            pool.submit(new ManejadorCliente(clienteRisk,jugadores.get(i),numConexiones));
                        }
                    }
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

}