package practica4.interfaces;

import java.io.Serializable;

public interface IRelacion extends Serializable {
    enum TipoRelacion{
        SolicitudePendente,
        Amigos,
        Ningunha
    }

    IUsuario getU1();

    void setU1(IUsuario u);

    IUsuario getU2();

    void setU2(IUsuario u);

    TipoRelacion getRelacion();

    void setRelacion(TipoRelacion tipoRelacion);

}
