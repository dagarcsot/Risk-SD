import java.io.Serializable;
import java.util.List;

import java.util.*;
public class Pais implements Serializable {
    private String nombre;
    private int numTropas; //num tropas que tiene ese país
    private Jugador propietario; //jugador que tiene el país
    private List<Pais> paisesFrontera;


    Pais(String nombre) {
        this.nombre = nombre;
        this.propietario = null;
        this.numTropas = 0;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getNumTropas() {
        return this.numTropas;
    }

    public void setNumTropas(int numTropas) {
        this.numTropas = numTropas;
    }

    public void setFronteras(List<Pais> l) {
        this.paisesFrontera = l;
    }

    public List<Pais> getPaisesFrontera() {
        return this.paisesFrontera;
    }

    public Jugador getPropietario() {
        return this.propietario;
    }
    public void setPropietario(Jugador j) {
         this.propietario = j;
    }

    public boolean sonFrontera(Pais p) {
        return this.paisesFrontera.contains(p);
    }

    public boolean puedeAtacar() {
        return this.numTropas >= 2;
    }

    public int numDadosMaxPuedeTirar() {
        if (this.numTropas < 4) {
            return this.numTropas - 1;
        } else {
            return 3;
        }
    }

    @Override
    public String toString() {
        return "Pais: " + this.nombre  + ", Num tropas: "+this.numTropas + ", Propietario: " +
                ((this.propietario != null) ? this.propietario.getNombre() : "No tiene propietario\n");
    }

    public void moverTropas(Pais p,int numTropas){
        //PRE: numTropas<this.getNumTropas()
        this.setNumTropas(this.getNumTropas()-numTropas);
        p.setNumTropas(p.getNumTropas()+numTropas);
    }


}
