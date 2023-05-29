/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROL;

import CALCULOS.ReservaCALC;
import DAO.ReservaDAO;
import DTO.ReservaDTO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author juan
 */
public class ReservaSRV extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("Llega a ReservaSRV");
        ReservaDTO dto = new ReservaDTO();
        ReservaDAO dao = new ReservaDAO();
        ReservaCALC calculo = new ReservaCALC();
        /**
         * "operacion": banderaOperacion, "id": $('#txtId').val(), "fecha":
         * $('#txtfecha').val(), "hora": $('#txthora').val(), "comboturno":
         * $('#combo_turno').val(), "idfuncionario":
         * $('#txtid_funcionario').val(), "nombrefuncionario":
         * $('#txtnombre_funcionario').val(), "idcliente":
         * $('#txtid_cliente').val(), "nombrecliente":
         * $('#txtnombre_cliente').val(), "ci": $('#txtci_cliente').val(),
         * "combotiporeserva": $('#combo_tiporeserva').val(),
         * "comboestadoreserva": $('#combo_estadoreserva').val(), "combodia":
         * $('#combo_dia').val(), "idmedico": $('#txtid_medico').val(),
         * "nombremedico": $('#txtnombre_medico').val(), "comboespecialidad":
         * $('#combo_especialidad').val(), "cupo": 5, "descripcion": 5,
         * "cupoactual": $('#txtactual_cupo').val()
         */
        PrintWriter salida = response.getWriter();
        Integer id = Integer.parseInt((request.getParameter("id").isEmpty() ? "0" : request.getParameter("id")));
        Integer operacion = Integer.parseInt((request.getParameter("operacion").isEmpty() ? "0" : request.getParameter("operacion")));
        String fecha = request.getParameter("fecha").trim();
        String hora = request.getParameter("hora").trim();
        Integer comboturno = Integer.parseInt((request.getParameter("comboturno").isEmpty() ? "0" : request.getParameter("comboturno")));
        Integer idfuncionario = Integer.parseInt((request.getParameter("idfuncionario").isEmpty() ? "0" : request.getParameter("idfuncionario")));

        Integer idcliente = Integer.parseInt((request.getParameter("idcliente").isEmpty() ? "0" : request.getParameter("idcliente")));

        Integer combotiporeserva = Integer.parseInt((request.getParameter("combotiporeserva").isEmpty() ? "0" : request.getParameter("combotiporeserva")));
        Integer comboestadoreserva = Integer.parseInt((request.getParameter("comboestadoreserva").isEmpty() ? "0" : request.getParameter("comboestadoreserva")));
        Integer combodia = Integer.parseInt((request.getParameter("combodia").isEmpty() ? "0" : request.getParameter("combodia")));
        Integer idmedico = Integer.parseInt((request.getParameter("idmedico").isEmpty() ? "0" : request.getParameter("idmedico")));

        Integer comboespecialidad = Integer.parseInt((request.getParameter("comboespecialidad").isEmpty() ? "0" : request.getParameter("comboespecialidad")));
        Integer cupoactual = Integer.parseInt((request.getParameter("cupoactual").isEmpty() ? "0" : request.getParameter("cupoactual")));
        Integer cuporestante = Integer.parseInt((request.getParameter("cuporestante").isEmpty() ? "0" : request.getParameter("cuporestante")));

        //System.out.println("comoboespe " + comboespecialidad);
        switch (operacion) {
            case 1://Cargar la tabla
                response.setContentType("application/json");
                salida.print(new Gson().toJson(dao.consultarTodos(dto)));
                salida.close();
                break;
            case 2: //Cargar tabla segun filtro
                response.setContentType("application/json");
                dto.setId(id);
                salida.print(new Gson().toJson(dao.consultarSegunFiltro(dto)));
                ///Investigando
//               calculo.RecuperarCupo(id);
//               calculo.RecuperarCupoRestante(id);
//               calculo.MostrarCupoActual();
//               calculo.MostrarCupoRestante();
//               calculo.CalcularCupos();
//               calculo.MostrarCuposDisponibles();
//               //salida.print(new Gson().toJson(calculo.CalcularCupos()));
                salida.close();
                break;
            case 3://Insertar datos
                //comboespecialidad = Integer.parseInt((request.getParameter("comboespecialidad").isEmpty() ? "0" : request.getParameter("comboespecialidad")));
                //System.out.println("comoboespe en caso 3 == " + comboespecialidad);
                cupoactual = calculo.RecuperarCupo(comboespecialidad);
                System.out.println("la especialidad es "+comboespecialidad);
                calculo.RecuperarCupoRestante(comboespecialidad);
                //calculo.MostrarCupoActual();
                //calculo.MostrarCupoRestante();
                cuporestante = calculo.CalcularCupos();
                System.out.println("Cupo actual = "+cupoactual + ", cupo restantes = " + cuporestante);
                //calculo.MostrarCuposDisponibles();
                if (cupoactual <= cuporestante && cupoactual >= 0) {
                    //dto.setId(id);
                    dto.setFecha(dto.convertir_fecha(fecha));
                    dto.setHora(hora);
                    dto.setId_turno(comboturno);
                    dto.setId_funcionario_fun(idfuncionario);

                    dto.setId_cliente(idcliente);

                    dto.setId_tiporeserva(combotiporeserva);
                    dto.setId_estadoreserva(comboestadoreserva);
                    dto.setId_dia(combodia);
                    dto.setId_funcionario_med(idmedico);

                    dto.setId_espec(comboespecialidad);
                    dto.setCupoactual(cupoactual + 1);
                    dao.agregar(dto);
                } else {
                    //No hay cupos disponibles
                    System.out.println("No hay cupos disponibles");
                }
                break;
            case 4://Modificar
                dto.setId(id);
                    dto.setFecha(dto.convertir_fecha(fecha));
                    dto.setHora(hora);
                    dto.setId_turno(comboturno);
                    dto.setId_funcionario_fun(idfuncionario);
                    dto.setId_cliente(idcliente);
                    dto.setId_tiporeserva(combotiporeserva);
                    dto.setId_estadoreserva(comboestadoreserva);
                    dto.setId_dia(combodia);
                    dto.setId_funcionario_med(idmedico);
                    dto.setId_espec(comboespecialidad);
                    //dto.setCupoactual(cupoactual);
                    dao.modificar(dto);
                break;
            case 5://eliminar
                dto.setId(id);
                dao.eliminar(dto);
                break;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
