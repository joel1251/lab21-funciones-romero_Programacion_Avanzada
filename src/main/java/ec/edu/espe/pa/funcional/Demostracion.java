package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.modelo.Incidencia;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Fase 2: cada interfaz expresa la entrada y la salida de una operación.
 * Definir una lambda no ejecuta su cuerpo; hay que invocar su método abstracto.
 */
public class Demostracion {

    public static void main(String[] args) {
        Predicate<Incidencia> estaAbierta =
                incidencia -> "ABIERTA".equals(incidencia.estado());

        Function<Incidencia, String> obtenerResumen =
                incidencia -> incidencia.id() + " - " + incidencia.titulo();

        Consumer<Incidencia> mostrar =
                incidencia -> System.out.println("Consumer.accept(): " + incidencia.titulo());

        Supplier<Incidencia> crearEjemplo =
                () -> new Incidencia("INC-001", "Error de acceso", "ABIERTA", 3);

        System.out.println("=== FASE 2: INTERFACES FUNCIONALES ===");
        System.out.println("Lambdas definidas; sus cuerpos todavía no se han ejecutado.");

        // Supplier: sin entrada, devuelve una incidencia al invocar get().
        Incidencia ejemplo = crearEjemplo.get();
        System.out.println("Supplier.get(): " + ejemplo);

        // Predicate: recibe una incidencia y devuelve boolean al invocar test().
        System.out.println("Predicate.test(): " + estaAbierta.test(ejemplo));

        // Function: recibe una incidencia y devuelve un texto al invocar apply().
        System.out.println("Function.apply(): " + obtenerResumen.apply(ejemplo));

        // Consumer: realiza una acción; accept() no devuelve ningún resultado.
        mostrar.accept(ejemplo);

        System.out.println("=== SINTAXIS DE LAS LAMBDAS ===");
        Predicate<Incidencia> prioridadAltaExpresion = i -> i.prioridad() >= 4;
        Predicate<Incidencia> prioridadAltaBloque = i -> {
            int prioridad = i.prioridad();
            return prioridad >= 4;
        };
        Predicate<Incidencia> prioridadAltaTipoExplicito =
                (Incidencia i) -> i.prioridad() >= 4;
        Predicate<Incidencia> prioridadAltaConVar =
                (var i) -> i.prioridad() >= 4;

        System.out.println("Lambda de expresión, prioridad 3: "
                + prioridadAltaExpresion.test(ejemplo));
        System.out.println("Lambda con bloque, prioridad 3: "
                + prioridadAltaBloque.test(ejemplo));

        Incidencia casoLimite = new Incidencia("INC-004", "Fallo de respaldo", "ABIERTA", 4);
        System.out.println("Tipo explícito, prioridad 4: "
                + prioridadAltaTipoExplicito.test(casoLimite));
        System.out.println("Parámetro con var, prioridad 4: "
                + prioridadAltaConVar.test(casoLimite));

        // No se reasigna umbral: es efectivamente final y puede capturarse.
        int umbral = 4;
        Predicate<Incidencia> superaUmbral = i -> i.prioridad() >= umbral;
        System.out.println("Umbral capturado = " + umbral);
        System.out.println("Prioridad 3 supera el umbral: " + superaUmbral.test(ejemplo));
        System.out.println("Prioridad 4 supera el umbral: " + superaUmbral.test(casoLimite));

        // Si se agrega aquí «umbral = 5;», la lambda deja de compilar:
        // una variable local capturada debe ser final o efectivamente final.
    }
}
