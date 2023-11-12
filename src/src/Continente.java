import java.util.ArrayList;
import java.util.List;

public class Continente {

    private String nombre;
    private int numTerritorios;
    private int valor;
    private List<String> territorios = new ArrayList<>();

    public Continente(String nombre){
        this.nombre = nombre;
        String nomTerritorios[] = {"Alaska","Alberta","Mexico","Estados Unidos del Este","Groenlandia","Territorios del Noroeste",
        "Ontario","Quebec","Estados Unidos del Oeste","Argentina","Brasil","Peru","Venezuela","Gran Bretaña","Islandia",
                "Europa del Norte","Escandinavia","Europa del Sur","Rusia","Europa Occidental","Congo","Africa Oriental",
        "Egipto","Madagascar","Africa del Norte","Sudafrica","Afganistan","China","India","Irkutsk","Japon","Kamchatka",
        "Oriente Medio","Mongolia","Sudeste Asiatico","Siberia","Urai","Yakutsk","Australia Oriental","Indonesia","Nueva Guinea",
        "Australia Occidental"};

        switch (nombre){
            case "America del Norte":
                this.numTerritorios=9;
                this.valor=5;
                for(int i=1;i<9;i++){
                    this.territorios.add(nomTerritorios[i]);
                }
            case "America del Sur":
                this.numTerritorios=4;
                this.valor=2;
                for(int i=9;i<13;i++){
                    this.territorios.add(nomTerritorios[i]);
                }
            case "Europa":
                this.numTerritorios=7;
                this.valor=5;
                for(int i=13;i<20;i++){
                    this.territorios.add(nomTerritorios[i]);
                }
            case "Africa":
                this.numTerritorios=6;
                this.valor=3;
                for(int i=20;i<26;i++){
                    this.territorios.add(nomTerritorios[i]);
                }
            case "Asia":
                this.numTerritorios=12;
                this.valor=7;
                for(int i=26;i<38;i++){
                    this.territorios.add(nomTerritorios[i]);
                }
            case "Oceania":
                this.numTerritorios=4;
                this.valor=2;
                for(int i=38;i<42;i++){
                    this.territorios.add(nomTerritorios[i]);
                }
        }
    }

    public String getNombre(){
        return this.nombre;
    }
}
