import java.util.ArrayList;
import java.util.List;

public class ManoTarjetas {

    private List<Tarjeta> mano;

    public ManoTarjetas(){
        mano = new ArrayList<Tarjeta>();
    }

    public void addTarjeta(Tarjeta t){
        mano.add(t);
    }

    public void eliminaTarjeta(int pos1, int pos2, int pos3){
        if(canjearTarjetas(pos1,pos2,pos3)){
            mano.remove(pos1);
            mano.remove(pos2);
            mano.remove(pos3);
        } else {
            System.out.println("Debes canjear 3 cartas del mismo tipo o 1 de cada tipo");
        }
    }

    public boolean canjearTarjetas(int pos1, int pos2, int pos3){
        boolean puede = false;
        if(mano.size()>=3){
            if(mano.get(pos1).getTropa().equals(mano.get(pos2).getTropa()) && mano.get(pos1).getTropa().equals(mano.get(pos3).getTropa())){
                //las 3 cartas tienen la misma tropa
                puede = true;
            }
            else if (!mano.get(pos1).getTropa().equals(mano.get(pos2).getTropa()) && !mano.get(pos1).getTropa().equals(mano.get(pos3).getTropa())
            && mano.get(pos3).getTropa().equals(mano.get(pos2).getTropa())){
                puede = true;
            }
        }
        return puede;
    }

    public boolean debeCanjear(){
        return mano.size() >= 5;
    }

    public List<Tarjeta> getMano(){
        return mano;
    }
}
