import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteRisk {
    private static final int puerto = 6666;
    private static Scanner entrada = new Scanner(System.in);

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

    private static void elegirJugada(Jugador j){
        int opcion=0;

        do{
            System.out.println("Que jugada desea realizar: ");
            System.out.println("1. Canjear tarjeta");
            System.out.println("2. Reforzar un territorio");
            System.out.println("3. Atacar un territorio");
            System.out.println("4. Acabar mi turno");
            opcion = entrada.nextInt();
        } while (opcion<0 || opcion>4);

        switch (opcion){

            case 1:
                System.out.println("Mano del jugador: ");
                for(int i=0; i<j.getTarjetas().getMano().size();i++){
                    System.out.println("0. "+j.getTarjetas().getMano().get(i).toString());
                }
                System.out.println("Introduce los numeros de las tres tarjetas que quieres canjear: ");
                int pos1 = entrada.nextInt();
                int pos2 = entrada.nextInt();
                int pos3 = entrada.nextInt();
                if(j.getTarjetas().canjearTarjetas(pos1,pos2,pos3) != 0){

                } //falta acabar
            case 2:
                System.out.println("fuck");
            case 3:

                Pais atacante = null;
                List<Pais> puedeAtacar = new ArrayList<>();

                do {
                    System.out.println("Introduzca con cual de sus paises desea atacar");
                    for(int i=0; i<j.getPaisesOcupados().size(); i++){ //mostramos los paises que tiene el jugador
                        System.out.println("0. "+j.getPaisesOcupados().get(i).getNombre());
                    }
                    int pos = entrada.nextInt();
                    atacante = j.getPaisesOcupados().get(pos);

                    if(!atacante.puedeAtacar()){
                        System.out.println("El pais atacante no tiene suficientes tropas para atacar, elige otro pais");
                    } else {
                        // si puede atacar comprobamos que tenga paises a los que puede atacar
                        for(int i=0; i<atacante.getPaisesFrontera().size(); i++){
                            if(!atacante.getPaisesFrontera().get(i).getPropietario().equals(j)){
                                //comprobamos que los paises frontera tienen otro propietario, si se puede atacar, se
                                //añade al arrayList puedeAtacar
                                puedeAtacar.add(atacante.getPaisesFrontera().get(i));
                            }
                        }
                        if(puedeAtacar.size() == 0){
                            System.out.println("El pais que has elegido no puede atacar a ninguna de sus fronteras");
                            System.out.println("¡YA SON TUYAS!");
                            System.out.println("Elija otro pais con el que atacar");
                        }
                    }
                } while (atacante.puedeAtacar() && puedeAtacar.size()>0);
                //comprobamos si el pais tiene mas de 2 tropas para atacar y tiene un pais al que atacar

                System.out.println("Los paises frontera a los que "+atacante.getNombre()+" puede atacar son: ");
                for(int i=0; i<puedeAtacar.size(); i++){ //mostramos los paises frontera de ese pais
                    System.out.println("0. "+puedeAtacar.get(i).getNombre());
                }
                int pos = entrada.nextInt();
                Pais atacado = puedeAtacar.get(pos);
                j.atacarPais(atacante,atacado); //atacamos pais
            case 4:
                System.out.println("Has decidido acabar tu turno");
                break;
            default:
                System.out.println("El numero que has introducido no es valido");
                System.out.println();
                elegirJugada(j);
        }
    }
}
