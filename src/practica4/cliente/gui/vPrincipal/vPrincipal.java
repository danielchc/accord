package practica4.cliente.gui.vPrincipal;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import practica4.cliente.controladores.ControladorChat;
import practica4.cliente.gui.obxectos.oMensaxe.oMensaxe;
import practica4.cliente.gui.obxectos.oUsuario.oUsuario;
import practica4.cliente.gui.vAmigos.vAmigosController;
import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class vPrincipal implements Initializable {

    private ControladorChat controladorChat;
    private ServidorCallback servidorCallback;
    private IUsuario usuarioActual;

    @FXML
    private TextField mensaxeEnviar;
    @FXML
    private ListView lvListaClientes;
    @FXML
    private ListView lvMensaxes;
    @FXML
    private Button btnAmigos;

    private ObservableList<IUsuario> amigos;

    public vPrincipal(ServidorCallback servidorCallback, IUsuario usuarioActual) throws RemoteException {
        this.servidorCallback=servidorCallback;
        this.usuarioActual=usuarioActual;
        this.controladorChat = new ControladorChat(usuarioActual, servidorCallback) {
            @Override
            public void mensaxeRecibido(Mensaxe m) {
                cargarMensaxeInterfaz(m);
            }

            @Override
            public void rexistroCorrecto(Map<UUID, IUsuario> amigos) {
                Platform.runLater(() -> {
                    lvListaClientes.getItems().addAll(amigos.values());
                });
            }

            @Override
            public void usuarioConectado(IUsuario u) {
                Platform.runLater(() -> {
                    lvListaClientes.getItems().removeIf(k->((IUsuario)k).getUuid().equals(u.getUuid()));
                    lvListaClientes.getItems().add(u);
                });
            }

            @Override
            public void usuarioDesconectado(IUsuario u) {
                Platform.runLater(() -> {
                    lvListaClientes.getItems().removeIf(k->((IUsuario)k).getUuid().equals(u.getUuid()));
                    lvListaClientes.getItems().add(u);
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
        lvListaClientes.setCellFactory(param -> new ListCell<IUsuario>() {
            @Override
            protected void updateItem(IUsuario item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setGraphic(new oUsuario(item));
                }
            }
        });
        btnAmigos.setText("Amigos (1)");
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
        mensaxeEnviar.clear();
        if(m==null)return;

        cargarMensaxeInterfaz(m);
    }

    @FXML
    private void engadirAmigos() throws IOException {
        Stage stage=new Stage();
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setController(new vAmigosController(servidorCallback,usuarioActual));
        fxmlLoader.setLocation(getClass().getResource("/practica4/cliente/gui/vAmigos/vAmigos.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Amigos");
        stage.setResizable(false);
        stage.show();
    }

}
