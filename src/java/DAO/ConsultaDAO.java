/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.ConsultaDTO;
import GENERICOS.Conexion;
import INT.ConsultaINT;
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
public class ConsultaDAO implements ConsultaINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public ConsultaDAO() {
//        bd = new Conexion();
//    }
    @Override
    public boolean agregar(ConsultaDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.consultas(\n"
                    + "            id_reserva, id_funcionario, obs_consulta, fecha_consulta)\n"
                    + "    VALUES (?, ?, ?, ?)";
            ps = bd.prepareStatement(consultaSQL);
            //ps.setInt(1, dto.getId());
            ps.setInt(1, dto.getId_reserva());
            ps.setInt(2, dto.getId_funcionario());
            ps.setString(3, dto.getObs());
            ps.setDate(4, java.sql.Date.valueOf(dto.getFecha()));

            filaAfectada = ps.executeUpdate();
            if (filaAfectada > 0) {
                //Exitoso
                System.out.println("Se guardo correctamente");
                bd.commit();
                return true;
            } else {
                //error
                bd.rollback();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error al insertar datos " + e);
            return false;
        }
    }

    @Override
    public boolean modificar(ConsultaDTO dto) {
        try {
            consultaSQL = "UPDATE public.consultas\n"
                    + "   SET id_reserva=?, id_funcionario=?, obs_consulta=?, \n"
                    + "       fecha_consulta=?\n"
                    + " WHERE id_consulta=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId_reserva());
            ps.setInt(2, dto.getId_funcionario());
            ps.setString(3, dto.getObs());
            ps.setDate(4, java.sql.Date.valueOf(dto.getFecha()));
            ps.setInt(5, dto.getId());
            filaAfectada = ps.executeUpdate();
            if (filaAfectada > 0) {
                //Exitoso
                System.out.println("Se actualizo correctamente");
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
        }
    }

    @Override
    public boolean eliminar(ConsultaDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.consultas\n"
                    + " WHERE id_consulta=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            filaAfectada = ps.executeUpdate();
            if (filaAfectada > 0) {
                //Exitoso
                System.out.println("Se elimino correctamente");
                bd.commit();
                return true;
            } else {
                //error
                bd.rollback();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error al insertar datos " + e);
            return false;
        }
    }

    @Override
    public List<ConsultaDTO> consultarTodos(ConsultaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ConsultaDTO> lista = new ArrayList<>();
            consultaSQL = "select con.id_consulta as nroconsulta,\n"
                    + "con.fecha_consulta as fechaconsulta,\n"
                    + "con.id_funcionario as nrofuncionario, pe.nombre01_persona as nombrefuncionario,\n"
                    + "con.id_reserva as nroreserva, re.id_cliente as nrocliente,pc.nombre01_persona, pc.apellido01_persona, pc.ci_persona,\n"
                    + "re.id_espec, esp.descripcion_espec,\n"
                    + "funme.id_funcionario, funme.id_persona, pm.nombre01_persona as doctor,\n"
                    + "con.obs_consulta\n"
                    + "from consultas con\n"
                    + "join funcionarios fun on fun.id_funcionario=con.id_funcionario\n"
                    + "join personas pe on pe.id_persona=fun.id_persona\n"
                    + "join reservas re on re.id_reserva=con.id_reserva\n"
                    + "join clientes cli on cli.id_cliente=re.id_cliente\n"
                    + "join personas pc on pc.id_persona=cli.id_persona\n"
                    + "join especialidades esp on esp.id_espec=re.id_espec\n"
                    + "join funcionarios funme on funme.id_funcionario=re.id_funcionario_med\n"
                    + "join personas pm on pm.id_persona=funme.id_persona";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new ConsultaDTO(rs.getInt("nroconsulta"), rs.getInt("nrofuncionario"), rs.getInt("nroreserva"),
                        rs.getString("fechaconsulta"), rs.getString("nombrefuncionario"), rs.getString("nombre01_persona"), rs.getString("apellido01_persona"),
                        rs.getString("ci_persona"), rs.getString("descripcion_espec"), rs.getString("doctor"), rs.getString("obs_consulta")));//Mantener el orden de los parámetros
            }

            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos en la consulta" + e);
            return null;
        } finally {
//           bd.delInstance();//probando el finally
        }
    }

    @Override
    public List<ConsultaDTO> consultarSegunFiltro(ConsultaDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ConsultaDTO> lista = new ArrayList<>();
            consultaSQL = "select con.id_consulta,\n"
                    + "con.fecha_consulta as fechaconsulta,\n"
                    + "con.id_funcionario as nrofuncionario, pe.nombre01_persona as nombrefuncionario,\n"
                    + "con.id_reserva as nroreserva, re.id_cliente as nrocliente,pc.nombre01_persona, pc.apellido01_persona, pc.ci_persona,\n"
                    + "re.id_espec, esp.descripcion_espec,\n"
                    + "funme.id_funcionario, funme.id_persona, pm.nombre01_persona as doctor,\n"
                    + "con.obs_consulta\n"
                    + "from consultas con\n"
                    + "join funcionarios fun on fun.id_funcionario=con.id_funcionario\n"
                    + "join personas pe on pe.id_persona=fun.id_persona\n"
                    + "join reservas re on re.id_reserva=con.id_reserva\n"
                    + "join clientes cli on cli.id_cliente=re.id_cliente\n"
                    + "join personas pc on pc.id_persona=cli.id_persona\n"
                    + "join especialidades esp on esp.id_espec=re.id_espec\n"
                    + "join funcionarios funme on funme.id_funcionario=re.id_funcionario_med\n"
                    + "join personas pm on pm.id_persona=funme.id_persona where con.id_consulta=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new ConsultaDTO(rs.getInt("id_consulta"), rs.getInt("nrofuncionario"), rs.getInt("nroreserva"),
                        rs.getString("fechaconsulta"), rs.getString("nombrefuncionario"), rs.getString("nombre01_persona"), rs.getString("apellido01_persona"),
                        rs.getString("ci_persona"), rs.getString("descripcion_espec"), rs.getString("doctor"), rs.getString("obs_consulta")));//Mantener el orden de los parámetros
            }

            return lista;
        } catch (SQLException e) {
            System.out.println("Error al listar datos con filtro en la consulta" + e);
            return null;
        } finally {
//           bd.delInstance();//probando el finally
        }
    }

}
