package DAO;

import DTO.PersonaDTO;
import GENERICOS.Conexion;
import INT.PersonaINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonaDAO implements PersonaINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private final Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();
    SimpleDateFormat fe = new SimpleDateFormat("dd/MM/YYYY");
    SimpleDateFormat feUSA = new SimpleDateFormat("YYYY/MM/dd");

//    public PersonaDAO() {
//        bd = new Conexion();
//    }
    @Override
    public boolean agregar(PersonaDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.personas(\n"
                    + "            nombre01_persona, nombre02_persona, apellido01_persona, \n"
                    + "            apellido02_persona, ci_persona, tel_persona, dir_persona, estadocivil_id, \n"
                    + "            ciudad_id, sexo_id, fechanac_persona, id_nacionalidades, email_persona)\n"
                    + "    VALUES (?, ?, ?, \n"
                    + "            ?, ?, ?, ?, ?, \n"
                    + "            ?, ?, ?, ?, ?)";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getNombre1());
            ps.setString(2, dto.getNombre2());
            ps.setString(3, dto.getApe1());
            ps.setString(4, dto.getApe2());
            ps.setString(5, dto.getCi());
            ps.setString(6, dto.getTel());
            ps.setString(7, dto.getDir());
            ps.setInt(8, dto.getId_ecivil());
            ps.setInt(9, dto.getId_ciudad());
            ps.setInt(10, dto.getId_sexo());
            ps.setDate(11, java.sql.Date.valueOf(dto.getFecha()));
            ps.setInt(12, dto.getId_nac());
            ps.setString(13, dto.getEmail());
            filaAfectada = ps.executeUpdate();
            if (filaAfectada > 0) {
                //Exitoso
                bd.commit();
                System.out.println("Se guardo correctamente el registro");
                return true;
            } else {
                //error
                bd.rollback();
                System.out.println("No se guardo correctamente el registro");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar datos " + e);
            return false;
        }
    }

    @Override
    public boolean modificar(PersonaDTO dto) {
        try {
            consultaSQL = "UPDATE public.personas\n"
                    + "   SET  nombre01_persona=?, nombre02_persona=?, apellido01_persona=?, \n"
                    + "       apellido02_persona=?, ci_persona=?, tel_persona=?, dir_persona=?, \n"
                    + "       estadocivil_id=?, ciudad_id=?, sexo_id=?, fechanac_persona=?, \n"
                    + "       id_nacionalidades=?, email_persona=?\n"
                    + " WHERE id_persona=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getNombre1());
            ps.setString(2, dto.getNombre2());
            ps.setString(3, dto.getApe1());
            ps.setString(4, dto.getApe2());
            ps.setString(5, dto.getCi());
            ps.setString(6, dto.getTel());
            ps.setString(7, dto.getDir());
            ps.setInt(8, dto.getId_ecivil());
            ps.setInt(9, dto.getId_ciudad());
            ps.setInt(10, dto.getId_sexo());
            ps.setDate(11, java.sql.Date.valueOf(dto.getFecha()));
            ps.setInt(12, dto.getId_nac());
            ps.setString(13, dto.getEmail());
            ps.setInt(14, dto.getId());
            filaAfectada = ps.executeUpdate();
            if (filaAfectada > 0) {
                //Exitoso
                System.out.println("Se modifico correctamente el registro");
                bd.commit();
                return true;
            } else {
                //error
                bd.rollback();
                System.out.println("No se modifico correctamente el registro");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar datos " + e);
            return false;
        } finally {
//                      bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public boolean eliminar(PersonaDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.personas\n"
                    + " WHERE id_persona=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            filaAfectada = ps.executeUpdate();
            if (filaAfectada > 0) {
                //Exitoso
                bd.commit();
                System.out.println("Se elimino correctamente el registro");
                return true;
            } else {
                //error
                bd.rollback();
                System.out.println("No se elimino correctamente el registro");
                return false;
            }
        } catch (SQLException e) {
            try {
                bd.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(PersonaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Error al eliminar datos " + e);
            return false;
        }
    }

    @Override
    public List<PersonaDTO> consultarTodos(PersonaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<PersonaDTO> lista_p = new ArrayList<>();
            consultaSQL = "SELECT pe.id_persona, pe.nombre01_persona, pe.nombre02_persona, pe.apellido01_persona, \n"
                    + "       pe.apellido02_persona, pe.ci_persona, pe.tel_persona, pe.dir_persona,e.estadocivil_id, e.estadocivil_descripcion, ciu.ciudad_id,\n"
                    + "       ciu.ciudad_descripcion,sex.sexo_id, sex.sexo_descripcion, pe.fechanac_persona,nac.id_nacionalidades ,nac.desc_nacionalidades, pe.email_persona\n"
                    + "  FROM public.personas pe\n"
                    + "  join estadocivil e on e.estadocivil_id=pe.estadocivil_id\n"
                    + "  join ciudades ciu on ciu.ciudad_id=pe.ciudad_id\n"
                    + "  join sexo sex on sex.sexo_id=pe.sexo_id\n"
                    + "  join nacionalidades nac on nac.id_nacionalidades=pe.id_nacionalidades order by pe.id_persona desc";
            ps = bd.prepareStatement(consultaSQL);

            rs = ps.executeQuery();
            while (rs.next()) {
                lista_p.add(new PersonaDTO(rs.getInt("id_persona"),
                        rs.getInt("estadocivil_id"),
                        rs.getInt("ciudad_id"),
                        rs.getInt("id_nacionalidades"),
                        rs.getInt("sexo_id"),
                        rs.getString("nombre01_persona"),
                        rs.getString("nombre02_persona"),
                        rs.getString("apellido01_persona"),
                        rs.getString("apellido02_persona"),
                        rs.getString("ci_persona"),
                        rs.getString("dir_persona"),
                        rs.getString("tel_persona"),
                        rs.getString("email_persona"),
                        rs.getString("estadocivil_descripcion"),
                        rs.getString("ciudad_descripcion"),
                        rs.getString("desc_nacionalidades"),
                        rs.getString("sexo_descripcion"),
                        fe.format(rs.getDate("fechanac_persona"))));//Mantener el orden de los parámetros
            }

            return lista_p;
        } catch (Exception e) {
            System.out.println("Error al listar datos de personas sin filtro " + e);
            return null;
        } finally {
//                      bd.delInstance();//probando el finally//probando el finally
        }
    }

    @Override
    public List<PersonaDTO> consultarSegunFiltro(PersonaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<PersonaDTO> lista_p = new ArrayList<>();
            consultaSQL = "SELECT pe.id_persona, pe.nombre01_persona, pe.nombre02_persona, pe.apellido01_persona, \n"
                    + "       pe.apellido02_persona, pe.ci_persona, pe.tel_persona, pe.dir_persona,e.estadocivil_id, e.estadocivil_descripcion, ciu.ciudad_id,\n"
                    + "       ciu.ciudad_descripcion,sex.sexo_id, sex.sexo_descripcion, pe.fechanac_persona,nac.id_nacionalidades ,nac.desc_nacionalidades, pe.email_persona\n"
                    + "  FROM public.personas pe\n"
                    + "  join estadocivil e on e.estadocivil_id=pe.estadocivil_id\n"
                    + "  join ciudades ciu on ciu.ciudad_id=pe.ciudad_id\n"
                    + "  join sexo sex on sex.sexo_id=pe.sexo_id\n"
                    + "  join nacionalidades nac on nac.id_nacionalidades=pe.id_nacionalidades\n"
                    + "  where id_persona=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista_p.add(new PersonaDTO(rs.getInt("id_persona"),
                        rs.getInt("estadocivil_id"),
                        rs.getInt("ciudad_id"),
                        rs.getInt("id_nacionalidades"),
                        rs.getInt("sexo_id"),
                        rs.getString("nombre01_persona"),
                        rs.getString("nombre02_persona"),
                        rs.getString("apellido01_persona"),
                        rs.getString("apellido02_persona"),
                        rs.getString("ci_persona"),
                        rs.getString("dir_persona"),
                        rs.getString("tel_persona"),
                        rs.getString("email_persona"),
                        rs.getString("estadocivil_descripcion"),
                        rs.getString("ciudad_descripcion"),
                        rs.getString("desc_nacionalidades"),
                        rs.getString("sexo_descripcion"),
                        fe.format(rs.getDate("fechanac_persona"))));//Mantener el orden de los parámetros
            }

            return lista_p;
        } catch (Exception e) {
            System.out.println("Error al listar datos de personas con filtro " + e);
            return null;
        } finally {
            //           bd.delInstance();//probando el finally//probando el finally
        }
    }

}
