/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROL;

import CALCULOS.ObtenerFechaCALC;
import DAO.ConsultaDAO;
import DTO.ConsultaDTO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author juan
 */
public class ConsultaSRV extends HttpServlet {

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
        System.out.println("Llegó a ConsultaSRV");
        PrintWriter salida = response.getWriter();
        ConsultaDTO dto = new ConsultaDTO();
        ConsultaDAO dao = new ConsultaDAO();
        ObtenerFechaCALC fecha = new ObtenerFechaCALC();
        Integer operacion = Integer.parseInt(request.getParameter("operacion"));
        Integer id = Integer.parseInt((request.getParameter("id").isEmpty() ? "0" : request.getParameter("id")));
        Integer idfuncionario = Integer.parseInt((request.getParameter("idfuncionario").isEmpty() ? "0" : request.getParameter("idfuncionario")));
        Integer idreserva = Integer.parseInt((request.getParameter("idreserva").isEmpty() ? "0" : request.getParameter("idreserva")));
        String fechavista = fecha.PrepararFecha();
        String obs = (request.getParameter("obs").trim()) == null ? "vacio" : request.getParameter("obs");

        switch (operacion) {
            case 1://Listar sin filtro
                response.setContentType("application/json");
                //dto.setId(id);
                salida.print(new Gson().toJson(dao.consultarTodos(dto)));
                //System.out.println("Lista " + new Gson().toJson(dao.consultarTodos(dto)));
                salida.close();
                break;
            case 2://Listar con filtro
                response.setContentType("application/json");
                dto.setId(id);
                salida.print(new Gson().toJson(dao.consultarSegunFiltro(dto)));
                salida.close();
                break;
            case 3://Obtener fecha actual del sistema
                response.setContentType("application/json");
                salida.print(new Gson().toJson(fechavista));
                //System.out.println("Fecha actual es " + new Gson().toJson(fechavista));
                //fecha.MostrarFechaActual();
                salida.close();
                break;
            case 4://Agregar
                dto.setId_reserva(idreserva);
                dto.setId_funcionario(idfuncionario);
                dto.setObs(obs);
                dto.setFecha(fecha.PrepararFechaUSA());
                dao.agregar(dto);
                break;
            case 5: //Modificar
                dto.setId(id);
                dto.setId_reserva(idreserva);
                dto.setId_funcionario(idfuncionario);
                dto.setObs(obs);
                dto.setFecha(fecha.PrepararFechaUSA());
                dao.modificar(dto);
                break;
            case 6: //Eliminar
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
