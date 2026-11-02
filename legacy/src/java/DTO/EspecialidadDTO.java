/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author juan
 */
public class EspecialidadDTO {
    private Integer id;
    private String descripcion;
    private Integer cupo;

    public EspecialidadDTO() {
    }

    public EspecialidadDTO(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public EspecialidadDTO(Integer id, String descripcion, Integer cupo) {
        this.id = id;
        this.descripcion = descripcion;
        this.cupo = cupo;
    }

    public Integer getCupo() {
        return cupo;
    }

    public void setCupo(Integer cupo) {
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
    
}
