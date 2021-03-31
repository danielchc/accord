package practica4.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public interface ServidorCallback extends Remote {

    boolean tenIniciadoSesion(String nomeUsuario) throws RemoteException;

    List<IUsuario> getListaUsuarios() throws RemoteException;

    void rexistrarCliente(ClienteCallback obj) throws RemoteException;

    void desRexistrarCliente(UUID uuid) throws RemoteException;

    ClienteCallback getCliente(UUID uuid) throws RemoteException;

    boolean comprobarUsuario(String nomeUsuario, String contrasinal) throws RemoteException;

    IUsuario getUsuario(String nomeUsuario) throws RemoteException;

    List<IUsuario> getAmigos(IUsuario usuario) throws RemoteException;

    List<IRelacion> buscarUsuarios(String query,IUsuario usuario) throws RemoteException;


    void enviarSolicitude(IRelacion relacion) throws RemoteException;

    void aceptarSolicitude(IRelacion item) throws RemoteException;

    void cancelarSolicitude(IRelacion item) throws RemoteException;

    void eliminarAmigo(IRelacion item) throws RemoteException;

    boolean comprobarUsuarioExiste(String u) throws RemoteException;

    IUsuario rexistrarUsuario(String u, String p) throws RemoteException;
}