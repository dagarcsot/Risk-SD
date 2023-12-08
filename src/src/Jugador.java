import java.util.*;

import static java.util.Arrays.stream;

public class Jugador {

    private String nombre;
    private int numTropas; //numero de tropas que tiene el jugador en el mapa
    private ManoTarjetas manoTarjetas;
    private final static int MAXTARJETAS = 5; //numero máximo de tarjetas por jugador
    private Map<String, Pais> paisesOcupados; //paises que posee el jugador
    private Map<String, Continente> continentesOcupados; //continentes que posee el jugador (se añadira cuando tenga todos los paises que completan ese continente)
    private static int numJugadores = 0; //numero de jugadores creados


    Jugador(String nombre, int numTropas) {
        this.nombre = nombre;
        this.numTropas = numTropas;
        this.manoTarjetas = new ManoTarjetas();
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

    public Pais getPais(String nombre){
        return this.paisesOcupados.get(nombre);
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

    public int comprobarContinentes(){
        //devuelve 0 si no ocupa ningun continente, si no, devuelve la suma del valor de los continentes que ocupa
        int suma = 0;
        if(this.continentesOcupados.size() == 0){
            return 0;
        }
        else {
            for(int i=0; i<this.continentesOcupados.size();i++) {
                suma = suma + this.getContinentes().get(i).getValor();
            }
            return suma;
        }
    }

    public void addTarjeta(Tarjeta t) {
        if (this.manoTarjetas.getTarjetas().size() < MAXTARJETAS) {
            this.manoTarjetas.addTarjeta(t);
        } else {
            System.out.println("¡Numero maximo de tarjetas alcanzado!");
            System.out.println("Si quieres quedarte la tarjeta debes eliminar una de las anteriores\n");
            for (int j = 0; j < this.manoTarjetas.getTarjetas().size(); j++) {
                System.out.println("Tarjeta: " + j);
                System.out.println(this.manoTarjetas.getTarjetas().get(j).toString() + "\n");
            }
            System.out.println("Introduce 0 si quieres quedarte con las tarjetas anteriores");
            System.out.println("Introduce 1 si quieres cambiar la tarjeta");
            Scanner entrada = new Scanner(System.in);
            int i = entrada.nextInt();
            switch (i) {
                case 0 -> System.out.println("Tarjeta: " + t.toString() + " eliminada");
                case 1 -> {
                    System.out.println("¿Por que tarjeta la quieres cambiar?");
                    i = entrada.nextInt();
                    this.manoTarjetas.getTarjetas().remove(i);
                    this.addTarjeta(t);
                    System.out.println("¡Tarjeta: " + t.toString() + " añadida con exito!");
                }
                default -> addTarjeta(t);
            }
        }
    }


    public void atacarPais(Pais atacante, Pais defensor) {
        //PRE: pais atacante debe pertenecer al jugador y pais defensor no.
        // Además son adyacentes.
        // El pais atacante debe tener más de 1 tropa.
        //numDados = {1,2,3}
        Dado dado = new Dado();
        String jugadorAtacante = this.getNombre();
        String jugadorDefensor = defensor.getPropietario().getNombre();
        int tropasAtacante = atacante.getNumTropas();
        int tropasDefensor = defensor.getNumTropas();

        Scanner read = new Scanner(System.in);

        int numDadosAtacante = 3;//logica de ataque simplificada (no tiene en cuenta el numero de tropas)
        int numDadosDefensor = 2;

        System.out.println(atacante.getNombre() + " (" + jugadorAtacante + ") ataca a " + defensor.getNombre() + " (" + jugadorDefensor + ").");
        System.out.println("El atacante" + "tirará " + numDadosAtacante + " dados.");
        System.out.println("El defensor" + "tirará " + numDadosDefensor + " dados.");

        System.out.println("El atacante tira los dados: [pulsa ENTER]");
        read.nextInt();
        int puntosAtacante = dado.tirarDados(3);
        int puntosDefensor = dado.tirarDados(2);
        System.out.println("El atacante ha sacado: " + puntosAtacante);
        System.out.println("El defensor ha sacado: " + puntosDefensor);

        if(puntosAtacante>puntosDefensor){
            System.out.println("Gana el jugador: " + jugadorAtacante);
            defensor.setPropietario(this);
            defensor.setNumTropas(1);
            atacante.setNumTropas(atacante.getNumTropas()-1);
            System.out.println("Ahora el pais: " + defensor.getNombre() + " es propiedad de " + jugadorAtacante);
            this.addPais(defensor);
        } else{
            if(puntosAtacante<puntosDefensor){
                System.out.println("Gana el jugador: " + jugadorDefensor);
                int tropasAntes = atacante.getNumTropas();
                atacante.setNumTropas(tropasAntes/2);
                System.out.println("Ahora el pais: " + atacante.getNombre() + " que tenia " + tropasAntes + " ahora tiene " + atacante.getNumTropas());
            } else{
                System.out.println("Los jugadores han quedado empate");
                int tropasAntes = atacante.getNumTropas();
                int tropasAhora = atacante.getNumTropas() * 2/3;
                System.out.println("Ahora el pais: " + atacante.getNombre() + " que tenia " + tropasAntes + " ahora tiene " + tropasAhora);
                System.out.println("Ahora el pais: " + defensor.getNombre() + " que tenia " + tropasAntes + " ahora tiene " + tropasAhora);
                atacante.setNumTropas(tropasAhora);
                defensor.setNumTropas(tropasAhora);
            }
        }

    }

    public void eliminarTarjeta(Tarjeta t) {
        this.manoTarjetas.getTarjetas().remove(t);
    }

    public ManoTarjetas getManoTarjetas() {
        return this.manoTarjetas;
    }

    public int getNumJugadores() {
        return numJugadores;
    }

    public boolean isEliminado(){
        return this.numTropas==0;
    }

}
