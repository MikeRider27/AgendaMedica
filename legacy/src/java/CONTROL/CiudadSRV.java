/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROL;

import DAO.CiudadDAO;
import DAO.PaisDAO;
import DTO.CiudadDTO;
import DTO.PaisDTO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author juan
 */
public class CiudadSRV extends HttpServlet {

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
        System.out.println("Llegó a CiudadSRV");
        PrintWriter salida = response.getWriter();
        CiudadDTO dto = new CiudadDTO();
//        PaisDTO dtoPais = new PaisDTO();
//        PaisDAO daoPais = new PaisDAO();
        CiudadDAO dao = new CiudadDAO();
        Integer operacion = Integer.parseInt(request.getParameter("operacion"));
        Integer id = Integer.parseInt((request.getParameter("id").isEmpty() ? "0" : request.getParameter("id")));
        //Integer id_pais = Integer.parseInt((request.getParameter("comboPais").isEmpty() ? "0" : request.getParameter("comboPais")));
        String ciudad = request.getParameter("ciudad").trim();
        
        //System.out.println(vbuscador);
        switch (operacion) {
            case 1://Agregar
                dto.setId(id);
                dto.setDescripcion(ciudad.trim());
                //dto.setId_pais(id_pais);
                if (dao.agregar(dto)) {
                    //exitoso
                } else {
                    //negativo
                }
                break;
            case 2://Modificar
                dto.setId(id);
                dto.setDescripcion(ciudad);
                //dto.setId_pais(id_pais);
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
            case 4://Busqueda
                response.setContentType("application/json");
                dto.setId(id);
//                dtoPais.setVpais_id(id_pais);
//                salida.print(new Gson().toJson(daoPais.consultarSegunFiltro(dtoPais)));
                salida.print(new Gson().toJson(dao.consultarSegunFiltro(dto)));
                //System.out.println("Lista " + new Gson().toJson(dao.consultarSegunFiltro(dto)));
                salida.close();
                break;
            case 5:// Cargar Combo de Ciudad
                response.setContentType("application/json");
                dto.setId(id);
                salida.print(new Gson().toJson(dao.consultarTodos(dto)));
                //System.out.println("Lista combo de ciudad" + new Gson().toJson(dao.consultarTodos(dto)));
                salida.close();
                break;
            case 6:// Cargar tabla de Ciudad
                response.setContentType("application/json");
                dto.setId(id);
                List<CiudadDTO> ciudades;
                ciudades = dao.consultarTodos(dto);
                if (ciudades != null) {
                    salida.print(new Gson().toJson(ciudades));
                }
                //System.out.println("Lista de ciudades de la tabla" + new Gson().toJson(ciudades));
                salida.close();
                break;
//            case 7:// Cargar combo de Pais
//                response.setContentType("application/json");
//                //dtoPais.setVpais_id(id_pais);
//                List<PaisDTO> paises_filtro;
//                paises_filtro = daoPais.consultarTodos(dtoPais);
//                if (paises_filtro != null) {
//                    salida.print(new Gson().toJson(paises_filtro));
//                }
//                //System.out.println("Lista combo de paises " + new Gson().toJson(paises_filtro));
//                break;
//            case 8:// Cargar tabla de País en el modal
//                response.setContentType("application/json");
//                //dtoPais.setVpais_id(id_pais);
//                List<PaisDTO> paises_filtro_modal;
//                paises_filtro_modal = daoPais.consultarTodos(dtoPais);
//                if (paises_filtro_modal != null) {
//                    salida.print(new Gson().toJson(paises_filtro_modal));
//                }
//                //System.out.println("Lista tabla de paises para el modal " + new Gson().toJson(paises_filtro_modal));
//                
//                break;
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
