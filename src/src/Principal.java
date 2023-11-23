public class Principal {
    public static void main(String[] args) {
        Creador c = new Creador();
        System.out.println(c.getPaises().get(0));
        System.out.println("Sus paises frontera son:");
        System.out.println(c.getPaises().get(0).getPaisesFrontera());
    }
}
