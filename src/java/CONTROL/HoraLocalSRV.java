/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROL;

import CALCULOS.CalcularConfirmados;
import DTO.PedidoDTO;
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
public class HoraLocalSRV extends HttpServlet {

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
        System.out.println("Llegó a HoraLocalSRV");
        CalcularConfirmados fecha = new CalcularConfirmados();
        PedidoDTO dto = new PedidoDTO();
        PrintWriter salida = response.getWriter();
        Integer operacion = Integer.parseInt((request.getParameter("opcion").isEmpty() ? "0" : request.getParameter("opcion")));
        Integer id = Integer.parseInt((request.getParameter("identificador").isEmpty() ? "0" : request.getParameter("identificador")));
        String fechavista = fecha.RecuperarFechaActual();
        System.out.println("la operacion ingresada es " + operacion);
        
         switch (operacion) {
            case 1://Fecha del sistema
                response.setContentType("application/json");
                salida.print(new Gson().toJson(fechavista));
                System.out.println("Fecha actual es " + new Gson().toJson(fechavista));
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
