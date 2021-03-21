package practica4.cliente.obxectos;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.UUID;

public interface IMensaxe extends Remote, Serializable {
    UUID getDe();

    void setDe(UUID de);

    UUID getPara();

    void setPara(UUID para);

    String getMensaxe();

    void setMensaxe(String mensaxe);

    @Override
    String toString();
}
