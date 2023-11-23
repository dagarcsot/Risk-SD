import java.io.*;
import java.util.*;


public class Creador {
    private List<Pais> paises; //todos los paises del mapa
    private List<Continente> continentes; //todos los continentes del mapa
    private Map<Pais,List<Pais>> fronteras;  //para cada nombre pais añadimos una lista con sus fronteras

    Creador(){
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

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public List<Pais> getPaises(){
        return this.paises;
    }
    public List<Continente> getContinentes(){
        return this.continentes;
    }
    public Map<Pais,List<Pais>> getFronteras(){
        return this.fronteras;
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
        pais.agnadirFronteras(front); //añadimos al pais sus paises fronteras
        this.fronteras.put(pais,front); //añadimos al mapa la relación del pais con sus fronteras
    }


}
