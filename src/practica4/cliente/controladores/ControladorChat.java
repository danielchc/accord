package practica4.cliente.controladores;

import javafx.application.Platform;
import practica4.cliente.obxectos.Chat;
import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.ClienteCallback;
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.rmi.RemoteException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ControladorChat {

    private final IUsuario usuarioActual;
    private final ServidorCallback servidorCallback;
    private final HashMap<UUID, Chat> chats;
    private Map<UUID, IUsuario> amigos;


    public ControladorChat(IUsuario usuarioActual, ServidorCallback servidorCallback) {
        this.servidorCallback = servidorCallback;
        this.chats = new HashMap<UUID, Chat>();
        this.usuarioActual = usuarioActual;
        this.amigos = new HashMap<UUID, IUsuario>();
    }


    private void comprobarChat(IUsuario u) {
        if (!chats.containsKey(u.getUuid()))
            chats.put(u.getUuid(), new Chat(u));
    }

    public void rexistrarCliente() {
        try {
            ClienteCallbackImpl clienteCallback = new ClienteCallbackImpl(usuarioActual) {
                @Override
                public void onMensaxeRecibido(Mensaxe mensaxe) {
                    comprobarChat(mensaxe.getDe());
                    chats.get(mensaxe.getDe().getUuid()).engadirMensaxe(mensaxe);
                    mensaxeRecibido(mensaxe);
                }

                @Override
                protected void onUsuarioConectado(IUsuario u) throws RemoteException {
                    if (u.getUuid().equals(usuarioActual.getUuid())) return;
                    amigos.put(u.getUuid(),u);
                    usuarioConectado(u);
                }

                @Override
                protected void onUsuarioDesconectado(IUsuario u) throws RemoteException {
                    amigos.put(u.getUuid(),u);
                    usuarioDesconectado(u);
                }
            };
            amigos = servidorCallback.getAmigos(usuarioActual).stream().collect(Collectors.toMap(IUsuario::getUuid, Function.identity()));
            usuarioActual.setConectado(true);
            servidorCallback.registrarCliente(clienteCallback);

            rexistroCorrecto(amigos);

        } catch (RemoteException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    public Mensaxe enviarMensaxe(IUsuario para, String mensaxe) throws RemoteException {
        if(!para.isConectado()) return null;
        ClienteCallback cl = servidorCallback.getCliente(para.getUuid());
        comprobarChat(para);
        Mensaxe m = new Mensaxe(usuarioActual, para, mensaxe);
        cl.enviarMensaxe(m);
        chats.get(m.getPara().getUuid()).engadirMensaxe(m);
        return m;
    }

    public boolean existeChat(UUID uuid) {
        return chats.containsKey(uuid);
    }

    public Chat getChat(UUID uuid) {
        return chats.get(uuid);
    }

    public abstract void mensaxeRecibido(Mensaxe m);

    public abstract void rexistroCorrecto(Map<UUID,IUsuario> amigos);

    public  abstract void usuarioConectado(IUsuario usuario);

    public  abstract void usuarioDesconectado(IUsuario usuario);
}
