/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.ProductoDTO;
import GENERICOS.Conexion;
import INT.ProductoINT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juan
 */
public class ProductoDAO implements ProductoINT {

    String consultaSQL;
    PreparedStatement ps;
    private int filaAfectada;
//    private Conexion bd = Conexion.getInstance();
    Connection bd = Conexion.obtenerConexion();

//    public ProductoDAO() {
//        bd = new Conexion();
//    }

    @Override
    public boolean agregar(ProductoDTO dto) {
        try {
            consultaSQL = "INSERT INTO public.productos(\n"
                    + "            descripcion_producto, preciocompra_producto, precioventa)\n"
                    + "    VALUES (?, ?, ?)";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getDescripcion_producto());
            ps.setInt(2, dto.getPreciocompra_producto());
            ps.setInt(3, dto.getPrecioventa());
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
            return false;
        }finally{
//           bd.delInstance();
        }
    }

    @Override
    public boolean modificar(ProductoDTO dto) {
        try {
            consultaSQL = "UPDATE public.productos\n"
                    + "   SET descripcion_producto=?, preciocompra_producto=?, precioventa=?      \n"
                    + " WHERE id_producto=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setString(1, dto.getDescripcion_producto());
            ps.setInt(2, dto.getPreciocompra_producto());
            ps.setInt(3, dto.getPrecioventa());
            ps.setInt(4, dto.getId_producto());
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
            return false;
        }finally{
//           bd.delInstance();
        }
    }

    @Override
    public boolean eliminar(ProductoDTO dto) {
        try {
            consultaSQL = "DELETE FROM public.productos\n"
                    + " WHERE id_producto=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId_producto());
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
            return false;
        }finally{
//           bd.delInstance();
        }
    }

    @Override
    public List<ProductoDTO> consultarTodos(ProductoDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ProductoDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_producto, descripcion_producto, preciocompra_producto, precioventa\n"
                    + "  FROM productos";
            ps = bd.prepareStatement(consultaSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new ProductoDTO(rs.getInt("id_producto"), rs.getString("descripcion_producto"), rs.getInt("preciocompra_producto"), rs.getInt("precioventa")));
            }
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar " + e);
            return null;
        }
    }

    @Override
    public List<ProductoDTO> consultarSegunFiltro(ProductoDTO dto) {
        try {
            ResultSet rs;
            ArrayList<ProductoDTO> lista = new ArrayList<>();
            consultaSQL = "SELECT id_producto, descripcion_producto, preciocompra_producto, precioventa\n"
                    + "  FROM productos where id_producto=?";
            ps = bd.prepareStatement(consultaSQL);
            ps.setInt(1, dto.getId_producto());
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new ProductoDTO(rs.getInt("id_producto"), rs.getString("descripcion_producto"), rs.getInt("preciocompra_producto"), rs.getInt("precioventa")));
            }
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error al listar " + e);
            return null;
        }finally{
//           bd.delInstance();
        }
    }

}
