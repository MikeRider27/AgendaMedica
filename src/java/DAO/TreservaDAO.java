package DAO;

import DTO.TreservaDTO;
import GENERICOS.Conexion;
import INT.TreservaINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TreservaDAO implements TreservaINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public TreservaDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(TreservaDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.tipo_reserva(\n"
                    + "            descripcion_reserva)\n"
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
           
        }
    }

    @Override
    public boolean modificar(TreservaDTO dto) {
        try {
            consultaSQL = "UPDATE public.tipo_reserva\n"
                    + "   SET descripcion_reserva=?\n"
                    + " WHERE id_tiporeserva=?";
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
//            bd.delInstance();//probando el finally
        }
    }

    @Override
    public boolean eliminar(TreservaDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.tipo_reserva\n"
                    + " WHERE id_tiporeserva=?";
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
    public List<TreservaDTO> consultarTodos(TreservaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<TreservaDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_tiporeserva, descripcion_reserva\n"
                    + "  FROM public.tipo_reserva";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new TreservaDTO(rs.getInt("id_tiporeserva"), rs.getString("descripcion_reserva")));//Mantener el orden de los parámetros
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
    public List<TreservaDTO> consultarSegunFiltro(TreservaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<TreservaDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_tiporeserva, descripcion_reserva\n"
                    + "  FROM public.tipo_reserva where id_tiporeserva=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista.add(new TreservaDTO(rs.getInt("id_tiporeserva"), rs.getString("descripcion_reserva")));//Mantener el orden de los parámetros
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
