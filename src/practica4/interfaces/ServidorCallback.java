package practica4.interfaces;

import java.rmi.*;
import java.util.List;
import java.util.UUID;


public interface ServidorCallback extends Remote{

    void registrarCliente(ClienteCallback obj) throws RemoteException;

    List<UUID> getListaClientes() throws RemoteException;

    ClienteCallback getCliente(UUID uuid) throws RemoteException;

    void desRegistrarCliente(ClienteCallback obj) throws RemoteException;

}