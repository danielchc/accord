package practica4.cliente.gui.obxectos.oUsuario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import practica4.interfaces.IUsuario;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class oUsuario extends HBox {
    @FXML
    private Label lblNomeUsuario;
    @FXML
    private Label lblIncial;
    @FXML
    private Circle isConectado;
    @FXML
    private Circle backGr;


    public oUsuario(IUsuario u) {
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm dd/MM");


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/practica4/cliente/gui/obxectos/oUsuario/oUsuario.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lblNomeUsuario.setText(u.getNomeUsuario());
        lblIncial.setText(u.getNomeUsuario().substring(0,1).toUpperCase());
        isConectado.setVisible(u.isConectado());
    }

}
