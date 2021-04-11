package practica4.cliente.obxectos;

import practica4.interfaces.IUsuario;

import java.util.UUID;

public class UsuarioLista{

    private IUsuario usuario;
    private int mensaxesPendentes;

    public UsuarioLista(IUsuario u) {
        this.usuario = u;
        this.mensaxesPendentes = 0;
    }

    public IUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(IUsuario u) {
        this.usuario = u;
    }

    public int getMensaxesPendentes() {
        return mensaxesPendentes;
    }

    public void setMensaxesPendentes(int mensaxesPendentes) {
        this.mensaxesPendentes = mensaxesPendentes;
    }

    public UUID getUuid(){
       return this.usuario.getUuid();
    }

    public void aumentarPendentes() {
        mensaxesPendentes++;
    }

    public void reiniciarPendentes() {
        mensaxesPendentes=0;
    }
}
