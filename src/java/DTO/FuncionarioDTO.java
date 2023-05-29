package DTO;

public class FuncionarioDTO {

    private Integer id, id_persona, id_tcargo, id_espec, id_subcargo;
    private String nombre_persona, ci_persona, desc_tcargo, desc_espec, desc_sucargo;

    public FuncionarioDTO() {
    }

    public FuncionarioDTO(Integer id, Integer id_persona, Integer id_tcargo, Integer id_espec, Integer id_subcargo, String nombre_persona, String ci_persona, String desc_tcargo, String desc_espec, String desc_sucargo) {
        this.id = id;
        this.id_persona = id_persona;
        this.id_tcargo = id_tcargo;
        this.id_espec = id_espec;
        this.id_subcargo = id_subcargo;
        this.nombre_persona = nombre_persona;
        this.ci_persona = ci_persona;
        this.desc_tcargo = desc_tcargo;
        this.desc_espec = desc_espec;
        this.desc_sucargo = desc_sucargo;
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

    public Integer getId_tcargo() {
        return id_tcargo;
    }

    public void setId_tcargo(Integer id_tcargo) {
        this.id_tcargo = id_tcargo;
    }

    public Integer getId_espec() {
        return id_espec;
    }

    public void setId_espec(Integer id_espec) {
        this.id_espec = id_espec;
    }

    public Integer getId_subcargo() {
        return id_subcargo;
    }

    public void setId_subcargo(Integer id_subcargo) {
        this.id_subcargo = id_subcargo;
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

    public String getDesc_tcargo() {
        return desc_tcargo;
    }

    public void setDesc_tcargo(String desc_tcargo) {
        this.desc_tcargo = desc_tcargo;
    }

    public String getDesc_espec() {
        return desc_espec;
    }

    public void setDesc_espec(String desc_espec) {
        this.desc_espec = desc_espec;
    }

    public String getDesc_sucargo() {
        return desc_sucargo;
    }

    public void setDesc_sucargo(String desc_sucargo) {
        this.desc_sucargo = desc_sucargo;
    }

}
