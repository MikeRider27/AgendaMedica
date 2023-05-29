package DAO;

import DTO.PaisDTO;
import GENERICOS.Conexion;
import INT.PaisINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaisDAO implements PaisINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public PaisDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(PaisDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.nacionalidades(\n"
                    + "            desc_nacionalidades)\n"
                    + "    VALUES (?)";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getVpais_descripcion());
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
        } finally {
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public boolean modificar(PaisDTO dto) {
        try {
            consultaSQL = "UPDATE public.nacionalidades\n"
                    + "   SET desc_nacionalidades=?\n"
                    + " WHERE id_nacionalidades=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getVpais_descripcion());
            ps.setInt(2, dto.getVpais_id());
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
        } finally {
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public boolean eliminar(PaisDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.nacionalidades\n"
                    + " WHERE id_nacionalidades=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getVpais_id());
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
        } finally {
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public List<PaisDTO> consultarTodos(PaisDTO dto) {
        try {
            ResultSet rs;
            ArrayList<PaisDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_nacionalidades, desc_nacionalidades\n"
                    + "  FROM public.nacionalidades";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new PaisDTO(rs.getInt("id_nacionalidades"), rs.getString("desc_nacionalidades")));//Mantener el orden de los parámetros
            }

            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos de paises sin filtro " + e);
            return null;
        } finally {
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public List<PaisDTO> consultarSegunFiltro(PaisDTO dto) {
        try {
            ResultSet rs;
            ArrayList<PaisDTO> lista_p = new ArrayList<>();
            consultaSQL = "SELECT id_nacionalidades, desc_nacionalidades\n"
                    + "  FROM public.nacionalidades where id_nacionalidades=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getVpais_id());
            rs = ps.executeQuery();
            while (rs.next()) {
                lista_p.add(new PaisDTO(rs.getInt("id_nacionalidades"), rs.getString("desc_nacionalidades")));//Mantener el orden de los parámetros
            }

            return lista_p;
        } catch (Exception e) {
            System.out.println("Error al listar datos de paises" + e);
            return null;
        } finally {
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

}
