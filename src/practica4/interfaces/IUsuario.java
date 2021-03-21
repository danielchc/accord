package practica4.interfaces;

import practica4.interfaces.ClienteCallback;

import java.io.Serializable;
import java.util.UUID;

public interface IUsuario extends Serializable {
    ClienteCallback getClienteCallback();

    void setClienteCallback(ClienteCallback clienteCallback);

    UUID getUuid();

    void setUuid(UUID uuid);

    String getNomeUsuario();

    void setNomeUsuario(String usuario);

    void setRegistrado(boolean conectado);

    boolean isRegistrado();
}
