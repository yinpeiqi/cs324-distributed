package myrmi.registry;

import myrmi.exception.RemoteException;

public class Main {
    public static void main(String args[]) {
        final int regPort = (args.length >= 1) ? Integer.parseInt(args[0])
                : Registry.REGISTRY_PORT;
        RegistryImpl registry;
        try {
            registry = new RegistryImpl(regPort);
        } catch (RemoteException e) {
            System.exit(1);
        }

        System.out.printf("RMI Registry is listening on port %d\n", regPort);

    }
}
