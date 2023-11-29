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
        try {
            ServerSocket ss = new ServerSocket(puerto);
            System.out.println("Esperando conexión de jugadores...\n");

            //Establecemos un temporizador de 1 minuto
            int tiempoEspera = 60;
            ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
            ScheduledFuture<?> timerFuture = timer.schedule(new Runnable() {
                @Override
                public void run() {
                    //Cerrar servidor cuando transcurre el tiempo
                    System.out.println("Tiempo de espera agotado. Cerrando el servidor.");
                    System.exit(0);
                }
            },tiempoEspera, TimeUnit.SECONDS);

            while (true){
                Socket clienteRisk;
                try {
                    clienteRisk = ss.accept();

                } catch (SocketTimeoutException e){
                    timerFuture.cancel(true); //cancelar temporizador
                    timer.shutdown(); //apagar temporizador
                    break; //salir del bucle para cerrar el servidor
                }

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

            }

        } catch (IOException e){
            e.printStackTrace();
        } finally {
            pool.shutdown();

        }
    }
}
