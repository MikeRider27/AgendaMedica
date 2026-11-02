
package DTO;

import java.util.List;

/*
id_pedido integer NOT NULL,
  id_producto integer NOT NULL,
  cantidad integer
*/
public class PedidoDetDTO {
    private Integer id_producto,cantidad;
    

    public PedidoDetDTO() {
    }

    public PedidoDetDTO(Integer id_producto, Integer cantidad) {
        super();
        this.id_producto = id_producto;
        this.cantidad = cantidad;
    }

    public Integer getId_producto() {
        return id_producto;
    }

    public void setId_producto(Integer id_producto) {
        this.id_producto = id_producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    
    
}
