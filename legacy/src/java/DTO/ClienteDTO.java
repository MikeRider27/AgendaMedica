/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
id_cliente integer NOT NULL DEFAULT nextval('clientes_id_cliente_seq_1'::regclass),
  nroseguro_cliente character varying NOT NULL,
  id_persona integer NOT NULL
 */
public class ClienteDTO {
    private Integer id,id_persona;
    private String nombre_persona, ci_persona,nro_seguro;

    public ClienteDTO() {
    }

    public ClienteDTO(Integer id, Integer id_persona, String nombre_persona, String ci_persona, String nro_seguro) {
        this.id = id;
        this.id_persona = id_persona;
        this.nombre_persona = nombre_persona;
        this.ci_persona = ci_persona;
        this.nro_seguro = nro_seguro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_persona() {
        return id_persona;
    }

    public void setId_persona(Integer id_persona) {
        this.id_persona = id_persona;
    }

    public String getNombre_persona() {
        return nombre_persona;
    }

    public void setNombre_persona(String nombre_persona) {
        this.nombre_persona = nombre_persona;
    }

    public String getCi_persona() {
        return ci_persona;
    }

    public void setCi_persona(String ci_persona) {
        this.ci_persona = ci_persona;
    }

    public String getNro_seguro() {
        return nro_seguro;
    }

    public void setNro_seguro(String nro_seguro) {
        this.nro_seguro = nro_seguro;
    }

}
