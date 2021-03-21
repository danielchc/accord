package practica4.cliente.gui.vPrincipal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import practica4.cliente.controladores.ClienteCallbackImpl;
import practica4.cliente.interfaces.EventosCliente;
import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class vPrincipal implements Initializable {

    private final List<UUID> clientes;
    private final ServidorCallback servidorCallback;
    private final IUsuario usuario;


    @FXML
    private TextField mensaxeEnviar;
    @FXML
    private ListView listaClientes;

    public vPrincipal(ServidorCallback servidorCallback, IUsuario usuario) throws RemoteException {
        this.servidorCallback=servidorCallback;
        this.clientes=servidorCallback.getListaClientes();
        this.usuario=usuario;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ClienteCallbackImpl clienteCallback= (ClienteCallbackImpl) usuario.getClienteCallback();

        for(UUID cUUID:clientes){
            Platform.runLater(()->{
                try{
                    listaClientes.getItems().add(servidorCallback.getNomeUsuario(cUUID));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }

        clienteCallback.setEventos(
                new EventosCliente(){
                    @Override
                    public void onMensaxeRecibido(Mensaxe mensaxe) {
                        System.out.println(mensaxe);
                    }

                    @Override
                    public void onUsuarioConectado(UUID uuid) {
                        clientes.add(uuid);

                        Platform.runLater(()->{
                            try{
                                listaClientes.getItems().add(servidorCallback.getNomeUsuario(uuid));
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
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
                System.out.println("Xa hai unha instacia de este usuario");
                System.exit(1);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
