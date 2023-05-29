/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROL;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author juan
 */
public class Menu_Salvaje extends HttpServlet {

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
        Integer operacion = Integer.parseInt(request.getParameter("operacion"));
        System.out.println("llego al Menu salvaje");
        switch (operacion) {
            case 1: // Sin comentario
                System.out.println("entro al caso 1");

                HttpSession actual = request.getSession();
                String id = actual.getId();
                String nombre = (String) actual.getAttribute("logueado");

                if (actual.isNew()) {
                    request.getSession().removeAttribute("logueado");
                    actual.invalidate();
                    response.sendRedirect("logueo.jsp");
                    return;
                }
                if (actual == null) {
                    request.getSession().removeAttribute("logueado");
                    actual.invalidate();
                    response.sendRedirect("logueo.jsp");
                } else {
                    if (actual.getAttribute("logueado") == null) {
                        request.getSession().removeAttribute("logueado");
                        actual.invalidate();
                        response.sendRedirect("logueo.jsp");
                    }
                }
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
