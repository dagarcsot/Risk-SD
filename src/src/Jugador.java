import java.util.*;

import static java.util.Arrays.stream;

public class Jugador {

    private String nombre;
    private int numTropas; //numero de tropas que tiene el jugador en el mapa
    private List<Tarjeta> tarjetas;
    private final static int MAXTARJETAS = 3; //numero máximo de tarjetas por jugador
    private Map<String, Pais> paisesOcupados; //paises que posee el jugador
    private Map<String, Continente> continentesOcupados; //continentes que posee el jugador (se añadira cuando tenga todos los paises que completan ese continente)
    private static int numJugadores = 0; //numero de jugadores creados


    Jugador(String nombre, int numTropas) {
        this.nombre = nombre;
        this.numTropas = numTropas;
        this.tarjetas = new ArrayList<>(MAXTARJETAS);
        this.paisesOcupados = new HashMap<>();
        this.continentesOcupados = new HashMap<>();
        numJugadores++;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getNumTropas() {
        return this.numTropas;
    }

    public List<Pais> getPaisesOcupados() {
        return new ArrayList<Pais>(this.paisesOcupados.values());
    }

    public void setNumTropas(int tropas, boolean b) { //tropas = numero de tropas a modificar
        if (b) {                                       //b = si es cierto suma las tropas ganadas, si es falso las elimina
            this.numTropas = this.numTropas + tropas;
            System.out.println(this.nombre + " ha ganado " + tropas + " tropas. Total de tropas: " + this.numTropas);
        } else {
            this.numTropas = this.numTropas - tropas;
            System.out.println(this.nombre + " ha perdido " + tropas + " tropas. Total de tropas: " + this.numTropas);
        }
    }

    public void addPais(List<Pais> l) { //añadir una lista de paises
        for (Pais pais : l) {
            this.paisesOcupados.put(pais.getNombre(), pais);
        }
    }

    public void addPais(Pais p) { //añadir un pais (en un ataque)
        this.paisesOcupados.put(p.getNombre(), p);
        System.out.println(this.nombre + " ha ocupado " + p.getNombre());
    }

    public void eliminarPais(String nom) { //eliminar un pais (en un ataque)
        this.paisesOcupados.remove(nom);
    }

    public List<Pais> getPaises() {
        return new ArrayList<>(this.paisesOcupados.values());
    }

    public void addContinente(Continente c) {
        this.continentesOcupados.put(c.getNombre(), c);
    }

    public void eliminarContinente(String nom) {
        this.continentesOcupados.remove(nom);
    }

    public List<Continente> getContinentes() {
        return new ArrayList<>(this.continentesOcupados.values());
    }

    public void addTarjeta(Tarjeta t) {
        if (this.tarjetas.size() < 3) {
            this.tarjetas.add(t);
        } else {
            System.out.println("¡Numero maximo de tarjetas alcanzado!");
            System.out.println("Si quieres quedarte la tarjeta debes eliminar una de las anteriores\n");
            for (int j = 0; j < this.tarjetas.size(); j++) {
                System.out.println("Tarjeta: " + j);
                System.out.println(this.tarjetas.get(j).toString() + "\n");
            }
            System.out.println("Introduce 0 si quieres quedarte con las tarjetas anteriores");
            System.out.println("Introduce 1 si quieres cambiar la tarjeta");
            Scanner entrada = new Scanner(System.in);
            int i = entrada.nextInt();
            switch (i) {
                case 0:
                    System.out.println("Tarjeta: " + t.toString() + " eliminada");
                    break;
                case 1:
                    System.out.println("¿Por que tarjeta la quieres cambiar?");
                    i = entrada.nextInt();
                    this.tarjetas.remove(i);
                    this.addTarjeta(t);
                    System.out.println("¡Tarjeta: " + t.toString() + " añadida con exito!");
                default:
                    addTarjeta(t);
            }
        }
    }


    public void atacarPais(Pais atacante, Pais defensor) {
        //PRE: atacante debe pertenecerle y defensor no.
        // Además son adyacentes.
        // El pais atacante debe tener más de 1 tropa.
        //numDados = {1,2,3}
        Dado d = new Dado();
        String jugadorAtacante = atacante.getPropietario().getNombre();
        String jugadorDefensor = defensor.getPropietario().getNombre();
        int tropasAtacante = atacante.getNumTropas();
        int tropasDefensor = defensor.getNumTropas();
        int numDadosMax = atacante.numDadosMaxPuedeTirar();
        String s = "(1";
        if (numDadosMax > 2) {
            s = s + ",2";
            if (numDadosMax > 3) {
                s = s + ",3";
            }
        }
        Scanner read = new Scanner(System.in);
        int numDadosAtacante;
        do {
            System.out.println(this.nombre + " elige los dados que vas a tirar. " + s + " dados).");
            numDadosAtacante = read.nextInt();
        } while (numDadosAtacante > numDadosMax);
        int numDadosDefensor = defensor.getNumTropas() == 1 ? 1 : 2;

        System.out.println(atacante.getNombre() + " (" + jugadorAtacante + ") ataca a " + defensor.getNombre() + " (" + jugadorDefensor + ").");
        System.out.println("El atacante" + "tirará " + numDadosAtacante + " dados.");
        System.out.println("El defensor" + "tirará " + numDadosDefensor + " dados.");

        int sumaAtaque = 0;

        System.out.println(jugadorAtacante + " tira los dados (ENTER):");
        read.nextLine();
        int[] vAtacante = d.tirarDados(numDadosAtacante);
        String s1 = jugadorAtacante + " has sacado: " + vAtacante[0];
        for (int i = 1; i < numDadosAtacante; i++) {
            s1 = s1 + "," + vAtacante[i];
        }

        System.out.println(jugadorDefensor + " tira los dados:");
        int[] vDefensor = d.tirarDados(numDadosDefensor);
        String s2 = jugadorDefensor + " has sacado: " + vDefensor[0];
        for (int i = 1; i < numDadosAtacante; i++) {
            s2 = s2 + "," + vDefensor[i];
        }


        if(numDadosDefensor == 1){ //1 dado del defensor
            if(stream(vAtacante).max().orElseThrow() > vDefensor[0]){ //gana atacante

            }else{ //gana defensor
                atacante.setNumTropas(tropasAtacante - 1);
            }

        }else{

        }







    }

    public void eliminarTarjeta(Tarjeta t) {
        this.tarjetas.remove(t);
    }

    public List<Tarjeta> getTarjetas() {
        return new ArrayList<>(this.tarjetas);
    }

    public int getNumJugadores() {
        return numJugadores;
    }

    public boolean isEliminado(){
        return this.numTropas==0;
    }

}
