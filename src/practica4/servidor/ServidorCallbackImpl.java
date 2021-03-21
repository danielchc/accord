package practica4.servidor;

import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class ServidorCallbackImpl extends UnicastRemoteObject implements ServidorCallback {

    private HashMap<UUID, IUsuario> listaClientes;
    public ServidorCallbackImpl() throws RemoteException{
        super();
        listaClientes=new HashMap<UUID,IUsuario>();
    }


    @Override
    public void registrarCliente(IUsuario novoCliente) throws RemoteException{
        System.out.println("Conectado novo cliente " + novoCliente.getUuid());
        listaClientes.put(novoCliente.getUuid(),novoCliente);
        notificarClientesConexion(novoCliente);
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
    public List<UUID> getListaClientes() throws RemoteException{
        return new ArrayList<>(listaClientes.keySet());
    }

    @Override
    public IUsuario getCliente(UUID uuid) throws RemoteException {
        return listaClientes.get(uuid);
    }




    private synchronized void notificarClientesConexion(IUsuario novoCliente) throws RemoteException{
        for(IUsuario cb:listaClientes.values()){
            cb.getClienteCallback().rexistrarClienteDisponible(novoCliente.getUuid());
        }
    }

    private synchronized void notificarClientesDesconexion(IUsuario velloCliente) throws RemoteException{
        for(IUsuario cb:listaClientes.values()){
            cb.getClienteCallback().eliminarClienteDisponible(velloCliente.getUuid());
        }
    }


}
