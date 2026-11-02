package DAO;

import DTO.RolDTO;
import GENERICOS.Conexion;
import INT.RolINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolDAO implements RolINT {

//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();
    String sql;
    ResultSet rs;
    PreparedStatement ps;
    private int filasAfectadas;

//    public RolDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(RolDTO dto) {
        try {
            sql = "INSERT INTO public.role(\n"
                    + "            descripcion_rol)\n"
                    + "    VALUES (?)";
            ps = bd.prepareStatement(sql);
            ps.setString(1, dto.getDescripcion());
            filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
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
    public boolean modificar(RolDTO dto) {
        try {
            sql = "UPDATE public.role\n"
                    + "   SET  descripcion_rol=?\n"
                    + " WHERE id_rol=?";
            ps = bd.prepareStatement(sql);
            ps.setString(1, dto.getDescripcion());
            ps.setInt(2, dto.getId());
            filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
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
    public boolean eliminar(RolDTO dto) {
        try {
            sql = "DELETE FROM public.role\n"
                    + " WHERE id_rol=?";
            ps = bd.prepareStatement(sql);
            ps.setInt(1, dto.getId());
            filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
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
    public List<RolDTO> consultarTodos(RolDTO dto) {
        try {
            ArrayList<RolDTO> lista = new ArrayList<>();
            sql = "SELECT id_rol, descripcion_rol\n"
                    + "  FROM public.role order by id_rol asc";
            ps = bd.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new RolDTO(rs.getInt("id_rol"), rs.getString("descripcion_rol")));
            }
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar sin filtro " + e);
            return null;
        }finally{
//           bd.delInstance();//probando el finally
        }
    }

    @Override
    public List<RolDTO> consultarSegunFiltro(RolDTO dto) {
        try {
            ArrayList<RolDTO> lista = new ArrayList<>();
            sql = "SELECT id_rol, descripcion_rol\n"
                    + "  FROM public.role where id_rol=?";
            ps = bd.prepareStatement(sql);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista.add(new RolDTO(rs.getInt("id_rol"), rs.getString("descripcion_rol")));
            }
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar con filtro " + e);
            return null;
        }finally{
//           bd.delInstance();//probando el finally
        }
    }

}
