import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoPaises<Pais> {

    private Map<Pais,List<Pais>> fronteras; //tabla fronteras

    public GrafoPaises() {
        // Pre:
        // Pos: create an empty graph

        this.fronteras = new HashMap<>(); //tabla vertices vacía
    }

    public void agnadePais(Pais p) {
        // Pre:
        // Pos: add the apex 'a' to the graph

        List<Pais> pais = new ArrayList<>();

        this.fronteras.put(p,pais);
    }

    public boolean contienePais(Pais p) {
        // Pre:
        // Pos: return true if the graph contains the apex 'a' and false if not

        return this.fronteras.containsKey(p);
    }

    public int numPaises() {
        // Pre:
        // Pos: return the number of apexes of the graph

        return this.fronteras.size();
    }

    public void agnadeFrontera(Pais p1, Pais p2) {
        // Pre:
        // Pos: add the arista a1 - a2 to the graph

        List<Pais> paises1 = this.fronteras.get(p1);
        List<Pais> paises2 = this.fronteras.get(p2);

        paises1.add(p2);
        paises2.add(p1);

        this.fronteras.put(p1,paises1);
        this.fronteras.put(p2,paises2);
    }

    public boolean esFrontera(Pais p1, Pais p2) {
        // Pre:
        // Pos: return true if a1 and a2 are two adjacent apexes and false if not

        List<Pais> list = this.fronteras.get(p1);
        return list.contains(p2);
    }

    public List<Pais> fronteras(Pais p) {
        // Pre:
        // Pos: return the list of adjacent apexes of the apex 'a'

        return this.fronteras.get(p);
    }

    public List<Pais> listaPaises() {
        // Pre:
        // Pos: return the list of apexes of the graph

        return new ArrayList<Pais> (this.fronteras.keySet());
    }
}
