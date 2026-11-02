/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CALCULOS;

import DAO.ReservaDAO;
import DTO.EnviarCorreoDTO;
import DTO.ReservaDTO;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class CalcularConfirmados {

    public String vfechareserva, vfechaActual, vcorreo;
    public String nombre_cliente, ci_cliente, estado, nombre_medico, especialidad;
    public Integer id_reserva;
    ObtenerFechaCALC f = new ObtenerFechaCALC();
    EnviarCorreoDTO env = new EnviarCorreoDTO();
    EnviarCorreo prg = new EnviarCorreo();
    ReservaDTO dto = new ReservaDTO();
    ReservaDAO dao = new ReservaDAO();

    public CalcularConfirmados() {
    }

    public String RecuperarFechaActual() {
        java.util.Date fecha = null;
        fecha = new java.util.Date();
        //System.out.println(fecha);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //System.out.println("FechaActual: " + dateFormat.format(fecha));
        vfechaActual = dateFormat.format(fecha);
        return vfechaActual;
    }

    public void RecuperarFechaReserva() {
        List<ReservaDTO> lista = dao.consultarEmailFechatodos(dto);
        for (ReservaDTO s : lista) {
            //System.out.println("La fecha es " + s.getFecha() + ",y el correo " + s.getEmail());
            vfechareserva = s.getFecha();
            vcorreo = s.getEmail();
            RecibirFechaReserva(vfechareserva, vcorreo);
        }
    }

    public void RecibirFechaReserva(String fecharecib, String correo) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechareserva = fecharecib, correoreserva = correo;
        String fechaactual = RecuperarFechaActual();
        if (fechareserva.equals(fechaactual)) {
            // System.out.println("enviar al correo " + correo);
            env.setCorreo(correo);
            
            prg.EjecutarEnvio(env);
        } else {
            System.out.println("No hay coincidencias");
        }
    }

    public static void main(String[] args) {
        CalcularConfirmados calc = new CalcularConfirmados();
        calc.RecuperarFechaReserva();
    }
}
