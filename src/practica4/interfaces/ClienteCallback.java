package practica4.interfaces;

import practica4.cliente.obxectos.Mensaxe;

import java.rmi.*;
import java.util.UUID;


public interface ClienteCallback extends Remote{

    void enviarMensaxe(Mensaxe message) throws RemoteException;

    void rexistrarClienteDisponible(IUsuario novoCliente) throws RemoteException;

    void eliminarClienteDisponible(IUsuario velloCliente) throws RemoteException;

}
