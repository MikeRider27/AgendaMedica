
package DTO;


public class CiudadDTO {
    /* Estas variables son creadas a partir de los campos de una tabla*/
    private Integer id;
    private String descripcion;
    

    public CiudadDTO() {
    }

    public CiudadDTO(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
 
}
