package practica4.cliente.gui.vPrincipal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import practica4.cliente.controladores.ClienteCallbackImpl;
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

    private final List<UUID> clientes;
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
        ClienteCallbackImpl clienteCallback= (ClienteCallbackImpl) usuario.getClienteCallback();

        for(UUID cUUID:clientes){
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
                    public void onUsuarioConectado(UUID uuid) {
                        clientes.add(uuid);

                        Platform.runLater(()->{
                            listaClientes.getItems().add(uuid);
                        });
                    }

                    @Override
                    public void onUsuarioDesconectado(UUID uuid) {
                        clientes.remove(uuid);
                        Platform.runLater(()->{
                            listaClientes.getItems().remove(uuid);
                        });
                    }
                }
        );

        try {
            usuario.setRegistrado(true);
            if(!servidorCallback.registrarCliente(usuario)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sesión iniciada");
                alert.setHeaderText("Este usuario xa ten inciada sesión");
                alert.setContentText(String.format("Xa existe unha instancia de este usuario iniciada"));
                alert.showAndWait();
                System.exit(1);
            }
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
    }


    @FXML
    private void enviarMensaxe(){
        if(listaClientes.getSelectionModel().getSelectedItems().size()!=1)return;
        UUID uuid= (UUID) listaClientes.getSelectionModel().getSelectedItems().get(0);

        try {
            IUsuario k=servidorCallback.getCliente(uuid);
            k.getClienteCallback().enviarMensaxe(new Mensaxe(usuario.getUuid(),uuid,mensaxeEnviar.getText()));

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
