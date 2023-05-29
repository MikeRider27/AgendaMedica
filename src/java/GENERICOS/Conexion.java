package GENERICOS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {

    private static Connection Conexion = null;
    private static Statement Statement;

    public static Connection obtenerConexion() {
        try {
            if (Conexion == null) {
                String ControladorBD = "org.postgresql.Driver";
                String BD = "taller";
                String Url = "jdbc:postgresql://localhost:5432/" + BD;
                String Usuario = "postgres";
                String Clave = "123";
                Class.forName(ControladorBD);
                Conexion = DriverManager.getConnection(Url, Usuario, Clave);
                Statement = Conexion.createStatement();
                Conexion.setAutoCommit(false);
                System.out.println("Conexion Exitosa !!");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return Conexion;
    }

    public void desConectarBD() {
        try {
            if (Conexion != null) {
                Conexion.close();
                System.out.println("Conexion cerrada !");
            }
        } catch (Exception e) {
            System.out.println("Error al querer cerrar la conexion " + e);
        }
    }
}
