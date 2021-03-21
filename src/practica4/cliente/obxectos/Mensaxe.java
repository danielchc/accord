package practica4.cliente.obxectos;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

public class Mensaxe implements IMensaxe  {
    private UUID de;
    private UUID para;
    private String mensaxe;

    public Mensaxe(UUID de, UUID para, String mensaxe){
        this.de = de;
        this.para = para;
        this.mensaxe = mensaxe;
    }

    @Override
    public UUID getDe() {
        return de;
    }

    @Override
    public void setDe(UUID de) {
        this.de = de;
    }

    @Override
    public UUID getPara() {
        return para;
    }

    @Override
    public void setPara(UUID para) {
        this.para = para;
    }

    @Override
    public String getMensaxe() {
        return mensaxe;
    }

    @Override
    public void setMensaxe(String mensaxe) {
        this.mensaxe = mensaxe;
    }

    @Override
    public String toString() {
        return "Mensaxe{" +
                "de=" + de +
                ", para=" + para +
                ", mensaxe='" + mensaxe + '\'' +
                '}';
    }
}
