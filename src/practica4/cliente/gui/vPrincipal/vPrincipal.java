package practica4.cliente.gui.vPrincipal;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import practica4.cliente.controladores.ClienteCallbackImpl;
import practica4.cliente.gui.oChat.oChatController;
import practica4.cliente.interfaces.EventosCliente;
import practica4.cliente.obxectos.Chat;
import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;


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
    private ListView lvListaClientes;
    @FXML
    private ListView lvMensaxes;

    public vPrincipal(ServidorCallback servidorCallback, IUsuario usuario) throws RemoteException {
        this.servidorCallback=servidorCallback;
        this.clientes=servidorCallback.getListaClientes();
        this.usuario=usuario;
        this.chats=new HashMap<UUID,Chat>();
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDisponibles();
        registrarCliente();
        lvListaClientes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                if(lvListaClientes.getSelectionModel().getSelectedItems().size()!=1)return;

                IUsuario u= (IUsuario) lvListaClientes.getSelectionModel().getSelectedItems().get(0);
                Platform.runLater(()-> {
                    lvMensaxes.getItems().removeAll(lvMensaxes.getItems());
                    if(chats.containsKey(u.getUuid()))
                        lvMensaxes.getItems().addAll(chats.get(u.getUuid()).getMensaxes());
                });
            }
        });
    }

    private void registrarCliente() {
        try {
            ClienteCallbackImpl clienteCallback= null;
            clienteCallback = new ClienteCallbackImpl();
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
                                lvListaClientes.getItems().add(u);
                            });
                        }

                        @Override
                        public void onUsuarioDesconectado(IUsuario u) {
                            clientes.remove(u.getUuid());
                            System.out.println(u);
                            Platform.runLater(()->{
                                lvListaClientes.getItems().remove(u);
                            });
                        }
                    }
            );
            usuario.setRegistrado(true);
            usuario.setClienteCallback(clienteCallback);
            servidorCallback.registrarCliente(usuario);
        } catch (RemoteException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    private void cargarDisponibles(){
        for(IUsuario cUUID:clientes){
            Platform.runLater(()->{
                lvListaClientes.getItems().add(cUUID);
            });
        }
    }

    private void mensaxeRecibido(Mensaxe m){
        if(!chats.containsKey(m.getDe())){
            chats.put(m.getDe(),new Chat(m.getDe()));
        }
        chats.get(m.getDe()).engadirMensaxe(m);
        System.out.println(chats);




        //LOLL
        if(lvListaClientes.getSelectionModel().getSelectedItems().size()==1){
            IUsuario u=(IUsuario) lvListaClientes.getSelectionModel().getSelectedItems().get(0);
            System.out.println(u.getNomeUsuario());
            if(m.getDe().equals(u.getUuid())) {
                Platform.runLater(() -> {
                    lvMensaxes.getItems().add(m);
                });
            }
        }


    }


    @FXML
    private void enviarMensaxe(){
        if(lvListaClientes.getSelectionModel().getSelectedItems().size()!=1)return;
        UUID uuid= ((IUsuario) lvListaClientes.getSelectionModel().getSelectedItems().get(0)).getUuid();

        try {
            Mensaxe m=new Mensaxe(usuario.getUuid(),uuid,mensaxeEnviar.getText());
            IUsuario k=servidorCallback.getCliente(uuid);
            k.getClienteCallback().enviarMensaxe(m);


            //DIOOS ARREGLA ESTO QUE DA ASCO


            if(!chats.containsKey(m.getPara())){
                chats.put(m.getPara(),new Chat(m.getPara()));
            }

            chats.get(m.getPara()).engadirMensaxe(m);
            if(m.getDe().equals(((IUsuario) lvListaClientes.getSelectionModel().getSelectedItems().get(0)).getUuid()));
                lvMensaxes.getItems().add(m);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mensaxeEnviar.clear();
    }

}
