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
import practica4.interfaces.IUsuario;
import practica4.interfaces.ServidorCallback;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.UUID;

public class ClienteGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ServidorCallback servidorCallback = (ServidorCallback) Naming.lookup(String.format("rmi://localhost:19000/servidor"));
        IUsuario usuario;

        Stage stage=new Stage();
        stage.setTitle("Práctica 1");
        stage.setResizable(false);


        FXMLLoader fxmlLoader = new FXMLLoader();



        String u="dani";
        String p="abc123..";


        if(servidorCallback.comprobarUsuario(u,p)){
            usuario=servidorCallback.getUsuario(u);
            usuario.setClienteCallback(new ClienteCallbackImpl());
            System.out.println(usuario.getUuid());
        }else{
            System.out.println("Contraseña podre");
            return;
        }


        fxmlLoader.setController(new vPrincipal(servidorCallback,usuario));
        fxmlLoader.setLocation(getClass().getResource("/practica4/cliente/gui/vPrincipal/vPrincipal.fxml"));

        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                try {
                    if(usuario.isRegistrado()){
                        usuario.setRegistrado(false);
                        servidorCallback.desRegistrarCliente(usuario);
                    }
                } catch (RemoteException e) {

                }
                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();
    }
}
