public final class Tarjeta {

    private final Pais pais;
    private final String tropa;

    public Tarjeta(Pais pais, String tropa){
        this.pais=pais;
        this.tropa=tropa;
    }

    public String getNombrePais(){
        return this.pais.getNombre();
    }

    public String getTropa(){
        return this.tropa;
    }

    @Override
    public String toString() {
        return "Pais: "+this.pais.getNombre()+"\n"+
                "Tropa: "+this.tropa+"\n";
    }
}
