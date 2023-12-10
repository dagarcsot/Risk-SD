import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteRisk {
    private static final int puerto = 6666;
    private static Scanner entrada = new Scanner(System.in);
    private static boolean partidaAcabada = false;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost",puerto)){
            System.out.println("Conectado al servidor\n");

            //Obtener nombre del jugador
            try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                 ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){

                //Leemos y enviamos el nombre al servidor
                System.out.println(br.readLine());
                entrada = new Scanner(System.in);
                String s = entrada.nextLine().trim();
                pw.println(s);

                while (!partidaAcabada){
                    //Manejar la salida del servidor
                    Mapa mapa = (Mapa) ois.readObject(); //nos dan mapa
                    Jugador jugador = (Jugador) ois.readObject(); //nos dan jugador que modifica el mapa
                    oos.writeObject(elegirJugada(jugador, mapa)); //elegimos opcion

                    //hay que modificar este bucle, esta mal (no cambiamos valor del while)
                    //cambios
                }

            } catch (IOException | ClassNotFoundException e ){
                e.printStackTrace();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static Mapa elegirJugada(Jugador jugador, Mapa mapa){
        //Al final de cada turno se devuelve el mapa modificado
        boolean turnoAcabado = false;

        System.out.println("Turno de: "+jugador.getNombre());

        //Vamos a gestionar el numero de tropas que recibe un jugador al principio de su turno
        System.out.println("El jugador recibe 3 tropas por iniciar su turno");
        int tropas = 3;
        for(int i=0; i<jugador.getContinentes().size(); i++){
            System.out.println("El jugador recibe "+jugador.getContinentes().get(i).getValor()+" por "+
                    jugador.getContinentes().get(i).getNombre());
            tropas = tropas + jugador.getContinentes().get(i).getValor();
        }
        System.out.println(jugador.getNombre()+" ha recibido un total de "+tropas+" tropas");
        System.out.println("Selecciona el pais al que se añaden las tropas: ");
        seleccionarPais(jugador,tropas);

        //Ahora recibe una tarjeta del mazo por empezar el turno
        if(mapa.getMazo().size()!=0){
            Tarjeta t = mapa.getMazo().get(0);
            mapa.getMazo().remove(0);
            System.out.println(jugador.getNombre()+" ha recibido la tarjeta: ");
            System.out.println(t.toString());
            jugador.addTarjeta(t);
        } else {
            System.out.println("No quedan tarjetas en el mazo...");
        }
        int opcion=0;

        if(!mapa.quedanPaisesSinOcupar()){
            int num = mapa.getPaisesLibres().size();

            System.out.println("Que pais quieres ocupar: "); //Mandamos cliente

            for(int i = 0;i<num;i++){
                System.out.println(i+". "+mapa.getPaisesLibres().get(i).toString());
                //mandas al cliente los paises que no tienen dueño para que el jugador coloque una tropa en uno de ellos
            }
            int opc = Integer.parseInt(entrada.nextLine());
            //Asignamos el pais al propietario y lo añadimos a su lista de paises
            String nom = mapa.getPaisesLibres().get(opc).getNombre();
            jugador.addPais(mapa.getPaisPorNombre(nom));
            jugador.getPais(nom).setNumTropas(1);
            mapa.getPaisesLibres().get(opc).setPropietario(jugador);
            jugador.actualizarNumtropas();

            //Yo creo que no hace falta mas
            //Revisa el orden del codigo porque es importante porque una vez le asignameos propietario al pais
            //ya no vamos a encontrarlo en la lista de paises libres
            //Lo que no se hacer es lo get las tropas
        } else{
            while (!turnoAcabado){
                do{
                    System.out.println("Que jugada desea realizar: ");
                    System.out.println("1. Canjear tarjetas");
                    System.out.println("2. Reforzar un territorio");
                    System.out.println("3. Atacar un territorio");
                    System.out.println("4. Acabar mi turno");
                    opcion = entrada.nextInt();
                } while (opcion<0 || opcion>4);

                switch (opcion) {
                    case 1 -> {
                        if(jugador.getManoTarjetas().getTarjetas().size()<3){
                            System.out.println("No tienes suficientes tarjetas");
                            System.out.println("Vuelve cuando tengas al menos 3 tarjetas");
                        }
                        else {
                            System.out.println("Mano del jugador: ");
                            for (int i = 0; i < jugador.getManoTarjetas().getTarjetas().size(); i++) {
                                System.out.println(i+". " + jugador.getManoTarjetas().getTarjetas().get(i).toString());
                            }
                            System.out.println("Introduce los numeros de las tres tarjetas que quieres canjear: ");
                            int pos1 = entrada.nextInt();
                            int pos2 = entrada.nextInt();
                            int pos3 = entrada.nextInt();
                            int canjea = jugador.getManoTarjetas().canjearTarjetas(pos1, pos2, pos3);
                            if (canjea != 0) {
                                System.out.println("Tarjetas canjeadas con exito, has recibido "+canjea+" tropas");
                                System.out.println("Selecciona el pais al que se añaden las tropas: ");
                                Pais p = jugador.getPaisesOcupados().get(jugador.mostrarPaises());
                                p.setNumTropas(p.getNumTropas() + canjea);
                                jugador.actualizarNumtropas();
                            } else {
                                System.out.println("No se pueden canjear las tres tarjetas que has escogido...");
                            }
                        }
                    }
                    case 2 -> {
                        List<Pais> puedeMover = new ArrayList<>();
                        Pais envia = null;
                        do {
                            System.out.println("Introduzca que pais desea reforzar");
                            int pos = jugador.mostrarPaises();
                            envia = jugador.getPaisesOcupados().get(pos);

                            if(!envia.puedeAtacar()){
                                System.out.println("El pais elegido solo tiene una tropa, no puedes trasladar tropas de él. Elige otro.");
                            } else {
                                puedeMover = envia.paisesPuedeMover();
                            }
                        } while (envia.puedeAtacar() && puedeMover.size()>0);
                        //comprobamos si el pais tiene mas de 1 tropa1 para enviar tropas y tiene un paises a los que enviar

                        System.out.println("Los paises a los que "+envia.getNombre()+" puede desplazar tropas son: ");
                        for(int i=0; i<puedeMover.size(); i++){ //mostramos los paises frontera de ese pais
                            System.out.println(i + ". "+puedeMover.get(i).getNombre());
                        }
                        int pos = entrada.nextInt();
                        Pais recibe = puedeMover.get(pos);
                        int numTropasMAX = recibe.getNumTropas() -1;
                        int trop;
                        do{
                            System.out.println("Elige el numero de tropas para enviar desde " + envia.getNombre()
                                    +" a " +recibe.getNombre() + ". Maximo " + numTropasMAX + " tropas.");
                            trop = entrada.nextInt();
                        }while(trop>numTropasMAX);

                        envia.moverTropas(recibe,trop);
                    }
                    case 3 -> {
                        Pais atacante = null;
                        List<Pais> puedeAtacar = new ArrayList<>();
                        do {
                            System.out.println("Introduzca con cual de sus paises desea atacar");
                            int pos = jugador.mostrarPaises();
                            atacante = jugador.getPaisesOcupados().get(pos);

                            if (!atacante.puedeAtacar()) {
                                System.out.println("El pais atacante no tiene suficientes tropas para atacar, elige otro pais");
                            } else {
                                // si puede atacar comprobamos que tenga paises a los que puede atacar
                                for (int i = 0; i < atacante.getPaisesFrontera().size(); i++) {
                                    if (!atacante.getPaisesFrontera().get(i).getPropietario().equals(jugador)) {
                                        //comprobamos que los paises frontera tienen otro propietario, si se puede atacar, se
                                        //añade al arrayList puedeAtacar
                                        puedeAtacar.add(atacante.getPaisesFrontera().get(i));
                                    }
                                }
                                if (puedeAtacar.size() == 0) {
                                    System.out.println("El pais que has elegido no puede atacar a ninguna de sus fronteras");
                                    System.out.println("¡YA SON TUYAS!");
                                    System.out.println("Elija otro pais con el que atacar");
                                }
                            }
                        } while (atacante.puedeAtacar() && puedeAtacar.size() > 0);
                        //comprobamos si el pais tiene mas de 2 tropas para atacar y tiene un pais al que atacar

                        System.out.println("Los paises frontera a los que " + atacante.getNombre() + " puede atacar son: ");
                        for (int i = 0; i < puedeAtacar.size(); i++) { //mostramos los paises frontera de ese pais
                            System.out.println("0. " + puedeAtacar.get(i).getNombre());
                        }
                        int pos = entrada.nextInt();
                        Pais atacado = puedeAtacar.get(pos);
                        jugador.atacarPais(atacante, atacado); //atacamos pais
                    }
                    case 4 -> {
                        System.out.println("Has decidido acabar tu turno");
                        turnoAcabado = true;
                    }
                    default -> {
                        System.out.println("El numero que has introducido no es valido");
                    }
                }
            }

        }



        return mapa;
        //al acabar devolvemos el mapa modificado para enviarselo al servidor y que se lo mande al otro cliente
    }

    public static void seleccionarPais(Jugador jugador, int numTropas){
        int n = jugador.mostrarPaises();
        Pais p = jugador.getPaisesOcupados().get(n);
        p.setNumTropas(p.getNumTropas() + numTropas);
        jugador.actualizarNumtropas();
    }
}
