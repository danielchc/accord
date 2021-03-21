package practica4.cliente.gui.vLogin;
/*
Daniel Chenel Cea
Práctica 1
 */
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {
    @FXML
    private TextField nomeUsuario;
    private Stage stage;
    private FXMLLoader fxmlLoader;

    public LoginController(Stage stage) {
        this.stage = stage;
        this.fxmlLoader = new FXMLLoader();
    }

    @FXML
    private void iniciarSesion() throws IOException {

    }

    @FXML
    private void nomeUsuarioEnter(KeyEvent e) throws IOException {
        if (e.getCode() == KeyCode.ENTER) iniciarSesion();
    }


}
