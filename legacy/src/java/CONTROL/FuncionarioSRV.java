/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROL;

import DAO.CargoDAO;
import DAO.FuncionarioDAO;
import DTO.CargoDTO;
import DTO.FuncionarioDTO;
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
public class FuncionarioSRV extends HttpServlet {

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
        System.out.println("llega al FuncionarioSRV");
        FuncionarioDTO dto = new FuncionarioDTO();
        CargoDTO dtoCargo = new CargoDTO();
        CargoDAO daoCargo = new CargoDAO();
        FuncionarioDAO dao = new FuncionarioDAO();
        PrintWriter salida = response.getWriter();
        Integer operacion = Integer.parseInt((request.getParameter("operacion").isEmpty() ? "0" : request.getParameter("operacion")));
        Integer id = Integer.parseInt((request.getParameter("id").isEmpty() ? "0" : request.getParameter("id")));
        Integer id_persona = Integer.parseInt((request.getParameter("id_persona").isEmpty() ? "0" : request.getParameter("id_persona")));
        Integer id_espe = Integer.parseInt((request.getParameter("id_espe").isEmpty() ? "0" : request.getParameter("id_espe")));;
        Integer id_subc = Integer.parseInt((request.getParameter("id_subc").isEmpty() ? "0" : request.getParameter("id_subc")));;
        Integer cbo_tcargo = Integer.parseInt((request.getParameter("cbo_tcargo").isEmpty() ? "0" : request.getParameter("cbo_tcargo")));;
        switch (operacion) {
            case 1://Cargar la tabla
                response.setContentType("application/json");
                salida.print(new Gson().toJson(dao.consultarTodos(dto)));
                break;
            case 2://Agregar
                dto.setId_persona(id_persona);
                dto.setId_espec(id_espe);
                dto.setId_subcargo(id_subc);
                dto.setId_tcargo(cbo_tcargo);
                if (dao.agregar(dto)) {
                    //exitoso
                } else {
                    //negativo
                }
                break;
            case 3://Modificar
                dto.setId(id);
                dto.setId_persona(id_persona);
                dto.setId_espec(id_espe);
                dto.setId_subcargo(id_subc);
                dto.setId_tcargo(cbo_tcargo);
                if (dao.modificar(dto)) {
                    //exitoso
                } else {
                    //negativo
                }
                break;
            case 4://eliminar
                dto.setId(id);
                if (dao.eliminar(dto)) {
                    //exitoso
                } else {
                    //negativo
                }
                break;
            case 5://Busqueda con filtro
                response.setContentType("application/json");
                dto.setId(id);
                salida.print(new Gson().toJson(dao.consultarSegunFiltro(dto)));
                //System.out.println("Lista " + new Gson().toJson(dao.consultarSegunFiltro(dto)));
                salida.close();
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
