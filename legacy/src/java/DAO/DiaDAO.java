/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.DiaDTO;
import DTO.EstadoReservaDTO;
import GENERICOS.Conexion;
import INT.DiaINT;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juan
 */
public class DiaDAO implements DiaINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd= Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public DiaDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(DiaDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean modificar(DiaDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminar(DiaDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DiaDTO> consultarTodos(DiaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<DiaDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_dia, descripcion_dia\n"
                    + "  FROM public.dias";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new DiaDTO(rs.getInt("id_dia"), rs.getString("descripcion_dia")));//Mantener el orden de los parámetros
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
    public List<DiaDTO> consultarSegunFiltro(DiaDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
