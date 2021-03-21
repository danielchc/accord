package practica4.cliente.interfaces;

import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.IUsuario;

import java.util.UUID;

public interface EventosCliente {
    void onMensaxeRecibido(Mensaxe mensaxe);
    void onUsuarioConectado(IUsuario usuario);
    void onUsuarioDesconectado(IUsuario usuario);
}
