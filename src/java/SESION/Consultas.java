package SESION;

import GENERICOS.ConexionRegistro;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Consultas extends ConexionRegistro {

    public boolean autenticacion(String usuario, String clave) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT nick_usuario, clave_usuario\n"
                    + "  FROM public.usuarios where nick_usuario=? and clave_usuario=md5(?)";
            pst = obtenerConexion().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst.setString(1, usuario);
            pst.setString(2, clave);
            rs = pst.executeQuery();

            if (rs.absolute(1)) {
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error " + e);
        } finally {
            try {
                if (obtenerConexion() != null) {
                    obtenerConexion().close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.err.println("Error " + e);
            }
        }
        return false;
    }

//    public static void main(String[] args) {
//        Consultas co = new Consultas();
//        System.out.println(co.autenticacion("gallo", "1"));
//    }
}
