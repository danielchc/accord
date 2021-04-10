package practica4.cliente.gui.vInicioSesion;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import practica4.cliente.gui.vPrincipal.vPrincipal;
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.UUID;

public class vInicioSesionController implements Initializable {
    private final ServidorCallback servidorCallback;
    private final Stage stage;
    private final FXMLLoader fxmlLoader;
    private boolean registro;
    private UUID authToken;
    @FXML
    private TextField tfNomeUsuario;
    @FXML
    private Button btnToogleRegistro;
    @FXML
    private Button btnAccion;
    @FXML
    private TextField tfContrasinal;
    @FXML
    private Label lblContrasinalIncorrecto;


    public vInicioSesionController(ServidorCallback servidorCallback) {
        this.servidorCallback = servidorCallback;
        this.fxmlLoader = new FXMLLoader();
        this.stage = new Stage();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registro=false;
    }


    @FXML
    private void accionClick(ActionEvent event) throws Exception {
        String u = tfNomeUsuario.getText();
        String p = tfContrasinal.getText();
        if (p.length() == 0 || u.length() == 0) return;
        if(registro){
            registroClick(u,p,event);
        }else{
            iniciarClick(u,p,event);
        }
    }


    @FXML
    private void btnToogleOnClick(){
        if(!registro){
            btnToogleRegistro.setText("Iniciar");
            btnAccion.setText("Rexistrarse");
        }else{
            btnToogleRegistro.setText("Rexistrarse");
            btnAccion.setText("Iniciar");
        }
        registro=!registro;
        tfNomeUsuario.clear();
        tfContrasinal.clear();
    }

    private void iniciarClick(String u, String p, ActionEvent event) throws IOException {
        authToken=servidorCallback.comprobarUsuario(u, p);
        if (authToken==null) {
            lblContrasinalIncorrecto.setVisible(true);
            System.out.println("Contraseña incorrecta");
            return;
        }

        if (servidorCallback.tenIniciadoSesion(u)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sesión iniciada");
            alert.setHeaderText("Este usuario xa ten iniciada sesión");
            alert.setContentText("Xa existe unha instancia de este usuario iniciada");
            alert.showAndWait();
            return;
        }

        iniciarSesion(servidorCallback.getUsuario(u));
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    private void registroClick(String u, String p, ActionEvent event) throws IOException {
        if (servidorCallback.comprobarUsuarioExiste(u)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error no rexistro");
            alert.setHeaderText("O nome de usuario está en uso");
            alert.setContentText(String.format("Xa existe un usuario que se chama %s",u));
            alert.showAndWait();
            return;
        }
        IUsuario usuario=servidorCallback.rexistrarUsuario(u,p);
        authToken=servidorCallback.comprobarUsuario(u,p);
        iniciarSesion(usuario);
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }


    private void iniciarSesion(IUsuario usuario) throws IOException {
        fxmlLoader.setController(new vPrincipal(authToken,servidorCallback, usuario));
        fxmlLoader.setLocation(getClass().getResource("/practica4/cliente/gui/vPrincipal/vPrincipal.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Accord: " + usuario.getNomeUsuario());
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                try {
                    if (usuario != null && usuario.isConectado()) {
                        usuario.setConectado(false);
                        servidorCallback.desRexistrarCliente(authToken,usuario.getUuid());
                        System.out.println("Desconectando...");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();

    }



}
