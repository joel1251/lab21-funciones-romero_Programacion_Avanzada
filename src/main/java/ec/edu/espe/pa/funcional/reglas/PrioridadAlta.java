package ec.edu.espe.pa.funcional.reglas;

import ec.edu.espe.pa.funcional.contrato.ReglaIncidencia;
import ec.edu.espe.pa.funcional.modelo.Incidencia;

/** Primera implementación del contrato: una clase con nombre propio. */
public class PrioridadAlta implements ReglaIncidencia {
    @Override
    public boolean evaluar(Incidencia incidencia) {
        return incidencia.prioridad() >= 4;
    }
}
