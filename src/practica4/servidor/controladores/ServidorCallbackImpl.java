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
    private ArrayList<UUID> listaToken;

    public ServidorCallbackImpl() throws RemoteException{
        super();
        listaClientes=new HashMap<UUID,ClienteCallback>();
        listaToken=new ArrayList<UUID>();
        bdControlador=new BDControlador();
    }


    @Override
    public void rexistrarCliente(ClienteCallback novoCliente) throws RemoteException{
        System.out.println("Conectado novo cliente " + novoCliente.getUuid());
        listaClientes.put(novoCliente.getUsuario().getUuid(),novoCliente);
        notificarClientesConexion(novoCliente);
    }

    @Override
    public void desRexistrarCliente(UUID authToken, UUID velloClienteUUID) throws RemoteException{
        if(estaAutenticado(authToken))return;
        
        System.out.println("Cliente desconectado " + velloClienteUUID);
        notificarClientesDesconexion(listaClientes.get(velloClienteUUID));
        listaClientes.remove(velloClienteUUID);
        listaToken.remove(authToken);
    }

    @Override
    public UUID comprobarUsuario(String nomeUsuario, String contrasinal) throws RemoteException {
        UUID token=null;
        if (bdControlador.comprobarUsuario(nomeUsuario,contrasinal)){
            token=UUID.randomUUID();
            listaToken.add(token);
        }
        return token;
    }

    @Override
    public IUsuario getUsuario(String nomeUsuario) throws RemoteException {
        return bdControlador.getUsuario(nomeUsuario);
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
    public boolean tenIniciadoSesion(String nomeUsuario){
        IUsuario u=bdControlador.getUsuario(nomeUsuario);
        return listaClientes.containsKey(u.getUuid());
    }


    @Override
    public List<IUsuario> getAmigos(UUID token, IUsuario usuario) throws RemoteException {
        if(!estaAutenticado(token))
            return null;

        return bdControlador.getAmigos(usuario).stream().map(k->{
            k.setConectado(listaClientes.containsKey(k.getUuid()));
            return k;
        }).collect(Collectors.toList());
    }

    @Override
    public List<IRelacion> buscarUsuarios(UUID token, String query, IUsuario usuario) throws RemoteException {
        if(!estaAutenticado(token))
            return null;
        return bdControlador.buscarUsuarios(query,usuario);
    }

    @Override
    public void enviarSolicitude(UUID token,IRelacion relacion) throws RemoteException{
        if(!estaAutenticado(token))
            return;

        bdControlador.crearSolicitude(relacion);
        if(listaClientes.containsKey(relacion.getU2().getUuid()))
            listaClientes.get(relacion.getU2().getUuid()).enviarSolicitude(relacion);

    }

    @Override
    public void aceptarSolicitude(UUID token, IRelacion item) throws RemoteException {
        if(!estaAutenticado(token))
            return;
        bdControlador.aceptarSolicitude(item);
        item.getU1().setConectado(listaClientes.containsKey(item.getU1().getUuid()));
        item.getU2().setConectado(listaClientes.containsKey(item.getU2().getUuid()));

        if(listaClientes.containsKey(item.getU1().getUuid()))
            listaClientes.get(item.getU1().getUuid()).novoAmigo(item.getU2());
        if(listaClientes.containsKey(item.getU2().getUuid()))
            listaClientes.get(item.getU2().getUuid()).novoAmigo(item.getU1());

    }

    @Override
    public void cancelarSolicitude(UUID token, IRelacion item) throws RemoteException{
        if(!estaAutenticado(token))
            return;
        bdControlador.eliminarAmigo(item);
        if(listaClientes.containsKey(item.getU1().getUuid()))
            listaClientes.get(item.getU1().getUuid()).eliminarAmigo(item.getU2());
        if(listaClientes.containsKey(item.getU2().getUuid()))
            listaClientes.get(item.getU2().getUuid()).eliminarAmigo(item.getU1());
    }

    @Override
    public void eliminarAmigo(UUID token, IRelacion item) throws RemoteException{
        if(!estaAutenticado(token))
            return;
        bdControlador.eliminarAmigo(item);
        if(listaClientes.containsKey(item.getU1().getUuid()))
            listaClientes.get(item.getU1().getUuid()).eliminarAmigo(item.getU2());
        if(listaClientes.containsKey(item.getU2().getUuid()))
            listaClientes.get(item.getU2().getUuid()).eliminarAmigo(item.getU1());
    }



    @Override
    public List<IUsuario> getListaUsuarios(UUID token) throws RemoteException{
        if(!estaAutenticado(token))
            return null;
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
    public ClienteCallback getCliente(UUID token, UUID uuid) throws RemoteException {
        if(!estaAutenticado(token))
            return null;
        return listaClientes.get(uuid);
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

    private boolean estaAutenticado(UUID token) throws RemoteException{
        return listaToken.contains(token);
    }

}
