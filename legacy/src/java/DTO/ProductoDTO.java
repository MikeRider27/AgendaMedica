package DTO;

public class ProductoDTO {
    /*id_producto serial NOT NULL,
  descripcion_producto character varying(100),
  preciocompra_producto integer,
  precioventa integer,
     */
    private Integer id_producto;
    private String descripcion_producto;
    private Integer preciocompra_producto;
    private Integer precioventa;

    public ProductoDTO() {
    }

    public ProductoDTO(Integer id_producto, String descripcion_producto, Integer preciocompra_producto, Integer precioventa) {
        this.id_producto = id_producto;
        this.descripcion_producto = descripcion_producto;
        this.preciocompra_producto = preciocompra_producto;
        this.precioventa = precioventa;
    }

    public Integer getId_producto() {
        return id_producto;
    }

    public void setId_producto(Integer id_producto) {
        this.id_producto = id_producto;
    }

    public String getDescripcion_producto() {
        return descripcion_producto;
    }

    public void setDescripcion_producto(String descripcion_producto) {
        this.descripcion_producto = descripcion_producto;
    }

    public Integer getPreciocompra_producto() {
        return preciocompra_producto;
    }

    public void setPreciocompra_producto(Integer preciocompra_producto) {
        this.preciocompra_producto = preciocompra_producto;
    }

    public Integer getPrecioventa() {
        return precioventa;
    }

    public void setPrecioventa(Integer precioventa) {
        this.precioventa = precioventa;
    }
    
    
}
