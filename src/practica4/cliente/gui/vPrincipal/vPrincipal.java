package practica4.cliente.gui.vPrincipal;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practica4.cliente.controladores.ControladorChat;
import practica4.cliente.gui.obxectos.oMensaxe.oMensaxe;
import practica4.cliente.gui.obxectos.oUsuario.oUsuario;
import practica4.cliente.gui.vAmigos.vAmigosController;
import practica4.cliente.obxectos.Mensaxe;
import practica4.cliente.obxectos.UsuarioLista;
import practica4.interfaces.IMensaxe;
import practica4.interfaces.IRelacion;
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class vPrincipal implements Initializable {

    private IUsuario usuarioActual;
    private ControladorChat controladorChat;
    private vAmigosController amigosController;
    private int solicitudesPendientes;

    @FXML
    private TextField mensaxeEnviar;
    @FXML
    private ListView lvListaClientes;
    @FXML
    private ListView lvMensaxes;
    @FXML
    private Button btnAmigos;

    private TrayIcon trayIcon;

    public vPrincipal(UUID authToken, ServidorCallback servidorCallback, IUsuario usuarioActual) throws RemoteException {
        this.solicitudesPendientes = 0;
        this.usuarioActual = usuarioActual;
        this.controladorChat = new ControladorChat(authToken, usuarioActual, servidorCallback) {
            @Override
            public void mensaxeRecibido(IMensaxe m) {
                cargarMensaxeInterfaz(m);

            }

            @Override
            public void rexistroCorrecto(Map<UUID, IUsuario> amigos) {
                Platform.runLater(() -> {
                    lvListaClientes.getItems().addAll(amigos.values().stream().map(k -> new UsuarioLista(k)).collect(Collectors.toList()));
                });
            }

            @Override
            public void usuarioConectado(IUsuario u) {
                Platform.runLater(() -> {
                    Optional<UsuarioLista> uv = (Optional<UsuarioLista>) lvListaClientes.getItems().stream().filter(k -> ((UsuarioLista) k).getUuid().equals(u.getUuid())).findFirst();
                    if (uv.isPresent()) {
                        uv.get().getUsuario().setConectado(true);
                        lvListaClientes.refresh();
                        cargarTodosMensaxesIntefaz();
                    }
                });
            }

            @Override
            public void usuarioDesconectado(IUsuario u) {
                Platform.runLater(() -> {
                    Optional<UsuarioLista> uv = (Optional<UsuarioLista>) lvListaClientes.getItems().stream().filter(k -> ((UsuarioLista) k).getUuid().equals(u.getUuid())).findFirst();
                    if (uv.isPresent()) {
                        uv.get().getUsuario().setConectado(false);
                        lvListaClientes.refresh();
                        cargarTodosMensaxesIntefaz();
                    }
                });
            }

            @Override
            public void amigoNovo(IUsuario u) {
                Platform.runLater(() -> {
                    lvListaClientes.getItems().removeIf(k -> ((UsuarioLista) k).getUuid().equals(u.getUuid()));
                    lvListaClientes.getItems().add(new UsuarioLista(u));
                    cargarTodosMensaxesIntefaz();
                });
                amigosController.actualizarRelacion();
            }

            @Override
            public void amigoEliminado(IUsuario u) {
                Platform.runLater(() -> {
                    lvListaClientes.getItems().removeIf(k -> ((UsuarioLista) k).getUuid().equals(u.getUuid()));
                    cargarTodosMensaxesIntefaz();
                });
                amigosController.actualizarRelacion();
            }

            @Override
            public void solicitudeRecibida(IRelacion relacion) {
                amigosController.actualizarRelacion();
                solicitudesPendientes++;
                Platform.runLater(() -> {
                    btnAmigos.setText(String.format("Amigos (%d)", solicitudesPendientes));
                });
            }
        };
        this.amigosController = new vAmigosController(controladorChat, usuarioActual);
    }


    private IUsuario getUsuarioSeleccionado() {
        return ((UsuarioLista) lvListaClientes.getSelectionModel().getSelectedItems().get(0)).getUsuario();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lvMensaxes.setPlaceholder(new Label("Benvido a Accord, comeza unha conversa"));
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
        lvListaClientes.setCellFactory(param -> new ListCell<UsuarioLista>() {
            @Override
            protected void updateItem(UsuarioLista item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setGraphic(new oUsuario(item));
                }
            }
        });
    }

    private void cargarMensaxeInterfaz(IMensaxe m) {
        if ((lvListaClientes.getSelectionModel().getSelectedItems().size() == 1) &&(m.getDe().getUuid().equals(getUsuarioSeleccionado().getUuid()) || m.getPara().getUuid().equals(getUsuarioSeleccionado().getUuid()))) {
            Platform.runLater(() -> {
                lvMensaxes.getItems().add(m);
            });
        }else{
            Platform.runLater(()->{
                Optional<UsuarioLista> uv = (Optional<UsuarioLista>) lvListaClientes.getItems().stream().filter(k -> ((UsuarioLista) k).getUuid().equals(m.getDe().getUuid())).findFirst();
                if (uv.isPresent()) {
                    uv.get().aumentarPendentes();
                    lvListaClientes.refresh();
                }
            });
        }
    }

    private void cargarTodosMensaxesIntefaz() {
        if (lvListaClientes.getSelectionModel().getSelectedItems().size() == 0) lvMensaxes.getItems().clear();
        if (lvListaClientes.getSelectionModel().getSelectedItems().size() != 1) return;
        Platform.runLater(() -> {
            mensaxeEnviar.setDisable(false);
            lvMensaxes.setPlaceholder(new Label("Non tes mensaxes con este usuario"));

            lvMensaxes.getItems().clear();

            IUsuario u = getUsuarioSeleccionado();
            Optional<UsuarioLista> uv = (Optional<UsuarioLista>) lvListaClientes.getItems().stream().filter(k -> ((UsuarioLista) k).getUuid().equals(u.getUuid())).findFirst();
            if (uv.isPresent()) {
                uv.get().reiniciarPendentes();
                lvListaClientes.refresh();
            }
            if (controladorChat.existeChat(u.getUuid())){
                lvMensaxes.getItems().addAll(controladorChat.getChat(u.getUuid()).getMensaxes());
            }
            if (!u.isConectado()) {
                lvMensaxes.setPlaceholder(new Label("O usuario non está conectado"));
                mensaxeEnviar.setDisable(true);
            }
        });
    }


    @FXML
    private void enviarMensaxeClick() {
        if (lvListaClientes.getSelectionModel().getSelectedItems().size() != 1) return;
        if (mensaxeEnviar.getText().isEmpty()) return;
        String mensaxe = mensaxeEnviar.getText();
        mensaxeEnviar.clear();
        IUsuario usuarioPara = getUsuarioSeleccionado();
        if (!usuarioPara.isConectado()) return;

        try {
            Mensaxe m = controladorChat.enviarMensaxe(usuarioPara, mensaxe);
            cargarMensaxeInterfaz(m);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void engadirAmigos() throws IOException {
        this.solicitudesPendientes = 0;
        Platform.runLater(() -> {
            btnAmigos.setText("Amigos");
        });
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(amigosController);
        stage.initModality(Modality.APPLICATION_MODAL);
        fxmlLoader.setLocation(getClass().getResource("/practica4/cliente/gui/vAmigos/vAmigos.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Amigos: " + this.usuarioActual.getNomeUsuario());
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void onKeyEnviarMensaxe(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER))
            enviarMensaxeClick();
    }
}
