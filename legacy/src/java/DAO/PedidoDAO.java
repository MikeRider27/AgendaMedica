/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.PedidoDTO;
import DTO.PedidoDetDTO;
import GENERICOS.Conexion;
import INT.PedidoINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class PedidoDAO implements PedidoINT {

    private String consultaSQL;
    private PreparedStatement ps, psDet;
    private int filaAfectada;
    private ResultSet rs;
    private Integer pk_generada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

    @Override
    public boolean agregar(List<PedidoDTO> dto) {
        try {
            consultaSQL = "INSERT INTO public.pedidos(\n"
                    + "            id_funcionario, id_cliente, obs, fecha)\n"
                    + "    VALUES (?, ?, ?, ?);";
            ps = bd.prepareStatement(consultaSQL, Statement.RETURN_GENERATED_KEYS);
            for (PedidoDTO dtoAsignado : dto) {
                ps.setObject(1, dtoAsignado.getId_funcionario());
                ps.setObject(2, dtoAsignado.getId_cliente());
                ps.setObject(3, dtoAsignado.getObs());
                ps.setObject(4, java.sql.Date.valueOf(dtoAsignado.convertir_fecha(dtoAsignado.getFecha())));
                filaAfectada = ps.executeUpdate();
                if (filaAfectada > 0) {
                    rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        pk_generada = rs.getInt(1);
                    }

                    consultaSQL = "INSERT INTO public.pedidodet(\n"
                            + "            id_pedido, id_producto, cantidad)\n"
                            + "    VALUES (?, ?, ?);";
                    psDet = bd.prepareStatement(consultaSQL);
                    System.out.println("detalle" + dtoAsignado.getDetalle());
                    for (PedidoDetDTO dtoDetalle : dtoAsignado.getDetalle()) {
                        psDet.setObject(1, pk_generada);
                        psDet.setObject(2, dtoDetalle.getId_producto());
                        psDet.setObject(3, dtoDetalle.getCantidad());
                        filaAfectada = psDet.executeUpdate();
                        if (filaAfectada <= 0) {
                            //debe morir la operacion
                            bd.rollback();
                            return false;
                        }
                    }
                } else {
                    //debe movir la operacion
                    bd.rollback();
                    return false;
                }
            }
            if (filaAfectada > 0) {
                //confirmacion de la operacion
                bd.commit();
            }
            return true;
        } catch (Exception e) {
            Logger.getLogger(PedidoDTO.class.getName()).log(Level.SEVERE, null, e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
    }

    @Override
    public boolean modificar(List<PedidoDTO> dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminar(List<PedidoDTO> dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<List<PedidoDTO>> consultarTodos(List<PedidoDTO> dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<List<PedidoDTO>> consultarSegunFiltro(List<PedidoDTO> dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PedidoDTO> consultarTodosSinFiltro(PedidoDTO dto) {
        try {
            ArrayList<PedidoDTO> lista = new ArrayList<>();
            consultaSQL = "select p.id_pedido, \n"
                    + "p.id_funcionario, pe.nombre01_persona as nombrefuncionario,\n"
                    + "p.id_cliente, per.nombre01_persona as nombrecliente,\n"
                    + "p.obs, \n"
                    + "p.fecha\n"
                    + "\n"
                    + "FROM pedidos p\n"
                    + "join funcionarios f on f.id_funcionario=p.id_funcionario\n"
                    + "join personas pe on pe.id_persona=f.id_persona\n"
                    + "join clientes c on c.id_cliente=p.id_cliente\n"
                    + "join personas per on per.id_persona=c.id_persona\n"
                    + "\n"
                    + "where p.estado=TRUE";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new PedidoDTO(rs.getInt("id_pedido"), rs.getInt("id_funcionario"), rs.getInt("id_cliente"),
                        rs.getString("obs"), rs.getString("fecha"), rs.getString("nombrefuncionario"), rs.getString("nombrecliente")));
            }
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos del pedido " + e);
            return null;
        }
    }

    @Override
    public List<PedidoDTO> consultarTodosConFiltro(PedidoDTO dto) {
        try {
            ArrayList<PedidoDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT pd.id_pedido, pd.id_producto, pro.descripcion_producto,pro.precioventa as precioventa, pd.cantidad as cantidad, (precioventa * cantidad) as total\n"
                    + "  FROM public.pedidodet pd \n"
                    + "  join productos pro on pro.id_producto=pd.id_producto\n"
                    + "  where pd.id_pedido=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId_pedido());
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new PedidoDTO(rs.getInt("id_pedido"), rs.getInt("id_producto"), rs.getInt("cantidad"), rs.getInt("total"),
                        rs.getString("descripcion_producto")));
            }
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos del pedido para con productos " + e);
            return null;
        }
    }

    @Override
    public boolean anular(PedidoDTO dto) {
        try {
            consultaSQL = "UPDATE public.pedidos\n"
                    + "   SET  estado=FALSE\n"
                    + " WHERE id_pedido=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId_pedido());
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
            System.out.println("Error al anular datos " + e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
    }

}
