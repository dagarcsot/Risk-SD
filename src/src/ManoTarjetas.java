import java.util.ArrayList;
import java.util.List;

public class ManoTarjetas {

    private List<Tarjeta> mano;

    public ManoTarjetas(){
        this.mano = new ArrayList<Tarjeta>();
    }

    public void addTarjeta(Tarjeta t){
        mano.add(t);
    }

    public void eliminaTarjeta(Tarjeta t){
        if(this.mano.remove(t)){
            System.out.println("Tarjeta eliminada con exito");
        } else{
            System.out.println("La tarjeta no se elimino correctamente");
        }
    }

    public void eliminaTarjeta(int pos1, int pos2, int pos3){
        if(canjearTarjetas(pos1,pos2,pos3) == 0){
            this.mano.remove(pos1);
            this.mano.remove(pos2);
            this.mano.remove(pos3);
        } else {
            System.out.println("Debes canjear 3 cartas del mismo tipo o 1 de cada tipo");
        }
    }

    public int canjearTarjetas(int pos1, int pos2, int pos3){
        //si puede canjear devuelve el valor de las tropas, si no devuelve 0
        int valor = 0;
        if(mano.size()>=3){
            if(mano.get(pos1).getTropa().equals(mano.get(pos2).getTropa()) && mano.get(pos1).getTropa().equals(mano.get(pos3).getTropa())){
                //las 3 cartas tienen la misma tropa
                valor = 3;
            }
            if (!this.mano.get(pos1).getTropa().equals(this.mano.get(pos2).getTropa()) &&
                    !this.mano.get(pos1).getTropa().equals(this.mano.get(pos3).getTropa()) &&
                    this.mano.get(pos3).getTropa().equals(this.mano.get(pos2).getTropa())){
                //diferente tropa las 3 cartas
                valor = 5;
            }
        }
        return valor;
    }

    public boolean debeCanjear(){
        return this.mano.size() >= 5;
    }

    public List<Tarjeta> getMano(){
        return this.mano;
    }
}
