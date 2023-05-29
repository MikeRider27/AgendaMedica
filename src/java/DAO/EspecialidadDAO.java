/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.EspecialidadDTO;
import GENERICOS.Conexion;
import INT.EspecialidadINT;
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
public class EspecialidadDAO implements EspecialidadINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public EspecialidadDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(EspecialidadDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.especialidades(\n"
                    + "            descripcion_espec, cupo_espec)\n"
                    + "    VALUES (?, ?)";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getDescripcion());
            ps.setInt(2, dto.getCupo());
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
//            bd.delInstance();//probando el finally
        }
    }

    @Override
    public boolean modificar(EspecialidadDTO dto) {
        try {
            consultaSQL = "UPDATE public.especialidades\n"
                    + "   SET  descripcion_espec=?, cupo_espec=?\n"
                    + " WHERE id_espec=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getDescripcion());
            ps.setInt(2, dto.getCupo());
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
            System.out.println("Error al actualizar datos " + e);
            return false;
        }finally{
//            bd.delInstance();//probando el finally
        }
    }

    @Override
    public boolean eliminar(EspecialidadDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.especialidades\n"
                    + " WHERE id_espec=?";
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
//            bd.delInstance();//probando el finally
        }
    }

    @Override
    public List<EspecialidadDTO> consultarTodos(EspecialidadDTO dto) {
        try {
            ResultSet rs;
            ArrayList<EspecialidadDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_espec, descripcion_espec, cupo_espec\n"
                    + "  FROM public.especialidades order by id_espec;";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new EspecialidadDTO(rs.getInt("id_espec"), rs.getString("descripcion_espec"), rs.getInt("cupo_espec")));//Mantener el orden de los parámetros
            }
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos de paises" + e);
            return null;
        }finally{
//            bd.delInstance();//probando el finally
        }
    }

    @Override
    public List<EspecialidadDTO> consultarSegunFiltro(EspecialidadDTO dto) {
        try {
            ResultSet rs;
            ArrayList<EspecialidadDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_espec, descripcion_espec,cupo_espec\n"
                    + "  FROM public.especialidades where id_espec=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista.add(new EspecialidadDTO(rs.getInt("id_espec"), rs.getString("descripcion_espec"),rs.getInt("cupo_espec")));//Mantener el orden de los parámetros
            }
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos de paises" + e);
            return null;
        }finally{
//            bd.delInstance();//probando el finally
        }
    }

}
