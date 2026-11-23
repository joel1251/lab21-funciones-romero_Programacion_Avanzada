package ec.edu.espe.pa.funcional.contrato;

import ec.edu.espe.pa.funcional.modelo.Incidencia;

/** Contrato del dominio: recibe una incidencia y decide si cumple una regla. */
@FunctionalInterface
public interface ReglaIncidencia {
    boolean evaluar(Incidencia incidencia);

    // Sigue existiendo un solo método abstracto: este método ya tiene cuerpo.
    default ReglaIncidencia y(ReglaIncidencia otra) {
        return incidencia -> this.evaluar(incidencia) && otra.evaluar(incidencia);
    }
}
