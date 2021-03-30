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


    public oSolicitud(IRelacion u, IUsuario usuarioActual) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/practica4/cliente/gui/obxectos/oSolicitud/oSolicitud.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnAceptar.setVisible(false);
        if(u.getU1().getUuid().equals(usuarioActual.getUuid())){
            lblNomeUsuario.setText(u.getU2().getNomeUsuario());
        }else{
            lblNomeUsuario.setText(u.getU1().getNomeUsuario());
        }

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
                btnAceptar.setVisible(!u.getU1().getUuid().equals(usuarioActual.getUuid()));
                btnEngadir.setText("Cancelar");
                lblRelacion.setText("Solicitude pendente");
                break;
        }

    }

    @FXML
    public abstract void onClickCancelar();

    @FXML
    public abstract void onClickAceptar();

}
