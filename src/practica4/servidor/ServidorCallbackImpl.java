package practica4.servidor;

import practica4.interfaces.ClienteCallback;
import practica4.interfaces.ServidorCallback;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.util.stream.Collectors;

public class ServidorCallbackImpl extends UnicastRemoteObject implements ServidorCallback {

    private HashMap<UUID,ClienteCallback> listaClientes;
    public ServidorCallbackImpl() throws RemoteException{
        super();
        listaClientes=new HashMap<UUID,ClienteCallback>();
    }


    public void registrarCliente(ClienteCallback novoCliente) throws RemoteException{
        System.out.println("Conectado novo cliente " + novoCliente.getUUID());
        listaClientes.put(novoCliente.getUUID(),novoCliente);
        notificarClientesConexion(novoCliente);
    }

    public void desRegistrarCliente(ClienteCallback velloCliente) throws RemoteException{
        System.out.println("Cliente desconectado " + velloCliente.getUUID());
        listaClientes.remove(velloCliente.getUUID());
        notificarClientesDesconexion(velloCliente);
    }

    private synchronized void notificarClientesConexion(ClienteCallback novoCliente) throws RemoteException{
        for(ClienteCallback cb:listaClientes.values()){
            cb.rexistrarClienteDisponible(novoCliente.getUUID());
        }
    }

    private synchronized void notificarClientesDesconexion(ClienteCallback velloCliente) throws RemoteException{
        for(ClienteCallback cb:listaClientes.values()){
            cb.eliminarClienteDisponible(velloCliente.getUUID());
        }
    }

    public List<UUID> getListaClientes() throws RemoteException{
        return new ArrayList<>(listaClientes.keySet());
    }

    @Override
    public ClienteCallback getCliente(UUID uuid) throws RemoteException {
        return listaClientes.get(uuid);
    }
}
