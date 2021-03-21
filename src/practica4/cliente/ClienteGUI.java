package practica4.cliente;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import practica4.cliente.gui.vPrincipal.vPrincipal;
import practica4.cliente.obxectos.Usuario;
import practica4.interfaces.ServidorCallback;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.UUID;

public class ClienteGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ServidorCallback servidorCallback = (ServidorCallback) Naming.lookup(String.format("rmi://localhost:19000/servidor"));
        Usuario usuario=new Usuario();

        Stage stage=new Stage();
        stage.setTitle("Práctica 1");
        stage.setResizable(false);


        FXMLLoader fxmlLoader = new FXMLLoader();

        Random r= new Random(System.currentTimeMillis());
        String nombres[]=new String[]{"Mono","Chimapancé","Bonobo","Mandril","Macaco"};
        String colores[]=new String[]{"Verde","Azul","Amarelo","Vermello","Negro"};

        String username= String.format("%s %s",nombres[Math.floorMod(r.nextInt(),nombres.length)],colores[Math.floorMod(r.nextInt(),colores.length)]);

        usuario.setUuid(UUID.randomUUID());
        usuario.setNomeUsuario(username);


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
