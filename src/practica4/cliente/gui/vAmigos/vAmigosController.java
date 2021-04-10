package practica4.cliente.gui.vAmigos;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import practica4.cliente.controladores.ControladorChat;
import practica4.cliente.gui.obxectos.oSolicitud.oSolicitud;
import practica4.interfaces.IRelacion;
import practica4.interfaces.IUsuario;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class vAmigosController implements Initializable {
    private ControladorChat controladorChat;
    private IUsuario usuarioActual;
    private boolean isInitialized = false;
    @FXML
    private ListView lvUsuarios;

    @FXML
    private TextField tfTextoBuscar;


    public vAmigosController(ControladorChat controladorChat, IUsuario usuarioActual) {
        this.controladorChat = controladorChat;
        this.usuarioActual = usuarioActual;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isInitialized = true;
        lvUsuarios.getItems().clear();
        try {
            lvUsuarios.getItems().addAll(controladorChat.buscarUsuarios("", usuarioActual));
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        lvUsuarios.setCellFactory(param -> new ListCell<IRelacion>() {
            @Override
            protected void updateItem(IRelacion item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setGraphic(new oSolicitud(item) {
                        @Override
                        public void onClickCancelar() {
                            String usuario = item.getU2().getNomeUsuario();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            try {
                                switch (item.getRelacion()) {
                                    case Ningunha:
                                        alert.setTitle("Solicitude");
                                        alert.setContentText(String.format("Solicitude enviada a %s", usuario));
                                        alert.showAndWait();
                                        controladorChat.enviarSolicitude(item);
                                        break;
                                    case Amigos:
                                        alert.setTitle("Amigo eliminado");
                                        alert.setContentText(String.format("Acabas de eliminar a %s de amigo", usuario));
                                        alert.showAndWait();
                                        controladorChat.eliminarAmigo(item);
                                        break;
                                    case SolicitudeEnviada:
                                    case SolicitudePendente:
                                        alert.setTitle("Solicitude cancelada");
                                        alert.setContentText(String.format("Acabas de cancelar a solicitude a %s", usuario));
                                        alert.showAndWait();
                                        controladorChat.cancelarSolicitude(item);
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
                                controladorChat.aceptarSolicitude(item);
                            } catch (Exception e) {
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
    private void buscarUsuario() {
        Platform.runLater(() -> {
            try {
                lvUsuarios.getItems().clear();
                lvUsuarios.getItems().addAll(controladorChat.buscarUsuarios(tfTextoBuscar.getText(), usuarioActual));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

    }

    public void actualizarRelacion() {
        if (!isInitialized) return;
        buscarUsuario();
    }
}
