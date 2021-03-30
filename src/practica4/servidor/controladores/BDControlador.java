package practica4.servidor.controladores;

import practica4.interfaces.IRelacion;
import practica4.interfaces.IUsuario;
import practica4.servidor.obxectos.Relacion;
import practica4.servidor.obxectos.Usuario;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
            stmUsuario = conn.prepareStatement("SELECT u.* FROM amigos AS a JOIN usuarios u on u.uuid = a.u1 OR a.u2 = u.uuid " +
                    "WHERE (a.u1=? or a.u2=?) AND NOT u.uuid=? AND aceptado=1;");
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
            stmUsuario = conn.prepareStatement("SELECT 1 FROM amigos WHERE ? IN (u1,u2) AND ? IN (u1,u2) AND aceptado=1");
            stmUsuario.setString(1, u1.getUuid().toString());
            stmUsuario.setString(2, u2.getUuid().toString());
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


    public List<IRelacion> buscarUsuarios(String query, IUsuario usuario) {
        String filter="";
        if(query.trim().isEmpty()) filter=" AND relacion IN (0,1)";
        ArrayList<IRelacion> usuarios= new ArrayList<IRelacion>();
        PreparedStatement stmUsuario = null;
        ResultSet resultValidacion;
        try {
            stmUsuario = conn.prepareStatement("SELECT *, (IFNULL((SELECT aceptado FROM amigos " +
                    "WHERE uuid IN (u1,u2) AND ? IN (u1,u2)),2)) AS relacion," +
                    "(IFNULL((SELECT 1 FROM amigos WHERE u2=? AND u1=uuid),0)) AS toMe " +
                    "FROM usuarios Where uuid!=? AND LOWER(nomeUsuario) LIKE LOWER(?) "+filter+";");

            stmUsuario.setString(1, usuario.getUuid().toString());
            stmUsuario.setString(2, usuario.getUuid().toString());
            stmUsuario.setString(3, usuario.getUuid().toString());
            stmUsuario.setString(4, "%"+query+"%");
            resultValidacion = stmUsuario.executeQuery();

            while (resultValidacion.next()){
                if (resultValidacion.getBoolean("toMe")){
                    usuarios.add(new Relacion(
                            new Usuario(
                                    UUID.fromString(resultValidacion.getString("uuid")),
                                    resultValidacion.getString("nomeUsuario")
                            ),
                            usuario,
                            IRelacion.TipoRelacion.values()[resultValidacion.getInt("relacion")]
                    ));
                }else{
                    usuarios.add(new Relacion(
                            usuario,
                            new Usuario(
                                    UUID.fromString(resultValidacion.getString("uuid")),
                                    resultValidacion.getString("nomeUsuario")
                            ),
                            IRelacion.TipoRelacion.values()[resultValidacion.getInt("relacion")]
                    ));
                }
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
        return usuarios;
    }

    public void crearSolicitude(IRelacion relacion) {
        PreparedStatement stmSolicitude = null;
        try {
            stmSolicitude = conn.prepareStatement("INSERT INTO amigos VALUES(?,?,0);");
            stmSolicitude.setString(1, relacion.getU1().getUuid().toString());
            stmSolicitude.setString(2, relacion.getU2().getUuid().toString());
            stmSolicitude.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmSolicitude != null) stmSolicitude.close();
            } catch (SQLException e) {
                System.out.println("Imposible pechar os cursores.");
            }
        }
    }

    public void eliminarAmigo(IRelacion item) {
        PreparedStatement stmAmigo = null;
        try {
            stmAmigo = conn.prepareStatement("DELETE FROM amigos WHERE ? IN (u1,u2) AND ? IN (u1,u2);");
            stmAmigo.setString(1, item.getU1().getUuid().toString());
            stmAmigo.setString(2, item.getU2().getUuid().toString());
            stmAmigo.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmAmigo != null) stmAmigo.close();
            } catch (SQLException e) {
                System.out.println("Imposible pechar os cursores.");
            }
        }
    }

    public void aceptarSolicitude(IRelacion item) {
        PreparedStatement stmAmigo = null;
        try {
            stmAmigo = conn.prepareStatement("UPDATE amigos SET aceptado=1 WHERE ? IN (u1,u2) AND ? IN (u1,u2);");
            stmAmigo.setString(1, item.getU1().getUuid().toString());
            stmAmigo.setString(2, item.getU2().getUuid().toString());
            stmAmigo.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmAmigo != null) stmAmigo.close();
            } catch (SQLException e) {
                System.out.println("Imposible pechar os cursores.");
            }
        }
    }

}
