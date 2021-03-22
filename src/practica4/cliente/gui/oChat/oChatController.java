package practica4.cliente.gui.oChat;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import practica4.cliente.obxectos.Chat;

import java.net.URL;
import java.util.ResourceBundle;

public class oChatController implements Initializable {
    @FXML
    private ListView lvListaMensaxes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void cargarChat(Chat chat){
        lvListaMensaxes.getItems().add(chat.getMensaxes());
    }
}
