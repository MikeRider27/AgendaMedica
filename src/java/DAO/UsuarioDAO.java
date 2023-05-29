package DAO;

import DTO.UsuarioDTO;
import GENERICOS.Conexion;
import INT.UsuarioINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements UsuarioINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public UsuarioDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(UsuarioDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.usuarios(\n"
                    + "            nick_usuario, clave_usuario, id_rol)\n"
                    + "    VALUES ( ?, md5(?), ?)";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getNombre_usuario());
            ps.setString(2, dto.getClave_usuario());
            ps.setInt(3, dto.getId_rol());
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
    public boolean modificar(UsuarioDTO dto) {
        try {
            consultaSQL = "UPDATE public.usuarios\n"
                    + "   SET  nick_usuario=?, clave_usuario=md5(?), id_rol=?\n"
                    + " WHERE id_usuario=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getNombre_usuario());
            ps.setString(2, dto.getClave_usuario());
            ps.setInt(3, dto.getId_rol());
            ps.setInt(4, dto.getId_usuario());
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
    public boolean eliminar(UsuarioDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.usuarios\n"
                    + " WHERE id_usuario=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId_usuario());
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
    public List<UsuarioDTO> consultarTodos(UsuarioDTO dto) {
        try {
            ResultSet rs;
            ArrayList<UsuarioDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT u.id_usuario, u.nick_usuario, u.clave_usuario,r.descripcion_rol\n"
                    + "  FROM usuarios u \n"
                    + "  join role r on u.id_rol=r.id_rol";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new UsuarioDTO(rs.getInt("id_usuario"),
                        rs.getString("nick_usuario"),
                        rs.getString("clave_usuario"),
                        rs.getString("descripcion_rol")));
            }
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar " + e);
            return null;
        }finally{
//           bd.delInstance();//probando el finally
        }
    }

    @Override
    public List<UsuarioDTO> consultarSegunFiltro(UsuarioDTO dto) {
        try {
            ResultSet rs;
            ArrayList<UsuarioDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT u.id_usuario, u.nick_usuario, u.clave_usuario, r.id_rol, r.descripcion_rol\n"
                    + "  FROM usuarios u \n"
                    + "  join role r on u.id_rol=r.id_rol"
                    + " where id_usuario=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId_usuario());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista.add(new UsuarioDTO(rs.getInt("id_usuario"),
                        rs.getString("nick_usuario"),
                        rs.getString("clave_usuario"),
                        rs.getInt("id_rol"),
                        rs.getString("descripcion_rol")));
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
