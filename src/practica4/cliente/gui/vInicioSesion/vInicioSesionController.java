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

public class vInicioSesionController implements Initializable {
    private ServidorCallback servidorCallback;
    private Stage stage;
    private IUsuario usuario;
    private FXMLLoader fxmlLoader;

    @FXML
    private TextField tfNomeUsuario;
    @FXML
    private TextField tfContrasinal;
    @FXML
    private Label lblContrasinalIncorrecto;


    public vInicioSesionController(ServidorCallback servidorCallback) {
        this.servidorCallback = servidorCallback;
        this.fxmlLoader = new FXMLLoader();
        this.stage=new Stage();
    }

    @FXML
    private void iniciarSesionClick(ActionEvent event) throws Exception {
        String u = tfNomeUsuario.getText();
        String p = tfContrasinal.getText();
        if (p.length() == 0 || u.length() == 0) {
            return;
        }

        if (!servidorCallback.comprobarUsuario(u, p)) {
            lblContrasinalIncorrecto.setVisible(true);
            System.out.println("Contraseña incorrecta");
            return;
        }

        if(servidorCallback.tenIniciadoSesion(u)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sesión iniciada");
            alert.setHeaderText("Este usuario xa ten inciada sesión");
            alert.setContentText(String.format("Xa existe unha instancia de este usuario iniciada"));
            alert.showAndWait();
            return;
        }

        iniciarSesion(u);

        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }


    private void iniciarSesion(String  u) throws IOException {
        usuario = servidorCallback.getUsuario(u);
        fxmlLoader.setController(new vPrincipal(servidorCallback,usuario));
        fxmlLoader.setLocation(getClass().getResource("/practica4/cliente/gui/vPrincipal/vPrincipal.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Accord: "+ u);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                try {
                    if(usuario!=null && usuario.isRegistrado()){
                        usuario.setRegistrado(false);
                        servidorCallback.desRegistrarCliente(usuario);
                    }
                } catch (RemoteException e) {
                    System.out.println(e);
                }
                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //BORRAR ESTOOOOOOOOOOOOOOO
        //tfNomeUsuario.setText("dani");
        tfContrasinal.setText("abc123..");
    }
}
