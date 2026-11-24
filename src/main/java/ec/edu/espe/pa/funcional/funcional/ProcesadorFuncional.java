package ec.edu.espe.pa.funcional.funcional;

import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.reglas.ReglasIncidencia;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Versión funcional: la selección, la transformación y la acción llegan
 * como parámetros, de modo que el método no conoce las reglas concretas.
 */
public class ProcesadorFuncional {

    public <R> List<R> seleccionar(List<Incidencia> incidencias,
                                   Predicate<Incidencia> regla,
                                   Function<Incidencia, R> transformacion) {
        return incidencias.stream()
                .filter(regla)
                .map(transformacion)
                .toList();
    }

    public List<String> titulosQueRequierenAtencion(List<Incidencia> incidencias) {
        return seleccionar(incidencias, ReglasIncidencia.REQUIERE_ATENCION, Incidencia::titulo);
    }

    public void ejecutar(List<Incidencia> incidencias,
                         Predicate<Incidencia> regla,
                         Consumer<Incidencia> accion) {
        incidencias.stream().filter(regla).forEach(accion);
    }
}
