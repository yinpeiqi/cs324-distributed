import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(2000);
            IFoo foo = new Foo();
            registry.bind("remoteFoo", foo);
            System.out.println("RMI registry started.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
