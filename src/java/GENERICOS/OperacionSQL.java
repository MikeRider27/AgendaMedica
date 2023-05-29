
package GENERICOS;

import java.util.List;


public interface OperacionSQL<T> {
    boolean agregar(T dto);
    boolean modificar(T dto);
    boolean eliminar(T dto);
    List<T> consultarTodos(T dto);
    List<T> consultarSegunFiltro(T dto);
}
