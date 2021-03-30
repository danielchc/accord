package practica4.cliente.gui.vAmigos;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import practica4.cliente.gui.obxectos.oSolicitud.oSolicitud;
import practica4.interfaces.IRelacion;
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
        lvBuscarUsuarios.getItems().clear();
        try {
            lvBuscarUsuarios.getItems().addAll(servidorCallback.buscarUsuarios("",usuarioActual));
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        lvBuscarUsuarios.setCellFactory(param -> new ListCell<IRelacion>() {
            @Override
            protected void updateItem(IRelacion item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setGraphic(new oSolicitud(item,usuarioActual) {
                        @Override
                        public void onClickCancelar() {
                            String usuario=item.getU1().getUuid().equals(usuarioActual.getUuid())?item.getU2().getNomeUsuario():item.getU2().getNomeUsuario();
                            Alert alert=new Alert(Alert.AlertType.INFORMATION);
                            try {
                                switch (item.getRelacion()){
                                    case Ningunha:
                                        alert.setTitle("Solicitude");
                                        alert.setContentText(String.format("Solicitude enviada a %s",usuario));
                                        alert.showAndWait();
                                        servidorCallback.enviarSolicitude(item);
                                        break;
                                    case Amigos:
                                        alert.setTitle("Amigo eliminado");
                                        alert.setContentText(String.format("Acabas de eliminar a %s de amigo",usuario));
                                        alert.showAndWait();
                                        servidorCallback.eliminarAmigo(item);
                                        break;
                                    case SolicitudePendente:
                                        alert.setTitle("Solicitude cancelada");
                                        alert.setContentText(String.format("Acabas de cancelar a solicitude a %s",usuario));
                                        alert.showAndWait();
                                        servidorCallback.cancelarSolicitude(item);
                                        break;

                                }
                                tfTextoBuscar.clear();
                                buscarUsuario();

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onClickAceptar() {
                            try {
                                servidorCallback.aceptarSolicitude(item);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            tfTextoBuscar.clear();
                            buscarUsuario();
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
