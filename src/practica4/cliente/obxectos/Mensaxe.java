package practica4.cliente.obxectos;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.UUID;

public class Mensaxe implements Serializable {
    private UUID de;
    private UUID para;
    private String mensaxe;
    private Date data;

    public Mensaxe(UUID de, UUID para, String mensaxe, Date data) {
        this.de = de;
        this.para = para;
        this.mensaxe = mensaxe;
        this.data = data;
    }

    public Mensaxe(UUID de, UUID para, String mensaxe){
        this.de = de;
        this.para = para;
        this.mensaxe = mensaxe;
        this.data=new Date();
    }

    public UUID getDe() {
        return de;
    }

    public void setDe(UUID de) {
        this.de = de;
    }

    public UUID getPara() {
        return para;
    }

    public void setPara(UUID para) {
        this.para = para;
    }

    public String getMensaxe() {
        return mensaxe;
    }

    public void setMensaxe(String mensaxe) {
        this.mensaxe = mensaxe;
    }

    public String toString() {
        return mensaxe;
    }
}
