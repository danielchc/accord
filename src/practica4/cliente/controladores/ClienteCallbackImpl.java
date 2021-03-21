package practica4.cliente.controladores;

import practica4.cliente.interfaces.EventosCliente;
import practica4.cliente.obxectos.Mensaxe;
import practica4.interfaces.ClienteCallback;

import java.rmi.*;
import java.rmi.server.*;
import java.util.UUID;


public class ClienteCallbackImpl extends UnicastRemoteObject implements ClienteCallback{


   private final UUID uuid;

   private EventosCliente eventos;
   public ClienteCallbackImpl() throws RemoteException {
      super();
      this.uuid=UUID.randomUUID();

   }

   @Override
   public UUID getUUID() throws RemoteException {
      return uuid;
   }

   public void enviarMensaxe(Mensaxe mensaxe){
      eventos.onMensaxeRecibido(mensaxe);
   }          

   public void rexistrarClienteDisponible(UUID uuid) throws RemoteException{
      eventos.onUsuarioConectado(uuid);
   }        
   
   public void eliminarClienteDisponible(UUID uuid) throws RemoteException{
      eventos.onUsuarioDesconectado(uuid);
   }

   public void setEventos(EventosCliente eventos) {
      this.eventos = eventos;
   }
}
