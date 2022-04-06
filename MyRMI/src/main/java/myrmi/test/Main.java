package myrmi.test;

import myrmi.registry.LocateRegistry;
import myrmi.registry.Registry;

public class Main {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            IFoo foo = (IFoo) new Foo("Hi from remote Foo!");
            registry.bind("remoteFoo", foo);
            System.out.println("RMI registry started.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
