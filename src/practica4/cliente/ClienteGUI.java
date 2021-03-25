package practica4.cliente;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import practica4.cliente.controladores.ClienteCallbackImpl;
import practica4.cliente.gui.vInicioSesion.vInicioSesionController;
import practica4.cliente.gui.vPrincipal.vPrincipal;
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.rmi.Naming;

public class ClienteGUI extends Application {
    final static String host="localhost";
    final static int porto=19000;
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Thread.setDefaultUncaughtExceptionHandler(ClienteGUI::showError);

        ServidorCallback servidorCallback;
        try {
            servidorCallback = (ServidorCallback) Naming.lookup(String.format("rmi://%s:%d/servidor",host,porto));
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro conectandose o servidor");
            alert.setHeaderText("Erro conectandose o servidor");
            alert.setContentText(String.format("Non se puido establecer conexión con rmi://%s:%d/servidor",host,porto));
            alert.getDialogPane().setExpandableContent(new TextArea(ex.getMessage()));
            alert.showAndWait();
            return;
        }

        Stage stage = new Stage();
        stage.setTitle("Accord");
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new vInicioSesionController(servidorCallback));
        fxmlLoader.setLocation(getClass().getResource("/practica4/cliente/gui/vInicioSesion/vInicioSesion.fxml"));

        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    private static void showError(Thread thread, Throwable throwable) {
        System.err.println(throwable.toString());
        if (Platform.isFxApplicationThread()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro na aplicación");
            alert.setHeaderText("Ocorreu un erro inesperado");
            TextArea tA=new TextArea(throwable.getMessage());
            tA.setEditable(false);
            tA.setWrapText(true);
            alert.getDialogPane().setExpandableContent(tA);
        }
    }
}
