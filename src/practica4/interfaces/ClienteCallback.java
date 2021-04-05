package practica4.interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;


public interface ClienteCallback extends Remote {

    void enviarMensaxe(IMensaxe message) throws RemoteException;

    void rexistrarUsuarioDisponible(IUsuario novoCliente) throws RemoteException;

    void eliminarUsuarioDisponible(IUsuario velloUsuario) throws RemoteException;

    void novoAmigo(IUsuario amigo) throws RemoteException;

    void eliminarAmigo(IUsuario amigo) throws RemoteException;

    IUsuario getUsuario() throws RemoteException;

    void setUsuario(IUsuario usuario) throws RemoteException;

    UUID getUuid() throws RemoteException;

    void enviarSolicitude(IRelacion relacion) throws RemoteException;

}
