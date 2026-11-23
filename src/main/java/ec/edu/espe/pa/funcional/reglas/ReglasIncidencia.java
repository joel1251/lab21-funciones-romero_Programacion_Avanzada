package ec.edu.espe.pa.funcional.reglas;

import ec.edu.espe.pa.funcional.modelo.Incidencia;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/** Funciones del caso práctico: cada tipo expresa sus entradas y su salida. */
public final class ReglasIncidencia {
    private ReglasIncidencia() { }

    public static final int UMBRAL_PRIORIDAD_ALTA = 4;

    // Predicate: recibe Incidencia y devuelve boolean para decidir si está abierta.
    public static final Predicate<Incidencia> ESTA_ABIERTA =
            incidencia -> "ABIERTA".equals(incidencia.estado());

    // Predicate: recibe Incidencia y devuelve boolean según el umbral compartido.
    public static final Predicate<Incidencia> PRIORIDAD_ALTA =
            incidencia -> incidencia.prioridad() >= UMBRAL_PRIORIDAD_ALTA;

    // Predicate compuesto: recibe Incidencia y exige ambas condiciones booleanas.
    public static final Predicate<Incidencia> REQUIERE_ATENCION =
            ESTA_ABIERTA.and(PRIORIDAD_ALTA);

    // Function: transforma una Incidencia en un resumen de tipo String.
    public static final Function<Incidencia, String> RESUMEN =
            incidencia -> incidencia.id() + " - " + incidencia.titulo();

    // Function: transforma la prioridad de una Incidencia en una categoría String.
    public static final Function<Incidencia, String> CATEGORIA = incidencia ->
            switch (incidencia.prioridad()) {
                case 5 -> "CRITICA";
                case 4 -> "ALTA";
                case 2, 3 -> "MEDIA";
                case 1 -> "BAJA";
                default -> throw new IllegalArgumentException("Prioridad fuera del rango válido");
            };

    // Consumer: recibe Incidencia, escribe una notificación y no devuelve un valor.
    public static final Consumer<Incidencia> NOTIFICAR =
            incidencia -> System.out.println("[ATENCION] " + RESUMEN.apply(incidencia));

    // Supplier: get() no recibe argumentos y entrega una nueva Incidencia sintética.
    // El método devuelve la función; cada generador conserva su propio contador.
    public static Supplier<Incidencia> generadorSintetico() {
        AtomicInteger contador = new AtomicInteger(1);
        String[] estados = {"ABIERTA", "EN_PROCESO", "CERRADA"};
        return () -> {
            int n = contador.getAndIncrement();
            return new Incidencia("INC-%03d".formatted(n), "Incidencia sintética " + n,
                    estados[n % estados.length], (n % 5) + 1);
        };
    }
}
