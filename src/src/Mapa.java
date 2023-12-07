import java.io.*;
import java.util.*;


public class Mapa implements Serializable{
    private List<Pais> paises; //todos los paises del mapa
    private List<Continente> continentes; //todos los continentes del mapa
    private Map<String,Pais> mapPaises; // almacenar los paises en un mapa por nombre para tardar menos en buscar
    private Map<String,Continente> mapContinentes; // almacenar los continentes en un mapa por nombre para tardar menos en buscar
    private Map<Pais,List<Pais>> fronteras;  //para cada nombre pais añadimos una lista con sus fronteras
    private ManoTarjetas mazo;

    public Mapa(){
        this.paises = new ArrayList<>();
        this.continentes = new ArrayList<>();
        this.fronteras = new HashMap<>();
        this.mapPaises = new HashMap<>();
        this.mapContinentes = new HashMap<>();
        this.mazo = new ManoTarjetas();

        //Variables para mazo
        Random r = new Random();
        String tropa = null;

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
                this.mapPaises.put(p.getNombre(),p); //mapa paises

                //Creamos el mazo de cartas con una carta por cada pais
                switch (r.nextInt(3)) {
                    case 0 -> tropa = "Infanteria";
                    case 1 -> tropa = "Caballeria";
                    case 2 -> tropa = "Artilleria";
                }
                this.mazo.addTarjeta(new Tarjeta(p,tropa));
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

    public List<Tarjeta> getMazo(){
        return this.mazo.getTarjetas();
    }

    public void setMazo(ManoTarjetas mazo) {
        this.mazo = mazo;
    }

    public List<Pais> getPaisesLibres(){ //devuelve los paises que no tengan propietario
        List<Pais> libres = new ArrayList<>();

        for(Pais p : this.paises){
            if(p.getPropietario()==null){
                libres.add(p);
            }
        }

        return libres;
    }

    public boolean sonFrontera(String pais1, String pais2){
        return mapPaises.get(pais1).getPaisesFrontera().contains(mapPaises.get(pais2));
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

    public String toString(){
        int n = this.continentes.size();
        String s = "";
        for(int i = 0; i<n;i++){
            s = s + this.continentes.get(i).toString();
        }
        return s;
    }

    public boolean quedanPaisesSinOcupar(){
        int n = this.paises.size();
        for(int i = 0;i<n;i++){
            if(this.paises.get(i).getPropietario() == null){
                return true;
            }
        }
        return false;
    }

    public boolean mapaConquistado(){
        Jugador j = this.paises.get(0).getPropietario();
        int n = this.paises.size();
        for(int i = 0;i<n;i++){
            if(!j.equals(this.paises.get(i).getPropietario())){
                return false;
            }
        }
        return true;
    }


}
