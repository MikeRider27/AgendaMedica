
package DTO;


public class TurnoDTO {
    private Integer id;
    private  String descripcion;
    private Integer cupo;

    public TurnoDTO() {
    }

    public TurnoDTO(Integer id, String descripcion, Integer cupo) {
        this.id = id;
        this.descripcion = descripcion;
        this.cupo = cupo;
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

    public Integer getCupo() {
        return cupo;
    }

    public void setCupo(Integer cupo) {
        this.cupo = cupo;
    }

    
}
