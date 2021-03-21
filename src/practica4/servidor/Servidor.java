package practica4.servidor;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Servidor{

    public static void main(String args[]) {
        int portNum=19000;
        String registryURL;
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:practica4.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }



















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