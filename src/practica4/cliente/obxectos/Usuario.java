package practica4.cliente.obxectos;

import practica4.cliente.controladores.ClienteCallbackImpl;
import practica4.interfaces.ClienteCallback;
import practica4.interfaces.IUsuario;

import java.rmi.RemoteException;
import java.util.UUID;

public class Usuario implements IUsuario {
    private ClienteCallback clienteCallback;
    private UUID uuid;
    private String nomeUsuario;
    private boolean registrado;

    public Usuario() throws RemoteException {
        this.registrado =false;
        this.clienteCallback=new ClienteCallbackImpl();
    }


    @Override
    public ClienteCallback getClienteCallback() {
        return clienteCallback;
    }

    @Override
    public void setClienteCallback(ClienteCallback clienteCallback) {
        this.clienteCallback = clienteCallback;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getNomeUsuario() {
        return nomeUsuario;
    }

    @Override
    public void setNomeUsuario(String usuario) {
        this.nomeUsuario = usuario;
    }

    @Override
    public boolean isRegistrado() {
        return registrado;
    }

    @Override
    public void setRegistrado(boolean registrado) {
        this.registrado = registrado;
    }
}
