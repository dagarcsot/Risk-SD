import java.util.ArrayList;
import java.util.List;

public class Continente {

    private String nombre;
    private int numPaises; //numero de Paises del continente
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
        return this.numPaises;
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
        String s = "Continente: " + this.nombre + "\n";
        for (int i = 0; i < this.numPaises; i++) {
            s = this.paises.get(i).toString() + "\n";
        }
        s = s + "\n";
        return s;
    }
}
