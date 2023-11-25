import java.util.*;

public class Jugador {

    private String nombre;
    private int numTropas; //numero de tropas que tiene el jugador en el mapa
    private List<Tarjeta> tarjetas;
    private final static int MAXTARJETAS=3; //numero máximo de tarjetas por jugador
    private Map<String,Pais> paisesOcupados; //paises que posee el jugador
    private Map<String,Continente> continentesOcupados;
    //continentes que posee el jugador (se añadira cuando tenga todos los paises que completan ese continente)


    Jugador(String nombre,int numTropas){
        this.nombre = nombre;
        this.numTropas = numTropas;
        this.tarjetas=new ArrayList<>(MAXTARJETAS);
        this.paisesOcupados = new HashMap<>();
        this.continentesOcupados = new HashMap<>();
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getNumTropas() {
        return this.numTropas;
    }

    public List<Pais> getPaisesOcupados(){
        return new ArrayList<Pais>(this.paisesOcupados.values());
    }

    public void setNumTropas(int tropas, boolean b){ //tropas = numero de tropas a modificar
        if(b){                                       //b = si es cierto suma las tropas ganadas, si es falso las elimina
            this.numTropas=this.numTropas+tropas;
            System.out.println(this.nombre+" ha ganado "+tropas+" tropas. Total de tropas: "+this.numTropas);
        } else {
            this.numTropas=this.numTropas-tropas;
            System.out.println(this.nombre+" ha perdido "+tropas+" tropas. Total de tropas: "+this.numTropas);
        }
    }

    public void addPais(List<Pais> l){ //añadir una lista de paises
        for (Pais pais : l) {
            this.paisesOcupados.put(pais.getNombre(), pais);
        }
    }

    public void addPais(Pais p){ //añadir un pais (en un ataque)
        this.paisesOcupados.put(p.getNombre(),p);
        System.out.println(this.nombre+" ha ocupado "+p.getNombre());
    }

    public void eliminarPais(String nom){ //eliminar un pais (en un ataque)
        this.paisesOcupados.remove(nom);
    }

    public List<Pais> getPaises(){
        return new ArrayList<>(this.paisesOcupados.values());
    }

    public void addContinente(Continente c){
        this.continentesOcupados.put(c.getNombre(),c);
    }

    public void eliminarContinente(String nom){
        this.continentesOcupados.remove(nom);
    }

    public List<Continente> getContinentes(){
        return new ArrayList<>(this.continentesOcupados.values());
    }

    public void addTarjeta(Tarjeta t){
        if(this.tarjetas.size()<3){
            this.tarjetas.add(t);
        } else {
            System.out.println("¡Numero maximo de tarjetas alcanzado!");
            System.out.println("Si quieres quedarte la tarjeta debes eliminar una de las anteriores\n");
            for (int j=0;j<this.tarjetas.size();j++){
                System.out.println("Tarjeta: "+j);
                System.out.println(this.tarjetas.get(j).toString()+"\n");
            }
            System.out.println("Introduce 0 si quieres quedarte con las tarjetas anteriores");
            System.out.println("Introduce 1 si quieres cambiar la tarjeta");
            Scanner entrada = new Scanner(System.in);
            int i = entrada.nextInt();
            switch (i){
                case 0:
                    System.out.println("Tarjeta: "+t.toString()+" eliminada");
                    break;
                case 1:
                    System.out.println("¿Por que tarjeta la quieres cambiar?");
                    i = entrada.nextInt();
                    this.tarjetas.remove(i);
                    this.addTarjeta(t);
                    System.out.println("¡Tarjeta: "+t.toString()+" añadida con exito!");
                default:
                    addTarjeta(t);
            }
        }
    }
    public void eliminarTarjeta(Tarjeta t){
        this.tarjetas.remove(t);
    }

    public List<Tarjeta> getTarjetas(){
        return new ArrayList<>(this.tarjetas);
    }


}
