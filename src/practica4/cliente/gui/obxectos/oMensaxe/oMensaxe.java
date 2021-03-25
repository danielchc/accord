package practica4.cliente.gui.obxectos.oMensaxe;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import practica4.cliente.obxectos.Mensaxe;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class oMensaxe extends VBox {
    private final Mensaxe mensaxe;
    @FXML
    private Label lblNomeUsuario;
    @FXML
    private Label lblMensaxe;
    @FXML
    private Label lblData;

    public oMensaxe(Mensaxe mensaxe) {
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm dd/MM");
        this.mensaxe = mensaxe;


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/practica4/cliente/gui/obxectos/oMensaxe/oMensaxe.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lblNomeUsuario.setText(mensaxe.getDe().getNomeUsuario());
        lblMensaxe.setText(mensaxe.getMensaxe());
        lblData.setText(dateFormat.format(mensaxe.getData()));
        lblMensaxe.setText(mensaxe.getMensaxe());


    }
}
