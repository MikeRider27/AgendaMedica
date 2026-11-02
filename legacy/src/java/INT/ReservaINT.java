
package INT;

import DTO.ReservaDTO;
import GENERICOS.OperacionSQL;
import java.util.List;


public interface ReservaINT extends OperacionSQL<ReservaDTO>{
    List<ReservaDTO> consultarCupos(ReservaDTO dto);
    List<ReservaDTO> consultarCuposEspe(ReservaDTO dto);
    List<ReservaDTO> consultarCuposDisponibles(ReservaDTO dto);
    List<ReservaDTO> consultarReservaCONFIRMconFiltro(ReservaDTO dto);
    List<ReservaDTO> consultarReservaCONFIRMsinFiltro(ReservaDTO dto);
    List<ReservaDTO> consultarEmailFechatodos(ReservaDTO dto);
}
