package practica4.servidor;

import practica4.servidor.controladores.ServidorCallbackImpl;

import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;


public class Servidor{

    public static void main(String args[]) {
        int portNum=19000;
        String registryURL;
        registryURL = String.format("rmi://0.0.0.0:%d/servidor",portNum);
        try{
            startRegistry(portNum);
            ServidorCallbackImpl exportedObj = new ServidorCallbackImpl();
            Naming.rebind(registryURL, exportedObj);

        }catch(Exception e){
            System.out.println(e);
        };
    }


    private static void startRegistry(int RMIPortNum) throws RemoteException{
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();

        }catch (RemoteException e) { 
            System.out.println("O rexistro RMI non se puido atopar no porto " + RMIPortNum);
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
            System.out.println("Rexistro RMI creado no porto " + RMIPortNum);
        }
    }


}