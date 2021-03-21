package practica4.cliente;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import practica4.cliente.controladores.ClienteCallbackImpl;
import practica4.cliente.gui.vPrincipal.vPrincipal;
import practica4.interfaces.ServidorCallback;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class ClienteGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ServidorCallback servidorCallback = (ServidorCallback) Naming.lookup(String.format("rmi://localhost:19000/servidor"));
        ClienteCallbackImpl clienteCallback=new ClienteCallbackImpl();

        Stage stage=new Stage();
        stage.setTitle("Práctica 1");
        stage.setResizable(false);


        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setController(new vPrincipal(servidorCallback,clienteCallback));
        fxmlLoader.setLocation(getClass().getResource("/practica4/cliente/gui/vPrincipal/vPrincipal.fxml"));

        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                try {
                    servidorCallback.desRegistrarCliente(clienteCallback);
                } catch (RemoteException e) {

                }
                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();
    }
}
