package DAO;

import DTO.SubcargoDTO;
import GENERICOS.Conexion;
import INT.SubcargoINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubcargoDAO implements SubcargoINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public SubcargoDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(SubcargoDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.subcargos(\n"
                    + "            descripcion_subcargo)\n"
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
//           bd.delInstance();//probando el finally
        }
    }

    @Override
    public boolean modificar(SubcargoDTO dto) {
        try {
            consultaSQL = "UPDATE public.subcargos\n"
                    + "   SET descripcion_subcargo=?\n"
                    + " WHERE id_subcargo=?";
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
//           bd.delInstance();//probando el finally
        }
    }

    @Override
    public boolean eliminar(SubcargoDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.subcargos\n"
                    + " WHERE id_subcargo=?";
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
//           bd.delInstance();//probando el finally
        }
    }

    @Override
    public List<SubcargoDTO> consultarTodos(SubcargoDTO dto) {
        try {
            ResultSet rs;
            ArrayList<SubcargoDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_subcargo, descripcion_subcargo\n"
                    + "  FROM public.subcargos";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new SubcargoDTO(rs.getInt("id_subcargo"), rs.getString("descripcion_subcargo")));//Mantener el orden de los parámetros
            }
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos " + e);
            return null;
        }finally{
//           bd.delInstance();//probando el finally
        }
    }

    @Override
    public List<SubcargoDTO> consultarSegunFiltro(SubcargoDTO dto) {
        try {
            ResultSet rs;
            ArrayList<SubcargoDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_subcargo, descripcion_subcargo\n"
                    + "  FROM public.subcargos where id_subcargo=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new SubcargoDTO(rs.getInt("id_subcargo"), rs.getString("descripcion_subcargo")));//Mantener el orden de los parámetros
            }
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos " + e);
            return null;
        }finally{
//           bd.delInstance();//probando el finally
        }
    }

}
