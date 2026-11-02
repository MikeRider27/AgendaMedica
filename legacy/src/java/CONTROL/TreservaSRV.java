/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROL;

import DAO.TreservaDAO;
import DTO.TreservaDTO;
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
public class TreservaSRV extends HttpServlet {

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
        System.out.println("Llegó a TreservaSRV");
        PrintWriter salida = response.getWriter();
        TreservaDTO dto = new TreservaDTO();
        TreservaDAO dao = new TreservaDAO();
        Integer operacion = Integer.parseInt(request.getParameter("operacion"));
        Integer id = Integer.parseInt((request.getParameter("id").isEmpty() ? "0" : request.getParameter("id")));
        String descripcion = request.getParameter("descripcion").trim();

        switch (operacion) {
            case 1://Agregar
                dto.setDescripcion(descripcion);
                if (dao.agregar(dto)) {
                    //exitoso
                } else {
                    //negativo
                }
                break;
            case 2://Modificar
                dto.setId(id);
                dto.setDescripcion(descripcion);
                if (dao.modificar(dto)) {
                    //exitoso
                } else {
                    //negativo
                }
                break;
            case 3://eliminar
                dto.setId(id);
                if (dao.eliminar(dto)) {
                    //exitoso
                } else {
                    //negativo
                }
                break;
            case 4://Busqueda con filtro
                response.setContentType("application/json");
                dto.setId(id);
                salida.print(new Gson().toJson(dao.consultarSegunFiltro(dto)));
                //System.out.println("Lista " + new Gson().toJson(dao.consultarSegunFiltro(dto)));  
                salida.close();
                break;
            case 5://Busqueda sin filtro
                response.setContentType("application/json");
                salida.print(new Gson().toJson(dao.consultarTodos(dto)));
                //System.out.println("Lista " + new Gson().toJson(dao.consultarTodos(dto)));
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
