public class Tarjeta {

    private String pais;
    private String tropa;

    public Tarjeta(){
        this.pais="";
        this.tropa="";
    }

    public Tarjeta(String territorio, String tropa){
        this.pais=territorio;
        this.tropa=tropa;
    }

    public String getTerritorio(){
        return this.pais;
    }

    public String getTropa(){
        return this.tropa;
    }

    @Override
    public String toString() {
        return "Pais: "+this.pais+"\n"+
                "Tropa: "+this.tropa+"\n";
    }
}
