import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class ServidorRisk {
    private static final int puerto = 6666;
    private static final int TIEMPO_ESPERA = 60000; //un minuto en milisegundos
    private static  List<Jugador> jugadores = new ArrayList<>();
    private static ServerSocket ss;
    private static boolean aceptarConexiones = true;
    private static final int MIN_JUGADORES = 2;
    private static final int MAX_JUGADORES = 6;
    private static boolean partidaEmpezada = false;
    private static boolean partidaAcabada = false;
    private static ExecutorService pool;

    private static Mapa mapa;

    public static void main(String[] args) {
        try {
            pool = Executors.newFixedThreadPool(MAX_JUGADORES);
            //a veces sobrarán hilos, no se como crearlo con el numero de jugadores real
            ss = new ServerSocket(puerto);
            mapa = new Mapa(); //creamos un unico mapa para la partida
            System.out.println("Esperando conexión de jugadores...\n");
            iniciarTemporizador(); //empezamos a aceptar conexiones

            while (true) {
                try {
                    if (aceptarConexiones && !partidaEmpezada && jugadores.size() < MAX_JUGADORES) {
                        Socket clienteRisk = ss.accept(); //aceptamos al jugador

                        try(BufferedReader br = new BufferedReader(new InputStreamReader(clienteRisk.getInputStream()));
                            PrintWriter pw = new PrintWriter(new OutputStreamWriter(clienteRisk.getOutputStream()));
                            ObjectInputStream ois = new ObjectInputStream(clienteRisk.getInputStream());
                            ObjectOutputStream oos = new ObjectOutputStream(clienteRisk.getOutputStream())){

                            //Crear nuevo jugador añadirlo a la lista
                            pw.println("Por favor, introduce tu nombre: ");
                            String nom = br.readLine();
                            Jugador jugador = new Jugador(nom.trim(), 0);
                            jugadores.add(jugador);

                            while(!partidaAcabada){
                                oos.writeObject(mapa);
                                oos.writeObject(jugador);
                                mapa = (Mapa) ois.readObject();

                                //Ejecutar el manejador risk en el pool de hilos
                                for (int i = 0; i < jugadores.size(); i++) {
                                    pool.execute(new ManejadorCliente(clienteRisk, jugadores.get(i), mapa));
                                }
                                partidaAcabada = mapa.mapaConquistado();

                                //hay que modificar este bucle, esta mal (no cambiamos valor del while)
                                //cambios
                            }
                            System.out.println("Ha ganado el jugador " + mapa.getPaises().get(0).getPropietario().getNombre());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    } else {
                        if (jugadores.size() >= MAX_JUGADORES) {
                            System.out.println("Límite de conexionnes alcanzada. Rechazando al cliente");
                        }
                        if (partidaEmpezada) {
                            System.out.println("La partida ya ha comenzado, no puedes unirte...");
                        }
                        if (!aceptarConexiones) {
                            System.out.println("El tiempo para conectarse a la partida ha excedido, ya no puedes unirte...");
                        }
                    }
                } catch (IOException e){
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

    private static void iniciarTemporizador(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                detenerAceptacionConexiones();
            }
        }, TIEMPO_ESPERA);
    }

    private static void detenerAceptacionConexiones(){
        aceptarConexiones = false;
        System.out.println("No se aceptarán más conexiones");
    }

}