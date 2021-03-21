package practica4.cliente.obxectos;

import java.util.ArrayList;
import java.util.UUID;

public class Chat {
    private UUID de;
    private ArrayList<Mensaxe> mensaxes;

    public Chat(UUID de) {
        this.de = de;
        this.mensaxes= new ArrayList<Mensaxe>();
    }

    public UUID getDe() {
        return de;
    }

    public void setDe(UUID de) {
        this.de = de;
    }

    public ArrayList<Mensaxe> getMensaxes() {
        return mensaxes;
    }

    public void setMensaxes(ArrayList<Mensaxe> mensaxes) {
        this.mensaxes = mensaxes;
    }

    public void engadirMensaxe(Mensaxe mensaxe){
        this.mensaxes.add(mensaxe);
    }
}
