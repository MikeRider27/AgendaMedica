/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROL;

import DAO.RolDAO;
import DAO.UsuarioDAO;
import DTO.RolDTO;
import DTO.UsuarioDTO;
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
public class UsuarioSRV extends HttpServlet {

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
        System.out.println("Llegó a UsuarioSRV");
        PrintWriter salida = response.getWriter();
        UsuarioDTO dto = new UsuarioDTO();
        RolDTO dtoRol = new RolDTO();
        UsuarioDAO dao = new UsuarioDAO();
        RolDAO daoRol = new RolDAO();
        Integer operacion = Integer.parseInt(request.getParameter("operacion"));
        Integer id = Integer.parseInt((request.getParameter("id").isEmpty() ? "0" : request.getParameter("id")));
        String nombre = request.getParameter("nombre").trim();
        String clave = request.getParameter("clave").trim();
        Integer comborol = Integer.parseInt((request.getParameter("comborol").isEmpty() ? "0" : request.getParameter("comborol")));

        switch (operacion) {
            case 1://Agregar
                dto.setNombre_usuario(nombre.trim());
                dto.setClave_usuario(clave.trim());
                dto.setId_rol(comborol);
                if (dao.agregar(dto)) {
                    //exitoso
                } else {
                    //negativo
                }
                break;
            case 2://Modificar
                dto.setId_usuario(id);
                dto.setNombre_usuario(nombre.trim());
                dto.setClave_usuario(clave);
                dto.setId_rol(comborol);
                if (dao.modificar(dto)) {
                    //exitoso
                } else {
                    //negativo
                }
                break;
            case 3://eliminar
                dto.setId_usuario(id);
                if (dao.eliminar(dto)) {
                    //exitoso
                } else {
                    //negativo
                }
                break;
            case 4://Busqueda con filtro
                response.setContentType("application/json");
                List<UsuarioDTO> usuario;
                System.out.println("El id ingresado es = " + id);
                dto.setId_usuario(id);
                usuario = dao.consultarSegunFiltro(dto);
                if (usuario != null) {
                    salida.print(new Gson().toJson(usuario));
//                    System.out.println("Lista con filtro " + new Gson().toJson(usuario));
                    salida.close();
                }
                break;
            case 5://Busqueda sin filtro
                response.setContentType("application/json");
                salida.print(new Gson().toJson(dao.consultarTodos(dto)));
//                System.out.println("Lista " + new Gson().toJson(dao.consultarTodos(dto)));
                salida.close();
                break;
            case 6://Busqueda sin filtro para el combo rol
                response.setContentType("application/json");
                List<RolDTO> rol;
                rol = daoRol.consultarTodos(dtoRol);
//                System.out.println("los roles son " + new Gson().toJson(rol));
                if (rol != null) {
                    salida.print(new Gson().toJson(rol));
                    salida.close();
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
