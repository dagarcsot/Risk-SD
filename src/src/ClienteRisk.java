import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClienteRisk {
    private static final int puerto = 6666;
    private static Scanner entrada;

    private static void elegirJugada(){
        int opcion=0;

        do{
            System.out.println("Que jugada desea realizar: ");
            System.out.println("1. Canjear tarjeta");
            System.out.println("2. Reforzar un territorio");
            System.out.println("3. Atacar un territorio");
            System.out.println("4. Acabar mi turno");
            Scanner entrada = new Scanner(System.in);

            try{
                opcion = entrada.nextInt();
            } catch (NumberFormatException e){
                e.printStackTrace();
            }

        } while (opcion<0 || opcion>4);

        switch (opcion){
            case 1:
                System.out.println("hola");
            case 2:
                System.out.println("fuck");
            case 3:
                System.out.println("si");
            case 4:
            default:
                break;

        }
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost",puerto)){
            System.out.println("Conectado al servidor\n");

            //Obtener nombre del jugador
            try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))){

                System.out.println(br.readLine());
                entrada = new Scanner(System.in);
                String s = entrada.nextLine().trim();
                pw.println(s);

                //Manejar la salida del servidor
                while (true){
                    String mensaje = br.readLine();
                    if(mensaje == null){
                        break;
                    }
                    System.out.println("Servidor: "+mensaje);
                }
            } catch (IOException e ){
                e.printStackTrace();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
