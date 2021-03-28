package practica4.interfaces;

import practica4.cliente.obxectos.Mensaxe;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;


public interface ClienteCallback extends Remote {

    void enviarMensaxe(Mensaxe message) throws RemoteException;

    void rexistrarUsuarioDisponible(IUsuario novoCliente) throws RemoteException;

    void eliminarUsuarioDisponible(IUsuario velloUsuario) throws RemoteException;

    IUsuario getUsuario() throws RemoteException;

    void setUsuario(IUsuario usuario) throws RemoteException;

    UUID getUuid() throws RemoteException;

}
