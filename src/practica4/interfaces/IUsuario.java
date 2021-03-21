package practica4.interfaces;

import practica4.interfaces.ClienteCallback;

import java.io.Serializable;
import java.util.UUID;

public interface IUsuario extends Serializable {

    UUID getUuid();

    void setUuid(UUID uuid);

    String getNomeUsuario();

    void setNomeUsuario(String usuario);

    void setRegistrado(boolean conectado);

    boolean isRegistrado();

    void setClienteCallback(ClienteCallback clienteCallback);

    ClienteCallback getClienteCallback();

}
