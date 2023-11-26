import java.io.*;
import java.lang.management.PlatformLoggingMXBean;
import java.util.*;


public class Mapa {
    private List<Pais> paises; //todos los paises del mapa
    private List<Continente> continentes; //todos los continentes del mapa
    private Map<String,Pais> mapPaises; // almacenar los paises en un mapa por nombre para tardar menos en buscar
    private Map<String,Continente> mapContinentes; // almacenar los continentes en un mapa por nombre para tardar menos en buscar
    private Map<Pais,List<Pais>> fronteras;  //para cada nombre pais añadimos una lista con sus fronteras

    public Mapa(){
        this.paises = new ArrayList<>();
        this.continentes = new ArrayList<>();
        this.fronteras = new HashMap<>();

        try (DataInputStream dis = new DataInputStream(new FileInputStream("paises.txt"));
             DataInputStream dis1 = new DataInputStream(new FileInputStream("continentes.txt"));
             DataInputStream dis2 = new DataInputStream(new FileInputStream("fronteras.txt"))
        ){
            String linea;
            while ( (linea = dis.readLine()) != null) { //se crean los paises
                this.paises.add(new Pais(linea.trim()));
            }
            while ( (linea = dis1.readLine()) != null) { //se crean los continentes
                this.continentes.add(crearContinente(linea));
            }
            while ( (linea = dis2.readLine()) != null) { // se crean las fronteras
                crearFronteras(linea);
            }
            for(Pais p:this.paises){
                this.mapPaises.put(p.getNombre(),p);
            }
            for(Continente c:this.continentes){
                this.mapContinentes.put(c.getNombre(),c);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public List<Continente> getContinentes(){ //devuelve una lista con los continentes del mapa
        return this.continentes;
    }

    public Continente getContinentePorNombre(String nom){ //devuelve el objeto continente buscado por su nombre
        return this.mapContinentes.get(nom);
    }

    public Map<Pais,List<Pais>> getFronteras(){ //devuelve las fronteras de un pais
        return this.fronteras;
    }

    public int getValorContinente(String nom){ //devuelve el valor de tropas por tener un continente entero
        return this.mapContinentes.get(nom).getValor();
    }

    public List<Pais> getPaisesDeContinente(String nom){ //devuelve los paises de un continente
        return this.mapContinentes.get(nom).getPaises();
    }

    public List<Pais> getPaises(){ //devuelve una lista con los paises del mapa
        return this.paises;
    }

    public Pais getPaisPorNombre(String nom){ //devuelve el pais buscando por su nombre
        return this.mapPaises.get(nom);
    }

    public void setJugador(String nom, Jugador j){ //asigna el jugador j al pais nom
        this.mapPaises.get(nom).asignarPropietario(j);
    }

    public Jugador getJugador(String nom){ //devuelve el jugador del pais j
        return this.mapPaises.get(nom).getPropietario();
    }

    public void setTropas(String nom, int num){ //asigna el numero de tropas de un pais
        this.mapPaises.get(nom).setNumTropas(num);
    }

    public int getTropas(String nom){ // devuelve el numero de tropas de un pais
        return this.mapPaises.get(nom).getNumTropas();
    }

    public List<Pais> getPaisesLibres(){ //devuelve los paises que no tengan propietario //creo que no hace falta si asignamos aleatoriamente a los paises un propietario al principio.
        List<Pais> libres = new ArrayList<>();

        for(Pais p : this.paises){
            if(p.getPropietario()==null){
                libres.add(p);
            }
        }

        return libres;
    }


    private Continente crearContinente(String linea) { //creamos los continentes añadiendole los paises ya creados antes
        String[] partes = linea.split(",");
        String nombreContinente = partes[0].trim();
        int tropas = Integer.parseInt(partes[1].trim());
        List<Pais> lPaises = new ArrayList<>();

        for (int i=2; i < partes.length; i++) {
            for(Pais p : this.paises){
                if(p.getNombre().equals(partes[i].trim())){
                    lPaises.add(p);
                }
            }
        }
        return new Continente(nombreContinente, tropas, lPaises);
    }

    private void crearFronteras(String linea){ //buscamos los paises frontera
        String[] partes = linea.split(",");
        Pais pais = null;
        List<Pais> front = new ArrayList<>();

        for(int i=1;i<partes.length;i++){
            for (Pais p : this.paises) {
                if(partes[0].trim().equals(p.getNombre())){
                    pais=p;
                }
                if (partes[i].trim().equals(p.getNombre())) {
                    front.add(p);
                }
            }
        }
        pais.setFronteras(front); //añadimos al pais sus paises fronteras
        this.fronteras.put(pais,front); //añadimos al mapa la relación del pais con sus fronteras
    }


}
