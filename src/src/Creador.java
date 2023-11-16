import java.io.*;
import java.util.*;


public class Creador {
    private final List<Pais> paises;
    private final List<Continente> continentes;
    Map<Pais,List<Pais>> fronteras;

    Creador(){
        paises = new ArrayList<>();
        continentes = new ArrayList<>();
        fronteras = new HashMap<>();

        try (DataInputStream dis1 = new DataInputStream(new FileInputStream("continentes.txt"));
            DataInputStream dis2 = new DataInputStream(new FileInputStream("fronteras.txt"))
        ){
            String linea;
            while ( (linea = dis1.readLine()) != null) { //se crean los paises y continentes
                this.continentes.add(crearContinente(linea));
            }
            while ( (linea = dis2.readLine()) != null) { // se crean las fronteras
                List<Pais> listaAUX = crearFronteras(linea);
                this.fronteras.put(listaAUX.get(0),listaAUX.subList(1, listaAUX.size()));

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

    private Continente crearContinente(String linea) {
        String[] partes = linea.split(",");
        String nombreContinente = partes[0].trim();
        int tropas = Integer.parseInt(partes[1].trim());
        List<Pais> lPaises = new ArrayList<>();

        for (int i = 2; i < partes.length; i++) {
            lPaises.add(new Pais(partes[i].trim()));
            this.paises.add(new Pais(partes[i].trim()));
        }

        return new Continente(nombreContinente, tropas, lPaises);
    }
    private List<Pais> crearFronteras(String linea) {
        String[] partes = linea.split(",");
        String paisAUX = partes[0];
        List<String> listaFronteras = Arrays.asList(partes).subList(1, partes.length);
        List<Pais> paisYFronteras = new ArrayList<>();
        for (Pais aux1 : this.paises) {
            if (paisAUX.equals(aux1.getNombre())) {
                paisYFronteras.add(aux1);
                break;
            }
        }
        for(int i = 0; i < listaFronteras.size();i++){
            for (Pais aux2 : this.paises) {
                if (listaFronteras.get(i).equals(aux2)) {
                    paisYFronteras.add(this.paises.get(i));
                }
            }

        }
        return  paisYFronteras;
    }

}
