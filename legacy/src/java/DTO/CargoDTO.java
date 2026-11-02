
package DTO;


public class CargoDTO {
    private Integer id;
    private String cargo;
    
    /************** Aqui van dos constructores **************/
    public CargoDTO(Integer id, String cargo){
        this.id = id;
        this.cargo = cargo;
    }
    /*Este segundo constructor inicializa con los valores por defecto*/
    public CargoDTO(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    /******************************************/
    
}
