package practica4.cliente.gui.vPrincipal;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practica4.cliente.controladores.ControladorChat;
import practica4.cliente.gui.obxectos.oMensaxe.oMensaxe;
import practica4.cliente.gui.obxectos.oUsuario.oUsuario;
import practica4.cliente.gui.vAmigos.vAmigosController;
import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.IMensaxe;
import practica4.interfaces.IRelacion;
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class vPrincipal implements Initializable {

    private ControladorChat controladorChat;
    private ServidorCallback servidorCallback;
    private IUsuario usuarioActual;
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
        this.solicitudesPendientes=0;
        this.servidorCallback=servidorCallback;
        this.usuarioActual=usuarioActual;

        this.controladorChat = new ControladorChat(authToken,usuarioActual, servidorCallback) {
            @Override
            public void mensaxeRecibido(IMensaxe m) {
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
                    Optional<IUsuario> uv= (Optional<IUsuario>) lvListaClientes.getItems().stream().filter(k->((IUsuario)k).getUuid().equals(u.getUuid())).findFirst();
                    if(uv.isPresent()){
                        uv.get().setConectado(true);
                        lvListaClientes.refresh();
                    }
                });
            }

            @Override
            public void usuarioDesconectado(IUsuario u) {
                Platform.runLater(() -> {
                    Optional<IUsuario> uv= (Optional<IUsuario>) lvListaClientes.getItems().stream().filter(k->((IUsuario)k).getUuid().equals(u.getUuid())).findFirst();
                    if(uv.isPresent()){
                        uv.get().setConectado(false);
                        lvListaClientes.refresh();
                    }
                });
            }

            @Override
            public void amigoNovo(IUsuario u) {
                Platform.runLater(() -> {
                    lvListaClientes.getItems().removeIf(k->((IUsuario)k).getUuid().equals(u.getUuid()));
                    lvListaClientes.getItems().add(u);
                });
                amigosController.actualizarRelacion();
            }

            @Override
            public void amigoEliminado(IUsuario u) {
                Platform.runLater(() -> {
                    lvListaClientes.getItems().removeIf(k->((IUsuario)k).getUuid().equals(u.getUuid()));
                });
                amigosController.actualizarRelacion();
            }

            @Override
            public void solicitudeRecibida(IRelacion relacion) {
                amigosController.actualizarRelacion();
                solicitudesPendientes++;
                Platform.runLater(()->{
                    btnAmigos.setText(String.format("Amigos (%d)",solicitudesPendientes));
                });
            }
        };
        this.amigosController=new vAmigosController(controladorChat,usuarioActual);
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
    }

    private void cargarMensaxeInterfaz(IMensaxe m) {
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
            mensaxeEnviar.setDisable(false);
            lvMensaxes.setPlaceholder(new Label("Non tes mensaxes con este usuario"));

            lvMensaxes.getItems().removeAll(lvMensaxes.getItems());


            if (lvListaClientes.getSelectionModel().getSelectedItems().size() != 1) return;
            IUsuario u = (IUsuario) lvListaClientes.getSelectionModel().getSelectedItems().get(0);
            if (controladorChat.existeChat(u.getUuid()))
                lvMensaxes.getItems().addAll(controladorChat.getChat(u.getUuid()).getMensaxes());
            if(!u.isConectado()){
                lvMensaxes.setPlaceholder(new Label("O usuario non está conectado"));
                mensaxeEnviar.setDisable(true);
            }
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
        this.solicitudesPendientes=0;
        Platform.runLater(()->{
            btnAmigos.setText("Amigos");
        });
        Stage stage=new Stage();
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setController(amigosController);
        stage.initModality(Modality.APPLICATION_MODAL);
        fxmlLoader.setLocation(getClass().getResource("/practica4/cliente/gui/vAmigos/vAmigos.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Amigos");
        stage.setResizable(false);
        stage.show();
    }
    @FXML
    private void onKeyEnviarMensaxe(KeyEvent e){
        if(e.getCode().equals(KeyCode.ENTER))
            enviarMensaxeClick();
    }
}
