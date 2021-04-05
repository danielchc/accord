package practica4.cliente.obxectos;

import practica4.interfaces.IMensaxe;
import practica4.interfaces.IUsuario;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mensaxe implements IMensaxe {
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

    @Override
    public IUsuario getDe() {
        return de;
    }

    @Override
    public void setDe(IUsuario de) {
        this.de = de;
    }

    @Override
    public IUsuario getPara() {
        return para;
    }

    @Override
    public void setPara(IUsuario para) {
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
    public Date getData() {
        return data;
    }

    @Override
    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm dd/MM");

        return String.format("[%s] %s: %s", dateFormat.format(data),para.getNomeUsuario(),mensaxe);
    }
}
