/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.TurnoDTO;
import GENERICOS.Conexion;
import INT.turnoINT;
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
public class TurnoDAO implements turnoINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd= Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public TurnoDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(TurnoDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.turnos(\n"
                    + "            descripcion_turno, cupo_turno)\n"
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
    public boolean modificar(TurnoDTO dto) {
        try {
            consultaSQL = "UPDATE public.turnos\n"
                    + "   SET  descripcion_turno=?, cupo_turno=?\n"
                    + " WHERE id_turno=?";
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
        } catch (Exception e) {
            System.out.println("Error al actualizar datos " + e);
            return false;
        }finally{
//            bd.delInstance();//probando el finally
        }
    }

    @Override
    public boolean eliminar(TurnoDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.turnos\n"
                    + " WHERE id_turno=?";
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
//            bd.delInstance();//probando el finally
        }
    }

    @Override
    public List<TurnoDTO> consultarTodos(TurnoDTO dto) {
        try {
            ResultSet rs;
            ArrayList<TurnoDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_turno, descripcion_turno, cupo_turno\n"
                    + "  FROM public.turnos";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new TurnoDTO(rs.getInt("id_turno"),
                        rs.getString("descripcion_turno"),
                        rs.getInt("cupo_turno")));//Mantener el orden de los parámetros
            }
//            bd.delInstance();
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos de paises" + e);
            return null;
        }finally{
//            bd.delInstance();
        }
    }

    @Override
    public List<TurnoDTO> consultarSegunFiltro(TurnoDTO dto) {
        try {
            ResultSet rs;
            ArrayList<TurnoDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_turno, descripcion_turno, cupo_turno\n"
                    + "  FROM public.turnos where id_turno=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista.add(new TurnoDTO(rs.getInt("id_turno"),
                        rs.getString("descripcion_turno"),
                        rs.getInt("cupo_turno")));//Mantener el orden de los parámetros
            }
            
//            bd.delInstance();
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos de paises" + e);
            return null;
        }finally{
//            bd.delInstance();
        }
    }

}
