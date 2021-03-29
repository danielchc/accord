package practica4.cliente.gui.vAmigos;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import practica4.cliente.gui.obxectos.oMensaxe.oMensaxe;
import practica4.cliente.gui.obxectos.oSolicitud.oSolicitud;
import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class vAmigosController implements Initializable {
    private ServidorCallback servidorCallback;
    private IUsuario usuarioActual;
    @FXML
    private ListView lvBuscarUsuarios;
    @FXML
    private TextField tfTextoBuscar;


    public vAmigosController(ServidorCallback servidorCallback,IUsuario usuarioActual) {
        this.servidorCallback=servidorCallback;
        this.usuarioActual=usuarioActual;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lvBuscarUsuarios.setCellFactory(param -> new ListCell<IUsuario>() {
            @Override
            protected void updateItem(IUsuario item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setGraphic(new oSolicitud(item) {
                        @Override
                        public void onClick() {
                            super.onClick();
                            try {
                                servidorCallback.enviarSolicitude(usuarioActual,item);
                                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Solicitude");
                                alert.setContentText(String.format("Solicitude enviada a %s",item.getNomeUsuario()));
                                alert.showAndWait();

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
    @FXML
    private void buscarUsuario(){
        try {
            lvBuscarUsuarios.getItems().clear();
            lvBuscarUsuarios.getItems().addAll(servidorCallback.buscarUsuarios(tfTextoBuscar.getText(),usuarioActual));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
