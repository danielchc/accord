package practica4.servidor.controladores;

import practica4.interfaces.ClienteCallback;
import practica4.interfaces.IRelacion;
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServidorCallbackImpl extends UnicastRemoteObject implements ServidorCallback {

    private final BDControlador bdControlador;
    private final HashMap<UUID, ClienteCallback> listaClientes;
    public ServidorCallbackImpl() throws RemoteException{
        super();
        listaClientes=new HashMap<UUID,ClienteCallback>();
        bdControlador=new BDControlador();
    }


    @Override
    public void rexistrarCliente(ClienteCallback novoCliente) throws RemoteException{
        System.out.println("Conectado novo cliente " + novoCliente.getUuid());
        listaClientes.put(novoCliente.getUsuario().getUuid(),novoCliente);
        notificarClientesConexion(novoCliente);
    }

    @Override
    public void desRexistrarCliente(UUID velloClienteUUID) throws RemoteException{
        System.out.println("Cliente desconectado " + velloClienteUUID);
        notificarClientesDesconexion(listaClientes.get(velloClienteUUID));
        listaClientes.remove(velloClienteUUID);
    }


    @Override
    public boolean comprobarUsuario(String nomeUsuario, String contrasinal) throws RemoteException {
        return bdControlador.comprobarUsuario(nomeUsuario,contrasinal);
    }

    @Override
    public IUsuario getUsuario(String nomeUsuario) throws RemoteException {
        return bdControlador.getUsuario(nomeUsuario);
    }

    @Override
    public List<IUsuario> getAmigos(IUsuario usuario) throws RemoteException {
        return bdControlador.getAmigos(usuario).stream().map(k->{
            k.setConectado(listaClientes.containsKey(k.getUuid()));
            return k;
        }).collect(Collectors.toList());
    }

    @Override
    public List<IRelacion> buscarUsuarios(String query, IUsuario usuario) throws RemoteException {
        return bdControlador.buscarUsuarios(query,usuario);
    }

    @Override
    public void enviarSolicitude(IRelacion relacion) throws RemoteException{
        bdControlador.crearSolicitude(relacion);

        if(listaClientes.containsKey(relacion.getU2().getUuid()))
            listaClientes.get(relacion.getU2().getUuid()).enviarSolicitude(relacion);

    }

    @Override
    public void aceptarSolicitude(IRelacion item) throws RemoteException {
        bdControlador.aceptarSolicitude(item);
        item.getU1().setConectado(listaClientes.containsKey(item.getU1().getUuid()));
        item.getU2().setConectado(listaClientes.containsKey(item.getU2().getUuid()));

        if(listaClientes.containsKey(item.getU1().getUuid()))
            listaClientes.get(item.getU1().getUuid()).novoAmigo(item.getU2());
        if(listaClientes.containsKey(item.getU2().getUuid()))
            listaClientes.get(item.getU2().getUuid()).novoAmigo(item.getU1());

    }

    @Override
    public void cancelarSolicitude(IRelacion item) throws RemoteException{
        bdControlador.eliminarAmigo(item);
        if(listaClientes.containsKey(item.getU1().getUuid()))
            listaClientes.get(item.getU1().getUuid()).eliminarAmigo(item.getU2());
        if(listaClientes.containsKey(item.getU2().getUuid()))
            listaClientes.get(item.getU2().getUuid()).eliminarAmigo(item.getU1());
    }

    @Override
    public void eliminarAmigo(IRelacion item) throws RemoteException{
        bdControlador.eliminarAmigo(item);
        if(listaClientes.containsKey(item.getU1().getUuid()))
            listaClientes.get(item.getU1().getUuid()).eliminarAmigo(item.getU2());
        if(listaClientes.containsKey(item.getU2().getUuid()))
            listaClientes.get(item.getU2().getUuid()).eliminarAmigo(item.getU1());
    }

    @Override
    public boolean comprobarUsuarioExiste(String u) throws RemoteException {
        return bdControlador.comprobarUsuarioExiste(u);
    }

    @Override
    public IUsuario rexistrarUsuario(String u, String p) throws RemoteException {
        return bdControlador.rexistrarUsuario(u,p);
    }

    @Override
    public List<IUsuario> getListaUsuarios() throws RemoteException{
        return new ArrayList<IUsuario>(listaClientes.values().stream().map(k-> {
            try {
                return k.getUsuario();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()));
    }

    @Override
    public ClienteCallback getCliente(UUID uuid) throws RemoteException {
        return listaClientes.get(uuid);
    }


    public boolean tenIniciadoSesion(String nomeUsuario){
        IUsuario u=bdControlador.getUsuario(nomeUsuario);
        return listaClientes.containsKey(u.getUuid());
    }

    private synchronized void notificarClientesConexion(ClienteCallback novoCliente) throws RemoteException{
        for(ClienteCallback cb:listaClientes.values()){
            if(bdControlador.sonAmigos(novoCliente.getUsuario(),cb.getUsuario()))
                cb.rexistrarUsuarioDisponible(novoCliente.getUsuario());
        }
    }

    private synchronized void notificarClientesDesconexion(ClienteCallback velloCliente) throws RemoteException{
        for(ClienteCallback cb:listaClientes.values()){
            cb.eliminarUsuarioDisponible(velloCliente.getUsuario());
        }
    }


}
