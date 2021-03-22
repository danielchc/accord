package practica4.cliente.gui.vPrincipal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import practica4.cliente.controladores.ClienteCallbackImpl;
import practica4.cliente.interfaces.EventosCliente;
import practica4.cliente.obxectos.Chat;
import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;


import java.awt.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class vPrincipal implements Initializable {

    private final List<IUsuario> clientes;
    private final ServidorCallback servidorCallback;
    private final IUsuario usuario;

    private HashMap<UUID, Chat> chats;

    @FXML
    private TextField mensaxeEnviar;
    @FXML
    private ListView listaClientes;

    public vPrincipal(ServidorCallback servidorCallback, IUsuario usuario) throws RemoteException {
        this.servidorCallback=servidorCallback;
        this.clientes=servidorCallback.getListaClientes();
        this.usuario=usuario;
        this.chats=new HashMap<UUID,Chat>();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ClienteCallbackImpl clienteCallback= null;

        try {
            clienteCallback = new ClienteCallbackImpl();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        for(IUsuario cUUID:clientes){
            Platform.runLater(()->{
                listaClientes.getItems().add(cUUID);
            });
        }

        clienteCallback.setEventos(
                new EventosCliente(){
                    @Override
                    public void onMensaxeRecibido(Mensaxe mensaxe) {
                        mensaxeRecibido(mensaxe);
                        System.out.println(mensaxe);
                    }

                    @Override
                    public void onUsuarioConectado(IUsuario u) {
                        if(u.getUuid().equals(usuario.getUuid()))return;
                        clientes.add(u);
                        Platform.runLater(()->{
                            listaClientes.getItems().add(u);
                        });
                    }

                    @Override
                    public void onUsuarioDesconectado(IUsuario u) {
                        clientes.remove(u.getUuid());
                        Platform.runLater(()->{
                            listaClientes.getItems().remove(u);
                        });
                    }
                }
        );

        try {
            usuario.setRegistrado(true);
            usuario.setClienteCallback(clienteCallback);
            servidorCallback.registrarCliente(usuario);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private void mensaxeRecibido(Mensaxe m){
        if(!chats.containsKey(m.getDe())){
            chats.put(m.getDe(),new Chat(m.getDe()));
        }

        chats.get(m.getDe()).engadirMensaxe(m);
        System.out.println(chats);


        if (SystemTray.isSupported()) {
            try{
                SystemTray tray = SystemTray.getSystemTray();

                Image image = Toolkit.getDefaultToolkit().createImage("BORRARR.png");
                TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("System tray icon demo");
                tray.add(trayIcon);
                trayIcon.displayMessage(m.getDe().toString(), m.getMensaxe(), TrayIcon.MessageType.INFO);
            }catch (Exception e){

            }


        }
    }


    @FXML
    private void enviarMensaxe(){
        if(listaClientes.getSelectionModel().getSelectedItems().size()!=1)return;
        UUID uuid= ((IUsuario) listaClientes.getSelectionModel().getSelectedItems().get(0)).getUuid();

        try {
            IUsuario k=servidorCallback.getCliente(uuid);
            k.getClienteCallback().enviarMensaxe(new Mensaxe(usuario.getUuid(),uuid,mensaxeEnviar.getText()));

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
