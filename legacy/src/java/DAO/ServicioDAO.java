/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.ServicioDTO;
import GENERICOS.Conexion;
import INT.ServicioINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juan
 */
public class ServicioDAO implements ServicioINT {

    private String consultaSQL;
    private PreparedStatement ps;
    private int filaAfectada;
    private ResultSet rs;

    Connection bd = Conexion.obtenerConexion();

    @Override
    public boolean agregar(ServicioDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean modificar(ServicioDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminar(ServicioDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ServicioDTO> consultarTodos(ServicioDTO dto) {
        try {
            ArrayList<ServicioDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_servicio, servi_descr, costo\n"
                    + "  FROM public.servicios";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new ServicioDTO(rs.getInt("id_servicio"), rs.getString("servi_descr"), rs.getInt("costo")));
            }
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos sin filtro de la venta " + e);
            return null;
        }
    }

    @Override
    public List<ServicioDTO> consultarSegunFiltro(ServicioDTO dto) {
        try {
            ArrayList<ServicioDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_servicio, servi_descr, costo\n"
                    + "  FROM public.servicios where id_servicio=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId_producto());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista.add(new ServicioDTO(rs.getInt("id_servicio"), rs.getString("servi_descr"), rs.getInt("costo")));
            }
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos con filtro de la venta " + e);
            return null;
        }
    }

}
