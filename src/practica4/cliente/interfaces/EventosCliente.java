package practica4.cliente.interfaces;

import practica4.cliente.obxectos.Mensaxe;

import java.util.UUID;

public interface EventosCliente {
    void onMensaxeRecibido(Mensaxe mensaxe);
    void onUsuarioConectado(UUID uuid);
    void onUsuarioDesconectado(UUID uuid);
}
