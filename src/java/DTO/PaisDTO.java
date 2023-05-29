
package DTO;


public class PaisDTO {
    private Integer vpais_id;
    private String vpais_descripcion;
    /**Los constructores**/
    public PaisDTO() {
    }

    public PaisDTO(Integer vpais_id, String vpais_descripcion) {
        this.vpais_id = vpais_id;
        this.vpais_descripcion = vpais_descripcion;
    }
    
    /*Los getter y setters*/
    public Integer getVpais_id() {
        return vpais_id;
    }

    public void setVpais_id(Integer vpais_id) {
        this.vpais_id = vpais_id;
    }

    public String getVpais_descripcion() {
        return vpais_descripcion;
    }

    public void setVpais_descripcion(String vpais_descripcion) {
        this.vpais_descripcion = vpais_descripcion;
    }
    
}
