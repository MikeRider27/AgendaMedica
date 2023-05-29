/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.ClienteDTO;
import GENERICOS.Conexion;
import INT.ClienteINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class ClienteDAO implements ClienteINT {
    
    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();
    
//    public ClienteDAO() {
//        bd = new Conexion();
//    }
    
    @Override
    public boolean agregar(ClienteDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.clientes(\n"
                    + "            nroseguro_cliente, id_persona)\n"
                    + "    VALUES (?, ?)";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getNro_seguro());
            ps.setInt(2, dto.getId_persona());
            filaAfectada = ps.executeUpdate();
            if (filaAfectada > 0) {
                //Exitoso
                bd.commit();
                return true;
            } else {
                //error
                bd.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar datos " + e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }finally{
//           bd.delInstance();//probando el finally
        }
    }
    
    @Override
    public boolean modificar(ClienteDTO dto) {
        try {
            consultaSQL = "UPDATE public.clientes\n"
                    + "   SET nroseguro_cliente=?, id_persona=?\n"
                    + " WHERE id_cliente=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getNro_seguro());
            ps.setInt(2, dto.getId_persona());
            ps.setInt(3, dto.getId());
            filaAfectada = ps.executeUpdate();
            if (filaAfectada > 0) {
                //Exitoso
                bd.commit();
                return true;
            } else {
                //error
                bd.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar datos " + e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }finally{
//           bd.delInstance();//probando el finally
        }
    }
    
    @Override
    public boolean eliminar(ClienteDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.clientes\n"
                    + " WHERE id_cliente=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            filaAfectada = ps.executeUpdate();
            if (filaAfectada > 0) {
                //Exitoso
                bd.commit();
                return true;
            } else {
                //error
                bd.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar datos " + e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }finally{
//           bd.delInstance();//probando el finally
        }
    }
    
    @Override
    public List<ClienteDTO> consultarTodos(ClienteDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ClienteDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT cli.id_cliente, cli.nroseguro_cliente, pe.id_persona,pe.nombre01_persona,pe.ci_persona\n"
                    + "  FROM public.clientes cli\n"
                    + "  join personas pe on pe.id_persona=cli.id_persona;";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new ClienteDTO(rs.getInt("id_cliente"),
                        rs.getInt("id_persona"),
                        rs.getString("nombre01_persona"),
                        rs.getString("ci_persona"),
                        rs.getString("nroseguro_cliente")));//Mantener el orden de los parámetros
            }
           
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos sin filtro " + e);
            return null;
        }finally{
//           bd.delInstance();//probando el finally
        }
    }
    
    @Override
    public List<ClienteDTO> consultarSegunFiltro(ClienteDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ClienteDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT cli.id_cliente, cli.nroseguro_cliente, pe.id_persona,pe.nombre01_persona,pe.ci_persona\n"
                    + "  FROM public.clientes cli\n"
                    + "  join personas pe on pe.id_persona=cli.id_persona\n"
                    + "  where id_cliente=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista.add(new ClienteDTO(rs.getInt("id_cliente"),
                        rs.getInt("id_persona"),
                        rs.getString("nombre01_persona"),
                        rs.getString("ci_persona"),
                        rs.getString("nroseguro_cliente")));//Mantener el orden de los parámetros
            }
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos con filtro " + e);
            return null;
        }finally{
//           bd.delInstance();//probando el finally
        }
    }
    
}
