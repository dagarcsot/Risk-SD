import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dado {
    Dado(){

    }
    public int[] tirarDados(int numTiradas){
        int d[] = new int[numTiradas];
        Random r = new Random();
        for(int i=0; i<numTiradas;i++){
            d[i] = r.nextInt(6) + 1;
        }
        return d;
    }
}
