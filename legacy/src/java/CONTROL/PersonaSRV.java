/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROL;

import DAO.CiudadDAO;
import DAO.ECivilDAO;
import DAO.PaisDAO;
import DAO.PersonaDAO;
import DAO.SexoDAO;
import DTO.CiudadDTO;
import DTO.ECivilDTO;
import DTO.PaisDTO;
import DTO.PersonaDTO;
import DTO.SexoDTO;
import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
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
public class PersonaSRV extends HttpServlet {

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
        System.out.println("Llegó a PersonaSRV");
        PrintWriter salida = response.getWriter();
        Integer operacion = Integer.parseInt((request.getParameter("operacion").isEmpty() ? "0" : request.getParameter("operacion")));
        PersonaDTO dto = new PersonaDTO();
        CiudadDTO dtoCiudad = new CiudadDTO();
        ECivilDTO dtoECivil = new ECivilDTO();
        PaisDTO dtoNac = new PaisDTO();
        SexoDTO dtoSexo = new SexoDTO();
        SexoDAO daoSexo = new SexoDAO();
        PaisDAO daoNac = new PaisDAO();
        ECivilDAO daoECivil = new ECivilDAO();
        CiudadDAO daoCiudad = new CiudadDAO();
        PersonaDAO dao = new PersonaDAO();
        Integer id = Integer.parseInt((request.getParameter("id").isEmpty() ? "0" : request.getParameter("id")));
        String nombre1 = request.getParameter("nombre1").trim();
        String nombre2 = request.getParameter("nombre2").trim();
        String ape1 = request.getParameter("apellido1").trim();
        String ape2 = request.getParameter("apellido2").trim();
        String ci = request.getParameter("ci").trim();
        String tel = request.getParameter("tel").trim();
        String dir = request.getParameter("dir").trim();
        String fecha = request.getParameter("fechanac").trim();
        String email = request.getParameter("email").trim();
        Integer cbo_ecivil = Integer.parseInt((request.getParameter("cbo_ecivil").isEmpty() ? "0" : request.getParameter("cbo_ecivil")));
        Integer cbo_ciudad = Integer.parseInt((request.getParameter("cbo_ciudad").isEmpty() ? "0" : request.getParameter("cbo_ciudad")));
        Integer cbo_nac = Integer.parseInt((request.getParameter("cbo_nac").isEmpty() ? "0" : request.getParameter("cbo_nac")));
        Integer cbo_sexo = Integer.parseInt((request.getParameter("cbo_sexo").isEmpty() ? "0" : request.getParameter("cbo_sexo")));
//        System.out.println("la operacion es " + operacion + " y el id ingresado es " + id);
        System.out.println("operacion es = " + operacion);

        switch (operacion) {
            case 7://Consultar sin filtrar, se usa para cargar la tabla
                response.setContentType("application/json");
                salida.print(new Gson().toJson(dao.consultarTodos(dto)));
//                System.out.println("Lista " + new Gson().toJson(dao.consultarTodos(dto)));
                salida.close();
                operacion = null;
                break;
            case 2:
                //System.out.println("Entro al caso 2");
                response.setContentType("application/json");
                dto.setId(id);
                salida.print(new Gson().toJson(dao.consultarSegunFiltro(dto)));
//                System.out.println("Lista del servlet " + new Gson().toJson(dao.consultarSegunFiltro(dto)));
                salida.close();
                operacion = null;
                break;
            case 3://Cargar el combo de ciudad
                response.setContentType("application/json");
                salida.print(new Gson().toJson(daoCiudad.consultarTodos(dtoCiudad)));
//                System.out.println("Lista " + new Gson().toJson(daoCiudad.consultarTodos(dtoCiudad)));
                salida.close();
                operacion = null;
                break;
            case 4://Cargar el combo de estado civil
                response.setContentType("application/json");
                salida.print(new Gson().toJson(daoECivil.consultarTodos(dtoECivil)));
//                System.out.println("Lista " + new Gson().toJson(daoECivil.consultarTodos(dtoECivil)));
                salida.close();
                operacion = null;
                break;
            case 5://Cargar el combo de nacionalidad
                response.setContentType("application/json");
                salida.print(new Gson().toJson(daoNac.consultarTodos(dtoNac)));
//                System.out.println("Lista " + new Gson().toJson(daoNac.consultarTodos(dtoNac)));
                salida.close();
                operacion = null;
                break;
            case 6://Cargar el combo de sexo
                response.setContentType("application/json");
                salida.print(new Gson().toJson(daoSexo.consultarTodos(dtoSexo)));
//                System.out.println("Lista " + new Gson().toJson(daoSexo.consultarTodos(dtoSexo)));
                salida.close();
                operacion = null;
                break;
            case 1://Agregar
                System.out.println("Entro al caso 1 Agregar");
                dto.setNombre1(nombre1);
                dto.setNombre2(nombre2);
                dto.setApe1(ape1);
                dto.setApe2(ape2);
                dto.setCi(ci);
                dto.setTel(tel);
                dto.setDir(dir);
                dto.setFecha(dto.convertir_fecha(fecha));
                dto.setEmail(email);
                dto.setId_ecivil(cbo_ecivil);
                dto.setId_ciudad(cbo_ciudad);
                dto.setId_nac(cbo_nac);
                dto.setId_sexo(cbo_sexo);
                if (dao.agregar(dto)) {
                    //exitoso
                    System.out.println("Exitoso agregado");
                } else {
                    System.out.println("No agregado");
                    //no exitoso
                }
                break;
            case 8://Modificar
                dto.setId(id);
                dto.setNombre1(nombre1);
                dto.setNombre2(nombre2);
                dto.setApe1(ape1);
                dto.setApe2(ape2);
                dto.setCi(ci);
                dto.setTel(tel);
                dto.setDir(dir);
                dto.setFecha(dto.convertir_fecha(fecha));
                dto.setEmail(email);
                dto.setId_ecivil(cbo_ecivil);
                dto.setId_ciudad(cbo_ciudad);
                dto.setId_nac(cbo_nac);
                dto.setId_sexo(cbo_sexo);
                if (dao.modificar(dto)) {
                    //exitoso
                } else {
                    //no exitoso
                }
                break;
            case 9://eliminar
                System.out.println("entro al caso 9 eliminar");
                dto.setId(id);
                if (dao.eliminar(dto)) {
                    //exitoso
                } else {
                    //no exitoso
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
