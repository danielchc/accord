package practica4.interfaces;

import java.io.Serializable;
import java.util.Date;

public interface IMensaxe extends Serializable {
    IUsuario getDe();

    void setDe(IUsuario de);

    IUsuario getPara();

    void setPara(IUsuario para);

    String getMensaxe();

    void setMensaxe(String mensaxe);

    Date getData();

    void setData(Date data);

}
