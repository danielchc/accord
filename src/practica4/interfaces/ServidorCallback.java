package practica4.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;


public interface ServidorCallback extends Remote{

    boolean tenIniciadoSesion(String nomeUsuario) throws RemoteException;

    List<ClienteCallback> getListaClientes() throws RemoteException;

    void registrarCliente(ClienteCallback obj) throws RemoteException;

    void desRegistrarCliente(UUID uuid) throws RemoteException;

    ClienteCallback getCliente(UUID uuid) throws RemoteException;

    boolean comprobarUsuario(String nomeUsuario,String contrasinal)  throws RemoteException;

    IUsuario getUsuario(String nomeUsuario)  throws RemoteException;

}