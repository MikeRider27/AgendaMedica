package DTO;

import java.text.SimpleDateFormat;

public class PersonaDTO {

    private Integer id, id_ecivil, id_ciudad, id_nac, id_sexo;
    private String nombre1, nombre2, ape1, ape2, ci, dir, tel, email, des_ecivil, desc_ciudad, desc_nac, desc_sex, fecha;

    public PersonaDTO() {
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

    public PersonaDTO(Integer id, Integer id_ecivil, Integer id_ciudad, Integer id_nac, Integer id_sexo, String nombre1, String nombre2, String ape1, String ape2, String ci, String dir, String tel, String email, String des_ecivil, String desc_ciudad, String desc_nac, String desc_sex, String fecha) {
        this.id = id;
        this.id_ecivil = id_ecivil;
        this.id_ciudad = id_ciudad;
        this.id_nac = id_nac;
        this.id_sexo = id_sexo;
        this.nombre1 = nombre1;
        this.nombre2 = nombre2;
        this.ape1 = ape1;
        this.ape2 = ape2;
        this.ci = ci;
        this.dir = dir;
        this.tel = tel;
        this.email = email;
        this.des_ecivil = des_ecivil;
        this.desc_ciudad = desc_ciudad;
        this.desc_nac = desc_nac;
        this.desc_sex = desc_sex;
        this.fecha = fecha;
    }

    public PersonaDTO(Integer id, String nombre1, String nombre2, String ape1, String ape2, String ci, String dir, String tel, String email, String des_ecivil, String desc_ciudad, String desc_sex) {
        this.id = id;
        this.nombre1 = nombre1;
        this.nombre2 = nombre2;
        this.ape1 = ape1;
        this.ape2 = ape2;
        this.ci = ci;
        this.dir = dir;
        this.tel = tel;
        this.email = email;
        this.des_ecivil = des_ecivil;
        this.desc_ciudad = desc_ciudad;
        this.desc_sex = desc_sex;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_ecivil() {
        return id_ecivil;
    }

    public void setId_ecivil(Integer id_ecivil) {
        this.id_ecivil = id_ecivil;
    }

    public Integer getId_ciudad() {
        return id_ciudad;
    }

    public void setId_ciudad(Integer id_ciudad) {
        this.id_ciudad = id_ciudad;
    }

    public Integer getId_nac() {
        return id_nac;
    }

    public void setId_nac(Integer id_nac) {
        this.id_nac = id_nac;
    }

    public Integer getId_sexo() {
        return id_sexo;
    }

    public void setId_sexo(Integer id_sexo) {
        this.id_sexo = id_sexo;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getApe1() {
        return ape1;
    }

    public void setApe1(String ape1) {
        this.ape1 = ape1;
    }

    public String getApe2() {
        return ape2;
    }

    public void setApe2(String ape2) {
        this.ape2 = ape2;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDes_ecivil() {
        return des_ecivil;
    }

    public void setDes_ecivil(String des_ecivil) {
        this.des_ecivil = des_ecivil;
    }

    public String getDesc_ciudad() {
        return desc_ciudad;
    }

    public void setDesc_ciudad(String desc_ciudad) {
        this.desc_ciudad = desc_ciudad;
    }

    public String getDesc_nac() {
        return desc_nac;
    }

    public void setDesc_nac(String desc_nac) {
        this.desc_nac = desc_nac;
    }

    public String getDesc_sex() {
        return desc_sex;
    }

    public void setDesc_sex(String desc_sex) {
        this.desc_sex = desc_sex;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
