package practica4.interfaces;

import practica4.cliente.obxectos.Mensaxe;

import java.rmi.*;
import java.util.UUID;


public interface ClienteCallback extends Remote{

    UUID getUUID() throws RemoteException;

    void enviarMensaxe(Mensaxe message) throws RemoteException;

    void rexistrarClienteDisponible(UUID novoCliente) throws RemoteException;

    void eliminarClienteDisponible(UUID velloCliente) throws RemoteException;

}
