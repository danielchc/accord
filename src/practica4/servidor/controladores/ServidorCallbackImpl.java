package practica4.servidor.controladores;

import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class ServidorCallbackImpl extends UnicastRemoteObject implements ServidorCallback {

    private BDControlador bdControlador;
    private HashMap<UUID, IUsuario> listaClientes;
    public ServidorCallbackImpl() throws RemoteException{
        super();
        listaClientes=new HashMap<UUID,IUsuario>();
        bdControlador=new BDControlador();
    }


    @Override
    public boolean registrarCliente(IUsuario novoCliente) throws RemoteException{
        if(listaClientes.containsKey(novoCliente.getUuid()))
            return false;

        System.out.println("Conectado novo cliente " + novoCliente.getUuid());
        listaClientes.put(novoCliente.getUuid(),novoCliente);
        notificarClientesConexion(novoCliente);
        return true;
    }

    @Override
    public void desRegistrarCliente(IUsuario velloCliente) throws RemoteException{
        System.out.println("Cliente desconectado " + velloCliente.getUuid());
        listaClientes.remove(velloCliente.getUuid());

        notificarClientesDesconexion(velloCliente);
    }

    @Override
    public String getNomeUsuario(UUID uuid) throws RemoteException {
        return listaClientes.get(uuid).getNomeUsuario();
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
    public List<IUsuario> getListaClientes() throws RemoteException{
        return new ArrayList<IUsuario>(listaClientes.values());
    }

    @Override
    public IUsuario getCliente(UUID uuid) throws RemoteException {
        return listaClientes.get(uuid);
    }



    private synchronized void notificarClientesConexion(IUsuario novoCliente) throws RemoteException{
        for(IUsuario cb:listaClientes.values()){
            cb.getClienteCallback().rexistrarClienteDisponible(novoCliente);
        }
    }

    private synchronized void notificarClientesDesconexion(IUsuario velloCliente) throws RemoteException{
        for(IUsuario cb:listaClientes.values()){
            cb.getClienteCallback().eliminarClienteDisponible(velloCliente);
        }
    }


}
