package DAO;

import DTO.ReservaDTO;
import GENERICOS.Conexion;
import INT.ReservaINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO implements ReservaINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();;
    Connection bd = Conexion.obtenerConexion();
    SimpleDateFormat fe = new SimpleDateFormat("dd/MM/YYYY");
    SimpleDateFormat feUSA = new SimpleDateFormat("YYYY/MM/dd");

//    public ReservaDAO() {
//        bd = new Conexion();
//    }
    @Override
    public boolean agregar(ReservaDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.reservas(\n"
                    + "            fecha_cita, id_cliente, id_tiporeserva, id_espec, \n"
                    + "            id_dia, hora_reserva, id_turno, id_estadoreserva, id_funcionario_fun, \n"
                    + "            id_funcionario_med, cupoact_reserva)\n"
                    + "    VALUES (?, ?, ?, ?, \n"
                    + "            ?, ?, ?, ?, ?, \n"
                    + "            ?, ?)";
            ps = bd.prepareStatement(consultaSQL);
            //ps.setInt(1, dto.getId());
            ps.setDate(1, java.sql.Date.valueOf(dto.getFecha()));
            ps.setInt(2, dto.getId_cliente());
            ps.setInt(3, dto.getId_tiporeserva());
            ps.setInt(4, dto.getId_espec());
            ps.setInt(5, dto.getId_dia());
            ps.setTime(6, java.sql.Time.valueOf(dto.getHora()));
            ps.setInt(7, dto.getId_turno());
            ps.setInt(8, dto.getId_estadoreserva());
            ps.setInt(9, dto.getId_funcionario_fun());
            ps.setInt(10, dto.getId_funcionario_med());
            ps.setInt(11, dto.getCupoactual());
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
//            bd.delInstance();
        }
    }

    @Override
    public boolean modificar(ReservaDTO dto) {
        try {
            consultaSQL = "UPDATE public.reservas\n"
                    + "   SET fecha_cita=?, id_cliente=?, id_tiporeserva=?, id_espec=?, \n"
                    + "       id_dia=?, hora_reserva=?, id_turno=?, id_estadoreserva=?, id_funcionario_fun=?, \n"
                    + "       id_funcionario_med=?\n"
                    + " WHERE id_reserva=?";
            ps = bd.prepareStatement(consultaSQL);

            ps.setDate(1, java.sql.Date.valueOf(dto.getFecha()));
            ps.setInt(2, dto.getId_cliente());
            ps.setInt(3, dto.getId_tiporeserva());
            ps.setInt(4, dto.getId_espec());
            ps.setInt(5, dto.getId_dia());
            ps.setTime(6, java.sql.Time.valueOf(dto.getHora()));
            ps.setInt(7, dto.getId_turno());
            ps.setInt(8, dto.getId_estadoreserva());
            ps.setInt(9, dto.getId_funcionario_fun());
            ps.setInt(10, dto.getId_funcionario_med());
            ps.setInt(11, dto.getId());
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
            System.out.println("Error al modificar datos " + e);
            return false;
        } finally {
//            bd.delInstance();
        }
    }

    @Override
    public boolean eliminar(ReservaDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.reservas\n"
                    + " WHERE id_reserva=?";
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
        } finally {
//            bd.delInstance();
        }
    }

    @Override
    public List<ReservaDTO> consultarTodos(ReservaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ReservaDTO> lista_p = new ArrayList<>();
            consultaSQL = "SELECT re.id_reserva, re.fecha_cita, re.hora_reserva,re.id_funcionario_fun, p.nombre01_persona as funcionario,\n"
                    + "       re.id_funcionario_med, pm.nombre01_persona as medico,\n"
                    + "cli.id_cliente,pe.nombre01_persona as cliente, pe.ci_persona, \n"
                    + "tip.id_tiporeserva,tip.descripcion_reserva as tiporeserva,\n"
                    + "esp.id_espec,esp.descripcion_espec as especialidad, \n"
                    + "di.id_dia,di.descripcion_dia as dia,  \n"
                    + "tu.id_turno,tu.descripcion_turno as turno, \n"
                    + "est.id_estadoreserva,est.desc_estadoreserva as estadoreserva\n"
                    + "  FROM reservas re\n"
                    + "  join clientes cli on cli.id_cliente=re.id_cliente\n"
                    + "  join personas pe on pe.id_persona=cli.id_persona\n"
                    + "\n"
                    + "  join tipo_reserva tip on tip.id_tiporeserva=re.id_tiporeserva\n"
                    + "  join especialidades esp on esp.id_espec=re.id_espec\n"
                    + "  join dias di on di.id_dia=re.id_dia\n"
                    + "  join turnos tu on tu.id_turno=re.id_turno\n"
                    + "  join estado_reservas est on est.id_estadoreserva=re.id_estadoreserva\n"
                    + "\n"
                    + "  join funcionarios fu on fu.id_funcionario=re.id_funcionario_fun\n"
                    + "  join funcionarios fun on fun.id_funcionario=re.id_funcionario_med\n"
                    + "\n"
                    + "  join personas p on p.id_persona=fu.id_persona\n"
                    + "  join personas pm on pm.id_persona=fun.id_persona order by re.id_reserva desc";
            ps = bd.prepareStatement(consultaSQL);

            rs = ps.executeQuery();
            while (rs.next()) {
                lista_p.add(new ReservaDTO(rs.getInt("id_reserva"),
                        rs.getInt("id_funcionario_fun"),
                        rs.getInt("id_funcionario_med"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_tiporeserva"),
                        rs.getInt("id_espec"),
                        rs.getInt("id_dia"),
                        rs.getInt("id_turno"),
                        rs.getInt("id_estadoreserva"),
                        fe.format(rs.getDate("fecha_cita")),
                        rs.getString("hora_reserva"),
                        rs.getString("funcionario"),
                        rs.getString("medico"),
                        rs.getString("cliente"),
                        rs.getString("ci_persona"),
                        rs.getString("tiporeserva"),
                        rs.getString("especialidad"),
                        rs.getString("dia"),
                        rs.getString("turno"),
                        rs.getString("estadoreserva")));//Mantener el orden de los parámetros
            }
            //rs.close();//Agregado
//            bd.desConectarBD();
            return lista_p;
        } catch (Exception e) {
            System.out.println("Error al listar sin filtro datos de personas " + e);
            return null;
        } finally {
//            bd.delInstance();
        }
    }

    @Override
    public List<ReservaDTO> consultarSegunFiltro(ReservaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ReservaDTO> lista_p = new ArrayList<>();
            consultaSQL = "SELECT re.id_reserva, re.fecha_cita, re.hora_reserva,re.id_funcionario_fun, p.nombre01_persona as funcionario,re.cupoact_reserva, esp.cupo_espec - cupoact_reserva as resta,\n"
                    + "       re.id_funcionario_med, pm.nombre01_persona as medico,\n"
                    + "cli.id_cliente,pe.nombre01_persona as cliente, pe.ci_persona, \n"
                    + "tip.id_tiporeserva,tip.descripcion_reserva as tiporeserva,\n"
                    + "esp.id_espec,esp.descripcion_espec as especialidad, \n"
                    + "di.id_dia,di.descripcion_dia as dia,  \n"
                    + "tu.id_turno,tu.descripcion_turno as turno, \n"
                    + "est.id_estadoreserva,est.desc_estadoreserva as estadoreserva\n"
                    + "  FROM reservas re\n"
                    + "  join clientes cli on cli.id_cliente=re.id_cliente\n"
                    + "  join personas pe on pe.id_persona=cli.id_persona\n"
                    + "\n"
                    + "  join tipo_reserva tip on tip.id_tiporeserva=re.id_tiporeserva\n"
                    + "  join especialidades esp on esp.id_espec=re.id_espec\n"
                    + "  join dias di on di.id_dia=re.id_dia\n"
                    + "  join turnos tu on tu.id_turno=re.id_turno\n"
                    + "  join estado_reservas est on est.id_estadoreserva=re.id_estadoreserva\n"
                    + "\n"
                    + "  join funcionarios fu on fu.id_funcionario=re.id_funcionario_fun\n"
                    + "  join funcionarios fun on fun.id_funcionario=re.id_funcionario_med\n"
                    + "\n"
                    + "  join personas p on p.id_persona=fu.id_persona\n"
                    + "  join personas pm on pm.id_persona=fun.id_persona\n"
                    + "  where id_reserva=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista_p.add(new ReservaDTO(rs.getInt("id_reserva"),
                        rs.getInt("id_funcionario_fun"),
                        rs.getInt("id_funcionario_med"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_tiporeserva"),
                        rs.getInt("id_espec"),
                        rs.getInt("id_dia"),
                        rs.getInt("id_turno"),
                        rs.getInt("id_estadoreserva"),
                        fe.format(rs.getDate("fecha_cita")),
                        rs.getString("hora_reserva"),
                        rs.getString("funcionario"),
                        rs.getString("medico"),
                        rs.getString("cliente"),
                        rs.getString("ci_persona"),
                        rs.getString("tiporeserva"),
                        rs.getString("especialidad"),
                        rs.getString("dia"),
                        rs.getString("turno"),
                        rs.getString("estadoreserva"),
                        rs.getInt("cupoact_reserva"), rs.getInt("resta")));//Mantener el orden de los parámetros
            }
            //rs.close();
//            bd.desConectarBD();
            return lista_p;
        } catch (Exception e) {
            System.out.println("Error al listar con filtro datos en reservas " + e);
            return null;
        } finally {
//            bd.delInstance();
        }
    }

    @Override
    public List<ReservaDTO> consultarCupos(ReservaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ReservaDTO> lista_p = new ArrayList<>();
            consultaSQL = "select count(id_espec) as cupoXespec  FROM reservas where id_espec=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId_espec());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista_p.add(new ReservaDTO(rs.getInt("cupoXespec")));//Mantener el orden de los parámetros
            }
//            bd.desConectarBD();
            return lista_p;
        } catch (Exception e) {
            System.out.println("Error al listar datos de personas en consultar cupos " + e);
            return null;
        } finally {
//            bd.delInstance();
        }
    }

    @Override
    public List<ReservaDTO> consultarCuposEspe(ReservaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ReservaDTO> lista_p = new ArrayList<>();
            consultaSQL = "SELECT id_espec,descripcion_espec, cupo_espec FROM especialidades where id_espec=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId_espec());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista_p.add(new ReservaDTO(rs.getInt("id_espec"), rs.getInt("cupo_espec")));//Mantener el orden de los parámetros
            }
            //rs.close();
//            bd.desConectarBD();
            return lista_p;
        } catch (Exception e) {
            System.out.println("Error al listar datos de personas en consultar cupos " + e);
            return null;
        } finally {
//            bd.delInstance();
        }
    }

    @Override
    public List<ReservaDTO> consultarCuposDisponibles(ReservaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ReservaDTO> lista_p = new ArrayList<>();
            consultaSQL = " SELECT esp.id_espec,esp.descripcion_espec as especialidad,esp.cupo_espec,esp.cupo_espec - cupoact_reserva as resta\n"
                    + "  FROM reservas re\n"
                    + "  join especialidades esp on esp.id_espec=re.id_espec where id_reserva=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista_p.add(new ReservaDTO(rs.getInt("resta")));//Mantener el orden de los parámetros
            }
            //rs.close();
            // bd.desConectarBD();
            return lista_p;
        } catch (Exception e) {
            System.out.println("Error al listar datos de personas en cuposdisponibles " + e);
            return null;
        } finally {
//            bd.delInstance();
        }
    }

    @Override
    public List<ReservaDTO> consultarReservaCONFIRMconFiltro(ReservaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ReservaDTO> lista_p = new ArrayList<>();
            consultaSQL = "SELECT re.id_reserva, re.fecha_cita, re.hora_reserva,re.id_funcionario_fun, p.nombre01_persona as funcionario,re.cupoact_reserva, esp.cupo_espec - cupoact_reserva as resta,\n"
                    + "       re.id_funcionario_med, pm.nombre01_persona as medico,\n"
                    + "cli.id_cliente,pe.nombre01_persona as cliente, pe.ci_persona,pe.apellido01_persona, \n"
                    + "tip.id_tiporeserva,tip.descripcion_reserva as tiporeserva,\n"
                    + "esp.id_espec,esp.descripcion_espec as especialidad, \n"
                    + "di.id_dia,di.descripcion_dia as dia,  \n"
                    + "tu.id_turno,tu.descripcion_turno as turno, \n"
                    + "est.id_estadoreserva,est.desc_estadoreserva as estadoreserva\n"
                    + "  FROM reservas re\n"
                    + "  join clientes cli on cli.id_cliente=re.id_cliente\n"
                    + "  join personas pe on pe.id_persona=cli.id_persona\n"
                    + "\n"
                    + "  join tipo_reserva tip on tip.id_tiporeserva=re.id_tiporeserva\n"
                    + "  join especialidades esp on esp.id_espec=re.id_espec\n"
                    + "  join dias di on di.id_dia=re.id_dia\n"
                    + "  join turnos tu on tu.id_turno=re.id_turno\n"
                    + "  join estado_reservas est on est.id_estadoreserva=re.id_estadoreserva\n"
                    + "\n"
                    + "  join funcionarios fu on fu.id_funcionario=re.id_funcionario_fun\n"
                    + "  join funcionarios fun on fun.id_funcionario=re.id_funcionario_med\n"
                    + "\n"
                    + "  join personas p on p.id_persona=fu.id_persona\n"
                    + "  join personas pm on pm.id_persona=fun.id_persona\n"
                    + "  where id_reserva=? and est.id_estadoreserva=2";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                lista_p.add(new ReservaDTO(rs.getInt("id_reserva"), rs.getString("medico"),
                        rs.getString("cliente"), rs.getString("ci_persona"), rs.getString("apellido01_persona"), rs.getString("especialidad")));//Mantener el orden de los parámetros
            }
            //rs.close();
//            bd.desConectarBD();
            return lista_p;
        } catch (Exception e) {
            System.out.println("Error al listar con filtro datos de personas " + e);
            return null;
        } finally {
//            bd.delInstance();
        }
    }

    @Override
    public List<ReservaDTO> consultarReservaCONFIRMsinFiltro(ReservaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ReservaDTO> lista_p = new ArrayList<>();
            consultaSQL = "SELECT re.id_reserva, re.fecha_cita, re.hora_reserva,re.id_funcionario_fun, p.nombre01_persona as funcionario,\n"
                    + "       re.id_funcionario_med, pm.nombre01_persona as medico,\n"
                    + "cli.id_cliente,pe.nombre01_persona as cliente, pe.ci_persona,pe.apellido01_persona, \n"
                    + "tip.id_tiporeserva,tip.descripcion_reserva as tiporeserva,\n"
                    + "esp.id_espec,esp.descripcion_espec as especialidad, \n"
                    + "di.id_dia,di.descripcion_dia as dia,  \n"
                    + "tu.id_turno,tu.descripcion_turno as turno, \n"
                    + "est.id_estadoreserva,est.desc_estadoreserva as estadoreserva\n"
                    + "  FROM reservas re\n"
                    + "  join clientes cli on cli.id_cliente=re.id_cliente\n"
                    + "  join personas pe on pe.id_persona=cli.id_persona\n"
                    + "\n"
                    + "  join tipo_reserva tip on tip.id_tiporeserva=re.id_tiporeserva\n"
                    + "  join especialidades esp on esp.id_espec=re.id_espec\n"
                    + "  join dias di on di.id_dia=re.id_dia\n"
                    + "  join turnos tu on tu.id_turno=re.id_turno\n"
                    + "  join estado_reservas est on est.id_estadoreserva=re.id_estadoreserva\n"
                    + "\n"
                    + "  join funcionarios fu on fu.id_funcionario=re.id_funcionario_fun\n"
                    + "  join funcionarios fun on fun.id_funcionario=re.id_funcionario_med\n"
                    + "\n"
                    + "  join personas p on p.id_persona=fu.id_persona\n"
                    + "  join personas pm on pm.id_persona=fun.id_persona where est.id_estadoreserva=2";
            ps = bd.prepareStatement(consultaSQL);

            rs = ps.executeQuery();
            while (rs.next()) {
                lista_p.add(new ReservaDTO(rs.getInt("id_reserva"), rs.getString("medico"),
                        rs.getString("cliente"), rs.getString("ci_persona"), rs.getString("apellido01_persona"), rs.getString("especialidad")));//Mantener el orden de los parámetros
            }
            //rs.close();
//            bd.desConectarBD();
            return lista_p;
        } catch (Exception e) {
            System.out.println("Error al listar sin filtro datos de personas " + e);
            return null;
        } finally {
//            bd.delInstance();
        }
    }

    @Override
    public List<ReservaDTO> consultarEmailFechatodos(ReservaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ReservaDTO> lista_p = new ArrayList<>();
            consultaSQL = "SELECT re.id_reserva, re.fecha_cita, re.hora_reserva,re.id_funcionario_fun, p.nombre01_persona as funcionario,\n"
                    + "       re.id_funcionario_med, pm.nombre01_persona as medico,\n"
                    + "cli.id_cliente,pe.nombre01_persona as cliente, pe.ci_persona,pe.email_persona, \n"
                    + "tip.id_tiporeserva,tip.descripcion_reserva as tiporeserva,\n"
                    + "esp.id_espec,esp.descripcion_espec as especialidad, \n"
                    + "di.id_dia,di.descripcion_dia as dia,  \n"
                    + "tu.id_turno,tu.descripcion_turno as turno, \n"
                    + "est.id_estadoreserva,est.desc_estadoreserva as estadoreserva\n"
                    + "  FROM reservas re\n"
                    + "  join clientes cli on cli.id_cliente=re.id_cliente\n"
                    + "  join personas pe on pe.id_persona=cli.id_persona\n"
                    + "\n"
                    + "  join tipo_reserva tip on tip.id_tiporeserva=re.id_tiporeserva\n"
                    + "  join especialidades esp on esp.id_espec=re.id_espec\n"
                    + "  join dias di on di.id_dia=re.id_dia\n"
                    + "  join turnos tu on tu.id_turno=re.id_turno\n"
                    + "  join estado_reservas est on est.id_estadoreserva=re.id_estadoreserva\n"
                    + "\n"
                    + "  join funcionarios fu on fu.id_funcionario=re.id_funcionario_fun\n"
                    + "  join funcionarios fun on fun.id_funcionario=re.id_funcionario_med\n"
                    + "\n"
                    + "  join personas p on p.id_persona=fu.id_persona\n"
                    + "  join personas pm on pm.id_persona=fun.id_persona where est.id_estadoreserva=1";
            ps = bd.prepareStatement(consultaSQL);

            rs = ps.executeQuery();
            while (rs.next()) {
                lista_p.add(new ReservaDTO(
                        fe.format(rs.getDate("fecha_cita")),
                        rs.getString("email_persona")));//Mantener el orden de los parámetros
            }
//            rs.close();
//            bd.desConectarBD();
            return lista_p;
        } catch (Exception e) {
            System.out.println("Error al listar el email y fecha de personas " + e);
            return null;
        } finally {
//            bd.delInstance();
        }
    }

}
