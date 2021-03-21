package practica4.servidor.controladores;

import practica4.interfaces.IUsuario;
import practica4.servidor.obxectos.Usuario;

import java.rmi.RemoteException;
import java.sql.*;
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

    public void rexistrarUsuario(){

    }

    public boolean comprobarUsuario(String nomeUsuario,String contrasinal){
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

    public IUsuario getUsuario(String nomeUsuario){
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


}
