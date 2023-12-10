import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Continente implements Serializable {

    private String nombre;
    private int valor; //numero de tropas que recibes por tener el pais completo
    private List<Pais> paises = new ArrayList<>(); //lista con los paises de cada continente

    public Continente(String nombre, int valor, List<Pais> lPaises) {
        this.nombre = nombre;
        this.valor = valor;
        this.paises = lPaises;
    }
    public String getNombre() {
        return this.nombre;
    }

    public int getNumTerritorios() {
        return this.paises.size();
    }

    public int getValor() {
        return this.valor;
    }

    public void agnadirPaises(List<Pais> listaPaises) {
        this.paises = listaPaises;
    }

    public List<Pais> getPaises() {
        return this.paises;
    }

    public boolean esConquistado(){
        int n = this.paises.size();
        Jugador j = this.paises.get(0).getPropietario();
        for(int i = 1;i<n;i++){
            if(this.paises.get(i).getPropietario() == j){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Continente: ").append(this.nombre).append("\n");
        for (int i = 0; i < this.paises.size(); i++) {
            s.append(this.paises.get(i).toString()).append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}
