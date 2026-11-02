
package DTO;


public class ReservaDTO {
    private Integer id,id_funcionario_fun;
    private Integer id_funcionario_med,id_cliente;
    private Integer id_tiporeserva,id_espec,id_dia;
    private Integer id_turno,id_estadoreserva;
    private String fecha,hora,desc_funcionario,desc_medico,desc_cliente,ci,ape_cliente,email;
    private String desc_reserva,desc_espec,desc_dia;
    private String desc_turno,desc_estadoreserva;
    private Integer cupoactual;
    private Integer cuporestante;
    private Integer resta;
    
     public String convertir_fecha(String fecha) {
        //Formato actual 25/08/2016 DD/MM/YYYY
        String dia = "", mes = "", ano = "";
        dia = fecha.substring(0, 2);
        mes = fecha.substring(3, 5);
        ano = fecha.substring(6, 10);
        fecha = ano + "-" + mes + "-" + dia;// Fecha estadounidense

        return fecha;

    }

    

    public ReservaDTO(Integer cuporestante) {
        this.cuporestante = cuporestante;
    }

    public ReservaDTO() {
    }

    public ReservaDTO(Integer id, Integer cupoactual) {
        this.id = id;
        this.cupoactual = cupoactual;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ReservaDTO(String fecha, String email) {
        this.fecha = fecha;
        this.email = email;
    }

    public ReservaDTO(Integer id, String desc_medico, String desc_cliente, String ci, String ape_cliente, String desc_espec) {
        this.id = id;
        this.desc_medico = desc_medico;
        this.desc_cliente = desc_cliente;
        this.ci = ci;
        this.ape_cliente = ape_cliente;
        this.desc_espec = desc_espec;
    }

    public ReservaDTO(Integer id, Integer id_funcionario_fun, Integer id_funcionario_med, Integer id_cliente, Integer id_tiporeserva, Integer id_espec, Integer id_dia, Integer id_turno, Integer id_estadoreserva, String fecha, String hora, String desc_funcionario, String desc_medico, String desc_cliente, String ci, String desc_reserva, String desc_espec, String desc_dia, String desc_turno, String desc_estadoreserva, Integer cupoactual, Integer resta) {
        this.id = id;
        this.id_funcionario_fun = id_funcionario_fun;
        this.id_funcionario_med = id_funcionario_med;
        this.id_cliente = id_cliente;
        this.id_tiporeserva = id_tiporeserva;
        this.id_espec = id_espec;
        this.id_dia = id_dia;
        this.id_turno = id_turno;
        this.id_estadoreserva = id_estadoreserva;
        this.fecha = fecha;
        this.hora = hora;
        this.desc_funcionario = desc_funcionario;
        this.desc_medico = desc_medico;
        this.desc_cliente = desc_cliente;
        this.ci = ci;
        this.desc_reserva = desc_reserva;
        this.desc_espec = desc_espec;
        this.desc_dia = desc_dia;
        this.desc_turno = desc_turno;
        this.desc_estadoreserva = desc_estadoreserva;
        this.cupoactual = cupoactual;
        this.resta = resta;
    }

    public ReservaDTO(Integer id, Integer id_funcionario_fun, Integer id_funcionario_med, Integer id_cliente, Integer id_tiporeserva, Integer id_espec, Integer id_dia, Integer id_turno, Integer id_estadoreserva, String fecha, String hora, String desc_funcionario, String desc_medico, String desc_cliente, String ci, String desc_reserva, String desc_espec, String desc_dia, String desc_turno, String desc_estadoreserva) {
        this.id = id;
        this.id_funcionario_fun = id_funcionario_fun;
        this.id_funcionario_med = id_funcionario_med;
        this.id_cliente = id_cliente;
        this.id_tiporeserva = id_tiporeserva;
        this.id_espec = id_espec;
        this.id_dia = id_dia;
        this.id_turno = id_turno;
        this.id_estadoreserva = id_estadoreserva;
        this.fecha = fecha;
        this.hora = hora;
        this.desc_funcionario = desc_funcionario;
        this.desc_medico = desc_medico;
        this.desc_cliente = desc_cliente;
        this.ci = ci;
        this.desc_reserva = desc_reserva;
        this.desc_espec = desc_espec;
        this.desc_dia = desc_dia;
        this.desc_turno = desc_turno;
        this.desc_estadoreserva = desc_estadoreserva;
    }

    public ReservaDTO(Integer id, Integer id_funcionario_fun, Integer id_funcionario_med, Integer id_cliente, Integer id_tiporeserva, Integer id_espec, Integer id_dia, Integer id_turno, Integer id_estadoreserva, String fecha, String hora, String desc_funcionario, String desc_medico, String desc_cliente, String ci, String desc_reserva, String desc_espec, String desc_dia, String desc_turno, String desc_estadoreserva, Integer cupoactual, Integer cuporestante, Integer resta) {
        this.id = id;
        this.id_funcionario_fun = id_funcionario_fun;
        this.id_funcionario_med = id_funcionario_med;
        this.id_cliente = id_cliente;
        this.id_tiporeserva = id_tiporeserva;
        this.id_espec = id_espec;
        this.id_dia = id_dia;
        this.id_turno = id_turno;
        this.id_estadoreserva = id_estadoreserva;
        this.fecha = fecha;
        this.hora = hora;
        this.desc_funcionario = desc_funcionario;
        this.desc_medico = desc_medico;
        this.desc_cliente = desc_cliente;
        this.ci = ci;
        this.desc_reserva = desc_reserva;
        this.desc_espec = desc_espec;
        this.desc_dia = desc_dia;
        this.desc_turno = desc_turno;
        this.desc_estadoreserva = desc_estadoreserva;
        this.cupoactual = cupoactual;
        this.cuporestante = cuporestante;
        this.resta = resta;
    }

    public ReservaDTO(Integer id, Integer id_funcionario_fun, Integer id_funcionario_med, Integer id_cliente, Integer id_tiporeserva, Integer id_espec, Integer id_dia, Integer id_turno, Integer id_estadoreserva, String fecha, String hora, String desc_funcionario, String desc_medico, String desc_cliente, String ci, String desc_reserva, String desc_espec, String desc_dia, String desc_turno, String desc_estadoreserva, Integer cupoactual) {
        this.id = id;
        this.id_funcionario_fun = id_funcionario_fun;
        this.id_funcionario_med = id_funcionario_med;
        this.id_cliente = id_cliente;
        this.id_tiporeserva = id_tiporeserva;
        this.id_espec = id_espec;
        this.id_dia = id_dia;
        this.id_turno = id_turno;
        this.id_estadoreserva = id_estadoreserva;
        this.fecha = fecha;
        this.hora = hora;
        this.desc_funcionario = desc_funcionario;
        this.desc_medico = desc_medico;
        this.desc_cliente = desc_cliente;
        this.ci = ci;
        this.desc_reserva = desc_reserva;
        this.desc_espec = desc_espec;
        this.desc_dia = desc_dia;
        this.desc_turno = desc_turno;
        this.desc_estadoreserva = desc_estadoreserva;
        this.cupoactual = cupoactual;
    }

    public String getApe_cliente() {
        return ape_cliente;
    }

    public void setApe_cliente(String ape_cliente) {
        this.ape_cliente = ape_cliente;
    }

    public Integer getResta() {
        return resta;
    }

    public void setResta(Integer resta) {
        this.resta = resta;
    }

    public Integer getCuporestante() {
        return cuporestante;
    }

    public void setCuporestante(Integer cuporestante) {
        this.cuporestante = cuporestante;
    }

    public Integer getCupoactual() {
        return cupoactual;
    }

    public void setCupoactual(Integer cupoactual) {
        this.cupoactual = cupoactual;
    }
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_funcionario_fun() {
        return id_funcionario_fun;
    }

    public void setId_funcionario_fun(Integer id_funcionario_fun) {
        this.id_funcionario_fun = id_funcionario_fun;
    }

    public Integer getId_funcionario_med() {
        return id_funcionario_med;
    }

    public void setId_funcionario_med(Integer id_funcionario_med) {
        this.id_funcionario_med = id_funcionario_med;
    }

    public Integer getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Integer id_cliente) {
        this.id_cliente = id_cliente;
    }

    public Integer getId_tiporeserva() {
        return id_tiporeserva;
    }

    public void setId_tiporeserva(Integer id_tiporeserva) {
        this.id_tiporeserva = id_tiporeserva;
    }

    public Integer getId_espec() {
        return id_espec;
    }

    public void setId_espec(Integer id_espec) {
        this.id_espec = id_espec;
    }

    public Integer getId_dia() {
        return id_dia;
    }

    public void setId_dia(Integer id_dia) {
        this.id_dia = id_dia;
    }

    public Integer getId_turno() {
        return id_turno;
    }

    public void setId_turno(Integer id_turno) {
        this.id_turno = id_turno;
    }

    public Integer getId_estadoreserva() {
        return id_estadoreserva;
    }

    public void setId_estadoreserva(Integer id_estadoreserva) {
        this.id_estadoreserva = id_estadoreserva;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDesc_funcionario() {
        return desc_funcionario;
    }

    public void setDesc_funcionario(String desc_funcionario) {
        this.desc_funcionario = desc_funcionario;
    }

    public String getDesc_medico() {
        return desc_medico;
    }

    public void setDesc_medico(String desc_medico) {
        this.desc_medico = desc_medico;
    }

    public String getDesc_cliente() {
        return desc_cliente;
    }

    public void setDesc_cliente(String desc_cliente) {
        this.desc_cliente = desc_cliente;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getDesc_reserva() {
        return desc_reserva;
    }

    public void setDesc_reserva(String desc_reserva) {
        this.desc_reserva = desc_reserva;
    }

    public String getDesc_espec() {
        return desc_espec;
    }

    public void setDesc_espec(String desc_espec) {
        this.desc_espec = desc_espec;
    }

    public String getDesc_dia() {
        return desc_dia;
    }

    public void setDesc_dia(String desc_dia) {
        this.desc_dia = desc_dia;
    }

    public String getDesc_turno() {
        return desc_turno;
    }

    public void setDesc_turno(String desc_turno) {
        this.desc_turno = desc_turno;
    }

    public String getDesc_estadoreserva() {
        return desc_estadoreserva;
    }

    public void setDesc_estadoreserva(String desc_estadoreserva) {
        this.desc_estadoreserva = desc_estadoreserva;
    }
    
}
