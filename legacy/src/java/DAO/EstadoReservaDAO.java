/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.EstadoReservaDTO;
import GENERICOS.Conexion;
import INT.EstadoReservaINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juan
 */
public class EstadoReservaDAO implements EstadoReservaINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd= Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public EstadoReservaDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(EstadoReservaDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean modificar(EstadoReservaDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminar(EstadoReservaDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EstadoReservaDTO> consultarTodos(EstadoReservaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<EstadoReservaDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_estadoreserva, desc_estadoreserva\n"
                    + "  FROM public.estado_reservas";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new EstadoReservaDTO(rs.getInt("id_estadoreserva"), rs.getString("desc_estadoreserva")));//Mantener el orden de los parámetros
            }
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos " + e);
            return null;
        }finally{
//            bd.delInstance();//probando el finally
        }
    }

    @Override
    public List<EstadoReservaDTO> consultarSegunFiltro(EstadoReservaDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
