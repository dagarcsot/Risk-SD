import java.util.Random;

public class Principal {
    public static void main(String[] args) {
        Mapa m = new Mapa();


        for(int i=0;i<m.getMazo().size();i++){
            System.out.println(i+". "+m.getMazo().get(i).toString());
        }


    }
}