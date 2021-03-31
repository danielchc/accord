package practica4.cliente.gui.obxectos.oSolicitud;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import practica4.interfaces.IRelacion;
import practica4.interfaces.IUsuario;

import java.io.IOException;

public abstract class oSolicitud extends AnchorPane {
    @FXML
    private Label lblNomeUsuario;
    @FXML
    private Label lblRelacion;

    @FXML
    private Button btnEngadir;
    @FXML
    private Button btnAceptar;


    public oSolicitud(IRelacion u) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/practica4/cliente/gui/obxectos/oSolicitud/oSolicitud.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnAceptar.setVisible(false);

        lblNomeUsuario.setText(u.getU2().getNomeUsuario());


        switch (u.getRelacion()){
            case Ningunha:
                btnEngadir.setText("Engadir");
                lblRelacion.setText("Ningunha");
                break;
            case Amigos:
                btnEngadir.setText("Eliminar");
                lblRelacion.setText("Amigos");
                break;
            case SolicitudePendente:
                btnAceptar.setVisible(true);
                btnEngadir.setText("Cancelar");
                lblRelacion.setText("Solicitude pendente aceptar");
                break;
            case SolicitudeEnviada:
                btnEngadir.setText("Cancelar");
                lblRelacion.setText("Solicitude pendente");
        }

    }

    @FXML
    public abstract void onClickCancelar();

    @FXML
    public abstract void onClickAceptar();

}
