/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GENERICOS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author juan
 */
public class SalvajeDB {
    /**
     * Se viene algo salvaje y duro *
     */
    protected Connection conn = null;
    protected PreparedStatement consulta;
    protected ResultSet datos;
    protected String bd = "taller";
    protected String login = "postgres";
    protected String clave = "123";
    protected final String CONTROLADOR_JDBC = "org.postgresql.Driver";
    protected final String URL_BASEDEDATOS = "jdbc:postgresql://localhost:5432/" + bd;
    
    public void conectar() throws ClassNotFoundException, SQLException{
        Class.forName(CONTROLADOR_JDBC);
        this.conn = DriverManager.getConnection(URL_BASEDEDATOS, login, clave);
    }
    public void desconectar() throws SQLException{
        this.conn.close();
//        this.consulta.close();
    }
}
