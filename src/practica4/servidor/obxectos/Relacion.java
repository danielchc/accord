package practica4.servidor.obxectos;

import practica4.interfaces.IRelacion;
import practica4.interfaces.IUsuario;

public class Relacion implements IRelacion {
    private IUsuario u1;
    private IUsuario u2;
    private TipoRelacion tipoRelacion;

    public Relacion(IUsuario u1,IUsuario u2, TipoRelacion tipoRelacion) {
        this.u1 = u1;
        this.u2=u2;
        this.tipoRelacion = tipoRelacion;
    }

    @Override
    public IUsuario getU1() {
        return u1;
    }

    @Override
    public void setU1(IUsuario u) {
        this.u1=u;
    }

    @Override
    public IUsuario getU2() {
        return u2;
    }

    @Override
    public void setU2(IUsuario u) {
        this.u2=u;
    }

    @Override
    public TipoRelacion getRelacion() {
        return this.tipoRelacion;
    }

    @Override
    public void setRelacion(TipoRelacion tipoRelacion) {
        this.tipoRelacion=tipoRelacion;
    }

    @Override
    public String toString() {
        return u2.getNomeUsuario();
    }
}
