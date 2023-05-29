/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package INT;

import DTO.PedidoDTO;
import GENERICOS.OperacionSQL;
import java.util.List;

/**
 *
 * @author juan
 */
public interface PedidoINT extends OperacionSQL<List<PedidoDTO>>{
    List<PedidoDTO> consultarTodosSinFiltro(PedidoDTO dto);
    List<PedidoDTO> consultarTodosConFiltro(PedidoDTO dto);
    boolean anular(PedidoDTO dto);
}
