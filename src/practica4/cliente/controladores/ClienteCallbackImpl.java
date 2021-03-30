package practica4.cliente.controladores;

import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.ClienteCallback;
import practica4.interfaces.IRelacion;
import practica4.interfaces.IUsuario;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;


public  abstract class ClienteCallbackImpl extends UnicastRemoteObject implements ClienteCallback{

   private IUsuario usuario;

   public ClienteCallbackImpl(IUsuario usuario) throws RemoteException {
      super();
      this.usuario=usuario;
   }

   @Override
   public void enviarMensaxe(Mensaxe mensaxe) throws RemoteException {
     onMensaxeRecibido(mensaxe);
   }

   @Override
   public void rexistrarUsuarioDisponible(IUsuario novoUsuario) throws RemoteException{
      onUsuarioConectado(novoUsuario);
   }

   @Override
   public void eliminarUsuarioDisponible(IUsuario velloUsuario) throws RemoteException{
      onUsuarioDesconectado(velloUsuario);
   }

   @Override
   public void novoAmigo(IUsuario amigo) throws RemoteException {
      onAmigoNovo(amigo);
   }

   @Override
   public void eliminarAmigo(IUsuario amigo) throws RemoteException {
      onAmigoEliminado(amigo);
   }

   @Override
   public IUsuario getUsuario() throws RemoteException{
      return usuario;
   }

   @Override
   public void setUsuario(IUsuario usuario) throws RemoteException{
      this.usuario=usuario;
   }

   @Override
   public UUID getUuid() throws RemoteException {
      return usuario.getUuid();
   }

   @Override
   public void enviarSolicitude(IRelacion relacion) throws RemoteException {
      onSolictudeRecibda(relacion);
   }


   protected abstract void onUsuarioConectado(IUsuario usuario) throws RemoteException;

   protected abstract void onUsuarioDesconectado(IUsuario usuario) throws RemoteException;

   protected abstract void onMensaxeRecibido(Mensaxe mensaxe) throws RemoteException;

   protected abstract void onAmigoNovo(IUsuario usuario) throws RemoteException;

   protected abstract void onAmigoEliminado(IUsuario usuario) throws RemoteException;

   protected abstract void onSolictudeRecibda(IRelacion relacion) throws RemoteException;


}
