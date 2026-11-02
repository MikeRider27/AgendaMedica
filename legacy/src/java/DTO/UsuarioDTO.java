package DTO;

public class UsuarioDTO {

    private Integer id_usuario;
    private String nombre_usuario;
    private String clave_usuario;
    private Integer id_rol;
    private String rol_descripcion;
    private boolean usua_anulado;
    private String usua_sesionid;
    private String usua_sesiontime;
    private Integer id_personal;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public UsuarioDTO(Integer id_usuario, String nombre_usuario, String clave_usuario, String rol_descripcion) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.clave_usuario = clave_usuario;
        this.rol_descripcion = rol_descripcion;
    }

    public UsuarioDTO(Integer id_usuario, String nombre_usuario, String clave_usuario, Integer id_rol, String rol_descripcion) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.clave_usuario = clave_usuario;
        this.id_rol = id_rol;
        this.rol_descripcion = rol_descripcion;
    }

    public UsuarioDTO(Integer id_usuario, String nombre_usuario) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
    }

    public boolean isUsua_anulado() {
        return usua_anulado;
    }

    public void setUsua_anulado(boolean usua_anulado) {
        this.usua_anulado = usua_anulado;
    }

    public String getUsua_sesionid() {
        return usua_sesionid;
    }

    public void setUsua_sesionid(String usua_sesionid) {
        this.usua_sesionid = usua_sesionid;
    }

    public String getUsua_sesiontime() {
        return usua_sesiontime;
    }

    public void setUsua_sesiontime(String usua_sesiontime) {
        this.usua_sesiontime = usua_sesiontime;
    }

    public Integer getId_personal() {
        return id_personal;
    }

    public void setId_personal(Integer id_personal) {
        this.id_personal = id_personal;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getClave_usuario() {
        return clave_usuario;
    }

    public void setClave_usuario(String clave_usuario) {
        this.clave_usuario = clave_usuario;
    }

    public Integer getId_rol() {
        return id_rol;
    }

    public void setId_rol(Integer id_rol) {
        this.id_rol = id_rol;
    }

    public String getRol_descripcion() {
        return rol_descripcion;
    }

    public void setRol_descripcion(String rol_descripcion) {
        this.rol_descripcion = rol_descripcion;
    }

}
