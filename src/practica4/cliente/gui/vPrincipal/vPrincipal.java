package practica4.cliente.gui.vPrincipal;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import practica4.cliente.controladores.ControladorChat;
import practica4.cliente.gui.obxectos.oMensaxe.oMensaxe;
import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class vPrincipal implements Initializable {

    ControladorChat controladorChat;

    @FXML
    private TextField mensaxeEnviar;
    @FXML
    private ListView lvListaClientes;
    @FXML
    private ListView lvMensaxes;

    public vPrincipal(ServidorCallback servidorCallback, IUsuario usuarioActual) throws RemoteException {
        this.controladorChat = new ControladorChat(usuarioActual, servidorCallback) {
            @Override
            public void mensaxeRecibido(Mensaxe m) {
                cargarMensaxeInterfaz(m);
            }

            @Override
            public void rexistroCorrecto(List<IUsuario> listaUsuariosConectados) {
                for (IUsuario usuario : listaUsuariosConectados) {
                    Platform.runLater(() -> {
                        lvListaClientes.getItems().add(usuario);
                    });
                }
            }

            @Override
            public void usuarioConectado(IUsuario u) {
                Platform.runLater(() -> {
                    lvListaClientes.getItems().add(u);
                });
            }

            @Override
            public void usuarioDesconectado(IUsuario u) {
                Platform.runLater(() -> {
                    lvListaClientes.getItems().remove(u);
                });
            }
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controladorChat.rexistrarCliente();
        lvListaClientes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                cargarTodosMensaxesIntefaz();
            }
        });
        lvMensaxes.setCellFactory(param -> new ListCell<Mensaxe>() {
            @Override
            protected void updateItem(Mensaxe item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setGraphic(new oMensaxe(item));
                }
            }
        });
    }

    private void cargarMensaxeInterfaz(Mensaxe m) {
        if (lvListaClientes.getSelectionModel().getSelectedItems().size() != 1) return;

        IUsuario u = (IUsuario) lvListaClientes.getSelectionModel().getSelectedItems().get(0);
        if (m.getDe().getUuid().equals(u.getUuid()) || m.getPara().getUuid().equals(u.getUuid())) {
            Platform.runLater(() -> {
                lvMensaxes.getItems().add(m);
            });
        }
    }

    private void cargarTodosMensaxesIntefaz() {
        Platform.runLater(() -> {
            lvMensaxes.getItems().removeAll(lvMensaxes.getItems());
        });
        if (lvListaClientes.getSelectionModel().getSelectedItems().size() != 1) return;
        IUsuario u = (IUsuario) lvListaClientes.getSelectionModel().getSelectedItems().get(0);
        Platform.runLater(() -> {
            if (controladorChat.existeChat(u.getUuid()))
                lvMensaxes.getItems().addAll(controladorChat.getChat(u.getUuid()).getMensaxes());
        });
    }


    @FXML
    private void enviarMensaxeClick() {
        if (lvListaClientes.getSelectionModel().getSelectedItems().size() != 1) return;
        if (mensaxeEnviar.getText().isEmpty()) return;

        IUsuario usuarioPara = ((IUsuario) lvListaClientes.getSelectionModel().getSelectedItems().get(0));

        Mensaxe m = null;
        try {
            m = controladorChat.enviarMensaxe(usuarioPara, mensaxeEnviar.getText());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        cargarMensaxeInterfaz(m);

        mensaxeEnviar.clear();
    }

}
