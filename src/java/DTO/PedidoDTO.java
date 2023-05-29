/*
 * id_pedido integer NOT NULL DEFAULT nextval('pedidos_id_pedido_seq'::regclass),
  id_funcionario integer,
  id_cliente integer,
  obs character varying
 */
package DTO;

import java.util.List;

public class PedidoDTO {

    private Integer id_pedido, id_funcionario, id_cliente, id_producto,pro_pv,pd_cant,total;
    private String obs, fecha, nombrefuncionario, nombrecliente,pro_descrip;
    private List<PedidoDetDTO> detalle;

    public String convertir_fecha(String fecha) {
        //Formato actual 25/08/2016 DD/MM/YYYY
        String dia = "", mes = "", ano = "";
        dia = fecha.substring(0, 2);
        mes = fecha.substring(3, 5);
        ano = fecha.substring(6, 10);
        fecha = ano + "-" + mes + "-" + dia;// Fecha estadounidense

        return fecha;

    }

    public PedidoDTO() {
    }

    public PedidoDTO(Integer id_pedido, Integer id_funcionario, Integer id_cliente, String obs, String fecha, List<PedidoDetDTO> detalle) {
        this.id_pedido = id_pedido;
        this.id_funcionario = id_funcionario;
        this.id_cliente = id_cliente;
        this.obs = obs;
        this.fecha = fecha;
        this.detalle = detalle;
    }

    public PedidoDTO(Integer id_pedido, Integer id_funcionario, Integer id_cliente, String obs, String fecha, String nombrefuncionario, String nombrecliente) {
        this.id_pedido = id_pedido;
        this.id_funcionario = id_funcionario;
        this.id_cliente = id_cliente;
        this.obs = obs;
        this.fecha = fecha;
        this.nombrefuncionario = nombrefuncionario;
        this.nombrecliente = nombrecliente;
    }

    public PedidoDTO(Integer id_pedido, Integer id_producto, Integer pd_cant, Integer total, String pro_descrip) {
        this.id_pedido = id_pedido;
        this.id_producto = id_producto;
        this.pd_cant = pd_cant;
        this.total = total;
        this.pro_descrip = pro_descrip;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    

    public Integer getId_producto() {
        return id_producto;
    }

    public void setId_producto(Integer id_producto) {
        this.id_producto = id_producto;
    }

    public Integer getPro_pv() {
        return pro_pv;
    }

    public void setPro_pv(Integer pro_pv) {
        this.pro_pv = pro_pv;
    }

    public Integer getPd_cant() {
        return pd_cant;
    }

    public void setPd_cant(Integer pd_cant) {
        this.pd_cant = pd_cant;
    }

    public String getNombrefuncionario() {
        return nombrefuncionario;
    }

    public void setNombrefuncionario(String nombrefuncionario) {
        this.nombrefuncionario = nombrefuncionario;
    }

    public String getNombrecliente() {
        return nombrecliente;
    }

    public void setNombrecliente(String nombrecliente) {
        this.nombrecliente = nombrecliente;
    }

    public String getPro_descrip() {
        return pro_descrip;
    }

    public void setPro_descrip(String pro_descrip) {
        this.pro_descrip = pro_descrip;
    }

    public Integer getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(Integer id_pedido) {
        this.id_pedido = id_pedido;
    }

    public Integer getId_funcionario() {
        return id_funcionario;
    }

    public void setId_funcionario(Integer id_funcionario) {
        this.id_funcionario = id_funcionario;
    }

    public Integer getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Integer id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<PedidoDetDTO> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<PedidoDetDTO> detalle) {
        this.detalle = detalle;
    }

}
