package practica4.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;


public interface ServidorCallback extends Remote {

    boolean tenIniciadoSesion(String nomeUsuario) throws RemoteException;

    void rexistrarCliente(UUID authToken,ClienteCallback obj) throws RemoteException;

    void desRexistrarCliente(UUID authToken, UUID uuid) throws RemoteException;

    UUID comprobarUsuario(String nomeUsuario, String contrasinal) throws RemoteException;

    boolean comprobarUsuarioExiste(String u) throws RemoteException;

    IUsuario getUsuario(UUID authToken,String nomeUsuario) throws RemoteException;

    IUsuario rexistrarUsuario(String u, String p) throws RemoteException;

    List<IUsuario> getListaUsuarios(UUID token) throws RemoteException;

    List<IUsuario> getAmigos(UUID token, IUsuario usuario) throws RemoteException;

    List<IRelacion> buscarUsuarios(UUID token, String query, IUsuario usuario) throws RemoteException;

    void enviarSolicitude(UUID token, IRelacion relacion) throws RemoteException;

    void aceptarSolicitude(UUID token, IRelacion item) throws RemoteException;

    void cancelarSolicitude(UUID token, IRelacion item) throws RemoteException;

    void eliminarAmigo(UUID token, IRelacion item) throws RemoteException;

    ClienteCallback getCliente(UUID token, UUID uuid) throws RemoteException;
}