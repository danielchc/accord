package practica4.cliente.controladores;

import javafx.application.Platform;
import practica4.cliente.obxectos.Chat;
import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.*;

import java.rmi.RemoteException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ControladorChat {

    private final IUsuario usuarioActual;
    private final ServidorCallback servidorCallback;
    private final HashMap<UUID, Chat> chats;
    private Map<UUID, IUsuario> amigos;
    private UUID authToken;


    public ControladorChat(UUID authToken,IUsuario usuarioActual, ServidorCallback servidorCallback) {
        this.servidorCallback = servidorCallback;
        this.chats = new HashMap<UUID, Chat>();
        this.usuarioActual = usuarioActual;
        this.amigos = new HashMap<UUID, IUsuario>();
        this.authToken=authToken;
    }


    private void comprobarChat(IUsuario u) {
        if (!chats.containsKey(u.getUuid()))
            chats.put(u.getUuid(), new Chat(u));
    }

    public void rexistrarCliente() {
        try {
            ClienteCallbackImpl clienteCallback = new ClienteCallbackImpl(authToken,usuarioActual) {
                @Override
                public void onMensaxeRecibido(IMensaxe mensaxe) {
                    comprobarChat(mensaxe.getDe());
                    chats.get(mensaxe.getDe().getUuid()).engadirMensaxe(mensaxe);
                    mensaxeRecibido(mensaxe);
                }

                @Override
                protected void onAmigoNovo(IUsuario u) throws RemoteException {
                    amigos.put(u.getUuid(),u);
                    amigoNovo(u);
                }

                @Override
                protected void onAmigoEliminado(IUsuario u) throws RemoteException {
                    amigos.remove(u.getUuid());
                    amigoEliminado(u);
                }

                @Override
                protected void onSolicitudeRecibida(IRelacion relacion) throws RemoteException {
                    solicitudeRecibida(relacion);
                }

                @Override
                protected void onUsuarioConectado(IUsuario u) throws RemoteException {
                    if (u.getUuid().equals(usuarioActual.getUuid())) return;
                    amigos.put(u.getUuid(),u);
                    usuarioConectado(u);
                }

                @Override
                protected void onUsuarioDesconectado(IUsuario u) throws RemoteException {
                    if(!amigos.containsKey(u.getUuid()))return;
                    amigos.put(u.getUuid(),u);
                    usuarioDesconectado(u);
                }
            };
            usuarioActual.setConectado(true);
            amigos = servidorCallback.getAmigos(authToken,usuarioActual).stream().collect(Collectors.toMap(IUsuario::getUuid, Function.identity()));
            servidorCallback.rexistrarCliente(clienteCallback);

            rexistroCorrecto(amigos);

        } catch (RemoteException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    public Mensaxe enviarMensaxe(IUsuario para, String mensaxe) throws RemoteException {
        if(!para.isConectado()) return null;
        ClienteCallback cl = servidorCallback.getCliente(authToken,para.getUuid());
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

    public abstract void mensaxeRecibido(IMensaxe m);

    public abstract void rexistroCorrecto(Map<UUID,IUsuario> amigos);

    public  abstract void usuarioConectado(IUsuario usuario);

    public  abstract void usuarioDesconectado(IUsuario usuario);

    public  abstract void amigoNovo(IUsuario usuario);

    public  abstract void amigoEliminado(IUsuario usuario);

    public  abstract void solicitudeRecibida(IRelacion relacion);
}
