
package ec.edu.espe.pa.funcional.modelo;

import java.util.Objects;

public record Incidencia(
        String id,
        String titulo,
        String estado,
        int prioridad) {

    public Incidencia {
        Objects.requireNonNull(id, "El id es obligatorio");
        Objects.requireNonNull(estado, "El estado es obligatorio");

        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException(
                "La prioridad debe estar entre 1 y 5: " + prioridad);
        }
    }
}
