package practica4.cliente.controladores;

import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.ClienteCallback;
import practica4.interfaces.IUsuario;

import java.rmi.*;
import java.rmi.server.*;


public abstract class ClienteCallbackImpl extends UnicastRemoteObject implements ClienteCallback{

   public ClienteCallbackImpl() throws RemoteException {
      super();

   }

   public void enviarMensaxe(Mensaxe mensaxe) throws RemoteException {
     onMensaxeRecibido(mensaxe);
   }
   public void rexistrarClienteDisponible(IUsuario novoCliente) throws RemoteException{
      onUsuarioConectado(novoCliente);
   }

   public void eliminarClienteDisponible(IUsuario velloCliente) throws RemoteException{
      onUsuarioDesconectado(velloCliente);
   }

   public abstract void onUsuarioConectado(IUsuario usuario) throws RemoteException;
   
   public abstract void onUsuarioDesconectado(IUsuario usuario) throws RemoteException;

   public abstract void onMensaxeRecibido(Mensaxe mensaxe) throws RemoteException;


}
