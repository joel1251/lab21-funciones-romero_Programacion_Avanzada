package ec.edu.espe.pa.funcional.contrato;

import ec.edu.espe.pa.funcional.modelo.Incidencia;

/**
 * Contrato propio para el constructor de cuatro parámetros:
 * java.util.function no ofrece una interfaz estándar con cuatro entradas.
 */
@FunctionalInterface
public interface FabricaIncidencia {
    Incidencia crear(String id, String titulo, String estado, int prioridad);
}
