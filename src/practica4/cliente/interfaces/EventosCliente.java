package practica4.cliente.interfaces;

import practica4.cliente.obxectos.IMensaxe;
import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.ClienteCallback;

import java.util.UUID;

public interface EventosCliente {
    void onMensaxeRecibido(IMensaxe mensaxe);
    void onUsuarioConectado(UUID uuid);
    void onUsuarioDesconectado(UUID uuid);
}
