/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package INT;


import DTO.VentaDTO;
import GENERICOS.OperacionSQL;
import java.util.List;

/**
 *
 * @author juan
 */
public interface VentaINT extends OperacionSQL<List<VentaDTO>>{
    List<VentaDTO> consultarTodosSinFiltro(VentaDTO dto);
    List<VentaDTO> consultarTodosConFiltro(VentaDTO dto);
    boolean anular(VentaDTO dto);
}
