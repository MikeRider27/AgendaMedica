package DAO;

import DTO.CargoDTO;
import GENERICOS.Conexion;
import INT.CargoINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CargoDAO implements CargoINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public CargoDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(CargoDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.tipo_cargo(\n"
                    + "            descripcion_tcargo)\n"
                    + "    VALUES (?)";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getCargo());
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
                Logger.getLogger(CargoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
    }

    @Override
    public boolean modificar(CargoDTO dto) {
        try {
            consultaSQL = "UPDATE public.tipo_cargo\n"
                    + "   SET  descripcion_tcargo=?\n"
                    + " WHERE id_tcargo=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getCargo());
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
            System.out.println("Error al actualizar datos " + e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(CargoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
    }

    @Override
    public boolean eliminar(CargoDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.tipo_cargo\n"
                    + " WHERE id_tcargo=?";
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
                Logger.getLogger(CargoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
    }

    @Override
    public List<CargoDTO> consultarTodos(CargoDTO dto) {
        try {
            ResultSet rs;
            ArrayList<CargoDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_tcargo, descripcion_tcargo\n"
                    + "  FROM public.tipo_cargo order by id_tcargo asc";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new CargoDTO(rs.getInt("id_tcargo"), rs.getString("descripcion_tcargo")));
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
    public List<CargoDTO> consultarSegunFiltro(CargoDTO dto) {
        try {
            ResultSet rs;
            ArrayList<CargoDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_tcargo, descripcion_tcargo\n"
                    + "  FROM public.tipo_cargo where id_tcargo=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista.add(new CargoDTO(rs.getInt("id_tcargo"), rs.getString("descripcion_tcargo")));
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
