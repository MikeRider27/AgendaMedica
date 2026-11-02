
package CONTROL;

import GENERICOS.ListarReportes;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author juan
 */
public class ReportesSRV extends HttpServlet {

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
        ListarReportes reportes;
        String urlJasper = "";
        Integer v_bandera = Integer.parseInt(request.getParameter("bandera").trim());
        HashMap<String, Object> parametros = new HashMap<>();
        reportes = new ListarReportes();
        try {
            switch(v_bandera){
                case 1://Reporte de Ciudades
                    urlJasper = "/home/juan/NetBeansProjects/AgendaMedica_V00.2/src/java/Reportes/ciudades.jrxml";
                    break;
                case 2://Reporte de Paises
                    urlJasper = "/home/juan/NetBeansProjects/AgendaMedica_V00.2/src/java/Reportes/paises.jrxml";
                    break;
                case 3://Reporte de Cargos
                    urlJasper = "/home/juan/NetBeansProjects/AgendaMedica_V00.2/src/java/Reportes/Clientes.jrxml";
                    break;
                case 4://Reporte de Subcargos
                    urlJasper = "/home/juan/NetBeansProjects/AgendaMedica_V00.2/src/java/Reportes/subcargos.jrxml";
                    break;
                case 5://Reporte de Usuarios
                    urlJasper = "/home/juan/NetBeansProjects/AgendaMedica_V00.2/src/java/Reportes/usuarios.jrxml";
                    break;
            }
            reportes.generarReportes(urlJasper, parametros);
        } catch (Exception e) {
            System.out.println("Error/Control " + e.getMessage());
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
