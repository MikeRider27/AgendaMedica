/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package INT;

import DTO.FuncionarioDTO;
import GENERICOS.OperacionSQL;
import java.util.List;

/**
 *
 * @author juan
 */
public interface FuncionarioINT extends OperacionSQL<FuncionarioDTO> {

    List<FuncionarioDTO> consultarTodosFuncionarios(FuncionarioDTO dto);

    List<FuncionarioDTO> consultarTodosFuncionariosConFiltro(FuncionarioDTO dto);

    List<FuncionarioDTO> consultarTodosMedicos(FuncionarioDTO dto);

    List<FuncionarioDTO> consultarTodosMedicosConFiltro(FuncionarioDTO dto);
}
