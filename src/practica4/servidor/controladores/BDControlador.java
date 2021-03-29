package practica4.servidor.controladores;

import practica4.interfaces.IUsuario;
import practica4.servidor.obxectos.Usuario;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class BDControlador {
    private Connection conn = null;

    public BDControlador() {
        try {
            String url = "jdbc:sqlite:practica4.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Conexión ca BD establecida.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public void rexistrarUsuario() {

    }

    public boolean comprobarUsuario(String nomeUsuario, String contrasinal) {
        PreparedStatement stmUsuario = null;
        ResultSet resultValidacion;
        try {
            stmUsuario = conn.prepareStatement("SELECT * FROM usuarios WHERE LOWER(nomeUsuario)=LOWER(?) AND contrasinal=?;");
            stmUsuario.setString(1, nomeUsuario);
            stmUsuario.setString(2, contrasinal);
            resultValidacion = stmUsuario.executeQuery();
            return resultValidacion.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmUsuario.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        return false;
    }

    public IUsuario getUsuario(String nomeUsuario) {
        PreparedStatement stmUsuario = null;
        ResultSet resultValidacion;
        try {
            stmUsuario = conn.prepareStatement("SELECT * FROM usuarios WHERE LOWER(nomeUsuario)=LOWER(?);");
            stmUsuario.setString(1, nomeUsuario);
            resultValidacion = stmUsuario.executeQuery();

            return new Usuario(
                    UUID.fromString(resultValidacion.getString("uuid")),
                    resultValidacion.getString("nomeUsuario")
            );
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        } finally {
            try {
                stmUsuario.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        return null;
    }

    public ArrayList<IUsuario> getAmigos(IUsuario usuario){
        ArrayList<IUsuario> amigos= new ArrayList<IUsuario>();
        PreparedStatement stmUsuario = null;
        ResultSet resultValidacion;
        try {
            stmUsuario = conn.prepareStatement("SELECT u.* FROM amigos AS a JOIN usuarios u on u.uuid = a.u1 OR a.u2 = u.uuid\n" +
                    "WHERE (a.u1=? or a.u2=?) AND NOT u.uuid=?;");
            stmUsuario.setString(1, usuario.getUuid().toString());
            stmUsuario.setString(2, usuario.getUuid().toString());
            stmUsuario.setString(3, usuario.getUuid().toString());
            resultValidacion = stmUsuario.executeQuery();

            while (resultValidacion.next()){
                amigos.add(new Usuario(
                        UUID.fromString(resultValidacion.getString("uuid")),
                        resultValidacion.getString("nomeUsuario")
                ));
            }
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        } finally {
            try {
                stmUsuario.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        return amigos;
    }

    public boolean sonAmigos(IUsuario u1,IUsuario u2){
        ArrayList<IUsuario> amigos= new ArrayList<IUsuario>();
        PreparedStatement stmUsuario = null;
        ResultSet resultValidacion;
        try {
            stmUsuario = conn.prepareStatement("SELECT 1 FROM amigos WHERE (u1=? OR u1=?) AND (u2=? OR u2=?)");
            stmUsuario.setString(1, u1.getUuid().toString());
            stmUsuario.setString(2, u2.getUuid().toString());
            stmUsuario.setString(3, u1.getUuid().toString());
            stmUsuario.setString(4, u2.getUuid().toString());
            resultValidacion = stmUsuario.executeQuery();
            return resultValidacion.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmUsuario.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        return false;
    }


}
