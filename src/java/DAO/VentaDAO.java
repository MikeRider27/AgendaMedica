/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.VentaDTO;
import DTO.PedidoDetDTO;
import GENERICOS.Conexion;
import INT.PedidoINT;
import INT.VentaINT;
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
public class VentaDAO implements VentaINT {

    private String consultaSQL;
    private PreparedStatement ps, psDet;
    private int filaAfectada;
    private ResultSet rs;
    private Integer pk_generada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

    @Override
    public boolean agregar(List<VentaDTO> dto) {
        try {
            consultaSQL = "INSERT INTO public.ventas(\n"
                    + "            id_funcionario, id_cliente, obs, fecha)\n"
                    + "    VALUES (?, ?, ?, ?)";
            ps = bd.prepareStatement(consultaSQL, Statement.RETURN_GENERATED_KEYS);
            for (VentaDTO dtoAsignado : dto) {
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

                    consultaSQL = "INSERT INTO public.ventadet(\n"
                            + "            id_venta, id_servicio, cantidad)\n"
                            + "    VALUES (?, ?, ?)";
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
            Logger.getLogger(VentaDTO.class.getName()).log(Level.SEVERE, null, e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(VentaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
    }

    @Override
    public boolean modificar(List<VentaDTO> dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean eliminar(List<VentaDTO> dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<List<VentaDTO>> consultarTodos(List<VentaDTO> dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<List<VentaDTO>> consultarSegunFiltro(List<VentaDTO> dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<VentaDTO> consultarTodosSinFiltro(VentaDTO dto) {
        try {
            ArrayList<VentaDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT ve.id_venta, \n"
                    + "ve.id_funcionario, pe.nombre01_persona as nombrefuncionario,\n"
                    + "ve.id_cliente, per.nombre01_persona as nombrecliente,\n"
                    + "ve.obs, \n"
                    + "ve.fecha\n"
                    + "FROM public.ventas ve\n"
                    + "join funcionarios f on f.id_funcionario = ve.id_funcionario\n"
                    + "join personas pe on pe.id_persona = f.id_persona\n"
                    + "join clientes c on c.id_cliente = ve.id_cliente\n"
                    + "join personas per on per.id_persona = c.id_persona\n"
                    + "where ve.estado=TRUE";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new VentaDTO(rs.getInt("id_venta"), rs.getInt("id_funcionario"), rs.getInt("id_cliente"),
                        rs.getString("obs"), rs.getString("fecha"), rs.getString("nombrefuncionario"), rs.getString("nombrecliente")));
            }
            return lista;
        } catch (Exception e) {
            System.out.println("Error del listado venta  " + e);
            return null;
        }
    }

    @Override
    public List<VentaDTO> consultarTodosConFiltro(VentaDTO dto) {
        try {
            ArrayList<VentaDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT vd.id_venta, \n"
                    + "vd.id_servicio, \n"
                    + "ser.servi_descr as descripcion,\n"
                    + "ser.costo as costo, \n"
                    + "vd.cantidad as cantidad, (costo * cantidad) as total\n"
                    + "FROM ventadet vd \n"
                    + "join servicios ser on ser.id_servicio=vd.id_servicio\n"
                    + "where vd.id_venta=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId_pedido());
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new VentaDTO(rs.getInt("id_venta"), rs.getInt("id_servicio"), rs.getInt("cantidad"), rs.getInt("total"),
                        rs.getString("descripcion")));
            }
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar datos del venta para con servicios  " + e);
            return null;
        }
    }

    @Override
    public boolean anular(VentaDTO dto) {
        try {
            consultaSQL = "UPDATE public.ventas\n"
                    + "   SET estado=FALSE\n"
                    + " WHERE id_venta=?";
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
                Logger.getLogger(VentaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
    }

}
