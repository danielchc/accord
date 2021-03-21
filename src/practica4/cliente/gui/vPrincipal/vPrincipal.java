package practica4.cliente.gui.vPrincipal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import practica4.cliente.controladores.ClienteCallbackImpl;
import practica4.cliente.interfaces.EventosCliente;
import practica4.cliente.obxectos.IMensaxe;
import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.ClienteCallback;
import practica4.interfaces.ServidorCallback;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class vPrincipal implements Initializable {

    private final List<UUID> clientes;
    private final ServidorCallback servidorCallback;
    private final ClienteCallbackImpl clienteCallback;


    @FXML
    private TextField mensaxeEnviar;
    @FXML
    private ListView listaClientes;

    public vPrincipal(ServidorCallback servidorCallback, ClienteCallbackImpl clienteCallback) throws RemoteException {
        this.servidorCallback=servidorCallback;
        this.clientes=servidorCallback.getListaClientes();
        this.clienteCallback=clienteCallback;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaClientes.getItems().addAll(clientes);
        clienteCallback.setEventos(
                new EventosCliente(){
                    @Override
                    public void onMensaxeRecibido(IMensaxe mensaxe) {
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
            servidorCallback.registrarCliente(clienteCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void enviarMensaxe(){
        if(listaClientes.getSelectionModel().getSelectedItems().size()!=1)return;
        UUID uuid= (UUID) listaClientes.getSelectionModel().getSelectedItems().get(0);

        try {
            ClienteCallback k=servidorCallback.getCliente(uuid);
            k.enviarMensaxe(new Mensaxe(clienteCallback.getUUID(),uuid,mensaxeEnviar.getText()));

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
