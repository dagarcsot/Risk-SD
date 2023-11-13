import java.util.ArrayList;
import java.util.List;

public class Continente {

    private String nombre;
    private int numPaises; //numero de Paises del continente
    private int valor; //numero de tropas que recibes por tener el pais completo
    private List<Pais> paises = new ArrayList<>(); //lista con los paises de cada continente

    public Continente(String nombre){
        this.nombre = nombre;
        String[] nomPaises = {"Alaska","Alberta","Mexico","Estados Unidos del Este","Groenlandia","Territorios del Noroeste",
        "Ontario","Quebec","Estados Unidos del Oeste","Argentina","Brasil","Peru","Venezuela","Gran Bretaña","Islandia",
                "Europa del Norte","Escandinavia","Europa del Sur","Rusia","Europa Occidental","Congo","Africa Oriental",
        "Egipto","Madagascar","Africa del Norte","Sudafrica","Afganistan","China","India","Irkutsk","Japon","Kamchatka",
        "Oriente Medio","Mongolia","Sudeste Asiatico","Siberia","Ural","Yakutsk","Australia Oriental","Indonesia","Nueva Guinea",
        "Australia Occidental"};

        switch (nombre){
            case "America del Norte":
                this.numPaises=9;
                this.valor=5;
                for(int i=1;i<9;i++){
                    Pais p = new Pais(nomPaises[i]);
                    this.paises.add(p);
                }
            case "America del Sur":
                this.numPaises=4;
                this.valor=2;
                for(int i=9;i<13;i++){
                    Pais p = new Pais(nomPaises[i]);
                    this.paises.add(p);
                }
            case "Europa":
                this.numPaises=7;
                this.valor=5;
                for(int i=13;i<20;i++){
                    Pais p = new Pais(nomPaises[i]);
                    this.paises.add(p);
                }
            case "Africa":
                this.numPaises=6;
                this.valor=3;
                for(int i=20;i<26;i++){
                    Pais p = new Pais(nomPaises[i]);
                    this.paises.add(p);
                }
            case "Asia":
                this.numPaises=12;
                this.valor=7;
                for(int i=26;i<38;i++){
                    Pais p = new Pais(nomPaises[i]);
                    this.paises.add(p);
                }
            case "Oceania":
                this.numPaises=4;
                this.valor=2;
                for(int i=38;i<42;i++){
                    Pais p = new Pais(nomPaises[i]);
                    this.paises.add(p);
                }
        }
    }

    public String getNombre(){
        return this.nombre;
    }

    public int getNumTerritorios() {
        return this.numPaises;
    }

    public int getValor() {
        return this.valor;
    }

    @Override
    public String toString() {
        String s="Continente: "+this.nombre+"\n";
        for(int i=0;i<this.numPaises;i++){
            s=this.paises.get(i).toString()+"\n";
        }
        s=s+"\n";
        return s;
    }
}
