package DAO;

import DTO.CiudadDTO;

import GENERICOS.Conexion;
import INT.CiudadINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CiudadDAO implements CiudadINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public CiudadDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(CiudadDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.ciudades(\n"
                    + "            ciudad_descripcion)\n"
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
        } catch (Exception e) {
            System.out.println("Error al insertar datos " + e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(CiudadDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }finally{
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public boolean modificar(CiudadDTO dto) {
        try {
            consultaSQL = "UPDATE public.ciudades\n"
                    + "   SET ciudad_descripcion=?\n"
                    + " WHERE ciudad_id=?";
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
        } catch (Exception e) {
            System.out.println("Error al modificar datos " + e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(CiudadDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }finally{
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public boolean eliminar(CiudadDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.ciudades\n"
                    + " WHERE ciudad_id=?";
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
            try {
                bd.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(CiudadDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }finally{
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public List<CiudadDTO> consultarTodos(CiudadDTO dto) {
        try {
            ResultSet rs;
            ArrayList<CiudadDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT ciudad_id, ciudad_descripcion\n"
                    + "  FROM public.ciudades order by ciudad_id asc";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new CiudadDTO(rs.getInt("ciudad_id"), rs.getString("ciudad_descripcion")));//Mantener el orden de los parámetros
            }

            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos sin filtro " + e);
            return null;
        }finally{
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public List<CiudadDTO> consultarSegunFiltro(CiudadDTO dto) {
        try {
            ResultSet rs;
            ArrayList<CiudadDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT ciudad_id, ciudad_descripcion\n"
                    + "  FROM public.ciudades where ciudad_id=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new CiudadDTO(rs.getInt("ciudad_id"), rs.getString("ciudad_descripcion")));//Mantener el orden de los parámetros
            }

            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos segun filtro " + e);
            return null;
        }finally{
           //           bd.delInstance();//probando el finally//probando el finally
        }
    }

}
