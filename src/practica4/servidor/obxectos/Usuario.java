package practica4.servidor.obxectos;

import practica4.interfaces.ClienteCallback;
import practica4.interfaces.IUsuario;

import java.rmi.RemoteException;
import java.util.Objects;
import java.util.UUID;

public class Usuario implements IUsuario {
    private UUID uuid;
    private String nomeUsuario;
    private boolean registrado;
    private ClienteCallback clienteCallback;

    public Usuario(UUID uuid, String nomeUsuario) throws RemoteException {
        this.registrado=false;
        this.uuid=uuid;
        this.nomeUsuario=nomeUsuario;
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

    public ClienteCallback getClienteCallback() {
        return clienteCallback;
    }

    public void setClienteCallback(ClienteCallback clienteCallback) {
        this.clienteCallback = clienteCallback;
    }

    @Override
    public String toString() {
        return getNomeUsuario();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(uuid, usuario.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
