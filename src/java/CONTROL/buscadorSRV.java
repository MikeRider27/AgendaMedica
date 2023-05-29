/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROL;

import DAO.ClienteDAO;
import DAO.EspecialidadDAO;
import DAO.FuncionarioDAO;
import DAO.PedidoDAO;
import DAO.PersonaDAO;
import DAO.ProductoDAO;
import DAO.ReservaDAO;
import DAO.ServicioDAO;
import DAO.SubcargoDAO;
import DAO.UsuarioDAO;
import DAO.VentaDAO;
import DTO.ClienteDTO;
import DTO.EspecialidadDTO;
import DTO.FuncionarioDTO;
import DTO.PedidoDTO;
import DTO.PersonaDTO;
import DTO.ProductoDTO;
import DTO.ReservaDTO;
import DTO.ServicioDTO;
import DTO.SubcargoDTO;
import DTO.UsuarioDTO;
import DTO.VentaDTO;
import GENERICOS.Conexion;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class buscadorSRV extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        System.out.println("Llega al Servlet buscadorSRV ");
        Integer bandera = Integer.parseInt(request.getParameter("opcion"));
//        System.out.println("Se recibio la bandera  = " + bandera);

        Integer id = Integer.parseInt((request.getParameter("identificador").isEmpty()) ? "0" : request.getParameter("identificador"));
//        System.out.println("Se recibio el identificador = " + id);

        UsuarioDTO dto = new UsuarioDTO();
        PersonaDTO dtoPersona = new PersonaDTO();
        ProductoDTO dtoProducto = new ProductoDTO();
        EspecialidadDTO dtoEspecialidad = new EspecialidadDTO();
        SubcargoDTO dtoSubcargo = new SubcargoDTO();
        FuncionarioDTO dtoFuncio = new FuncionarioDTO();
        ClienteDTO dtoCliente = new ClienteDTO();
        ReservaDTO dtoReserva = new ReservaDTO();
        PedidoDTO dtoPedido = new PedidoDTO();
        ServicioDTO dtoServicio = new ServicioDTO();
        VentaDTO dtoVenta = new VentaDTO();
        VentaDAO daoVenta = new VentaDAO();
        ServicioDAO daoServicio = new ServicioDAO();
        PedidoDAO daoPedido = new PedidoDAO();
        ReservaDAO daoReserva = new ReservaDAO();
        ClienteDAO daoCliente = new ClienteDAO();
        FuncionarioDAO daoFuncio = new FuncionarioDAO();
        SubcargoDAO daoSubcargo = new SubcargoDAO();
        EspecialidadDAO daoEspecialidad = new EspecialidadDAO();
        UsuarioDAO dao = new UsuarioDAO();
        PersonaDAO daoPersona = new PersonaDAO();
        ProductoDAO daoProducto = new ProductoDAO();

        ResultSet rs;
//        Conexion conec = Conexion.getInstance();
        Connection conec = Conexion.obtenerConexion();
        PreparedStatement ps;
        String query;
        switch (bandera) {
            case 1://Buscador en caso de la opcion 'usuarios'
                response.setContentType("application/json");
                System.out.println("case 1");
                try {
                    ArrayList<UsuarioDTO> usuario = new ArrayList<>();
                    query = "SELECT usuarios_nick,usuario_id\n"
                            + "  FROM public.usuarios\n"
                            + "  order by usuario_id";
                    ps = conec.prepareStatement(query);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        usuario.add(new UsuarioDTO(
                                rs.getInt("usuario_id"),
                                rs.getString("usuarios_nick"))
                        );
                    }
                    List<UsuarioDTO> usuarioSalida;
                    usuarioSalida = usuario;
                    out.println(new Gson().toJson(usuarioSalida));

                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                break;
            case 2://Recuperar descripcion de usuario por id
                System.out.println("case 2");
                response.setContentType("application/json");
                dto.setId_usuario(id);
                out.print(new Gson().toJson(dao.consultarSegunFiltro(dto)));
                out.close();
                break;
            case 3://Buscador en caso e la opcion 'persona'
                System.out.println("case 3");
                response.setContentType("application/json");
                out.print(new Gson().toJson(daoPersona.consultarTodos(dtoPersona)));
//                System.out.println("Lista de personas " + new Gson().toJson(daoPersona.consultarTodos(dtoPersona)));
                out.close();
                break;
            case 4://Recuperar descripcion de cliente por id
                response.setContentType("application/json");
                dtoPersona.setId(id);
                out.print(new Gson().toJson(daoPersona.consultarSegunFiltro(dtoPersona)));
                out.close();
                break;
            /* Obviamos el caso 5 porque tenemos a ese numero de señuelo para el javascript */
            case 6://Buscador en caso e la opcion 'producto'
                response.setContentType("application/json");
                out.print(new Gson().toJson(daoProducto.consultarTodos(dtoProducto)));
                out.close();
                break;
            case 7://Recuperar descripcion de producto por id
                response.setContentType("application/json");
                dtoProducto.setId_producto(id);
                out.print(new Gson().toJson(daoProducto.consultarSegunFiltro(dtoProducto)));
                out.close();
                break;
            case 8://Recuperar descripcion por id para persona
                System.out.println("Caso 8");
                response.setContentType("application/json");
                dtoPersona.setId(id);
                out.print(new Gson().toJson(daoPersona.consultarSegunFiltro(dtoPersona)));
                out.close();
                break;
            case 9://Buscador en caso e la opcion 'especialidades'
                response.setContentType("application/json");
                out.print(new Gson().toJson(daoEspecialidad.consultarTodos(dtoEspecialidad)));
                out.close();
                break;
            case 10:
                response.setContentType("application/json");
                dtoEspecialidad.setId(id);
                out.print(new Gson().toJson(daoEspecialidad.consultarSegunFiltro(dtoEspecialidad)));
                out.close();
                break;
            case 11://Buscador en caso e la opcion 'subcargo'
                response.setContentType("application/json");
                out.print(new Gson().toJson(daoSubcargo.consultarTodos(dtoSubcargo)));
                out.close();
                break;
            case 12:
                response.setContentType("application/json");
                dtoSubcargo.setId(id);
                out.print(new Gson().toJson(daoSubcargo.consultarSegunFiltro(dtoSubcargo)));
                out.close();
                break;
            case 13://Buscador en caso de 'funcionario'
                response.setContentType("application/json");
                //dtoSubcargo.setId(id);
                out.print(new Gson().toJson(daoFuncio.consultarTodosFuncionarios(dtoFuncio)));
                out.close();
                break;
            case 14://Buscador con filtro en caso de 'funcionario'
                response.setContentType("application/json");
                dtoFuncio.setId(id);
                out.print(new Gson().toJson(daoFuncio.consultarTodosFuncionariosConFiltro(dtoFuncio)));
                out.close();
                break;
            case 15://Buscador en caso de 'cliente'
                response.setContentType("application/json");
                //dtoSubcargo.setId(id);
                out.print(new Gson().toJson(daoCliente.consultarTodos(dtoCliente)));
                out.close();
                break;
            case 16://Buscador con filtro en caso de 'cliente'
                response.setContentType("application/json");
                dtoCliente.setId(id);
                out.print(new Gson().toJson(daoCliente.consultarSegunFiltro(dtoCliente)));
                out.close();
                break;
            case 17://Buscador en caso de 'medico'
                response.setContentType("application/json");
                //dtoSubcargo.setId(id);
                out.print(new Gson().toJson(daoFuncio.consultarTodosMedicos(dtoFuncio)));
                out.close();
                break;
            case 18://Buscador con filtro en caso de 'medico'
                response.setContentType("application/json");
                dtoFuncio.setId(id);
                out.print(new Gson().toJson(daoFuncio.consultarTodosMedicosConFiltro(dtoFuncio)));
                out.close();
                break;
            case 19://Buscador en caso de 'reserva' solo con el estado confirmado
                response.setContentType("application/json");
                //dtoSubcargo.setId(id);
                out.print(new Gson().toJson(daoReserva.consultarReservaCONFIRMsinFiltro(dtoReserva)));
                out.close();
                break;
            case 20://Buscador con filtro en caso de 'reserva'
                response.setContentType("application/json");
                dtoReserva.setId(id);
                out.print(new Gson().toJson(daoReserva.consultarReservaCONFIRMconFiltro(dtoReserva)));
                out.close();
                break;
            case 21://Buscador  en caso de 'producto' 
                response.setContentType("application/json");
                out.print(new Gson().toJson(daoProducto.consultarTodos(dtoProducto)));
                out.close();
                break;
            case 22://Buscador con filtro en caso de 'producto' 
                response.setContentType("application/json");
                dtoProducto.setId_producto(id);
                out.print(new Gson().toJson(daoProducto.consultarSegunFiltro(dtoProducto)));
                out.close();
                break;
            case 23:// Buscador sin filtro en caso de 'pedidos'
                response.setContentType("application/json");
                out.print(new Gson().toJson(daoPedido.consultarTodosSinFiltro(dtoPedido)));
                out.close();
                break;
            case 24://Buscador con filtro en caso de 'pedidos'
                response.setContentType("application/json");
                dtoPedido.setId_pedido(id);
                out.print(new Gson().toJson(daoPedido.consultarTodosConFiltro(dtoPedido)));
                out.close();
                break;
            case 25:// Anular Pedido
                response.setContentType("application/json");
                dtoPedido.setId_pedido(id);
                out.print(new Gson().toJson(daoPedido.anular(dtoPedido)));
                out.close();
                break;
            case 26: // Buscador sin filtro para 'servicio'
                response.setContentType("application/json");
                out.print(new Gson().toJson(daoServicio.consultarTodos(dtoServicio)));
                out.close();
                break;
            case 27: // Buscador sin filtro para 'ventas'
                response.setContentType("application/json");
                out.print(new Gson().toJson(daoVenta.consultarTodosSinFiltro(dtoVenta)));
                out.close();
                break;
            case 28: //Buscador con filtro para servicios con ventas
                response.setContentType("application/json");
                dtoVenta.setId_pedido(id);
                out.print(new Gson().toJson(daoVenta.consultarTodosConFiltro(dtoVenta)));
                out.close();
                break;
            case 29: // Anular venta
                response.setContentType("application/json");
                dtoVenta.setId_pedido(id);
                out.print(new Gson().toJson(daoVenta.anular(dtoVenta)));
                out.close();
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
