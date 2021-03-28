package practica4.servidor.controladores;

import practica4.interfaces.ClienteCallback;
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
    public void registrarCliente(ClienteCallback novoCliente) throws RemoteException{
        System.out.println("Conectado novo cliente " + novoCliente.getUuid());
        listaClientes.put(novoCliente.getUsuario().getUuid(),novoCliente);
        notificarClientesConexion(novoCliente);
    }

    @Override
    public void desRegistrarCliente(UUID velloClienteUUID) throws RemoteException{
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
            if(true) //Comprobar se son amigos novoClient e cb
                cb.rexistrarUsuarioDisponible(novoCliente.getUsuario());
        }
    }

    private synchronized void notificarClientesDesconexion(ClienteCallback velloCliente) throws RemoteException{
        for(ClienteCallback cb:listaClientes.values()){
            cb.eliminarUsuarioDisponible(velloCliente.getUsuario());
        }
    }


}
