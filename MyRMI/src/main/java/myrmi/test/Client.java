package myrmi.test;

import myrmi.registry.LocateRegistry;
import myrmi.registry.Registry;

public class Client {

    public static void main(String[] args) throws Exception {
        Registry r = LocateRegistry.getRegistry();
        IFoo foo = (IFoo) r.lookup("remoteFoo");
        System.out.println(foo.getMessage());
        foo.printMessage();
    }
}
