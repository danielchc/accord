package practica4.cliente.controladores;

import javafx.application.Platform;
import practica4.cliente.interfaces.EventosCliente;
import practica4.cliente.obxectos.Chat;
import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class ControladorMensaxes {

    private final IUsuario usuarioActual;
    private final ServidorCallback servidorCallback;
    private HashMap<UUID, Chat> chats;
    private ArrayList<IUsuario> usuariosDisponibles;


    public ControladorMensaxes(IUsuario usuarioActual,ServidorCallback servidorCallback) {
        this.servidorCallback=servidorCallback;
        this.chats=new HashMap<UUID,Chat>();
        this.usuarioActual =usuarioActual;
        this.usuariosDisponibles =new ArrayList<IUsuario>();
    }


    private void comprobarChat(IUsuario u) {
        if (!chats.containsKey(u.getUuid()))
            chats.put(u.getUuid(), new Chat(u));
    }

    public void registrarCliente() {
        try {
            ClienteCallbackImpl clienteCallback = new ClienteCallbackImpl();
            usuariosDisponibles= (ArrayList<IUsuario>) servidorCallback.getListaClientes();
            clienteCallback.setEventos(new EventosCliente() {
                @Override
                public void onMensaxeRecibido(Mensaxe mensaxe) {
                    comprobarChat(mensaxe.getDe());
                    chats.get(mensaxe.getDe().getUuid()).engadirMensaxe(mensaxe);
                    mensaxeRecibido(mensaxe);
                }

                @Override
                public void onUsuarioConectado(IUsuario u) {
                    if(u.getUuid().equals(usuarioActual.getUuid()))return;
                    if(!usuariosDisponibles.contains(u))
                        usuariosDisponibles.add(u);
                    usuarioConectado(u);
                }

                @Override
                public void onUsuarioDesconectado(IUsuario u) {
                    usuariosDisponibles.remove(u);
                    usuarioDesconectado(u);
                }
            });
            usuarioActual.setRegistrado(true);
            usuarioActual.setClienteCallback(clienteCallback);
            servidorCallback.registrarCliente(usuarioActual);

            rexistroCorrecto(usuariosDisponibles);

        } catch (RemoteException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    public Mensaxe enviarMensaxe(IUsuario para,String mensaxe) throws RemoteException {
        comprobarChat(para);
        Mensaxe m = new Mensaxe(usuarioActual, para, mensaxe);
        para.getClienteCallback().enviarMensaxe(m);
        chats.get(m.getPara().getUuid()).engadirMensaxe(m);
        return m;
    }

    public boolean existeChat(UUID uuid){
        return chats.containsKey(uuid);
    }

    public Chat getChat(UUID uuid){
        return chats.get(uuid);
    }

    public abstract void mensaxeRecibido(Mensaxe m);

    public abstract void rexistroCorrecto(List<IUsuario> listaUsuariosConectados);

    public  abstract void usuarioConectado(IUsuario usuario);

    public  abstract void usuarioDesconectado(IUsuario usuario);
}
