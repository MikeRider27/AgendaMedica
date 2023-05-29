/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.ECivilDTO;
import GENERICOS.Conexion;
import INT.ECivilINT;
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
public class ECivilDAO implements ECivilINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public ECivilDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(ECivilDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.estadocivil(\n"
                    + "            estadocivil_descripcion)\n"
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
    public boolean modificar(ECivilDTO dto) {
        try {
            consultaSQL = "UPDATE public.estadocivil\n"
                    + "   SET estadocivil_descripcion=?\n"
                    + " WHERE estadocivil_id=?";
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
    public boolean eliminar(ECivilDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.estadocivil\n"
                    + " WHERE estadocivil_id=?";
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
            return false;
        }finally{
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public List<ECivilDTO> consultarTodos(ECivilDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ECivilDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT estadocivil_id, estadocivil_descripcion\n"
                    + "  FROM public.estadocivil";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new ECivilDTO(rs.getInt("estadocivil_id"), rs.getString("estadocivil_descripcion")));//Mantener el orden de los parámetros
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
    public List<ECivilDTO> consultarSegunFiltro(ECivilDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ECivilDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT estadocivil_id, estadocivil_descripcion\n"
                    + "  FROM public.estadocivil where estadocivil_id=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista.add(new ECivilDTO(rs.getInt("estadocivil_id"), rs.getString("estadocivil_descripcion")));//Mantener el orden de los parámetros
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
