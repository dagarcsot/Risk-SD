import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorRisk {
    private static final int puerto = 6666;
    private static final int TIEMPO_ESPERA = 60000; // un minuto en milisegundos
    private static final List<Jugador> jugadores = new ArrayList<>();
    private static boolean aceptarConexiones = true;
    private static final int MIN_JUGADORES = 1;
    private static final int MAX_JUGADORES = 6;
    private static boolean partidaEmpezada = false;
    private static boolean partidaAcabada = false;
    private static Mapa mapa;

    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(puerto);
            mapa = new Mapa(); // creamos un único mapa para la partida
            System.out.println("Esperando conexión de jugadores...\n");
            iniciarTemporizador(); // empezamos a aceptar conexiones

            while (true) {
                try {
                    if (aceptarConexiones && !partidaEmpezada && jugadores.size() < MAX_JUGADORES) {
                        Socket clienteRisk = ss.accept(); // aceptamos al jugador

                        try (ObjectOutputStream oos = new ObjectOutputStream(clienteRisk.getOutputStream());
                             ObjectInputStream ois = new ObjectInputStream(clienteRisk.getInputStream())){

                            //mandamos pregunta
                            oos.writeBytes("Hola jugador, como te llamas? \n");
                            oos.flush();
                            //recibimos nombre y creamos jugador
                            String nombre = ois.readLine();
                            Jugador jugador = new Jugador(nombre,0);
                            //lo añadimos a la partida
                            jugadores.add(jugador);

                            oos.writeBytes("Esperamos 1 minuto a que se conecten el resto de jugadores \n");
                            oos.flush();
                            while (aceptarConexiones){ //esperamos 1 min a que se conecten jugadores

                            }

                            while (!partidaAcabada){ //hay que mandar cada vez un jugador, lo pongo mal ahora
                                //mandamos jugador y mapa
                                oos.writeObject(jugadores.get(0));
                                oos.writeObject(mapa);
                                oos.flush();
                                //nos quedamos esperando a que nos manden el mapa otra vez
                                mapa = (Mapa) ois.readObject();
                                if(mapa.mapaConquistado()){ //comprobamos si el mapa ha sido completado
                                    oos.writeBytes("Ha ganado el jugador "+mapa.getPaises().get(0).getPropietario().getNombre()+"\n");
                                    oos.writeBytes("Partida finalizada \n");
                                    oos.flush();
                                }
                            }

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (jugadores.size() >= MAX_JUGADORES) {
                            System.out.println("Límite de conexiones alcanzado. Rechazando al cliente");
                        }
                        if (partidaEmpezada) {
                            System.out.println("La partida ya ha comenzado, no puedes unirte...");
                        }
                        if (!aceptarConexiones) {
                            System.out.println("El tiempo para conectarse a la partida ha excedido, ya no puedes unirte...");
                        }
                    }
                } catch (SocketTimeoutException e) {
                    // Manejar la excepción de tiempo de espera
                    System.out.println("Tiempo de espera para conexiones agotado.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ss != null) {
                    ss.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void iniciarTemporizador() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                detenerAceptacionConexiones();
            }
        }, TIEMPO_ESPERA);
    }

    private static void detenerAceptacionConexiones() {
        aceptarConexiones = false;
        System.out.println("No se aceptarán más conexiones");
    }
}