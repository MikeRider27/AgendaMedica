/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.SexoDTO;
import GENERICOS.Conexion;
import INT.SexoINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juan
 */
public class SexoDAO implements SexoINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public SexoDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(SexoDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.sexo(\n"
                    + "            sexo_descripcion)\n"
                    + "    VALUES (?)";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getDescripcion());
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
            return false;
        }finally{
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public boolean modificar(SexoDTO dto) {
        try {
            consultaSQL = "UPDATE public.sexo\n"
                    + "   SET sexo_descripcion=?\n"
                    + " WHERE sexo_id=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getDescripcion());
            ps.setInt(2, dto.getId());
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
            System.out.println("Error al actualizar datos " + e);
            return false;
        }finally{
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public boolean eliminar(SexoDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.sexo\n"
                    + " WHERE sexo_id=?";
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
        } catch (Exception e) {
            System.out.println("Error al eliminar datos " + e);
            return false;
        }finally{
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public List<SexoDTO> consultarTodos(SexoDTO dto) {
        try {
            ResultSet rs;
            ArrayList<SexoDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT sexo_id, sexo_descripcion\n"
                    + "  FROM public.sexo";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new SexoDTO(rs.getInt("sexo_id"), rs.getString("sexo_descripcion")));//Mantener el orden de los parámetros
            }

            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos de paises" + e);
            return null;
        }finally{
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public List<SexoDTO> consultarSegunFiltro(SexoDTO dto) {
        try {
            ResultSet rs;
            ArrayList<SexoDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT sexo_id, sexo_descripcion\n"
                    + "  FROM public.sexo where sexo_id=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista.add(new SexoDTO(rs.getInt("sexo_id"), rs.getString("sexo_descripcion")));//Mantener el orden de los parámetros
            }

            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos de paises" + e);
            return null;
        }finally{
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

}
