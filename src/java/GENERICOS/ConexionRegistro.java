/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GENERICOS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author juan
 */
public class ConexionRegistro {

    private Connection con = null;
    private String bd = "taller";
    private String login = "postgres";
    private String clave = "123";
    private final String CONTROLADOR_JDBC = "org.postgresql.Driver";
    private final String URL_BASEDEDATOS = "jdbc:postgresql://localhost:5432/" + bd;

    public ConexionRegistro() {
        try {
            Class.forName(CONTROLADOR_JDBC);
            con = DriverManager.getConnection(URL_BASEDEDATOS, login, clave);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error " + e);
        }

    }

    public Connection obtenerConexion() {
        return con;
    }
}
