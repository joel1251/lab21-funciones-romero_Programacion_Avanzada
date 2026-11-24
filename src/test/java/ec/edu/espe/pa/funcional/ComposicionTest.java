package ec.edu.espe.pa.funcional;

import ec.edu.espe.pa.funcional.contrato.FabricaIncidencia;
import ec.edu.espe.pa.funcional.funcional.ProcesadorFuncional;
import ec.edu.espe.pa.funcional.modelo.Incidencia;
import ec.edu.espe.pa.funcional.reglas.ReglasIncidencia;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.CATEGORIA;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.ESTA_ABIERTA;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.PRIORIDAD_ALTA;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.REQUIERE_ATENCION;
import static ec.edu.espe.pa.funcional.reglas.ReglasIncidencia.RESUMEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Fase 5: composición de funciones y referencias a métodos. */
class ComposicionTest {

    private final Incidencia abiertaAlta = new Incidencia("INC-001", "Caída del servidor", "ABIERTA", 5);
    private final Incidencia abiertaBaja = new Incidencia("INC-002", "Error tipográfico", "ABIERTA", 2);
    private final Incidencia cerradaAlta = new Incidencia("INC-003", "Fallo de respaldo", "CERRADA", 5);

    @Test
    void negateYAndDescribenLasPendientesDePrioridadBaja() {
        Predicate<Incidencia> noAbierta = ESTA_ABIERTA.negate();
        Predicate<Incidencia> pendienteBaja = ESTA_ABIERTA.and(PRIORIDAD_ALTA.negate());

        assertTrue(noAbierta.test(cerradaAlta));
        assertFalse(noAbierta.test(abiertaAlta));
        assertTrue(pendienteBaja.test(abiertaBaja));
        assertFalse(pendienteBaja.test(abiertaAlta));
    }

    @Test
    void orAmpliaLaReglaDeAtencion() {
        Predicate<Incidencia> revisarHoy = REQUIERE_ATENCION.or(i -> i.prioridad() == 5);

        assertTrue(revisarHoy.test(abiertaAlta));
        assertTrue(revisarHoy.test(cerradaAlta)); // entra por el or, no por REQUIERE_ATENCION
        assertFalse(revisarHoy.test(abiertaBaja));
    }

    @ParameterizedTest
    @CsvSource({"1, BAJA", "3, MEDIA", "4, ALTA", "5, CRITICA"})
    void andThenEtiquetaLaCategoria(int prioridad, String categoria) {
        Incidencia caso = new Incidencia("INC-L", "Límite", "ABIERTA", prioridad);
        Function<Incidencia, String> etiqueta = CATEGORIA.andThen(c -> "[" + c + "]");

        assertEquals("[" + categoria + "]", etiqueta.apply(caso));
    }

    @Test
    void andThenYComposeProducenElMismoTexto() {
        Function<String, String> aMayusculas = String::toUpperCase;
        Function<Incidencia, String> conAndThen = RESUMEN.andThen(aMayusculas);
        Function<Incidencia, String> conCompose = aMayusculas.compose(RESUMEN);

        assertEquals("INC-001 - CAÍDA DEL SERVIDOR", conAndThen.apply(abiertaAlta));
        assertEquals(conAndThen.apply(abiertaAlta), conCompose.apply(abiertaAlta));
    }

    @Test
    void consumerAndThenRegistraSoloLasSeleccionadas() {
        List<String> bitacora = new ArrayList<>();
        Consumer<Incidencia> registrar = i -> bitacora.add(i.id());
        Consumer<Incidencia> notificarYRegistrar = ReglasIncidencia.NOTIFICAR.andThen(registrar);

        new ProcesadorFuncional().ejecutar(List.of(abiertaAlta, abiertaBaja, cerradaAlta),
                REQUIERE_ATENCION, notificarYRegistrar);

        assertEquals(List.of("INC-001"), bitacora);
    }

    @Test
    void lasCuatroReferenciasAMetodosSeComportanComoSusLambdas() {
        Predicate<Incidencia> esCritica = ReglasIncidencia::esCritica;      // estático
        Function<Incidencia, String> titulo = Incidencia::titulo;           // instancia arbitraria
        Supplier<List<Incidencia>> nuevaLista = ArrayList::new;             // constructor
        FabricaIncidencia fabrica = Incidencia::new;                        // constructor del record

        assertTrue(esCritica.test(abiertaAlta));
        assertFalse(esCritica.test(abiertaBaja));
        assertEquals("Caída del servidor", titulo.apply(abiertaAlta));
        assertTrue(nuevaLista.get().isEmpty());
        assertEquals(abiertaAlta, fabrica.crear("INC-001", "Caída del servidor", "ABIERTA", 5));
    }

    @Test
    void seleccionarTransformaSoloLoSeleccionado() {
        List<String> resumenes = new ProcesadorFuncional().seleccionar(
                List.of(abiertaAlta, abiertaBaja, cerradaAlta), ESTA_ABIERTA,
                RESUMEN.andThen(String::toUpperCase));

        assertEquals(List.of("INC-001 - CAÍDA DEL SERVIDOR", "INC-002 - ERROR TIPOGRÁFICO"), resumenes);
    }
}
