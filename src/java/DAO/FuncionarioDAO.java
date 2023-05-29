/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.FuncionarioDTO;
import GENERICOS.Conexion;
import INT.FuncionarioINT;
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
public class FuncionarioDAO implements FuncionarioINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public FuncionarioDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(FuncionarioDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.funcionarios(\n"
                    + "            id_persona, id_tcargo, id_espec, id_subcargo)\n"
                    + "    VALUES (?, ?, ?, ?)";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId_persona());
            ps.setInt(2, dto.getId_tcargo());
            ps.setInt(3, dto.getId_espec());
            ps.setInt(4, dto.getId_subcargo());
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
    public boolean modificar(FuncionarioDTO dto) {
        try {
            consultaSQL = "UPDATE public.funcionarios\n"
                    + "   SET  id_persona=?, id_tcargo=?, id_espec=?, id_subcargo=?\n"
                    + " WHERE id_funcionario=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId_persona());
            ps.setInt(2, dto.getId_tcargo());
            ps.setInt(3, dto.getId_espec());
            ps.setInt(4, dto.getId_subcargo());
            ps.setInt(5, dto.getId());
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
    public boolean eliminar(FuncionarioDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.funcionarios\n"
                    + " WHERE id_funcionario=?";
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
    public List<FuncionarioDTO> consultarTodos(FuncionarioDTO dto) {
        try {
            ResultSet rs;
            ArrayList<FuncionarioDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT fun.id_funcionario, pe.id_persona, pe.nombre01_persona,pe.ci_persona, \n"
                    + "tip.id_tcargo, tip.descripcion_tcargo, \n"
                    + "esp.id_espec, esp.descripcion_espec, \n"
                    + "sub.id_subcargo, sub.descripcion_subcargo\n"
                    + "  FROM public.funcionarios fun\n"
                    + "  join personas pe on pe.id_persona=fun.id_persona\n"
                    + "  join tipo_cargo tip on tip.id_tcargo=fun.id_tcargo\n"
                    + "  join especialidades esp on esp.id_espec=fun.id_espec\n"
                    + "  join subcargos sub on sub.id_subcargo=fun.id_subcargo";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new FuncionarioDTO(rs.getInt("id_funcionario"),
                        rs.getInt("id_persona"),
                        rs.getInt("id_tcargo"),
                        rs.getInt("id_espec"),
                        rs.getInt("id_subcargo"),
                        rs.getString("nombre01_persona"),
                        rs.getString("ci_persona"),
                        rs.getString("descripcion_tcargo"),
                        rs.getString("descripcion_espec"),
                        rs.getString("descripcion_subcargo")));
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
    public List<FuncionarioDTO> consultarSegunFiltro(FuncionarioDTO dto) {
        try {
            ResultSet rs;
            ArrayList<FuncionarioDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT fun.id_funcionario, pe.id_persona, pe.nombre01_persona,pe.ci_persona, \n"
                    + "tip.id_tcargo, tip.descripcion_tcargo, \n"
                    + "esp.id_espec, esp.descripcion_espec, \n"
                    + "sub.id_subcargo, sub.descripcion_subcargo\n"
                    + "  FROM public.funcionarios fun\n"
                    + "  join personas pe on pe.id_persona=fun.id_persona\n"
                    + "  join tipo_cargo tip on tip.id_tcargo=fun.id_tcargo\n"
                    + "  join especialidades esp on esp.id_espec=fun.id_espec\n"
                    + "  join subcargos sub on sub.id_subcargo=fun.id_subcargo\n"
                    + "  where id_funcionario=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new FuncionarioDTO(rs.getInt("id_funcionario"),
                        rs.getInt("id_persona"),
                        rs.getInt("id_tcargo"),
                        rs.getInt("id_espec"),
                        rs.getInt("id_subcargo"),
                        rs.getString("nombre01_persona"),
                        rs.getString("ci_persona"),
                        rs.getString("descripcion_tcargo"),
                        rs.getString("descripcion_espec"),
                        rs.getString("descripcion_subcargo")));
            }
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar con filtro " + e);
            return null;
        }finally{
//           bd.delInstance();//probando el finally
        }
    }

    @Override
    public List<FuncionarioDTO> consultarTodosFuncionarios(FuncionarioDTO dto) {
        try {
            ResultSet rs;
            ArrayList<FuncionarioDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT fun.id_funcionario, pe.id_persona, pe.nombre01_persona,pe.ci_persona, \n"
                    + "tip.id_tcargo, tip.descripcion_tcargo, \n"
                    + "esp.id_espec, esp.descripcion_espec, \n"
                    + "sub.id_subcargo, sub.descripcion_subcargo\n"
                    + "  FROM public.funcionarios fun\n"
                    + "  join personas pe on pe.id_persona=fun.id_persona\n"
                    + "  join tipo_cargo tip on tip.id_tcargo=fun.id_tcargo\n"
                    + "  join especialidades esp on esp.id_espec=fun.id_espec\n"
                    + "  join subcargos sub on sub.id_subcargo=fun.id_subcargo where tip.id_tcargo=2";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new FuncionarioDTO(rs.getInt("id_funcionario"),
                        rs.getInt("id_persona"),
                        rs.getInt("id_tcargo"),
                        rs.getInt("id_espec"),
                        rs.getInt("id_subcargo"),
                        rs.getString("nombre01_persona"),
                        rs.getString("ci_persona"),
                        rs.getString("descripcion_tcargo"),
                        rs.getString("descripcion_espec"),
                        rs.getString("descripcion_subcargo")));
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
    public List<FuncionarioDTO> consultarTodosMedicos(FuncionarioDTO dto) {
        try {
            ResultSet rs;
            ArrayList<FuncionarioDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT fun.id_funcionario, pe.id_persona, pe.nombre01_persona,pe.ci_persona, \n"
                    + "tip.id_tcargo, tip.descripcion_tcargo, \n"
                    + "esp.id_espec, esp.descripcion_espec, \n"
                    + "sub.id_subcargo, sub.descripcion_subcargo\n"
                    + "  FROM public.funcionarios fun\n"
                    + "  join personas pe on pe.id_persona=fun.id_persona\n"
                    + "  join tipo_cargo tip on tip.id_tcargo=fun.id_tcargo\n"
                    + "  join especialidades esp on esp.id_espec=fun.id_espec\n"
                    + "  join subcargos sub on sub.id_subcargo=fun.id_subcargo where tip.id_tcargo=1";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new FuncionarioDTO(rs.getInt("id_funcionario"),
                        rs.getInt("id_persona"),
                        rs.getInt("id_tcargo"),
                        rs.getInt("id_espec"),
                        rs.getInt("id_subcargo"),
                        rs.getString("nombre01_persona"),
                        rs.getString("ci_persona"),
                        rs.getString("descripcion_tcargo"),
                        rs.getString("descripcion_espec"),
                        rs.getString("descripcion_subcargo")));
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
    public List<FuncionarioDTO> consultarTodosFuncionariosConFiltro(FuncionarioDTO dto) {
        try {
            ResultSet rs;
            ArrayList<FuncionarioDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT fun.id_funcionario, pe.id_persona, pe.nombre01_persona,pe.ci_persona, \n"
                    + "tip.id_tcargo, tip.descripcion_tcargo, \n"
                    + "esp.id_espec, esp.descripcion_espec, \n"
                    + "sub.id_subcargo, sub.descripcion_subcargo\n"
                    + "  FROM public.funcionarios fun\n"
                    + "  join personas pe on pe.id_persona=fun.id_persona\n"
                    + "  join tipo_cargo tip on tip.id_tcargo=fun.id_tcargo\n"
                    + "  join especialidades esp on esp.id_espec=fun.id_espec\n"
                    + "  join subcargos sub on sub.id_subcargo=fun.id_subcargo where tip.id_tcargo=2 and fun.id_funcionario=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new FuncionarioDTO(rs.getInt("id_funcionario"),
                        rs.getInt("id_persona"),
                        rs.getInt("id_tcargo"),
                        rs.getInt("id_espec"),
                        rs.getInt("id_subcargo"),
                        rs.getString("nombre01_persona"),
                        rs.getString("ci_persona"),
                        rs.getString("descripcion_tcargo"),
                        rs.getString("descripcion_espec"),
                        rs.getString("descripcion_subcargo")));
            }
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar con filtro funcionarios " + e);
            return null;
        }finally{
//           bd.delInstance();//probando el finally
        }
    }

    @Override
    public List<FuncionarioDTO> consultarTodosMedicosConFiltro(FuncionarioDTO dto) {
        try {
            ResultSet rs;
            ArrayList<FuncionarioDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT fun.id_funcionario, pe.id_persona, pe.nombre01_persona,pe.ci_persona, \n"
                    + "tip.id_tcargo, tip.descripcion_tcargo, \n"
                    + "esp.id_espec, esp.descripcion_espec, \n"
                    + "sub.id_subcargo, sub.descripcion_subcargo\n"
                    + "  FROM public.funcionarios fun\n"
                    + "  join personas pe on pe.id_persona=fun.id_persona\n"
                    + "  join tipo_cargo tip on tip.id_tcargo=fun.id_tcargo\n"
                    + "  join especialidades esp on esp.id_espec=fun.id_espec\n"
                    + "  join subcargos sub on sub.id_subcargo=fun.id_subcargo where tip.id_tcargo=1 and fun.id_funcionario=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new FuncionarioDTO(rs.getInt("id_funcionario"),
                        rs.getInt("id_persona"),
                        rs.getInt("id_tcargo"),
                        rs.getInt("id_espec"),
                        rs.getInt("id_subcargo"),
                        rs.getString("nombre01_persona"),
                        rs.getString("ci_persona"),
                        rs.getString("descripcion_tcargo"),
                        rs.getString("descripcion_espec"),
                        rs.getString("descripcion_subcargo")));
            }
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar con filtro medicos  " + e);
            return null;
        }finally{
//           bd.delInstance();//probando el finally
        }
    }

}
