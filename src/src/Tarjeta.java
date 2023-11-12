public class Tarjeta {

    private String territorio;
    private String tropa;

    public Tarjeta(){
        this.territorio="";
        this.tropa="";
    }

    public Tarjeta(String territorio, String tropa){
        this.territorio=territorio;
        this.tropa=tropa;
    }

    public String getTerritorio(){
        return this.territorio;
    }

    public String getTropa(){
        return this.tropa;
    }

    @Override
    public String toString() {
        return "Territorio: "+this.territorio+"\n"+
                "Tropa: "+this.tropa+"\n";
    }
}
