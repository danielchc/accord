package practica4.cliente.obxectos;

import practica4.interfaces.IUsuario;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mensaxe implements Serializable {
    private IUsuario de;
    private IUsuario para;
    private String mensaxe;
    private Date data;

    public Mensaxe(IUsuario de, IUsuario para, String mensaxe, Date data) {
        this.de = de;
        this.para = para;
        this.mensaxe = mensaxe;
        this.data = data;
    }

    public Mensaxe(IUsuario de, IUsuario para, String mensaxe){
        this.de = de;
        this.para = para;
        this.mensaxe = mensaxe;
        this.data=new Date();
    }

    public IUsuario getDe() {
        return de;
    }

    public void setDe(IUsuario de) {
        this.de = de;
    }

    public IUsuario getPara() {
        return para;
    }

    public void setPara(IUsuario para) {
        this.para = para;
    }

    public String getMensaxe() {
        return mensaxe;
    }

    public void setMensaxe(String mensaxe) {
        this.mensaxe = mensaxe;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String toString() {
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm dd/MM");

        return String.format("[%s] %s: %s", dateFormat.format(data),para.getNomeUsuario(),mensaxe);
    }
}
