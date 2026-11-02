package DTO;

public class ConsultaDTO {

    private Integer id, id_funcionario, id_reserva;
    private String fecha, desc_funcionario, nombre_cliente, apellido_cliente, ci_cliente, especialidad, medico, obs;

    public ConsultaDTO() {
    }

    public String convertir_fecha(String fecha) {
        //Formato actual 25/08/2016 DD/MM/YYYY
        String dia = "", mes = "", ano = "";
        dia = fecha.substring(0, 2);
        mes = fecha.substring(3, 5);
        ano = fecha.substring(6, 10);
        fecha = ano + "-" + mes + "-" + dia;// Fecha estadounidense

        return fecha;

    }

    public ConsultaDTO(Integer id, Integer id_funcionario, Integer id_reserva, String fecha, String desc_funcionario, String nombre_cliente, String apellido_cliente, String ci_cliente, String especialidad, String medico, String obs) {
        this.id = id;
        this.id_funcionario = id_funcionario;
        this.id_reserva = id_reserva;
        this.fecha = fecha;
        this.desc_funcionario = desc_funcionario;
        this.nombre_cliente = nombre_cliente;
        this.apellido_cliente = apellido_cliente;
        this.ci_cliente = ci_cliente;
        this.especialidad = especialidad;
        this.medico = medico;
        this.obs = obs;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_funcionario() {
        return id_funcionario;
    }

    public void setId_funcionario(Integer id_funcionario) {
        this.id_funcionario = id_funcionario;
    }

    public Integer getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(Integer id_reserva) {
        this.id_reserva = id_reserva;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDesc_funcionario() {
        return desc_funcionario;
    }

    public void setDesc_funcionario(String desc_funcionario) {
        this.desc_funcionario = desc_funcionario;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getApellido_cliente() {
        return apellido_cliente;
    }

    public void setApellido_cliente(String apellido_cliente) {
        this.apellido_cliente = apellido_cliente;
    }

    public String getCi_cliente() {
        return ci_cliente;
    }

    public void setCi_cliente(String ci_cliente) {
        this.ci_cliente = ci_cliente;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

}
