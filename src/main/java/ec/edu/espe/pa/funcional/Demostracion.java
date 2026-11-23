package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.contrato.ReglaIncidencia;
import ec.edu.espe.pa.funcional.reglas.PrioridadAlta;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Demostraciones de las fases del laboratorio, seleccionables desde la consola.
 * Definir una lambda no ejecuta su cuerpo; hay que invocar su método abstracto.
 */
public class Demostracion {

    public static void main(String[] args) {
        String fase = args.length == 0 ? "todas" : args[0];
        switch (fase) {
            case "2" -> demostrarInterfaces();
            case "3" -> demostrarContrato();
            case "todas" -> {
                demostrarInterfaces();
                demostrarContrato();
            }
            default -> throw new IllegalArgumentException("Elige 2, 3 o todas");
        }
    }

    private static void demostrarInterfaces() {
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

    private static void demostrarContrato() {
        System.out.println("=== FASE 3: CONTRATO PROPIO ===");

        // Las tres implementaciones evalúan la MISMA regla de prioridad alta.
        ReglaIncidencia prioridadClase = new PrioridadAlta();
        ReglaIncidencia prioridadAnonima = new ReglaIncidencia() {
            @Override
            public boolean evaluar(Incidencia incidencia) {
                return incidencia.prioridad() >= 4;
            }
        };
        ReglaIncidencia prioridadLambda = incidencia -> incidencia.prioridad() >= 4;

        for (int prioridad : List.of(3, 4, 5)) {
            Incidencia caso = new Incidencia("INC-P" + prioridad, "Caso de prioridad", "ABIERTA", prioridad);
            System.out.printf("Prioridad %d | clase=%s | anónima=%s | lambda=%s%n",
                    prioridad, prioridadClase.evaluar(caso),
                    prioridadAnonima.evaluar(caso), prioridadLambda.evaluar(caso));
        }

        ReglaIncidencia abiertaLambda = incidencia -> "ABIERTA".equals(incidencia.estado());
        ReglaIncidencia requiereAtencion = abiertaLambda.y(prioridadClase);
        Predicate<Incidencia> comoPredicado = requiereAtencion::evaluar;
        Incidencia abiertaAlta = new Incidencia("INC-A", "Error de acceso", "ABIERTA", 4);
        Incidencia cerradaAlta = new Incidencia("INC-C", "Error resuelto", "CERRADA", 5);

        System.out.println("Contrato con método default y():");
        System.out.println("ABIERTA y prioridad 4 requiere atención: " + requiereAtencion.evaluar(abiertaAlta));
        System.out.println("CERRADA y prioridad 5 requiere atención: " + requiereAtencion.evaluar(cerradaAlta));
        System.out.println("Adaptación a Predicate con ::evaluar: " + comoPredicado.test(abiertaAlta));
    }
}
