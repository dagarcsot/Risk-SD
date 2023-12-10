import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteRisk {
    private static final int puerto = 6666;
    private static Scanner entrada = new Scanner(System.in);
    private static boolean partidaAcabada = false;
    private static Mapa mapaViejo;
    private static Mapa mapaNuevo;
    private static Jugador jugador;

    public static void main(String[] args)  {
        String ip = "localhost";//hay que poner la ip del ordenador que haga el servidor
        try (Socket cliente = new Socket(ip, puerto)) {
            System.out.println("Conectado al servidor en el puerto " + cliente.getLocalPort() + "\n");

            // Obtener nombre del jugador
            try (ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
                 ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream())) {

                //recibimos pregunta
                System.out.println(ois.readLine());
                //mandamos nombre
                String nombre = entrada.nextLine().trim();
                oos.writeBytes(nombre+"\n");
                oos.flush();

                //aviso de espera
                System.out.println(ois.readLine());

                while(!partidaAcabada){
                    jugador = (Jugador) ois.readObject();
                    mapaViejo = (Mapa) ois.readObject();
                    if(jugador.getNombre().equals(nombre)){ //si coincide el nombre del jugador que mandan nos toca
                        //modificamos el mapa
                        mapaNuevo = elegirJugada(jugador,mapaViejo);
                        if(mapaNuevo.mapaConquistado()){ //Comprobamos si hemos ganado
                            System.out.println("Has ganado la partida!!!");
                            partidaAcabada = true;
                        } else {
                            //enviamos el nuevo mapa
                            oos.writeObject(mapaNuevo);
                            oos.flush();
                        }
                    } else { // si no esperamos
                        System.out.println("No es tu turno, te toca esperar...");
                        System.out.println("Te avisaremos cuando acabe el turno de "+jugador.getNombre());
                    }
                }

                //Recibimos el ganador
                System.out.println(ois.readLine());
                System.out.println(ois.readLine());


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static Mapa elegirJugada(Jugador jugador, Mapa mapa){
        //Al final de cada turno se devuelve el mapa modificado

        for(int i=0; i<jugador.getPaisesOcupados().size(); i++) {
            //comprobamos al principio de cada turno que un jugador no tiene paises que le hayan quitado
            if(!jugador.getPaisesOcupados().get(i).getPropietario().equals(jugador)){
                jugador.eliminarPais(jugador.getPaisesOcupados().get(i).getNombre());
            }
        }


        boolean turnoAcabado = false;

        System.out.println("------------------------------------------------"); //Visual
        System.out.println("Turno de: "+jugador.getNombre());


        int opcion=0;

        if(mapa.quedanPaisesSinOcupar()){
            int num = mapa.getPaisesLibres().size();

            System.out.println("Que pais quieres ocupar: "); //Mandamos cliente

            for(int i = 0;i<num;i++){
                System.out.print(i+". "+mapa.getPaisesLibres().get(i).toString());
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
                //Vamos a gestionar el numero de tropas que recibe un jugador al principio de su turno
                System.out.println("El jugador recibe 3 tropas por iniciar su turno");
                int tropas = 3;
                for(int i=0; i<jugador.getContinentes().size(); i++){
                    System.out.println("El jugador recibe "+jugador.getContinentes().get(i).getValor()+" por "+
                            jugador.getContinentes().get(i).getNombre());
                    tropas = tropas + jugador.getContinentes().get(i).getValor();
                }
                System.out.println(jugador.getNombre()+" ha recibido un total de "+tropas+" tropas");
                System.out.println(); //Espacio visual

                System.out.println("Selecciona el pais al que se añaden las tropas: ");
                seleccionarPais(jugador,tropas);
                System.out.println();  //Visual

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


                do{
                    System.out.println("Que jugada desea realizar: ");
                    System.out.println("1. Canjear tarjetas");
                    System.out.println("2. Reforzar un territorio");
                    System.out.println("3. Atacar un territorio");
                    System.out.println("4. Mostrar mis paises por continente");
                    System.out.println("5. Mostrar el mapa");
                    System.out.println("6. Acabar mi turno");
                    System.out.println("7. Acabar partida");
                    opcion = entrada.nextInt();
                    System.out.println(); //visual

                    if(opcion < 0 ||opcion>7){
                        System.out.println("El numero que has introducido no es valido");
                        System.out.println(); //Visual
                    }
                } while (opcion<0 || opcion>7);

                switch (opcion) {
                    case 1 -> {
                        if(jugador.getManoTarjetas().getTarjetas().size()<3){
                            System.out.println("No tienes suficientes tarjetas");
                            System.out.println("Vuelve cuando tengas al menos 3 tarjetas");
                            System.out.println();
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
                            System.out.println("Introduzca que pais desea donar tropas: ");
                            System.out.println("(Solo pueden donar tropas los paises con numTropas>1)");
                            int pos = jugador.mostrarPaises();
                            envia = jugador.getPaisesOcupados().get(pos);

                            if(!envia.puedeAtacar()){
                                System.out.println("El pais elegido solo tiene una tropa, no puedes trasladar tropas de él. Elige otro.");
                                System.out.println();
                            } else {
                                puedeMover = jugador.getPaisesOcupados();
                                puedeMover.remove(envia); // un pais no se puede donar tropas a el mismo
                                System.out.println(); //visual
                            }
                        } while (!envia.puedeAtacar());
                        //comprobamos si el pais tiene mas de 1 tropa para enviar tropas

                        System.out.println("Los paises a los que "+envia.getNombre()+" puede desplazar tropas son: ");
                        for(int i=0; i<puedeMover.size(); i++){ //mostramos los paises frontera de ese pais
                            System.out.println(i + ". "+puedeMover.get(i).getNombre());
                        }
                        int pos = entrada.nextInt();
                        System.out.println(); //visual
                        Pais recibe = puedeMover.get(pos);
                        int numTropasMAX = envia.getNumTropas()-1;
                        int trop;
                        do{
                            System.out.println("Elige el numero de tropas para enviar desde " + envia.getNombre()
                                    +" a " +recibe.getNombre() + ". Maximo " + numTropasMAX + " tropas.");
                            trop = entrada.nextInt();
                            System.out.println(); //visual
                        }while(trop>numTropasMAX);

                        envia.moverTropas(recibe,trop);

                        System.out.println("Las tropas quedan de la siguiente forma: ");
                        for(int i=0; i<jugador.getPaisesOcupados().size(); i++){
                            System.out.println(jugador.getPaisesOcupados().get(i).toString());
                        }
                        System.out.println(); //visual
                    }
                    case 3 -> {
                        Pais atacante = null;
                        List<Pais> puedeAtacar = new ArrayList<>();
                        do {
                            System.out.println("Introduzca con cual de sus paises desea atacar: ");
                            System.out.println("(Solo pueden atacar los paises con numTropas>1)");
                            int pos = jugador.mostrarPaises();
                            atacante = jugador.getPaisesOcupados().get(pos);

                            if (!atacante.puedeAtacar()) {
                                System.out.println(); //visual
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
                                    System.out.println("Elija otro pais con el que atacar");
                                }
                            }
                        } while (!atacante.puedeAtacar());
                        //comprobamos si el pais tiene mas de 2 tropas para atacar

                        System.out.println();
                        System.out.println("Los paises frontera a los que " + atacante.getNombre() + " puede atacar son: ");
                        for (int i = 0; i < puedeAtacar.size(); i++) { //mostramos los paises frontera de ese pais
                            System.out.println("0. " + puedeAtacar.get(i).getNombre());
                        }
                        int pos = entrada.nextInt();
                        System.out.println(); //visual
                        Pais atacado = puedeAtacar.get(pos);
                        jugador.atacarPais(atacante, atacado); //atacamos pais
                        System.out.println(); //visual
                    }
                    case 4 -> {
                        System.out.println(jugador.getNombre()+" tiene "+jugador.getPaisesOcupados().size());
                        System.out.println("Los paises son: ");
                        System.out.println(); //visual
                        Continente continente = null;

                        for(int i=0; i<mapa.getContinentes().size(); i++){
                            continente = mapa.getContinentes().get(i);
                            System.out.println("Continente: "+continente.getNombre());
                            for(int n=0; n<jugador.getPaisesOcupados().size(); n++){
                                if(mapa.getPaisesDeContinente(continente.getNombre()).contains(jugador.getPaisesOcupados().get(n))) {
                                    System.out.println(jugador.getPaisesOcupados().get(n).toString());
                                }
                            }
                            System.out.println(); //visual
                        }
                    }
                    case 5 -> {
                        System.out.println("Actualmente el mapa esta asi: ");
                        System.out.println(); //visual
                        System.out.println(mapa);
                    }
                    case 6 ->{
                        System.out.println("Has decidido acabar tu turno");
                        System.out.println(); //visual
                        turnoAcabado = true;
                    }
                    case 7 ->{
                        System.exit(0);
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
        System.out.println("Ahora "+jugador.getPaisesOcupados().get(n).getNombre()+" tiene "+
                jugador.getPaisesOcupados().get(n).getNumTropas() + " tropas");
    }
}
