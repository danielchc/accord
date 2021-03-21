package practica4.interfaces;

import java.rmi.*;
import java.util.List;
import java.util.UUID;


public interface ServidorCallback extends Remote{

    void registrarCliente(IUsuario obj) throws RemoteException;

    List<UUID> getListaClientes() throws RemoteException;

    IUsuario getCliente(UUID uuid) throws RemoteException;

    void desRegistrarCliente(IUsuario obj) throws RemoteException;

    String getNomeUsuario(UUID uuid) throws RemoteException;

}