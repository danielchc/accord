package practica4.cliente.gui.obxectos.oSolicitud;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import practica4.interfaces.IUsuario;

import java.io.IOException;
import java.text.SimpleDateFormat;

public abstract class oSolicitud extends AnchorPane {
    @FXML
    private Label lblNomeUsuario;

    @FXML
    private Button btnEngadir;


    public oSolicitud(IUsuario u) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/practica4/cliente/gui/obxectos/oSolicitud/oSolicitud.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lblNomeUsuario.setText(u.getNomeUsuario());

    }

    @FXML
    public void onClick(){
        btnEngadir.setText("Pendiente");
        btnEngadir.setDisable(true);
    };

}
