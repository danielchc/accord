package practica4.cliente.obxectos;

import practica4.interfaces.IUsuario;

import java.util.ArrayList;

public class Chat {
    private IUsuario con;
    private ArrayList<Mensaxe> mensaxes;

    public Chat(IUsuario con) {
        this.con = con;
        this.mensaxes= new ArrayList<Mensaxe>();
    }

    public IUsuario getDe() {
        return con;
    }

    public void setDe(IUsuario de) {
        this.con = de;
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

    @Override
    public String toString() {
        return "\nChat{" +
                "\ncon=" + con +
                "\n}\n";
    }
}
