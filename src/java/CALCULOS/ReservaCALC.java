/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CALCULOS;

import DAO.EspecialidadDAO;
import DAO.ReservaDAO;
import DTO.EspecialidadDTO;
import DTO.ReservaDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juan
 */
public class ReservaCALC {
    
    ReservaDTO dto = new ReservaDTO();
    EspecialidadDTO dtoEspe = new EspecialidadDTO();
    EspecialidadDAO daoEspe = new EspecialidadDAO();
    ReservaDAO dao = new ReservaDAO();
    
    Integer cupo_actual, cupo_restante, restacupos;
    
    public int RecuperarCupo(int idespecialidad) {
        dto.setId_espec(idespecialidad);
        for (ReservaDTO r : dao.consultarCupos(dto)) {
            //System.out.println(r.getCupoactual());
            cupo_actual = r.getCuporestante();
            break;
        }
        return cupo_actual;        
    }

    public int RecuperarCupoRestante(int idespecialidad) {
        dto.setId_espec(idespecialidad);
        System.out.println("el idespecialidad es "+idespecialidad);
        for (ReservaDTO r : dao.consultarCuposEspe(dto)) {
            //System.out.println(r.getCupoactual());
            cupo_restante = r.getCupoactual();
            break;
        }
        return cupo_restante;        
    }

    public void MostrarCupoActual() {
        System.out.println("Mostrar cupo actual " + cupo_actual);
    }

    public void MostrarCupoRestante() {
        System.out.println("Mostrar cupo restante " + cupo_restante);
    }

    public int CalcularCupos() {
        
        restacupos = cupo_restante - cupo_actual;
        return restacupos;
    }

    public void MostrarCuposDisponibles() {
        System.out.println("Mostrar cupo disponible " + restacupos);
    }
}
